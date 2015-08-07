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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 8/7/2015.
 */
public class MatterStorage implements IMatterStorage
{
    protected int matter;
    protected int capacity;
    protected int maxExtract;
    protected int maxReceive;

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
        this.maxExtract = maxExtract;
        this.maxReceive = maxReceive;
        this.capacity = capacity;
    }

    @Override
    public int getMatterStored() {
        return matter;
    }

    @Override
    public void setMatterStored(int amount) {
        matter = amount;
    }

    @Override
    public int extractMatter(ForgeDirection direction,int amount,boolean simulate)
    {
        return extractMatter(amount,simulate);
    }

    public int extractMatter(int amount,boolean simulate)
    {
        int maxDrain = MathHelper.clampI(Math.min(amount, getMaxExtract()), 0, this.matter);

        if(!simulate)
        {
            this.matter -= maxDrain;
        }

        return maxDrain;
    }

    @Override
    public int receiveMatter(ForgeDirection side,int amount,boolean simulate)
    {
        int maxFill = MathHelper.clampI(Math.min(amount, getMaxReceive()),0,getCapacity() - this.matter);

        if(!simulate)
        {
            this.matter += maxFill;
        }

        return maxFill;
    }


    @Override
    public int getCapacity()
    {
        return this.capacity;
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
        if(this.matter < 0)
        {
            this.matter = 0;
        }
        nbt.setInteger("Matter", this.matter);
    }


    public void readFromNBT(NBTTagCompound nbt)
    {
        this.matter = nbt.getInteger("Matter");
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
