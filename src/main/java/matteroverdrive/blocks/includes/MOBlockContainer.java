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

package matteroverdrive.blocks.includes;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public abstract class MOBlockContainer<TE extends TileEntity> extends MOBlock
{
	public MOBlockContainer(Material material, String name)
	{
		super(material, name);
		this.isBlockContainer = true;
	}

	public abstract Class<TE> getTileEntityClass();

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}

	@SuppressWarnings("unchecked")
	public TE getTileEntity(IBlockAccess world, BlockPos pos)
	{
		return (TE)world.getTileEntity(pos);
	}

}
