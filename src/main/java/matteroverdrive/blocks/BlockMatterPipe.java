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

package matteroverdrive.blocks;

import matteroverdrive.tile.pipes.TileEntityMatterPipe;
import matteroverdrive.util.MOBlockHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by Simeon on 3/7/2015.
 */
public class BlockMatterPipe extends BlockPipe<TileEntityMatterPipe>
{
	public BlockMatterPipe(Material material, String name)
	{
		super(material, name);
		setHardness(10.0F);
		this.setResistance(5.0f);
		setRotationType(MOBlockHelper.RotationType.PREVENT);
	}

	@Override
	public Class<TileEntityMatterPipe> getTileEntityClass()
	{
		return TileEntityMatterPipe.class;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state)
	{
		return new TileEntityMatterPipe();
	}
}
