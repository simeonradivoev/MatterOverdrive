/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.machines.fusionReactorController;


import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyReceiver;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedPeripheral;
import li.cil.oc.api.network.SimpleComponent;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.data.BlockPos;
import matteroverdrive.init.MatterOverdriveBlocks;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.fusionReactorController.components.ComponentComputers;
import matteroverdrive.multiblock.IMultiBlockTile;
import matteroverdrive.multiblock.MultiBlockTileStructureMachine;
import matteroverdrive.tile.MOTileEntityMachineEnergy;
import matteroverdrive.tile.MOTileEntityMachineMatter;
import matteroverdrive.tile.TileEntityFusionReactorPart;
import matteroverdrive.tile.TileEntityGravitationalAnomaly;
import matteroverdrive.util.MOEnergyHelper;
import matteroverdrive.util.TimeTracker;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import java.text.DecimalFormat;
import java.util.EnumSet;

import static java.lang.Math.round;
import static matteroverdrive.util.MOBlockHelper.getAboveSide;
import static matteroverdrive.util.MOBlockHelper.getOppositeSide;

/**
 * Created by Simeon on 5/14/2015.
 */
@Optional.InterfaceList({
		@Optional.Interface(modid = "ComputerCraft", iface = "dan200.computercraft.api.peripheral.IPeripheral"),
		@Optional.Interface(modid = "OpenComputers", iface = "li.cil.oc.api.network.SimpleComponent"),
		@Optional.Interface(modid = "OpenComputers", iface = "li.cil.oc.api.network.ManagedPeripheral")
})
public class TileEntityMachineFusionReactorController extends MOTileEntityMachineMatter implements IPeripheral, SimpleComponent, ManagedPeripheral
{
    public static int STRUCTURE_CHECK_DELAY = 40;
    public static final int[] positions = new int[]{0,5,1,0,2,0,3,1,4,2,5,3,5,4,5,5,5,6,5,7,4,8,3,9,2,10,1,10,0,10,-1,10,-2,10,-3,9,-4,8,-5,7,-5,6,-5,5,-5,4,-5,3,-4,2,-3,1,-2,0,-1,0};
    public static final int[] blocks = new int[]{255,2,0,0,0,0,1,1,1,0,0,0,0,1,1,1,0,0,0,0,1,1,1,0,0,0,0,2};
    public static final int positionsCount = positions.length / 2;
    public static int MAX_GRAVITATIONAL_ANOMALY_DISTANCE = 3;
    public static int ENERGY_STORAGE = 100000000;
    public static int MATTER_STORAGE = 2048;
    public static int ENERGY_PER_TICK = 2048;
    public static double MATTER_DRAIN_PER_TICK = 1.0D / 80.0D;

    private boolean validStructure = false;
    private String monitorInfo = "INVALID STRUCTURE";
    private float energyEfficiency;
    private int energyPerTick;
    private TimeTracker structureCheckTimer;
    private BlockPos anomalyPosition;
    private float matterPerTick;
    private float matterDrain;
    private ComponentComputers componentComputers;
    private MultiBlockTileStructureMachine multiBlock;


    public TileEntityMachineFusionReactorController() {
        super(4);

        structureCheckTimer = new TimeTracker();
        energyStorage.setCapacity(ENERGY_STORAGE);
        energyStorage.setMaxTransfer(ENERGY_STORAGE);
        energyStorage.setMaxReceive(0);

        matterStorage.setCapacity(MATTER_STORAGE);
        matterStorage.setMaxExtract(0);
        matterStorage.setMaxReceive(MATTER_STORAGE);

        multiBlock = new MultiBlockTileStructureMachine(this);
    }

