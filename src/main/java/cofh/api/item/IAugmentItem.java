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

package cofh.api.item;

import net.minecraft.item.ItemStack;

import java.util.Set;

public interface IAugmentItem {

	/**
	 * Get the augmentation level for a given Augment and Augment Type.
	 *
	 * @param stack
	 *            ItemStack representing the Augment.
	 * @param type
	 *            String containing the Augment type name.
	 * @return The Augment level of the stack for the requested type - 0 if it does not affect that attribute.
	 */
	int getAugmentLevel(ItemStack stack, String type);

	/**
	 * Get the Augment Types for a given Augment. Set ensure that there are no duplicates.
	 *
	 * @param stack
	 *            ItemStack representing the Augment.
	 * @return Set of the Augmentation Types. Should return an empty set if there are none (but this would be really stupid to make). DO NOT RETURN NULL.
	 */
	Set<String> getAugmentTypes(ItemStack stack);

}
