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

package matteroverdrive.data;

import cofh.lib.util.helpers.MathHelper;
import matteroverdrive.api.matter.IMatterStorage;
import matteroverdrive.init.MatterOverdriveFluids;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

/**
 * Created by Simeon on 8/7/2015.
 */
public class MatterStorage implements IMatterStorage, IFluidTank
{
    protected int capacity;
    protected int maxExtract;
    protected int maxReceive;
    private FluidStack fluidStack;

    public MatterStorage(int capacity)
    {
        this(capacity,capacity,capacity);
    }

    public MatterStorage(int capacity, int maxExtract)
    {
        this(capacity,maxExtract,maxExtract);
    }

    public MatterStorage(int capacity, int maxExtract, int maxReceive)
    {
        fluidStack = new FluidStack(MatterOverdriveFluids.matterPlasma,0);
        this.maxExtract = maxExtract;
        this.maxReceive = maxReceive;
        this.capacity = capacity;
    }

    @Override
    public int getMatterStored() {
        return fluidStack.amount;
    }

    @Override
    public void setMatterStored(int amount) {
        fluidStack.amount = amount;
    }

    @Override
    public int extractMatter(ForgeDirection direction,int amount,boolean simulate)
    {
        return extractMatter(amount,simulate);
    }

    public int extractMatter(int amount,boolean simulate)
    {
        int maxDrain = MathHelper.clampI(Math.min(amount, getMaxExtract()), 0, getFluid().amount);

        if(!simulate)
        {
            getFluid().amount -= maxDrain;
        }

        return maxDrain;
    }

    @Override
    public int receiveMatter(ForgeDirection side,int amount,boolean simulate)
    {
        int maxFill = MathHelper.clampI(Math.min(amount, getMaxReceive()),0,getCapacity() - getFluid().amount);

        if(!simulate)
        {
            getFluid().amount += maxFill;
        }

        return maxFill;
    }

    public int modifyMatterStored(int amount)
    {
        int lastAmount = getFluid().amount;
        getFluid().amount += amount;
        getFluid().amount = net.minecraft.util.MathHelper.clamp_int(getFluid().amount,0,getCapacity());
        return lastAmount - amount;
    }


    @Override
    public FluidStack getFluid()
    {
        return fluidStack;
    }

    @Override
    public int getFluidAmount()
    {
        return getFluid().amount;
    }

    @Override
    public int getCapacity()
    {
        return this.capacity;
    }

    @Override
    public FluidTankInfo getInfo() {
        return new FluidTankInfo(this);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill)
    {
        if (resource == null)
        {
            return 0;
        }

        if (getFluid() == null)
        {
            return Math.min(capacity, resource.amount);
        }

        if (!getFluid().isFluidEqual(resource))
        {
            return 0;
        }

        return receiveMatter(ForgeDirection.UNKNOWN,resource.amount,!doFill);
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain)
    {
        if (getFluid() == null)
        {
            return null;
        }

        int drained = extractMatter(ForgeDirection.UNKNOWN,maxDrain,!doDrain);
        if (drained <= 0)
            return null;
        else
            return new FluidStack(MatterOverdriveFluids.matterPlasma,drained);
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
        nbt.setInteger("Matter", getMatterStored());
    }


    public void readFromNBT(NBTTagCompound nbt)
    {
        setMatterStored(nbt.getInteger("Matter"));
    }

    public void setMaxReceive(int maxReceive) {
        this.maxReceive = maxReceive;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setMaxExtract(int maxExtract) {
        this.maxExtract = maxExtract;
    }

    public int getMaxExtract()
    {
        return maxExtract;
    }

    public int getMaxReceive() {
        return maxReceive;
    }
}
