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

package matteroverdrive.items.includes;

import cofh.api.energy.IEnergyContainerItem;
import matteroverdrive.init.MatterOverdriveCapabilities;
import matteroverdrive.util.MOEnergyHelper;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;
import java.util.List;

import static matteroverdrive.util.MOEnergyHelper.setDefaultEnergyTag;

public class MOItemEnergyContainer extends MOBaseItem implements IEnergyContainerItem
{
	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;

	public MOItemEnergyContainer(String name)
	{
		this(name, 32000);
	}

	public MOItemEnergyContainer(String name, int capacity)
	{
		this(name, capacity, capacity, capacity);
	}

	public MOItemEnergyContainer(String name, int capacity, int maxTransfer)
	{

		this(name, capacity, maxTransfer, maxTransfer);
	}

	public MOItemEnergyContainer(String name, int capacity, int maxReceive, int maxExtract)
	{
		super(name);
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
	}

	@Override
	public int getMaxDamage(ItemStack stack)
	{
		return getMaxEnergyStored(stack);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return true;
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		return (getMaxEnergyStored(stack) - getEnergyStored(stack)) / (double)getMaxDamage(stack);
	}

	@Override
	public void addDetails(ItemStack stack, EntityPlayer player, List<String> infos)
	{
		ITeslaHolder holder = stack.getCapability(MatterOverdriveCapabilities.TESLA_HOLDER, null);
		infos.add(TextFormatting.YELLOW + MOEnergyHelper.formatEnergy(holder.getStoredPower(), holder.getCapacity()));
	}

	@Override
	public boolean hasDetails(ItemStack itemStack)
	{
		return true;
	}

	public MOItemEnergyContainer setCapacity(int capacity)
	{

		this.capacity = capacity;
		return this;
	}

	public void setMaxTransfer(int maxTransfer)
	{

		setMaxReceive(maxTransfer);
		setMaxExtract(maxTransfer);
	}

	public void setMaxReceive(int maxReceive)
	{

		this.maxReceive = maxReceive;
	}

	public void setMaxExtract(int maxExtract)
	{

		this.maxExtract = maxExtract;
	}

	/* IEnergyContainerItem */
	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate)
	{
		return (int)container.getCapability(MatterOverdriveCapabilities.TESLA_CONSUMER, null).givePower(maxReceive, simulate);
	}

	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate)
	{
		return (int)container.getCapability(MatterOverdriveCapabilities.TESLA_PRODUCER, null).takePower(maxExtract, simulate);
	}

	public void setEnergyStored(ItemStack container, int amount)
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setLong("TeslaPower", amount);
		((BaseTeslaContainer)container.getCapability(MatterOverdriveCapabilities.TESLA_HOLDER, null)).deserializeNBT(tag);
	}

	@Override
	public int getEnergyStored(ItemStack container)
	{
		return (int)container.getCapability(MatterOverdriveCapabilities.TESLA_HOLDER, null).getStoredPower();
	}

	@Override
	public int getMaxEnergyStored(ItemStack container)
	{
		return capacity;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		return new TeslaProvider(capacity, maxReceive, maxExtract);
	}

	public static class TeslaProvider implements ICapabilitySerializable<NBTTagCompound> {

		private BaseTeslaContainer tesla;

		public TeslaProvider(int capacity, int input, int output)
		{
			tesla = new BaseTeslaContainer(capacity, input, output);
		}

		@Override
		public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
		{
			return capability == MatterOverdriveCapabilities.TESLA_HOLDER || capability == MatterOverdriveCapabilities.TESLA_PRODUCER || capability == MatterOverdriveCapabilities.TESLA_CONSUMER;
		}

		@Override
		@SuppressWarnings("unchecked")
		public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
		{
			if (capability == MatterOverdriveCapabilities.TESLA_HOLDER || capability == MatterOverdriveCapabilities.TESLA_PRODUCER || capability == MatterOverdriveCapabilities.TESLA_CONSUMER)
			{
				return (T)tesla;
			}
			return null;
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			return tesla.serializeNBT();
		}

		@Override
		public void deserializeNBT(NBTTagCompound tag)
		{
			tesla.deserializeNBT(tag);
		}
	}

}
