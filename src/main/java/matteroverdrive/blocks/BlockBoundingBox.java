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

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.machines.IDismantleable;
import matteroverdrive.blocks.includes.MOBlockContainer;
import matteroverdrive.init.MatterOverdriveBlocks;
import matteroverdrive.multiblock.IMultiBlockTile;
import matteroverdrive.tile.IMultiBlockTileEntity;
import matteroverdrive.tile.TileEntityBoundingBox;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shadowfacts
 */
public class BlockBoundingBox extends MOBlockContainer<TileEntityBoundingBox> implements IDismantleable
{

	public BlockBoundingBox(String name)
	{
		super(Material.ROCK, name);
		setCreativeTab(null);
	}

	public static void createBoundingBox(World world, BlockPos pos, BlockPos ownerPos, Block ownerBlock)
	{
		world.setBlockState(pos, MatterOverdrive.blocks.boundingBox.getDefaultState());
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityBoundingBox)
		{
			TileEntityBoundingBox boundingBox = (TileEntityBoundingBox)te;
			boundingBox.setOwnerPos(ownerPos);
			boundingBox.setOwnerBlock(ownerBlock);
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntityBoundingBox te = getTileEntity(world, pos);
		IBlockState ownerState = world.getBlockState(te.getOwnerPos());
		if (ownerState.getBlock() == te.getOwnerBlock()) {
			return te.getOwnerBlock().onBlockActivated(world, te.getOwnerPos(), ownerState, player, hand, heldItem, side, hitX, hitY, hitZ);
		}
		return false;
	}

	@Override
	public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager)
	{
		return true; // see javadoc, this actually disables break effects
	}

	@Nonnull
	@Override
	@Deprecated
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.INVISIBLE;
	}

	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Nonnull
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune)
	{
		return new ArrayList<>();
	}

	@Override
	@Deprecated
	@SuppressWarnings("deprecation")
	public float getBlockHardness(IBlockState blockState, World world, BlockPos pos)
	{
		TileEntityBoundingBox te = getTileEntity(world, pos);
		return te.getOwnerBlock().getBlockHardness(world.getBlockState(te.getOwnerPos()), world, te.getOwnerPos());
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntityBoundingBox te = getTileEntity(world, pos);
		TileEntity ownerTe = world.getTileEntity(te.getOwnerPos());
		if (ownerTe instanceof IMultiBlockTileEntity)
		{
			((IMultiBlockTileEntity)ownerTe).getBoundingBlocks().forEach(world::setBlockToAir);
		}
		world.destroyBlock(te.getOwnerPos(), true);
	}

	@Override
	public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, BlockPos pos, boolean returnDrops)
	{
		TileEntityBoundingBox te = getTileEntity(world, pos);
		if (te.getOwnerBlock() instanceof IDismantleable)
		{
			return ((IDismantleable)te.getOwnerBlock()).dismantleBlock(player, world, te.getOwnerPos(), returnDrops);
		}
		else
		{
			return new ArrayList<>();
		}
	}

	@Override
	public boolean canDismantle(EntityPlayer player, World world, BlockPos pos)
	{
		TileEntityBoundingBox te = getTileEntity(world, pos);
		return te.getOwnerBlock() instanceof IDismantleable && ((IDismantleable)te.getOwnerBlock()).canDismantle(player, world, te.getOwnerPos());
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
