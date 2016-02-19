package matteroverdrive.machines.dimensional_pylon;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.blocks.BlockPylon;
import matteroverdrive.client.data.Color;
import matteroverdrive.client.render.RenderParticlesHandler;
import matteroverdrive.data.MachineEnergyStorage;
import matteroverdrive.fluids.FluidMatterPlasma;
import matteroverdrive.fx.Lightning;
import matteroverdrive.fx.LightningCircle;
import matteroverdrive.init.MatterOverdriveBlocks;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.events.MachineEvent;
import matteroverdrive.network.packet.client.PacketSpawnParticle;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.tile.MOTileEntityMachineMatter;
import matteroverdrive.util.MOLog;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.RenderUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

/**
 * Created by Simeon on 2/4/2016.
 */
public class TileEntityMachineDimensionalPylon extends MOTileEntityMachineMatter
{
    public static int MAX_CHARGE = 2048;
    BlockPos mainBlock;
    List<BlockPos> children;
    ComponentPowerGeneration powerGeneration;
    int charge;

    public TileEntityMachineDimensionalPylon()
    {
        super(4);
        this.matterStorage.setCapacity(2048);
        this.matterStorage.setMaxExtract(0);
        this.matterStorage.setMaxReceive(128);
        this.energyStorage.setCapacity(1000000);
        this.energyStorage.setMaxExtract(2048);
        this.energyStorage.setMaxReceive(0);
    }

    @Override
    public String getSound()
    {
        return "pylon";
    }

    @Override
    public boolean hasSound()
    {
        return true;
    }

    @Override
    public boolean getServerActive()
    {
        return mainBlock != null && children != null && children.size() > 0;
    }

    @Override
    public float soundVolume()
    {
        return 0.3f * getDimensionalValue();
    }

