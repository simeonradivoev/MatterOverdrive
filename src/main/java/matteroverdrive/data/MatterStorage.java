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

import matteroverdrive.api.matter.IMatterHandler;
import matteroverdrive.init.MatterOverdriveFluids;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.*;

/**
 * Created by Simeon on 8/7/2015.
 */
public class MatterStorage extends FluidTank implements IMatterHandler
{

	private int maxExtract;
	private int maxReceive;

	public MatterStorage(int capacity)
	{
		this(capacity, capacity, capacity);
	}

	public MatterStorage(int capacity, int maxTransfer) {
		this(capacity, maxTransfer, maxTransfer);
	}

	public MatterStorage(int capacity, int maxExtract, int maxReceive)
	{
		super(capacity);
		this.maxExtract = maxExtract;
		this.maxReceive = maxReceive;
	}

	public int getMaxExtract()
	{
		return maxExtract;
	}

	public int getMaxReceive()
	{
		return maxReceive;
	}

	public void setMaxExtract(int maxExtract)
	{
		this.maxExtract = maxExtract;
	}

	public void setMaxReceive(int maxReceive)
	{
		this.maxReceive = maxReceive;
	}

	@Override
	public boolean canFillFluidType(FluidStack fluid)
	{
		return fluid.getFluid() == MatterOverdriveFluids.matterPlasma;
	}

	@Override
	public boolean canDrainFluidType(FluidStack fluid)
	{
		return fluid.getFluid() == MatterOverdriveFluids.matterPlasma;
	}

	@Override
	public int modifyMatterStored(int amount)
	{
		int lastAmount = getFluid() == null ? 0 : getFluid().amount;
		int newAmount = lastAmount + amount;
		newAmount = MathHelper.clamp_int(newAmount, 0, getCapacity());
		setMatterStored(newAmount);
		return lastAmount - newAmount;
	}

	@Override
	public int getMatterStored()
	{
		return getFluidAmount();
	}

	@Override
	public void setMatterStored(int amount)
	{
		if (amount <= 0) {
			setFluid(null);
		}
		else
		{
			drainInternal(getFluidAmount(), true);
			fillInternal(new FluidStack(MatterOverdriveFluids.matterPlasma, amount), true);
		}
	}

	@Override
	public int receiveMatter(int amount, boolean simulate)
	{
		return fill(new FluidStack(MatterOverdriveFluids.matterPlasma, amount), simulate);
	}

	@Override
	public int extractMatter(int amount, boolean simulate)
	{
		FluidStack drained = drain(amount, simulate);
		if (drained == null) {
			return 0;
		} else {
			return drained.amount;
		}
	}

//	private final FluidStack fluidStack;
//	protected int capacity;
//	protected int maxExtract;
//	protected int maxReceive;
//
//	public MatterStorage(int capacity)
//	{
//		this(capacity, capacity, capacity);
//	}
//
//	public MatterStorage(int capacity, int maxExtract)
//	{
//		this(capacity, maxExtract, maxExtract);
//	}
//
//	public MatterStorage(int capacity, int maxExtract, int maxReceive)
//	{
//		fluidStack = new FluidStack(MatterOverdriveFluids.matterPlasma, 0);
//		this.maxExtract = maxExtract;
//		this.maxReceive = maxReceive;
//		this.capacity = capacity;
//	}
//
//	@Override
//	public int getMatterStored()
//	{
//		return fluidStack.amount;
//	}
//
//	@Override
//	public void setMatterStored(int amount)
//	{
//		fluidStack.amount = amount;
//	}
//
//	@Override
//	public int extractMatter(EnumFacing direction, int amount, boolean simulate)
//	{
//		return extractMatter(amount, simulate);
//	}
//
//	public int extractMatter(int amount, boolean simulate)
//	{
//		int maxDrain = MathHelper.clamp_int(Math.min(amount, getMaxExtract()), 0, getFluid().amount);
//
//		if (!simulate)
//		{
//			getFluid().amount -= maxDrain;
//		}
//
//		return maxDrain;
//	}
//
//	@Override
//	public int receiveMatter(EnumFacing side, int amount, boolean simulate)
//	{
//		int maxFill = MathHelper.clamp_int(Math.min(amount, getMaxReceive()), 0, getCapacity() - getFluid().amount);
//
//		if (!simulate)
//		{
//			getFluid().amount += maxFill;
//		}
//
//		return maxFill;
//	}
//
//	public int modifyMatterStored(int amount)
//	{
//		int lastAmount = getFluid().amount;
//		getFluid().amount += amount;
//		getFluid().amount = MathHelper.clamp_int(getFluid().amount, 0, getCapacity());
//		return lastAmount - amount;
//	}
//
//
//	@Override
//	public FluidStack getFluid()
//	{
//		return fluidStack;
//	}
//
//	@Override
//	public int getFluidAmount()
//	{
//		return getFluid().amount;
//	}
//
//	@Override
//	public int getCapacity()
//	{
//		return this.capacity;
//	}
//
//	public void setCapacity(int capacity)
//	{
//		this.capacity = capacity;
//	}
//
//	@Override
//	public FluidTankInfo getInfo()
//	{
//		return new FluidTankInfo(this);
//	}
//
//	@Override
//	public int fill(FluidStack resource, boolean doFill)
//	{
//		if (resource == null)
//		{
//			return 0;
//		}
//
//		if (getFluid() == null)
//		{
//			return Math.min(capacity, resource.amount);
//		}
//
//		if (!getFluid().isFluidEqual(resource))
//		{
//			return 0;
//		}
//
//		return receiveMatter(EnumFacing.DOWN, resource.amount, !doFill);
//	}
//
//	@Override
//	public FluidStack drain(int maxDrain, boolean doDrain)
//	{
//		if (getFluid() == null)
//		{
//			return null;
//		}
//
//		int drained = extractMatter(EnumFacing.DOWN, maxDrain, !doDrain);
//		if (drained <= 0)
//		{
//			return null;
//		}
//		else
//		{
//			return new FluidStack(MatterOverdriveFluids.matterPlasma, drained);
//		}
//	}
//
//	public void writeToNBT(NBTTagCompound nbt)
//	{
//		nbt.setInteger("Matter", getMatterStored());
//	}
//
//	public void readFromNBT(NBTTagCompound nbt)
//	{
//		setMatterStored(nbt.getInteger("Matter"));
//	}
//
//	public int getMaxExtract()
//	{
//		return maxExtract;
//	}
//
//	public void setMaxExtract(int maxExtract)
//	{
//		this.maxExtract = maxExtract;
//	}
//
//	public int getMaxReceive()
//	{
//		return maxReceive;
//	}
//
//	public void setMaxReceive(int maxReceive)
//	{
//		this.maxReceive = maxReceive;
//	}
}
