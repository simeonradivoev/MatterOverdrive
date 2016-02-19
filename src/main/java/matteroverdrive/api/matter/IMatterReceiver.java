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
 * Used by matter receivers to receive matter.
 */
public interface IMatterReceiver
{
	/**
	 * @return the matter stored.
	 */
	int getMatterStored();

	/**
	 * @return matter capacity.
	 */
	int getMatterCapacity();

	/**
	 * Used to receive matter.
	 * @param side the receiving side.
	 * @param amount amount of matter received.
	 * @param simulated is this a simulation ?
	 *                  No matter will be received.
	 *                  Used to check if the specified amount of matter can be received.
	 * @return the actual received amount of matter.
	 */
	int receiveMatter(EnumFacing side, int amount, boolean simulated);
}
