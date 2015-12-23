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

package cofh.api.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Implement this interface on Tile Entities which can write a limited amount of data about themselves.
 *
 * This is typically for the purposes of being transferred to a similar tile entity.
 *
 * @author King Lemming
 *
 */
public interface IPortableData {

	/**
	 * Data identifier of the Tile Entity/Block. Used for display as well as verification purposes. Tiles with completely interchangeable data should return the
	 * same type.
	 *
	 * @return
	 */
	String getDataType();

	/**
	 * Read the data from a tag. The player object exists because this should always be called via player interaction!
	 */
	void readPortableData(EntityPlayer player, NBTTagCompound tag);

	/**
	 * Write the data to a tag. The player object exists because this should always be called via player interaction!
	 */
	void writePortableData(EntityPlayer player, NBTTagCompound tag);
}
