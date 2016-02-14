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

package matteroverdrive.tile.pipes;

import cpw.mods.fml.relauncher.Side;
import matteroverdrive.api.transport.IPipe;
import matteroverdrive.data.MatterStorage;
import matteroverdrive.data.transport.FluidPipeNetwork;
import matteroverdrive.data.transport.IFluidPipe;
import matteroverdrive.fluids.FluidMatterPlasma;
import matteroverdrive.init.MatterOverdriveFluids;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.util.FluidNetworkHelper;
import matteroverdrive.util.TimeTracker;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 3/7/2015.
 */
public class TileEntityMatterPipe extends TileEntityPipe implements IFluidPipe
{
    protected MatterStorage storage;
    protected FluidPipeNetwork fluidPipeNetwork;
    public  static Random rand = new Random();
    protected int transferSpeed;
    TimeTracker t;

    public TileEntityMatterPipe()
    {
        t = new TimeTracker();
        storage = new MatterStorage(32);
        this.transferSpeed = 10;
    }

    @Override
    public  void updateEntity()
    {
        super.updateEntity();
        if (!worldObj.isRemote)
        {
            manageNetwork();
            manageTransfer();
        }
    }

    public void manageNetwork()
    {
        if (fluidPipeNetwork == null)
        {
            FluidNetworkHelper.getFluidPipeNetworkFromPool().addPipe(this);
        }

        FluidPipeNetwork lastNetwork = null;
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            TileEntity tileEntity = worldObj.getTileEntity(xCoord+direction.offsetX,yCoord+direction.offsetY,zCoord+direction.offsetZ);
            if (tileEntity instanceof IFluidPipe)
            {
                if (lastNetwork == null || ((IFluidPipe) tileEntity).getNetwork() != null && lastNetwork.getNetworkPipes().size() < ((IFluidPipe) tileEntity).getNetwork().getNetworkPipes().size())
                {
                    lastNetwork = ((IFluidPipe) tileEntity).getNetwork();
                }
            }
        }
        if (lastNetwork != null && lastNetwork != getNetwork())
        {
            lastNetwork.addPipe(this);
            //MatterOverdrive.log.info("Matter Pipe Added to network");
        }
    }

    public void manageTransfer()
    {
        if (getMatterStored() > 0 && getNetwork() != null)
        {
            for (IFluidPipe pipe : getNetwork().getFluidHandlers())
            {
                for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
                {
                    TileEntity fluidHandler = pipe.getTile().getWorldObj().getTileEntity(pipe.getTile().xCoord + direction.offsetX,pipe.getTile().yCoord + direction.offsetY,pipe.getTile().zCoord + direction.offsetZ);
                    if (fluidHandler != null && fluidHandler instanceof IFluidHandler && !(fluidHandler instanceof  IFluidPipe) && getMatterStored() > 0 && ((IFluidHandler)fluidHandler).canFill(ForgeDirection.UNKNOWN,MatterOverdriveFluids.matterPlasma))
                    {
                        extractMatter(ForgeDirection.UNKNOWN,((IFluidHandler)fluidHandler).fill(ForgeDirection.UNKNOWN,new FluidStack(MatterOverdriveFluids.matterPlasma,getMatterStored()),true),false);
                    }
                }

            }
        }
    }

    @Override
    public boolean canConnectTo(TileEntity entity,ForgeDirection direction)
    {
        if (entity != null)
        {
            if (entity instanceof IFluidHandler)
            {
                return ((IFluidHandler) entity).canDrain(direction, MatterOverdriveFluids.matterPlasma) || ((IFluidHandler) entity).canFill(direction,MatterOverdriveFluids.matterPlasma);
            }
        }
        return false;
    }

    @Override
    public void writeCustomNBT(NBTTagCompound comp, EnumSet<MachineNBTCategory> categories, boolean toDisk)
    {
        if(!worldObj.isRemote && categories.contains(MachineNBTCategory.DATA) && toDisk)
        {
            storage.writeToNBT(comp);
        }
    }

    @Override
    public  void  readCustomNBT(NBTTagCompound comp, EnumSet<MachineNBTCategory> categories)
    {
        if (categories.contains(MachineNBTCategory.DATA)) {
            storage.readFromNBT(comp);
        }
    }

    @Override
    protected void onAwake(Side side)
    {

    }

    @Override
    public int getMatterStored()
    {
        return storage.getMatterStored();
    }

    @Override
    public int getMatterCapacity()
    {
        return storage.getCapacity();
    }

    @Override
    public int receiveMatter(ForgeDirection side, int amount, boolean simulate)
    {
        return storage.receiveMatter(side,amount,simulate);
    }

    @Override
    public int extractMatter(ForgeDirection direction, int amount, boolean simulate)
    {
        return storage.extractMatter(direction,amount,simulate);
    }

    @Override
    public void onAdded(World world, int x, int y, int z)
    {
        //MatterOverdrive.log.info("Tile Entity Matter Pipe placed");
    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    public void onDestroyed()
    {
        if (getNetwork() != null)
        {
            getNetwork().destroyPipe(this);
        }
        //MatterOverdrive.log.info("Tile Entity Matter Pipe Destroyed");
    }

    @Override
    public void writeToDropItem(ItemStack itemStack) {

    }

    @Override
    public void readFromPlaceItem(ItemStack itemStack) {

    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return storage.fill(resource,doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return storage.drain(resource.amount,doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return storage.drain(maxDrain,doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return fluid instanceof FluidMatterPlasma;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return fluid instanceof FluidMatterPlasma;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[]{storage.getInfo()};
    }

    @Override
    public TileEntity getTile()
    {
        return this;
    }

    @Override
    public void onNetworkUpdate()
    {

    }

    @Override
    public FluidPipeNetwork getNetwork()
    {
        return fluidPipeNetwork;
    }

    @Override
    public void setNetwork(FluidPipeNetwork network)
    {
        this.fluidPipeNetwork = network;
    }


    @Override
    public List<IPipe<FluidPipeNetwork>> getConnections()
    {
        List<IPipe<FluidPipeNetwork>> connections = new ArrayList<>();
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            TileEntity tileEntity = worldObj.getTileEntity(xCoord+direction.offsetX,yCoord+direction.offsetY,zCoord+direction.offsetZ);
            if (tileEntity instanceof IFluidPipe)
            {
                connections.add((IFluidPipe)tileEntity);
            }
        }
        return connections;
    }

    @Override
    public void onNeighborBlockChange()
    {
        super.onNeighborBlockChange();
        if (getNetwork() != null)
        {
            getNetwork().networkUpdate(this);
        }
    }
}