    @Override
    public String getSound() {
        return null;
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
    {
        super.writeCustomNBT(nbt, categories);
        if (categories.contains(MachineNBTCategory.DATA)) {
            nbt.setBoolean("ValidStructure", validStructure);
            nbt.setString("MonitorInfo", monitorInfo);
            nbt.setFloat("EnergyEfficiency", energyEfficiency);
            nbt.setFloat("MatterPerTick", matterPerTick);
            nbt.setInteger("EnergyPerTick", energyPerTick);
        }
    }

    @Override
    protected void onActiveChange() {

    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
    {
        super.readCustomNBT(nbt, categories);
        if (categories.contains(MachineNBTCategory.DATA))
        {
            validStructure = nbt.getBoolean("ValidStructure");
            monitorInfo = nbt.getString("MonitorInfo");
            energyEfficiency = nbt.getFloat("EnergyEfficiency");
            matterPerTick = nbt.getFloat("MatterPerTick");
            energyPerTick = nbt.getInteger("EnergyPerTick");
        }
    }

    @Override
    protected void onAwake(Side side) {

    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (!worldObj.isRemote) {
            //System.out.println("Fusion Reactor Update in chunk that is loaded:" + worldObj.getChunkFromBlockCoords(xCoord,zCoord).isChunkLoaded);
            manageStructure();
            manageEnergyGeneration();
            manageEnergyExtract();
        }
    }

    @Override
    protected void registerComponents()
    {
        super.registerComponents();
        componentComputers = new ComponentComputers(this);
        addComponent(componentComputers);
    }

    @Override
    public boolean hasSound() {
        return false;
    }

    @Override
    public boolean getServerActive() {
        return isValidStructure() &&
                isGeneratingPower();
    }

    @Override
    public float soundVolume() {
        return 0;
    }

    public Vec3 getPosition(int i,int meta)
    {
        if (i < positionsCount)
        {
            ForgeDirection back = ForgeDirection.getOrientation(getOppositeSide(meta));
            Vec3 pos = Vec3.createVectorHelper(TileEntityMachineFusionReactorController.positions[i * 2], 0, TileEntityMachineFusionReactorController.positions[(i * 2) + 1]);

            if (back == ForgeDirection.NORTH)
            {
                pos.rotateAroundY((float)Math.PI);
            }
            else if (back == ForgeDirection.WEST)
            {
                pos.rotateAroundY((float)(Math.PI + Math.PI / 2));
            }
            else if (back == ForgeDirection.EAST)
            {
                pos.rotateAroundY((float)(Math.PI / 2));
            }
            else if (back == ForgeDirection.UP)
            {
                pos.rotateAroundX((float)(Math.PI / 2));
            }
            else if (back == ForgeDirection.DOWN)
            {
                pos.rotateAroundX((float) (Math.PI + Math.PI / 2));

            }

            return pos;
        }
        return null;
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        multiBlock.invalidate();
    }

    public void manageStructure()
    {
        if (structureCheckTimer.hasDelayPassed(worldObj, STRUCTURE_CHECK_DELAY))
        {
            multiBlock.update();
            int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
            int anomalyDistance = MAX_GRAVITATIONAL_ANOMALY_DISTANCE+1;
            boolean validStructure = true;
            String info = this.monitorInfo;
            float energyEfficiency  = this.energyEfficiency;
            float matterPerTick = this.matterPerTick;

            for (int i = 0; i < positionsCount; i++) {
                Vec3 offset = getPosition(i, meta);
                BlockPos position = new BlockPos(xCoord + (int) round(offset.xCoord), yCoord + (int) round(offset.yCoord), zCoord + (int) round(offset.zCoord));

                if (blocks[i] == 255)
                {
                    BlockPos anomalyOffset = checkForGravitationalAnomaly(position, ForgeDirection.getOrientation(getAboveSide(meta)));

                    if (anomalyOffset != null)
                    {
                        anomalyDistance = (int)Math.sqrt((anomalyOffset.x * anomalyOffset.x) + (anomalyOffset.y * anomalyOffset.y) + (anomalyOffset.z * anomalyOffset.z));
                        if (anomalyDistance > MAX_GRAVITATIONAL_ANOMALY_DISTANCE)
                        {
                            validStructure = false;
                            info = "GRAVITATIONAL\nANOMALY\nTOO\nFAR";
                            break;
                        }
                        anomalyPosition = new BlockPos((int) offset.xCoord + anomalyOffset.x, (int) offset.yCoord + anomalyOffset.y, (int) offset.zCoord + anomalyOffset.z);
                    }else
                    {
                        validStructure = false;
                        info = "NO\nGRAVITATIONAL\nANOMALY";
                        anomalyPosition = null;
                        break;
                    }

                    energyEfficiency = 1f - ((float)anomalyDistance / (float)(MAX_GRAVITATIONAL_ANOMALY_DISTANCE+1));
                    energyPerTick = (int)Math.round(ENERGY_PER_TICK * getEnergyEfficiency() * getGravitationalAnomalyEnergyMultiply());
                    double energyMultipy = getGravitationalAnomalyEnergyMultiply();
                    matterPerTick = (float)(MATTER_DRAIN_PER_TICK * energyMultipy);
                }
                else {
                    Block block = position.getBlock(worldObj);
                    TileEntity tileEntity = position.getTileEntity(worldObj);

                    if (block == Blocks.air) {
                        validStructure = false;
                        info = "INVALID\nSTRUCTURE";
                        break;
                    } else if (block == MatterOverdriveBlocks.machine_hull) {
                        if (blocks[i] == 1) {
                            validStructure = false;
                            info = "NEED\nMORE\nCOILS";
                            break;
                        }
                    } else if (block == MatterOverdriveBlocks.fusion_reactor_coil || tileEntity instanceof IMultiBlockTile) {
                        if (blocks[i] == 0) {
                            validStructure = false;
                            info = "INVALID\nMATERIALS";
                            break;
                        }
                    } else if (block == MatterOverdriveBlocks.decomposer)
                    {
                        if (blocks[i] != 2)
                        {
                            validStructure = false;
                            info = "INVALID\nMATERIALS";
                            break;
                        }
                    }
                    else {
                        validStructure = false;
                        info = "INVALID\nMATERIALS";
                        break;
                    }

                    if (tileEntity instanceof IMultiBlockTile)
                    {
                        multiBlock.addMultiBlockTile((IMultiBlockTile)tileEntity);
                    }
                }
            }

            if (validStructure)
            {
                info = "POWER " + Math.round((1f - ((float)anomalyDistance / (float)(MAX_GRAVITATIONAL_ANOMALY_DISTANCE+1))) * 100) + "%";
                info += "\nCHARGE " + DecimalFormat.getPercentInstance().format((double)getEnergyStored(ForgeDirection.UNKNOWN)/(double)getMaxEnergyStored(ForgeDirection.UNKNOWN));
                info += "\nMATTER " + DecimalFormat.getPercentInstance().format((double)getMatterStored()/(double)getMatterCapacity());
            }else
            {
                energyEfficiency = 0;
            }

            if (this.validStructure != validStructure || !this.monitorInfo.equals(info)|| this.energyEfficiency != energyEfficiency || this.matterPerTick != matterPerTick) {
                this.validStructure = validStructure;
                this.monitorInfo = info;
                this.energyEfficiency = energyEfficiency;
                this.matterPerTick = matterPerTick;
                forceSync();
            }
        }
    }

    private void manageEnergyGeneration()
    {
        if (isActive())
        {
            int energyPerTick = getEnergyPerTick();
            int energyRecived = energyStorage.modifyEnergyStored(energyPerTick);
            if (energyRecived != 0)
            {
                matterDrain += getMatterDrainPerTick() * ((float)energyRecived / (float)energyPerTick);
                if (MathHelper.floor_float(matterDrain) >= 1)
                {
                    matterStorage.modifyMatterStored(-MathHelper.floor_float(matterDrain));
                    matterDrain -= MathHelper.floor_float(matterDrain);
                }
            }
        }
    }

    private void manageEnergyExtract()
    {
        if (energyStorage.getEnergyStored() > 0)
        {
            for (IMultiBlockTile tile : multiBlock.getTiles())
            {
                if (tile instanceof TileEntityFusionReactorPart)
                {
                    manageExtractFrom((TileEntityFusionReactorPart)tile);
                }
            }
        }

        manageExtractFrom(this);
    }

    private void manageExtractFrom(MOTileEntityMachineEnergy source)
    {
        TileEntity entity;
        int energy;
        int startDir = random.nextInt(6);

        for (int i = 0; i < 6; i++) {
            energy = Math.min(energyStorage.getEnergyStored(), ENERGY_STORAGE);
            ForgeDirection dir = ForgeDirection.getOrientation((i + startDir) % 6);
            entity = worldObj.getTileEntity(source.xCoord + dir.offsetX, source.yCoord + dir.offsetY, source.zCoord + dir.offsetZ);

            if (entity instanceof IEnergyConnection) {
                ((IEnergyConnection) entity).canConnectEnergy(dir.getOpposite());
            }

            if (entity instanceof IEnergyReceiver) {
                int receivedEnergy = ((IEnergyReceiver) entity).receiveEnergy(dir.getOpposite(), energy, false);
                modifyEnergyStored(-receivedEnergy);
            }
        }
    }

    @Override
    public boolean isCharging()
    {
        return this.inventory.getStackInSlot(energySlotID) != null && MOEnergyHelper.isEnergyContainerItem(this.inventory.getStackInSlot(energySlotID));
    }

    @Override
    protected void manageCharging()
    {
        if(isCharging())
        {
            if(!this.worldObj.isRemote)
            {
                int maxExtracted = Math.min(energyStorage.getMaxExtract(),energyStorage.getEnergyStored());
                int extracted = MOEnergyHelper.insertEnergyIntoContainer(this.inventory.getStackInSlot(energySlotID),maxExtracted,false);
                modifyEnergyStored(extracted);
            }
        }
    }

    public int getEnergyPerTick()
    {
        return energyPerTick;
    }

    public double getGravitationalAnomalyEnergyMultiply()
    {
        if (anomalyPosition != null)
        {
            TileEntity entity = worldObj.getTileEntity(xCoord + anomalyPosition.x, yCoord + anomalyPosition.y, zCoord + anomalyPosition.z);
            if (entity instanceof TileEntityGravitationalAnomaly)
            {
                return ((TileEntityGravitationalAnomaly) entity).getRealMassUnsuppressed();
            }
        }
        return 0;
    }

    public float getMatterDrainPerTick()
    {
        return matterPerTick;
    }

    public boolean isGeneratingPower()
    {
        return getEnergyEfficiency() > 0
                && getEnergyStorage().getEnergyStored() < getEnergyStorage().getMaxEnergyStored()
                && getMatterStorage().getMatterStored() > getMatterDrainPerTick();
    }

    public float getEnergyEfficiency()
    {
        return energyEfficiency;
    }

    private BlockPos checkForGravitationalAnomaly(BlockPos position, ForgeDirection up)
    {
        int offsetX,offsetY,offsetZ;

        for (int i = -MAX_GRAVITATIONAL_ANOMALY_DISTANCE; i < MAX_GRAVITATIONAL_ANOMALY_DISTANCE+1;i++)
        {
            offsetX = up.offsetX * i;
            offsetY = up.offsetY * i;
            offsetZ = up.offsetZ * i;
            Block block = worldObj.getBlock(position.x + offsetX, position.y + offsetY, position.z + offsetZ);
            if (block != null && block == MatterOverdriveBlocks.gravitational_anomaly)
            {
                return new BlockPos(offsetX, offsetY, offsetZ);
            }
        }

        return null;
    }

    public boolean shouldRenderInPass(int pass)
    {
        return pass == 1;
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        ForgeDirection backSide = ForgeDirection.getOrientation(getOppositeSide(worldObj.getBlockMetadata(xCoord,yCoord,zCoord)));
        return AxisAlignedBB.getBoundingBox(xCoord,yCoord,zCoord,xCoord + backSide.offsetX * 10,yCoord + backSide.offsetY * 10,zCoord + backSide.offsetZ * 10);
    }

    public boolean isValidStructure()
    {
        return validStructure;
    }

    public String getMonitorInfo()
    {
        return monitorInfo;
    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type)
    {
        return type == UpgradeTypes.PowerStorage || type == UpgradeTypes.Range || type == UpgradeTypes.Speed;
    }

    @Override
    public void onAdded(World world, int x, int y, int z) {

    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    public void onDestroyed() {

    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        return false;
    }

    //region All Computers
    //region ComputerCraft
	@Override
	@Optional.Method(modid = "ComputerCraft")
	public String getType() {
		return componentComputers.getType();
	}

	@Override
	@Optional.Method(modid = "ComputerCraft")
	public String[] getMethodNames() {
		return componentComputers.getMethodNames();
	}

	@Override
	@Optional.Method(modid = "ComputerCraft")
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		return componentComputers.callMethod(computer,context,method,arguments);
	}

	@Override
	@Optional.Method(modid = "ComputerCraft")
	public void attach(IComputerAccess computer) {componentComputers.attach(computer);}

	@Override
	@Optional.Method(modid = "ComputerCraft")
	public void detach(IComputerAccess computer) {componentComputers.detach(computer);}

	@Override
	@Optional.Method(modid = "ComputerCraft")
	public boolean equals(IPeripheral other) {
		return componentComputers.equals(other);
	}
    //endregion
    //region OpenComputers
	@Override
	@Optional.Method(modid = "OpenComputers")
	public String getComponentName() {
		return componentComputers.getComponentName();
	}

	@Override
	@Optional.Method(modid = "OpenComputers")
	public String[] methods() {
		return componentComputers.methods();
	}

	@Override
	@Optional.Method(modid = "OpenComputers")
	public Object[] invoke(String method, Context context, Arguments args) throws Exception {
		return componentComputers.invoke(method,context,args);
	}
    //endregion
    //endregion
}
