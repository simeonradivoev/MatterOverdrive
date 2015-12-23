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

/**
 * Implement this interface on Tile Entities which allow for reconfiguration of their facing.
 *
 * Coordination with the containing block is required.
 *
 * @author King Lemming
 *
 */
public interface IReconfigurableFacing {

	/**
	 * Returns the current facing of the block.
	 */
	int getFacing();

	/**
	 * Returns whether or not the block's face can be aligned with the Y Axis.
	 */
	boolean allowYAxisFacing();

	/**
	 * Attempt to rotate the block. Arbitrary based on implementation.
	 *
	 * @return True if rotation was successful, false otherwise.
	 */
	boolean rotateBlock();

	/**
	 * Set the facing of the block.
	 *
	 * @param side
	 *            The side to set the facing to.
	 * @return True if the facing was set, false otherwise.
	 */
	boolean setFacing(int side);

}
