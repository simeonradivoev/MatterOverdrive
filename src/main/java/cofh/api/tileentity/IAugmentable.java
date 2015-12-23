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

import net.minecraft.item.ItemStack;

/**
 * Implemented on objects which support Augments.
 *
 * @author King Lemming
 *
 */
public interface IAugmentable {

	/**
	 * Attempt to reconfigure the tile based on the Augmentations present.
	 */
	void installAugments();

	/**
	 * Returns an array of the Augment slots for this Tile Entity.
	 */
	ItemStack[] getAugmentSlots();

	/**
	 * Returns a status array for the Augmentations installed in the Tile Entity.
	 */
	boolean[] getAugmentStatus();

}
