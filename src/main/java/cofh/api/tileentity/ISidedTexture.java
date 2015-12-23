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

import net.minecraft.util.IIcon;

/**
 * Implement this interface on Tile Entities which can change their block's texture based on the current render pass. The block must defer the call to its Tile
 * Entity.
 *
 * @author Zeldo Kavira
 *
 */
public interface ISidedTexture {

	/**
	 * Returns the icon to use for a given side and render pass.
	 *
	 * @param side
	 *            Block side to get the texture for.
	 * @param pass
	 *            Render pass.
	 * @return The icon to use.
	 */
	IIcon getTexture(int side, int pass);

}
