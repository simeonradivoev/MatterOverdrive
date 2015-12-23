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

package cofh.api.transport;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * This interface is implemented on ItemDucts. Use it to attempt to eject items into an entry point.
 *
 * @author Zeldo Kavira, King Lemming
 *
 */
public interface IItemDuct {

	/**
	 * Insert an ItemStack into the IItemDuct. Will only accept items if there is a valid destination. This returns what is remaining of the original stack - a
	 * null return means that the entire stack was accepted/routed!
	 *
	 * @param from
	 *            Orientation the item is inserted from.
	 * @param item
	 *            ItemStack to be inserted. The size of this stack corresponds to the maximum amount to insert.
	 * @return An ItemStack representing how much is remaining after the item was inserted into the Duct.
	 */
	public ItemStack insertItem(ForgeDirection from, ItemStack item);

}
