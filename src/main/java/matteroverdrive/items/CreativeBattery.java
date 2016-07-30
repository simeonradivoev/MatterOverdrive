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

package matteroverdrive.items;

import matteroverdrive.init.MatterOverdriveCapabilities;
import matteroverdrive.util.MOEnergyHelper;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class CreativeBattery extends Battery
{
	public CreativeBattery(String name, int capacity, int input, int output)
	{
		super(name, capacity, input, output);
		this.setMaxStackSize(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> list)
	{
		ItemStack unpowered = new ItemStack(item);
		list.add(unpowered);
	}

	@Override
	public int getEnergyStored(ItemStack container)
	{
		return capacity;
	}

	@Override
	public void setEnergyStored(ItemStack container, int amount)
	{
		MOEnergyHelper.setDefaultEnergyTag(container, capacity);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		return new InfiniteTeslaProvider(capacity, maxExtract);
	}

	public static class InfiniteTeslaProvider implements ICapabilitySerializable<NBTTagCompound> {

		private BaseTeslaContainer tesla;

		public InfiniteTeslaProvider(long capacity, long output) {
			tesla = new BaseTeslaContainer(capacity, capacity, output, 0) {
				@Override
				public long takePower(long Tesla, boolean simulated)
				{
					return Math.max(Tesla, getOutputRate());
				}

				@Override
				public long givePower(long Tesla, boolean simulated)
				{
					return 0;
				}
			};
		}

		@Override
		public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
		{
			return capability == MatterOverdriveCapabilities.TESLA_HOLDER || capability == MatterOverdriveCapabilities.TESLA_PRODUCER;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
		{
			if (capability == MatterOverdriveCapabilities.TESLA_HOLDER || capability == MatterOverdriveCapabilities.TESLA_PRODUCER)
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
