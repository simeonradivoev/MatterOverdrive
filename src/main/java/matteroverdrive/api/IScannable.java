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

package matteroverdrive.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Simeon on 5/11/2015.
 * Used for scannable blocks or tile entities.
 */
public interface IScannable
{
	/**
	 * Used to add info to the Holo Screen.
	 * @param world the world.
	 * @param x the X coordinate of the scannable target.
	 * @param y the Y coordinate of the scannable target.
	 * @param z the Z coordinate of the scannable target.
	 * @param infos the info lines list.
	 *              Here is where you add the info lines you want displayed.
	 */
	void addInfo(World world, double x, double y, double z, List<String> infos);

	/**
	 * Called when the target is scanned.
	 * Once the scanning process is complete.
	 * @param world the world.
	 * @param x the X coordinates of the scanned target.
	 * @param y the Y coordinates of the scanned target.
	 * @param z the Z coordinates of the scanned target.
	 * @param player the player who scanned the target.
	 * @param scanner the scanner item stack.
	 */
	void onScan(World world, double x, double y, double z, EntityPlayer player, ItemStack scanner);
}
