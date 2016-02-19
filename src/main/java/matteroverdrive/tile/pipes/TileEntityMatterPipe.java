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

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.transport.IGridNode;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import matteroverdrive.data.MatterStorage;
import matteroverdrive.data.transport.FluidPipeNetwork;
import matteroverdrive.data.transport.IFluidPipe;
import matteroverdrive.fluids.FluidMatterPlasma;
import matteroverdrive.init.MatterOverdriveFluids;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.util.TimeTracker;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.EnumSet;
import java.util.Random;

/**
 * Created by Simeon on 3/7/2015.
 */
public class TileEntityMatterPipe extends TileEntityPipe implements IFluidPipe
{
    protected final MatterStorage storage;
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
    public  void update()
    {
        super.update();
        if (!worldObj.isRemote)
        {
            manageTransfer();
            manageNetwork();
        }
    }

    public void manageNetwork()
    {
        if (fluidPipeNetwork == null)
        {
            if(!tryConnectToNeighborNetworks(worldObj))
            {
                FluidPipeNetwork network = MatterOverdrive.fluidNetworkHandler.getNetwork(this);
                network.addNode(this);
            }
        }
    }

    public void manageTransfer()
    {
        /*if (getMatterStored() > 0 && getNetwork() != null)
        {
            for (IFluidPipe pipe : getNetwork().getFluidHandlers())
            {
                for (EnumFacing direction : EnumFacing.VALUES)
                {
                    TileEntity fluidHandler = pipe.getTile().getWorld().getTileEntity(pipe.getTile().getPos().offset(direction));
                    if (fluidHandler != null && fluidHandler instanceof IFluidHandler && !(fluidHandler instanceof  IFluidPipe) && getMatterStored() > 0 && ((IFluidHandler)fluidHandler).canFill(EnumFacing.DOWN,MatterOverdriveFluids.matterPlasma))
                    {
                        extractMatter(EnumFacing.DOWN,((IFluidHandler)fluidHandler).fill(EnumFacing.DOWN,new FluidStack(MatterOverdriveFluids.matterPlasma,getMatterStored()),true),false);
                    }
                }

            }
        }*/
    }

    @Override
    public boolean canConnectToPipe(TileEntity entity, EnumFacing direction)
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
    public int receiveMatter(EnumFacing side, int amount, boolean simulate)
    {
        return storage.receiveMatter(side,amount,simulate);
    }

    @Override
    public int extractMatter(EnumFacing direction, int amount, boolean simulate)
    {
        return storage.extractMatter(direction,amount,simulate);
    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    public void onAdded(World world, BlockPos pos, IBlockState state)
    {

    }

    public boolean tryConnectToNeighborNetworks(World world)
    {
        boolean hasConnected = false;
        for (EnumFacing side : EnumFacing.VALUES)
        {
            TileEntity neighborEntity = world.getTileEntity(pos.offset(side));
            if (neighborEntity instanceof TileEntityMatterPipe)
            {
                if (((TileEntityMatterPipe) neighborEntity).getNetwork() != null && ((TileEntityMatterPipe) neighborEntity).getNetwork() != this.fluidPipeNetwork)
                {
                    ((TileEntityMatterPipe) neighborEntity).getNetwork().addNode(this);
                    hasConnected = true;
                }
            }
        }
        return hasConnected;
    }

    @Override
    public void onDestroyed(World worldIn, BlockPos pos, IBlockState state)
    {
        if (fluidPipeNetwork != null)
        {
            fluidPipeNetwork.onNodeDestroy(state, this);
        }
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        updateSides(false);
    }

    @Override
    public void writeToDropItem(ItemStack itemStack) {

    }

    @Override
    public void readFromPlaceItem(ItemStack itemStack) {

    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        return storage.fill(resource,doFill);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        return storage.drain(resource.amount,doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return storage.drain(maxDrain,doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        return fluid instanceof FluidMatterPlasma;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return fluid instanceof FluidMatterPlasma;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return new FluidTankInfo[]{storage.getInfo()};
    }

    @Override
    public TileEntity getTile()
    {
        return this;
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
    public boolean canConnectToNetworkNode(IBlockState blockState, IGridNode toNode, EnumFacing direction)
    {
        return true;
    }

    @Override
    public boolean canConnectFromSide(IBlockState blockState, EnumFacing side)
    {
        return true;
    }
}
