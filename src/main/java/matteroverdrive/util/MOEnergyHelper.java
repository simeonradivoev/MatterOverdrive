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

package matteroverdrive.util;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class MOEnergyHelper
{
	public static final String ENERGY_UNIT = " RF";

	public static String formatEnergy(int energy, int capacity)
	{
		return MOStringHelper.formatNumber(energy) + " / " + MOStringHelper.formatNumber(capacity) + ENERGY_UNIT;
	}

	public static String formatEnergy(int energy)
	{
		return formatEnergy("Charge: ", energy);
	}

	public static String formatEnergy(String prefix, int energy)
	{
		return (prefix != null ? prefix : "") + MOStringHelper.formatNumber(energy) + ENERGY_UNIT;
	}

	public static boolean extractExactAmount(IEnergyProvider provider, EnumFacing direction, int amount, boolean simulate)
	{
		int hasEnergy = provider.getEnergyStored(direction);
		if (hasEnergy >= amount)
		{
			while (amount > 0)
			{
				if (provider.extractEnergy(direction, amount, true) >= 0)
				{
					amount -= provider.extractEnergy(direction, amount, simulate);
				}
				else
				{
					return false;
				}
			}
		}
		return true;
	}

	public static ItemStack setDefaultEnergyTag(ItemStack itemStack, int energy)
	{
		if (itemStack.getTagCompound() == null)
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}

		itemStack.getTagCompound().setInteger("Energy", energy);
		return itemStack;
	}

	public static int extractEnergyFromContainer(ItemStack itemStack, int amount, boolean simulate)
	{
		return isEnergyContainerItem(itemStack) ? ((IEnergyContainerItem)itemStack.getItem()).extractEnergy(itemStack, amount, simulate) : 0;
	}

	public static int insertEnergyIntoContainer(ItemStack itemStack, int amount, boolean simulate)
	{
		return isEnergyContainerItem(itemStack) ? ((IEnergyContainerItem)itemStack.getItem()).receiveEnergy(itemStack, amount, simulate) : 0;
	}

	public static boolean isEnergyContainerItem(ItemStack itemStack)
	{
		return itemStack != null && itemStack.getItem() instanceof IEnergyContainerItem;
	}

	public static int insertEnergyIntoAdjacentEnergyReceiver(TileEntity tileEntity, EnumFacing side, int amount, boolean simulate)
	{
		TileEntity var4 = tileEntity.getWorld().getTileEntity(tileEntity.getPos().offset(side));
		return var4 instanceof IEnergyReceiver ? ((IEnergyReceiver)var4).receiveEnergy(side.getOpposite(), amount, simulate) : 0;
	}
}
