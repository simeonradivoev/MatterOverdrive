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

package cofh.api.block;

import cofh.api.tileentity.ITileInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Implement this interface on blocks which can provide information about themselves. If the block contains Tile Entities, then it is recommended that this
 * function serve as a passthrough for {@link ITileInfo}.
 *
 * @author King Lemming
 *
 */
public interface IBlockInfo {

	/**
	 * This function appends information to a list provided to it.
	 *
	 * @param world
	 *            Reference to the world.
	 * @param x
	 *            X coordinate of the block.
	 * @param y
	 *            Y coordinate of the block.
	 * @param z
	 *            Z coordinate of the block.
	 * @param side
	 *            The side of the block that is being queried.
	 * @param player
	 *            Player doing the querying - this can be NULL.
	 * @param info
	 *            The list that the information should be appended to.
	 * @param debug
	 *            If true, the block should return "debug" information.
	 */
	void getBlockInfo(IBlockAccess world, int x, int y, int z, ForgeDirection side, EntityPlayer player, List<IChatComponent> info, boolean debug);

}
