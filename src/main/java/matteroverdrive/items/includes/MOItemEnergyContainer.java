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
import com.mojang.realmsclient.gui.ChatFormatting;
import matteroverdrive.util.MOEnergyHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

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
	public void addDetails(ItemStack itemstack, EntityPlayer player, List infos)
	{
		this.TagCompountCheck(itemstack);
		infos.add(TextFormatting.YELLOW + MOEnergyHelper.formatEnergy(getEnergyStored(itemstack), getMaxEnergyStored(itemstack)));
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

		this.TagCompountCheck(container);

		int energy = container.getTagCompound().getInteger("Energy");
		int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));

		if (!simulate)
		{
			energy += energyReceived;
			container.getTagCompound().setInteger("Energy", energy);
		}
		return energyReceived;
	}

	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate)
	{

		if (container.getTagCompound() != null && container.getTagCompound().hasKey("Energy"))
		{
			int energy = container.getTagCompound().getInteger("Energy");
			int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));

			if (!simulate)
			{
				energy -= energyExtracted;
				container.getTagCompound().setInteger("Energy", energy);
			}
			return energyExtracted;
		}
		return 0;
	}

	protected void setEnergyStored(ItemStack container, int amount)
	{
		setDefaultEnergyTag(container, amount);
	}

	@Override
	public int getEnergyStored(ItemStack container)
	{
		this.TagCompountCheck(container);
		return container.getTagCompound().getInteger("Energy");
	}

	@Override
	public int getMaxEnergyStored(ItemStack container)
	{
		return capacity;
	}
}
