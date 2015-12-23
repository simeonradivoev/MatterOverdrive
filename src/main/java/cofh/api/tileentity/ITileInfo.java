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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Implement this interface on Tile Entities which can provide information about themselves.
 *
 * @author King Lemming
 *
 */
public interface ITileInfo {

	/**
	 * This function appends information to a list provided to it.
	 *
	 * @param info
	 *            The list that the information should be appended to.
	 * @param side
	 *            The side of the block that is being queried.
	 * @param player
	 *            Player doing the querying - this can be NULL.
	 * @param debug
	 *            If true, the tile should return "debug" information.
	 */
	void getTileInfo(List<IChatComponent> info, ForgeDirection side, EntityPlayer player, boolean debug);

}
