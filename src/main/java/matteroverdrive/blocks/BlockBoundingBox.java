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

import matteroverdrive.blocks.includes.MOBlockContainer;
import matteroverdrive.init.MatterOverdriveBlocks;
import matteroverdrive.tile.TileEntityBoundingBox;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * @author shadowfacts
 */
public class BlockBoundingBox extends MOBlockContainer<TileEntityBoundingBox>
{

	public BlockBoundingBox(Material material, String name)
	{
		super(material, name);
		setBlockUnbreakable();
		setCreativeTab(null);
	}

	public static void createBoundingBox(World world, BlockPos pos, BlockPos ownerPos, Block ownerBlock)
	{
		world.setBlockState(pos, MatterOverdriveBlocks.boundingBox.getDefaultState());
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityBoundingBox)
		{
			TileEntityBoundingBox boundingBox = (TileEntityBoundingBox)te;
			boundingBox.setOwnerPos(ownerPos);
			boundingBox.setOwnerBlock(ownerBlock);
		}
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.INVISIBLE;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public Class<TileEntityBoundingBox> getTileEntityClass()
	{
		return TileEntityBoundingBox.class;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state)
	{
		return new TileEntityBoundingBox();
	}
}
