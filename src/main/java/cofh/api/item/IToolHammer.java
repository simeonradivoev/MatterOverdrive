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

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * Implement this interface on subclasses of Item to have that item work as a tool for CoFH mods.
 */
public interface IToolHammer {

	/**
	 * Called to ensure that the tool can be used.
	 *
	 * @param item
	 *            The itemstack for the tool. Not required to match equipped item (e.g., multi-tools that contain other tools)
	 * @param user
	 *            The entity using the tool
	 * @param x
	 *            X location of the block/tile
	 * @param y
	 *            Y location of the block/tile
	 * @param z
	 *            Z location of the block/tile
	 * @return True if this tool can be used
	 */
	boolean isUsable(ItemStack item, EntityLivingBase user, int x, int y, int z);

	/**
	 * Callback for when the tool has been used reactively.
	 *
	 * @param item
	 *            The ItemStack for the tool. Not required to match equipped item (e.g., multi-tools that contain other tools)
	 * @param user
	 *            The entity using the tool
	 * @param x
	 *            X location of the block/tile
	 * @param y
	 *            Y location of the block/tile
	 * @param z
	 *            Z location of the block/tile
	 */
	void toolUsed(ItemStack item, EntityLivingBase user, int x, int y, int z);

}
