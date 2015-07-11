package matteroverdrive.items;

import cofh.lib.util.position.BlockPosition;
import com.mojang.realmsclient.gui.ChatFormatting;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.Reference;
import matteroverdrive.api.IScannable;
import matteroverdrive.api.matter.IMatterDatabase;
import matteroverdrive.client.sound.MachineSound;
import matteroverdrive.gui.GuiMatterScanner;
import matteroverdrive.handler.KeyHandler;
import matteroverdrive.handler.SoundHandler;
import matteroverdrive.items.includes.MOBaseItem;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.MOPhysicsHelper;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.MatterDatabaseHelper;
import matteroverdrive.util.MatterHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class MatterScanner extends MOBaseItem
{
	public static final String SELECTED_TAG_NAME = "lastSelected";
	public static final String PAGE_TAG_NAME = "page";
	public static final String PANEL_OPEN_TAG_NAME = "panelOpen";
	public static final int PROGRESS_PER_ITEM = 10;
	public static final int SCAN_TIME = 60;
	@SideOnly(Side.CLIENT)
	public static MachineSound scanningSound;
	public static IIcon offline_icon;

	public MatterScanner(String name)
	{
		super(name);
	}

	/**
	 * Returns the icon index of the stack given as argument.
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconIndex(ItemStack itemStack)
	{
		if (itemStack.hasTagCompound() && Minecraft.getMinecraft().theWorld != null)
		{
			if (isLinked(itemStack))
				return this.itemIcon;
		}
		return offline_icon;
	}

	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List infos, boolean p_77624_4_)
	{
		if(hasDetails(itemstack))
		{
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
			{
				addDetails(itemstack,player,infos);
			}
			else
			{
				infos.add(MOStringHelper.MORE_INFO);
				infos.add("Press '"+ EnumChatFormatting.YELLOW + Keyboard.getKeyName(ClientProxy.keyHandler.getBinding(KeyHandler.MATTER_SCANNER_KEY).getKeyCode()) + EnumChatFormatting.GRAY + "' to open GUI");
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon(this.getIconString());
		offline_icon = iconRegister.registerIcon(Reference.MOD_ID + ":" + "matter_scanner_offline");
	}

	@Override
	public void addDetails(ItemStack itemstack, EntityPlayer player, List infos)
	{
		if (isLinked(itemstack))
		{
			if (itemstack.hasTagCompound()) {
				infos.add(ChatFormatting.GREEN + "Online");
			}

			NBTTagCompound lastSelected = getSelectedAsNBT(itemstack);
			if(lastSelected != null) {
				infos.add("Progress: " + lastSelected.getByte(MatterDatabaseHelper.PROGRESS_TAG_NAME) + " / " + 100 + " %");
				infos.add("Selected: " + MatterDatabaseHelper.GetItemStackFromNBT(lastSelected).getDisplayName());
			}
		} else {
			infos.add(ChatFormatting.RED + "Offline");
		}
	}

	@Override
	public boolean hasDetails(ItemStack itemStack)
	{
		return true;
	}

	public static IMatterDatabase getLink(World world,ItemStack scanner)
	{
		if(scanner != null && scanner.getItem() instanceof MatterScanner)
		{
			if(scanner.hasTagCompound())
			{
				if(scanner.getTagCompound().getBoolean("isLinked"))
				{
					int x = scanner.getTagCompound().getInteger("link_x");
					int y = scanner.getTagCompound().getInteger("link_y");
					int z = scanner.getTagCompound().getInteger("link_z");

					TileEntity e = world.getTileEntity(x, y, z);
					if (e instanceof IMatterDatabase)
						return (IMatterDatabase) e;
				}
			}
		}
		return null;
	}

	public static BlockPosition getLinkPosition(ItemStack scanner)
	{
		if(scanner != null && scanner.getItem() instanceof MatterScanner) {
			if (scanner.hasTagCompound())
			{
				return new BlockPosition(scanner.getTagCompound().getInteger("link_x"),scanner.getTagCompound().getInteger("link_y"),scanner.getTagCompound().getInteger("link_z"));
			}
		}
		return new BlockPosition(0,0,0);
	}

	public static boolean isLinked(ItemStack scanner)
	{
		if(scanner != null && scanner.getItem() instanceof MatterScanner) {
			if (scanner.hasTagCompound()) {
				return scanner.getTagCompound().getBoolean("isLinked");
			}
		}
		return false;
	}

	public static void unLink(World world,ItemStack scanner)
	{
		if(scanner.hasTagCompound())
		{
			scanner.getTagCompound().setBoolean("isLinked", false);
		}
	}

	public static void link(World world, int xCoord, int yCoord, int zCoord,ItemStack scanner)
	{
		if(scanner.getItem() instanceof MatterScanner)
		{
			((MatterScanner)scanner.getItem()).TagCompountCheck(scanner);
		}

		if(scanner.hasTagCompound())
		{
			scanner.getTagCompound().setBoolean("isLinked",true);
			scanner.getTagCompound().setInteger("link_x", xCoord);
			scanner.getTagCompound().setInteger("link_y", yCoord);
			scanner.getTagCompound().setInteger("link_z", zCoord);
		}
	}

	public int getItemStackLimit(ItemStack item)
	{
		return 1;
	}

	private void resetScanProgress(ItemStack item)
	{
		if(item.hasTagCompound())
		{
			item.getTagCompound().setInteger(MatterDatabaseHelper.PROGRESS_TAG_NAME,0);
		}
	}

	private boolean HarvestBlock(ItemStack scanner,EntityPlayer player,World world,int x,int y,int z)
	{
		if(!world.isRemote)
		{
			ItemStack item = MatterDatabaseHelper.GetItemStackFromWorld(world, x, y, z);
			return world.func_147480_a(x, y, z, false);
		}

		return false;
	}



	public static void setSelected(World world,ItemStack scanner,ItemStack itemStack)
	{
		if(scanner.hasTagCompound())
		{
			NBTTagCompound tagCompound = getSelectedFromDatabase(world,scanner,itemStack);
            if (tagCompound == null)
            {
                tagCompound = new NBTTagCompound();
                itemStack.writeToNBT(tagCompound);
            }

			setSelected(scanner, tagCompound);
		}
	}

	public static void setSelected(ItemStack scanner,NBTTagCompound tagCompound)
	{
		if(scanner.hasTagCompound())
		{
			scanner.getTagCompound().setTag(SELECTED_TAG_NAME, tagCompound);
		}
	}

	public static NBTTagCompound getSelectedAsNBT(ItemStack scanner)
	{
		if(scanner.hasTagCompound() && scanner.getTagCompound().hasKey(SELECTED_TAG_NAME))
		{
			return scanner.getTagCompound().getCompoundTag(SELECTED_TAG_NAME);
		}
		return null;
	}

	public static ItemStack getSelectedAsItem(ItemStack scanner)
	{
		if(scanner.hasTagCompound())
		{
			return ItemStack.loadItemStackFromNBT(scanner.getTagCompound().getCompoundTag(SELECTED_TAG_NAME));
		}
		return null;
	}

    public static NBTTagCompound getSelectedFromDatabase(World world,ItemStack scanner,ItemStack forItem)
    {
        IMatterDatabase database = getLink(world,scanner);
        if (database != null)
        {
            return database.getItemAsNBT(forItem);
        }
        return null;
    }

	
	@Override
	public void InitTagCompount(ItemStack stack)
	{
        MatterDatabaseHelper.InitTagCompound(stack);
	}

	@SideOnly(Side.CLIENT)
    public static void DisplayGuiScreen()
    {
		if(MatterHelper.isMatterScanner(Minecraft.getMinecraft().thePlayer.getHeldItem()))
		{
			Minecraft.getMinecraft().displayGuiScreen(new GuiMatterScanner(Minecraft.getMinecraft().thePlayer.getHeldItem(),Minecraft.getMinecraft().thePlayer.inventory.currentItem));
			return;
		}

		for (int i = 0; i < Minecraft.getMinecraft().thePlayer.inventory.getSizeInventory(); i++)
		{
			if (MatterHelper.isMatterScanner(Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i)))
			{
				Minecraft.getMinecraft().displayGuiScreen(new GuiMatterScanner(Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(i),i));
				return;
			}
		}
    }

	public static int getLastPage(ItemStack scanner)
	{
		if(scanner.hasTagCompound())
		{
			return scanner.getTagCompound().getShort(PAGE_TAG_NAME);
		}
		return 0;
	}

	/**
	 * returns the action that specifies what animation to play when the items is being used
	 */
	public EnumAction getItemUseAction(ItemStack p_77661_1_)
	{
		return EnumAction.block;
	}

	/**
	 * How long it takes to use or consume an item
	 */
	public int getMaxItemUseDuration(ItemStack scanner)
	{
        ItemStack selected = getSelectedAsItem(scanner);
        if (selected != null)
        {
            if (MatterHelper.CanScan(selected))
            {
                return SCAN_TIME + MatterHelper.getMatterAmountFromItem(selected);
            }
        }

        return Integer.MAX_VALUE;
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		MovingObjectPosition hit = getScanningPos(entityplayer);
		if( hit != null && hit.typeOfHit != MovingObjectPosition.MovingObjectType.MISS)
		{
			if (world.isRemote)
				playSound(entityplayer.posX, entityplayer.posY, entityplayer.posZ);

			entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
		}
		return itemstack;
	}

	@Override
	public void onUpdate(ItemStack itemStack,World world, Entity entity, int p_77663_4_, boolean p_77663_5_)
	{
		super.onUpdate(itemStack,world,entity,p_77663_4_,p_77663_5_);

		if (world.isRemote) {
			if (entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				if (player.isUsingItem())
				{

				} else
				{
					stopScanSounds();
				}
			}
		}
	}

	public ItemStack onEaten(ItemStack scanner, World world, EntityPlayer player)
	{
		if(world.isRemote)
		{
			stopScanSounds();
			return scanner;
		}

		MovingObjectPosition position = getScanningPos(player);
		if (position != null && position.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			if (!world.isRemote) {
				int x = position.blockX;
				int y = position.blockY;
				int z = position.blockZ;
				ItemStack worldItem = MatterDatabaseHelper.GetItemStackFromWorld(world, x, y, z);

				//finished scanning
				Scan(world, scanner, player, worldItem, x, y, z);
			}
		}
		return scanner;
	}

	public MovingObjectPosition getScanningPos(EntityPlayer player)
	{
		return MOPhysicsHelper.rayTrace(player, player.worldObj, 5, 0, Vec3.createVectorHelper(0, player.getEyeHeight(), 0), true, false);
	}

	@Override
	public void onUsingTick(ItemStack scanner, EntityPlayer player, int count)
	{
		MovingObjectPosition hit = getScanningPos(player);

		if (hit != null) {

            if (hit.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                int x = hit.blockX;
                int y = hit.blockY;
                int z = hit.blockZ;
                ItemStack lastSelected = getSelectedAsItem(scanner);
                ItemStack worldItem = MatterDatabaseHelper.GetItemStackFromWorld(player.worldObj, x, y, z);

                if (!MatterDatabaseHelper.areEqual(lastSelected, worldItem)) {

                    setSelected(player.worldObj,scanner, worldItem);

                    player.stopUsingItem();
                    if (player.worldObj.isRemote) {
                        stopScanSounds();
                    }
                }
            }
        }
		else
		{
			if (player.worldObj.isRemote)
			{
				stopScanSounds();
				player.stopUsingItem();
			}
		}
	}

	@SideOnly(Side.CLIENT)
	private void playSound(double x,double y,double z)
	{
		if(scanningSound == null)
		{
			scanningSound = new MachineSound(new ResourceLocation(Reference.MOD_ID + ":" +"scanner_scanning"),(float)x,(float)y,(float)z,0.6f,1);
			Minecraft.getMinecraft().getSoundHandler().playSound(scanningSound);
		}
	}

	public void onPlayerStoppedUsing(ItemStack scanner, World world, EntityPlayer player, int count)
	{
		if (world.isRemote)
			stopScanSounds();
	}

	@SideOnly(Side.CLIENT)
	private void stopScanSounds()
	{
		if(scanningSound != null)
		{
			scanningSound.stopPlaying();
			scanningSound = null;
		}
	}

	public boolean Scan(World world,ItemStack scanner,EntityPlayer player,ItemStack worldBlock,int x,int y,int z)
	{
		this.TagCompountCheck(scanner);

        StringBuilder scanInfo = new StringBuilder();
		IMatterDatabase database = getLink(world, scanner);

		if (database != null && MatterHelper.CanScan(worldBlock))
		{
			resetScanProgress(scanner);
			scanInfo.append(EnumChatFormatting.YELLOW + "[" + scanner.getDisplayName() + "] ");

			if (database.addItem(worldBlock, PROGRESS_PER_ITEM,false,scanInfo)) {
				//scan successful
				SoundHandler.PlaySoundAt(world, "scanner_success", player);
				DisplayInfo(player, scanInfo, EnumChatFormatting.GREEN);
				return HarvestBlock(scanner, player, world, x, y, z);
			}
			else
			{
				//scan fail
				DisplayInfo(player, scanInfo, EnumChatFormatting.RED);
				SoundHandler.PlaySoundAt(world, "scanner_fail", player);
				return false;
			}
		}else
		{
            if (world.getBlock(x,y,z) instanceof IScannable)
            {
                ((IScannable) world.getBlock(x,y,z)).onScan(world,x,y,z,player,scanner);
                //DisplayInfo(player, scanInfo, EnumChatFormatting.GREEN);
                return true;
            }else if (world.getTileEntity(x,y,z) instanceof IScannable)
            {
                ((IScannable) world.getBlock(x,y,z)).onScan(world,x,y,z,player,scanner);
                return true;
            }
		}

		return false;
	}

	private void DisplayInfo(EntityPlayer player,StringBuilder scanInfo,EnumChatFormatting formatting)
	{
		if (player != null && !scanInfo.toString().isEmpty())
		{
			player.addChatMessage(new ChatComponentText(formatting + scanInfo.toString()));
		}
	}
}
