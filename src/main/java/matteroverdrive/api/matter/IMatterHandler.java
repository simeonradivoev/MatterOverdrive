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

package matteroverdrive.api.matter;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Used as a interface for all matter storage entities or components.
 */
public interface IMatterHandler extends INBTSerializable<NBTTagCompound>
{
	/**
	 * @return the current matter stored.
	 */
	int getMatterStored();

	void setMatterStored(int amount);

	/**
	 * @return the maximum capacity.
	 */
	int getCapacity();

	/**
	 * @param capacity Sets the capacity
	 */
	void setCapacity(int capacity);

	/**
	 * Modifies the amount of matter stored by the given amount.
	 * @param amount The amount of matter to add/subtract
	 * @return The amount added/subtracted
	 */
	int modifyMatterStored(int amount);

	/**
	 * Used to receive matter.
	 * @param amount the amount of received matter.
	 * @param simulate is this a simulation.
	 *                 No matter will be stored if this is true.
	 *                 Used to check if the given amount of matter can be received.
	 * @return the amount of matter received.
	 * This is the same, not matter if the call is a simulation.
	 */
	int receiveMatter(int amount, boolean simulate);

	/**
	 * Used to extract matter.
	 * @param amount the amount of matter extracted.
	 * @param simulate No matter will be stored if this is set to true.
	 *                 Used to check if the specified amount of matter can be extracted.
	 * @return the amount of matter extracted.
	 * This is the same, no matter if the call is a simulation.
	 */
	int extractMatter(int amount, boolean simulate);

	@Override
	default NBTTagCompound serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("matter", getMatterStored());
		tag.setInteger("capacity", getCapacity());
		return tag;
	}

	@Override
	default void deserializeNBT(NBTTagCompound tag) {
		setMatterStored(tag.getInteger("matter"));
		setCapacity(tag.getInteger("capacity"));
	}

}
