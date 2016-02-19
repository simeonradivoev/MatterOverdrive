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

import net.minecraft.util.EnumFacing;

/**
 * Used by any {@link net.minecraft.tileentity.TileEntity} that provides matter
 */
public interface IMatterProvider
{
	/**
	 * @return stored matter.
	 */
	int getMatterStored();

	/**
	 * @return the capacity
	 */
	int getMatterCapacity();

	/**
	 * Used to extract matter.
	 * @param direction the extraction side.
	 * @param amount amount of extracted matter.
	 * @param simulate is this a simulation ?
	 *                 No matter will be extracted.
	 *                 Used to check if the specified amount can be extracted.
	 * @return the extracted amount of matter.
	 */
	int extractMatter(EnumFacing direction, int amount, boolean simulate);
}