    @Override
    protected void registerComponents()
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
        {
            super.registerComponents();
        }
    }
    private void registerPylonComponents()
    {
        super.registerComponents();
        powerGeneration = new ComponentPowerGeneration(this);
        addComponent(powerGeneration);
    }

    public float getDimensionalValue()
    {
        if (mainBlock != null)
        {
            return MatterOverdrive.moWorld.getDimensionalRifts().getValueAt(mainBlock);
        }return MatterOverdrive.moWorld.getDimensionalRifts().getValueAt(getPos());
    }

    @Override
    public void update()
    {
        super.update();
        if (worldObj.isRemote)
        {
            manageLightningClientLightning();
        }
    }

    @SideOnly(Side.CLIENT)
    public void manageLightningClientLightning()
    {
        if (isActive())
        {
            Color color = RenderUtils.lerp(Reference.COLOR_MATTER.multiplyWithoutAlpha(0.5f),Reference.COLOR_HOLO_RED,(float)charge / (float)MAX_CHARGE);
            if (getWorld().getWorldTime() % 2 == 0)
            {
                float dimValue = getDimensionalValue();
                if (random.nextFloat() < dimValue)
                {
                    double y = getPos().getY() + 0.5 + random.nextDouble() * 2;
                    double dirX = MathHelper.clamp_double(random.nextGaussian(), -1, 1) * 0.1;
                    double dirZ = MathHelper.clamp_double(random.nextGaussian(), -1, 1) * 0.1;
                    LightningCircle lightning = new LightningCircle(getWorld(), getPos().getX() - dirX, y, getPos().getZ() - dirZ, 0.03f, 1, 0.3f, 0.2f);
                    lightning.setColorRGBA(color);
                    ClientProxy.renderHandler.getRenderParticlesHandler().addEffect(lightning, RenderParticlesHandler.Blending.LinesAdditive);
                }
            }
        }
    }

    public void addCharge(int charge)
    {
        if (mainBlock != null && !mainBlock.equals(getPos()))
        {
            TileEntity tileEntity = worldObj.getTileEntity(mainBlock);
            if (tileEntity != null && tileEntity instanceof TileEntityMachineDimensionalPylon)
            {
                ((TileEntityMachineDimensionalPylon) tileEntity).addCharge(charge);
            }
        }else
        {
            this.charge += charge;
            forceSync();
        }
    }

    public int removeCharge(int charge)
    {
        if (mainBlock != null && !mainBlock.equals(getPos()))
        {
            TileEntity tileEntity = worldObj.getTileEntity(mainBlock);
            if (tileEntity != null && tileEntity instanceof TileEntityMachineDimensionalPylon)
            {
                return ((TileEntityMachineDimensionalPylon) tileEntity).removeCharge(charge);
            }
        }else
        {
            charge = Math.min(this.charge,charge);
            this.charge -= charge;
            if (charge != 0)
            {
                forceSync();
            }
            return charge;
        }
        return 0;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk)
    {
        super.writeCustomNBT(nbt, categories, toDisk);
        if (categories.contains(MachineNBTCategory.DATA))
        {
            if (mainBlock != null && toDisk)
            {
                nbt.setLong("mainBlock", mainBlock.toLong());
            }

            nbt.setShort("charge",(short) charge);
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
    {
        super.readCustomNBT(nbt, categories);
        if (categories.contains(MachineNBTCategory.DATA))
        {
            if (nbt.hasKey("mainBlock"))
            {
                mainBlock = BlockPos.fromLong(nbt.getLong("mainBlock"));
            }
            if (nbt.hasKey("charge"))
            {
                charge = nbt.getShort("charge");
            }
        }
    }

    @Override
    public boolean isActive()
    {
        if (isPartOfStructure() && !mainBlock.equals(getPos()))
        {
            TileEntity tileEntity = worldObj.getTileEntity(mainBlock);
            if (tileEntity != null && tileEntity instanceof TileEntityMachineDimensionalPylon)
            {
                return ((TileEntityMachineDimensionalPylon) tileEntity).isActive();
            }
        }
        return super.isActive();
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid)
    {
        if (from == EnumFacing.DOWN)
        {
            return fluid instanceof FluidMatterPlasma;
        }
        return false;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid)
    {
        if (from == EnumFacing.DOWN)
        {
            return fluid instanceof FluidMatterPlasma;
        }
        return false;
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return from.equals(EnumFacing.DOWN);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        return direction.equals(EnumFacing.DOWN);
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        if (direction.equals(EnumFacing.DOWN))
        {
            return isItemValidForSlot(index, itemStackIn);
        }
        return false;
    }

    @Override
    protected void onMachineEvent(MachineEvent event)
    {
        if (event instanceof MachineEvent.Destroyed)
        {
            if (children != null)
            {
                invalidateChildren();
            }
            else if (mainBlock != null && !mainBlock.equals(getPos()))
            {
                TileEntity tileEntity = worldObj.getTileEntity(mainBlock);
                if (tileEntity instanceof TileEntityMachineDimensionalPylon)
                {
                    ((TileEntityMachineDimensionalPylon) tileEntity).removeChild(getPos());
                }
            }
        }else if (event instanceof MachineEvent.Awake)
        {
            if (mainBlock != null)
            {
                components.clear();
                TileEntity tileEntity = worldObj.getTileEntity(mainBlock);
                if (tileEntity instanceof TileEntityMachineDimensionalPylon)
                {
                    if (tileEntity != this)
                    {
                        TileEntityMachineDimensionalPylon tileEntityDimensionalPylon = (TileEntityMachineDimensionalPylon) tileEntity;
                        tileEntityDimensionalPylon.addChild(getPos());
                        this.matterStorage = tileEntityDimensionalPylon.getMatterStorage();
                        this.energyStorage = (MachineEnergyStorage<TileEntityMachineDimensionalPylon>) tileEntityDimensionalPylon.getEnergyStorage();
                    }else
                    {
                        registerPylonComponents();
                    }
                }else
                {
                    worldObj.setBlockState(getPos(),worldObj.getBlockState(getPos()).withProperty(BlockPylon.TYPE, BlockPylon.MultiblockType.NORMAL));
                    removeMainBlock();
                }
            }
        }
    }

    public void addChild(BlockPos pos)
    {
        if (children == null)
        {
            children = new ArrayList<>();
            registerPylonComponents();
        }

        if(!children.contains(pos))
        {
            children.add(pos);
        }
    }

    public void removeChild(BlockPos child)
    {
        if (children != null)
        {
            children.remove(child);
        }
        invalidateChildren();
    }

    public void invalidateChildren()
    {
        if (children != null)
        {
            for (BlockPos pos : children)
            {
                IBlockState state = worldObj.getBlockState(pos);
                TileEntity tileEntity = worldObj.getTileEntity(pos);
                worldObj.setBlockState(pos, state.withProperty(BlockPylon.TYPE, BlockPylon.MultiblockType.NORMAL));
                if (tileEntity instanceof TileEntityMachineDimensionalPylon)
                {
                    ((TileEntityMachineDimensionalPylon) tileEntity).removeMainBlock();
                }
            }
        }

        IBlockState state = worldObj.getBlockState(getPos());
        if (state.getBlock() instanceof BlockPylon)
        {
            this.worldObj.setBlockState(getPos(),state.withProperty(BlockPylon.TYPE, BlockPylon.MultiblockType.NORMAL));
        }
        children = null;
        removeMainBlock();
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[0];
    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type)
    {
        return false;
    }

    public boolean onWrenchHit(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (mainBlock == null)
        {
            return tryFormStructure(world,stack,player,pos);
        }
        return false;
    }

    public boolean openMultiBlockGui(World world,EntityPlayer entityPlayer)
    {
        if (mainBlock != null)
        {
            TileEntity mainPylon = worldObj.getTileEntity(mainBlock);
            if (mainPylon instanceof TileEntityMachineDimensionalPylon)
            {
                if (((MOTileEntityMachine) mainPylon).isUseableByPlayer(entityPlayer)) {
                    FMLNetworkHandler.openGui(entityPlayer, MatterOverdrive.instance, -1, world, mainBlock.getX(),mainBlock.getY(),mainBlock.getZ());
                    return true;
                } else
                {
                    ChatComponentText message = new ChatComponentText(EnumChatFormatting.GOLD + "[Matter Overdrive] " + EnumChatFormatting.RED + MOStringHelper.translateToLocal("alert.no_rights").replace("$0", ((MOTileEntityMachine) mainPylon).getDisplayName().toString()));
                    message.setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED));
                    entityPlayer.addChatMessage(message);
                }
            }
        }
        return false;
    }

    public boolean tryFormStructure(World world,ItemStack stack, EntityPlayer player,BlockPos pos)
    {
        if (world.isRemote)
            return false;

        AxisAlignedBB bounds = new AxisAlignedBB(pos,pos);

        for (int i = 0;i < 1;i++)
        {
            //min
            if (isBlockConsumable(world,new BlockPos(bounds.minX,bounds.minY,bounds.minZ).add(-1,0,0)))
            {
                bounds = bounds.addCoord(-1,0,0);
            }

            if (isBlockConsumable(world,new BlockPos(bounds.maxX,bounds.maxY,bounds.maxZ).add(1,0,0)))
            {
                bounds = bounds.addCoord(1,0,0);
            }
        }

        for (int i = 0;i < 1;i++)
        {
            if (isBlockConsumable(world,new BlockPos(bounds.minX,bounds.minY,bounds.minZ).add(0,0,-1)))
            {
                bounds = bounds.addCoord(0,0,-1);
            }
            if (isBlockConsumable(world,new BlockPos(bounds.maxX,bounds.maxY,bounds.maxZ).add(0,0,1)))
            {
                bounds = bounds.addCoord(0,0,1);
            }
        }

        for (int i = 0;i < 2;i++)
        {

            if (isBlockConsumable(world, new BlockPos(bounds.minX, bounds.minY, bounds.minZ).add(0, -1, 0)))
            {
                bounds = bounds.addCoord(0, -1, 0);
            }
            if (isBlockConsumable(world, new BlockPos(bounds.maxX, bounds.maxY, bounds.maxZ).add(0, 1, 0)))
            {
                bounds = bounds.addCoord(0, 1, 0);
            }
        }

        double xLength = bounds.maxX - bounds.minX;
        double yLength = bounds.maxY - bounds.minY;
        double zLength = bounds.maxZ - bounds.minZ;

        List<BlockPos> positions = new ArrayList<>(2*2*3);
        BlockPos mainBlock = null;

        if (xLength == 1 && yLength == 2 && zLength == 1)
        {
            for (int x = 0;x <= 1;x++)
            {
                for (int y = 0;y <= 2;y++)
                {
                    for (int z = 0;z <= 1;z++)
                    {
                        BlockPos p = new BlockPos(bounds.minX + x,bounds.minY + y,bounds.minZ + z);
                        if (!isBlockConsumable(world,p))
                        {
                            MOLog.info("Invalid Structure");
                            return false;
                        }else
                        {
                            if (x == 1 && y == 0 && z == 1)
                            {
                                mainBlock = p;
                            }else
                            {
                                positions.add(p);
                            }
                        }
                    }
                }
            }
        }else
        {
            MOLog.info("Invalid Structure");
            return false;
        }

        if (mainBlock != null)
        {
            for (BlockPos p : positions)
            {
                IBlockState pylonBlockstate = world.getBlockState(p);
                world.setBlockState(p,pylonBlockstate.withProperty(BlockPylon.TYPE, BlockPylon.MultiblockType.DUMMY));
                TileEntityMachineDimensionalPylon pylon = (TileEntityMachineDimensionalPylon)world.getTileEntity(p);
                pylon.setMainBlock(mainBlock);

            }

            IBlockState pylonBlockstate = world.getBlockState(mainBlock);
            world.setBlockState(mainBlock,pylonBlockstate.withProperty(BlockPylon.TYPE, BlockPylon.MultiblockType.MAIN));
            TileEntityMachineDimensionalPylon pylon = (TileEntityMachineDimensionalPylon)world.getTileEntity(mainBlock);
            pylon.children = positions;
            pylon.setMainBlock(mainBlock);
            pylon.registerPylonComponents();
            MOLog.info("Valid Structure");
        }else
        {
            MOLog.info("Invalid Structure");
            return false;
        }
        return true;
    }

    public void setMainBlock(BlockPos mainBlock)
    {
        this.mainBlock = new BlockPos(mainBlock);
        components.clear();
    }

    public void removeMainBlock()
    {
        this.mainBlock = null;
        components.clear();
    }

    private boolean isBlockConsumable(World world,BlockPos pos)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityMachineDimensionalPylon)
        {
            return !((TileEntityMachineDimensionalPylon) tileEntity).isPartOfStructure();
        }
        return false;
    }

    public boolean isPartOfStructure()
    {
        return mainBlock != null;
    }

    public boolean isMainStructureBlock()
    {
        return children != null && children.size() > 0;
    }

    public int getEnergyGenPerTick()
    {
        if (powerGeneration != null)
        {
            return powerGeneration.getEnergyGenPerTick();
        }return 0;
    }

    public int getMatterDrainPerSec()
    {
        if (powerGeneration != null)
        {
            return powerGeneration.getMatterDrainPerSec();
        }return 0;
    }

    public int getCharge(){return charge;}
}
