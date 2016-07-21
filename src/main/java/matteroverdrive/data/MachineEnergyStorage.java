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
import net.darkhax.tesla.api.BaseTeslaContainer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Simeon on 8/7/2015.
 */
public class MachineEnergyStorage<T extends MOTileEntityMachine> extends BaseTeslaContainer implements IEnergyStorage
{
	protected final T machine;
//	protected int energy;
//	protected int capacity;
//	protected int maxReceive;
//	protected int maxExtract;

	public MachineEnergyStorage(T machine)
	{
		this.machine = machine;
	}

	public MachineEnergyStorage(long capacity, long input, long output, T machine)
	{
		super(capacity, input, output);
		this.machine = machine;
	}

	public MachineEnergyStorage(long power, long capacity, long input, long output, T machine)
	{
		super(power, capacity, input, output);
		this.machine = machine;
	}

	public MachineEnergyStorage(NBTTagCompound dataTag, T machine)
	{
		super(dataTag);
		this.machine = machine;
	}


//	public MachineEnergyStorage(T machine, int capacity)
//	{
//		this(machine, capacity, capacity, capacity);
//	}
//
//	public MachineEnergyStorage(T machine, int capacity, int maxReceive, int maxExtract)
//	{
//		this.machine = machine;
//		this.capacity = capacity;
//		this.maxReceive = maxReceive;
//		this.maxExtract = maxExtract;
//	}

	public int modifyEnergyStored(int amount)
	{
		if (amount > 0) {
			return (int)givePower(amount, false);
		} else if (amount < 0) {
			return (int)takePower(-amount, false);
		} else {
			return 0;
		}
	}

	@Override
	public int receiveEnergy(int amount, boolean simulate)
	{
		return (int)givePower(amount, simulate);
	}

	@Override
	public int extractEnergy(int amount, boolean simulate)
	{
		return (int)takePower(amount, simulate);
	}

	@Override
	public long getInputRate()
	{
		return (long)(super.getInputRate() * machine.getUpgradeMultiply(UpgradeTypes.PowerTransfer));
	}

	@Override
	public long getOutputRate()
	{
		return (long)(super.getOutputRate() * machine.getUpgradeMultiply(UpgradeTypes.PowerTransfer));
	}

	@Override
	public long getCapacity()
	{
		return (long)(super.getCapacity() * machine.getUpgradeMultiply(UpgradeTypes.PowerStorage));
	}

	@Override
	public int getEnergyStored()
	{
		return (int)getStoredPower();
	}

	@Override
	public int getMaxEnergyStored()
	{
		return (int)getCapacity();
	}

	//	public int getMaxReceive()
//	{
//		return Math.max(0, (int)(maxReceive * machine.getUpgradeMultiply(UpgradeTypes.PowerTransfer)));
//	}

//	public void setMaxReceive(int maxReceive)
//	{
//		this.maxReceive = maxReceive;
//	}
//
//	public int getMaxExtract()
//	{
//		return Math.max(0, (int)(maxExtract * machine.getUpgradeMultiply(UpgradeTypes.PowerTransfer)));
//	}
//
//	public void setMaxExtract(int maxExtract)
//	{
//		this.maxExtract = maxExtract;
//	}

//	@Override
//	public int getEnergyStored()
//	{
//		return energy;
//	}
//
//	public void setEnergyStored(int energy)
//	{
//		this.energy = energy;
//	}
//
//	@Override
//	public int getMaxEnergyStored()
//	{
//		return Math.max(0, (int)(capacity * machine.getUpgradeMultiply(UpgradeTypes.PowerStorage)));
//	}
//
//	public void setMaxTransfer(int amount)
//	{
//		this.setMaxReceive(amount);
//		this.setMaxExtract(amount);
//	}
//
//	public void setCapacity(int capacity)
//	{
//		this.capacity = capacity;
//	}
}
