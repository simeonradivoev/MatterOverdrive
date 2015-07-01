package matteroverdrive.blocks.includes;

import cofh.api.block.IDismantleable;
import cofh.lib.util.helpers.InventoryHelper;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.data.inventory.Slot;
import matteroverdrive.data.inventory.UpgradeSlot;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.items.includes.MOEnergyMatterBlockItem;
import matteroverdrive.tile.IMOTileEntity;
import matteroverdrive.tile.MOTileEntityMachine;
import matteroverdrive.util.IConfigSubscriber;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.MatterHelper;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;

/**
 * Created by Simeon on 4/5/2015.
 */
public abstract class MOBlockMachine extends MOBlockContainer implements IDismantleable, IConfigSubscriber
{
    public float volume;
    public boolean hasGui;

    public MOBlockMachine(Material material, String name)
    {
        super(material, name);
    }

    @Override
    protected void registerBlock()
    {
        GameRegistry.registerBlock(this, MOEnergyMatterBlockItem.class, this.getUnlocalizedName().substring(5));
    }

    public boolean doNormalDrops(World world, int x, int y, int z)
    {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);

        IMOTileEntity entity = (IMOTileEntity) world.getTileEntity(x, y, z);
        if (entity != null) {
            try {
                entity.readFromPlaceItem(itemStack);
            } catch (Exception e) {
                e.printStackTrace();
                FMLLog.log(Level.ERROR,"Could not load settings from placing item",e);
            }

            entity.onPlaced(world, entityLiving);
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        super.breakBlock(world,x,y,z,block,meta);

        //drops inventory
        MOTileEntityMachine machine = (MOTileEntityMachine) world.getTileEntity(x, y, z);
        if (machine != null)
            MatterHelper.DropInventory(world, (MOTileEntityMachine) world.getTileEntity(x, y, z), x, y, z);
    }

    @Override
    public boolean onBlockActivated(World world,int x,int y,int z,EntityPlayer player,int side,float hitX,float hitY,float hitZ)
    {
        if(!world.isRemote && hasGui)
        {
            TileEntity tileEntity = world.getTileEntity(x,y,z);
            if (tileEntity instanceof MOTileEntityMachine)
            {
                if (((MOTileEntityMachine) tileEntity).isUseableByPlayer(player)) {
                    FMLNetworkHandler.openGui(player, MatterOverdrive.instance, -1, world, x, y, z);
                    return true;
                }else
                {
                    ChatComponentText message = new ChatComponentText(EnumChatFormatting.GOLD + "[Matter Overdrive] " + EnumChatFormatting.RED + MOStringHelper.translateToLocal(getUnlocalizedMessage(0)).replace("$0", getLocalizedName()));
                    message.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED));
                    player.addChatMessage(message);
                }
            }
        }

        return false;
    }

    protected String getUnlocalizedMessage(int type)
    {
        switch (type)
        {
            case 0:
                return "alert.no_rights";
            default:
                return  "alert.no_access_default";
        }
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
    {
        TileEntity tileEntity = world.getTileEntity(x,y,z);
        if (tileEntity != null && tileEntity instanceof MOTileEntityMachine)
        {
            if (!player.capabilities.isCreativeMode && ((MOTileEntityMachine) tileEntity).hasOwner() && !((MOTileEntityMachine) tileEntity).getOwner().equals(player.getGameProfile().getId()))
            {
                    ChatComponentText message = new ChatComponentText(EnumChatFormatting.GOLD + "[Matter Overdrive] " + EnumChatFormatting.RED + MOStringHelper.translateToLocal("alert.no_rights.brake").replace("$0",getLocalizedName()));
                    message.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED));
                    player.addChatMessage(message);
                return false;
            }
        }
        return world.setBlockToAir(x, y, z);
    }

    public ItemStack getNBTDrop(World world, int x, int y, int z, IMOTileEntity te)
    {
        int meta = damageDropped(world.getBlockMetadata(x,y,z));
        ItemStack itemStack = new ItemStack(this, 1, meta);
        if(te != null)
            te.writeToDropItem(itemStack);
        return itemStack;
    }

    public boolean hasGui() {
        return hasGui;
    }

    public void setHasGui(boolean hasGui) {
        this.hasGui = hasGui;
    }

    @Override
    public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, int x, int y, int z, boolean returnDrops)
    {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        ItemStack blockItem = getNBTDrop(world, x, y, z, (IMOTileEntity) world.getTileEntity(x, y, z));
        MOTileEntityMachine machine = (MOTileEntityMachine)world.getTileEntity(x,y,z);
        items.add(blockItem);

        Block block = world.getBlock(x, y, z);
        int l = world.getBlockMetadata(x, y, z);
        boolean flag = block.removedByPlayer(world, player, x, y, z, true);
        super.breakBlock(world,x,y,z,block,l);

        if (flag)
        {
            block.onBlockDestroyedByPlayer(world, x, y, z, l);
        }

        if (!returnDrops)
        {
            for (int i1 = 0; i1 < machine.getInventoryContainer().getSizeInventory(); ++i1)
            {
                Slot slot = machine.getInventoryContainer().getSlot(i1);
                ItemStack itemstack = slot.getItem();

                if (itemstack != null && !(slot instanceof UpgradeSlot))
                {
                    float f = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = world.rand.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0)
                    {
                        int j1 = world.rand.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize)
                        {
                            j1 = itemstack.stackSize;
                        }
                        itemstack.stackSize -= j1;
                        EntityItem entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }

                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)world.rand.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)world.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)world.rand.nextGaussian() * f3);
                        world.spawnEntityInWorld(entityitem);
                    }
                }
            }

            dropBlockAsItem(world, x, y, z, blockItem);
        }
        else
        {
            InventoryHelper.insertItemStackIntoInventory(player.inventory, blockItem, 0);
        }

        return items;
    }

    @Override
    public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getTileEntity(x,y,z);
        if (tileEntity instanceof MOTileEntityMachine)
        {
            if (player.capabilities.isCreativeMode || !((MOTileEntityMachine) tileEntity).hasOwner())
            {
                return true;
            }else
            {
                if (((MOTileEntityMachine) tileEntity).getOwner().equals(player.getGameProfile().getId()))
                {
                    return true;
                }else
                {
                    if (world.isRemote) {
                        ChatComponentText message = new ChatComponentText(EnumChatFormatting.GOLD + "[Matter Overdrive] " + EnumChatFormatting.RED + MOStringHelper.translateToLocal("alert.no_rights.dismantle").replace("$0",getLocalizedName()));
                        message.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED));
                        player.addChatMessage(message);
                    }
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config)
    {
        config.initMachineCategory(getUnlocalizedName());
        volume = (float)config.getMachineDouble(getUnlocalizedName(), "volume",1,"The volume of the Machine");
    }
}
