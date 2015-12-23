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
 * Implement this interface on Tile Entities which allow for reconfiguration of their sides.
 *
 * Coordination with the containing block is required.
 *
 * @author King Lemming
 *
 */
public interface IReconfigurableSides {

	/**
	 * Decrement the config for a given side.
	 *
	 * @param side
	 *            The side to decrement.
	 * @return True if config was changed, false otherwise.
	 */
	boolean decrSide(int side);

	/**
	 * Increment the config for a given side.
	 *
	 * @param side
	 *            The side to decrement.
	 * @return True if config was changed, false otherwise.
	 */
	boolean incrSide(int side);

	/**
	 * Set the config for a given side.
	 *
	 * @param side
	 *            The side to set.
	 * @param config
	 *            The config value to use.
	 * @return True of config was set, false otherwise.
	 */
	boolean setSide(int side, int config);

	/**
	 * Reset configs on all sides to their base values.
	 *
	 * @return True if reset was successful, false otherwise.
	 */
	boolean resetSides();

	/**
	 * Returns the number of possible config settings for a given side.
	 */
	int getNumConfig(int side);

}
