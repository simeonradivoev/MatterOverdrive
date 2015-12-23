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

/**
 * Implement this interface on subclasses of Item to change how the item works in Thermal Dynamics Itemducts filter slots.
 *
 * This can be used to create customizable Items which are determined to be "equal" for the purposes of filtering.
 */
public interface ISpecialFilterItem {

	/**
	 * This method is called to find out if the given ItemStack should be matched by the given Filter ItemStack.
	 *
	 * @param filter
	 *            ItemStack representing the filter.
	 * @param item
	 *            ItemStack representing the queried item.
	 * @return True if the filter should match. False if the default matching should be used.
	 */
	public boolean matchesItem(ItemStack filter, ItemStack item);

}
