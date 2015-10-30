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

import cofh.api.energy.IEnergyStorage;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.machines.MOTileEntityMachine;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;

/**
 * Created by Simeon on 8/7/2015.
 */
public class MachineEnergyStorage<T extends MOTileEntityMachine> implements IEnergyStorage
{
    protected int energy;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;
    protected final T machine;

    public MachineEnergyStorage(T machine,int capacity)
    {
        this(machine, capacity, capacity, capacity);
    }
    public MachineEnergyStorage(T machine,int capacity,int maxReceive,int maxExtract)
    {
        this.machine = machine;
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    public void readFromNBT(NBTTagCompound tagCompound)
    {
        this.energy = tagCompound.getInteger("Energy");
    }

    public void writeToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setInteger("Energy",energy);
    }

    public int modifyEnergyStored(int amount) {
        int lastEnergy = this.energy;
        this.energy = MathHelper.clamp_int(this.energy + amount,0,getMaxEnergyStored());
        return this.energy - lastEnergy;
    }

    @Override
    public int receiveEnergy(int amount, boolean simulate) {
        int clampedAmount = Math.min(getMaxEnergyStored() - this.energy, Math.min(getMaxReceive(), amount));
        if(!simulate) {
            this.energy += clampedAmount;
        }

        return clampedAmount;
    }

    @Override
    public int extractEnergy(int amount, boolean simulate) {
        int clampedAmount = Math.min(getMaxEnergyStored(), Math.min(getMaxExtract(), amount));
        if(!simulate) {
            this.energy -= clampedAmount;
        }

        return clampedAmount;
    }

    public int getMaxReceive()
    {
        return Math.max(0,(int)(maxReceive * machine.getUpgradeMultiply(UpgradeTypes.PowerTransfer)));
    }

    public int getMaxExtract()
    {
        return Math.max(0,(int)(maxExtract * machine.getUpgradeMultiply(UpgradeTypes.PowerTransfer)));
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return Math.max(0, (int) (capacity * machine.getUpgradeMultiply(UpgradeTypes.PowerStorage)));
    }

    public void setMaxTransfer(int amount)
    {
        this.setMaxReceive(amount);
        this.setMaxExtract(amount);
    }

    public void setEnergyStored(int energy)
    {
        this.energy = energy;
    }

    public void setCapacity(int capacity)
    {
        this.capacity = capacity;
    }

    public void setMaxReceive(int maxReceive)
    {
        this.maxReceive = maxReceive;
    }

    public void setMaxExtract(int maxExtract)
    {
        this.maxExtract = maxExtract;
    }
}
