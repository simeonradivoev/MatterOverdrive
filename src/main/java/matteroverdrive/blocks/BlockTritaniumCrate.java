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

import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.blocks.includes.MOBlockMachine;
import matteroverdrive.data.Inventory;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.tile.TileEntityTritaniumCrate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.shadowfacts.shadowmc.util.RotationHelper;

import javax.annotation.Nonnull;

/**
 * Created by Simeon on 11/5/2015.
 */
public class BlockTritaniumCrate extends MOBlockMachine<TileEntityTritaniumCrate>
{

	private static final AxisAlignedBB BOX_NORTH_SOUTH = new AxisAlignedBB(0, 0, 2/16d, 1, 12/16d, 14/16d);
	private static final AxisAlignedBB BOX_EAST_WEST = new AxisAlignedBB(2/16d, 0, 0, 14/16d, 12/16d, 1);

	public BlockTritaniumCrate(Material material, String name)
	{
		super(material, name);
		setHardness(20.0F);
		this.setResistance(9.0f);
		this.setHarvestLevel("pickaxe", 2);
		setHasRotation();
	}

	@Override
	@Deprecated
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		EnumFacing dir = state.getValue(MOBlock.PROPERTY_DIRECTION);
		return dir == EnumFacing.NORTH || dir == EnumFacing.SOUTH ? BOX_NORTH_SOUTH : BOX_EAST_WEST;
	}

	@Override
	public Class<TileEntityTritaniumCrate> getTileEntityClass()
	{
		return TileEntityTritaniumCrate.class;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state)
	{
		return new TileEntityTritaniumCrate();
	}

	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
		{
			return true;
		}
		else
		{
			TileEntity entity = worldIn.getTileEntity(pos);
			if (entity instanceof TileEntityTritaniumCrate)
			{
				//FMLNetworkHandler.openGui(entityPlayer, MatterOverdrive.instance, GuiHandler.TRITANIUM_CRATE, world, x, y, z);
				worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), MatterOverdriveSounds.blocksCrateOpen, SoundCategory.BLOCKS, 0.5f, 1);
				playerIn.displayGUIChest(((TileEntityTritaniumCrate)entity).getInventory());
				return true;
			}
		}
		return false;
	}

	@Override
	protected Inventory getInventory(World world, BlockPos pos)
	{
		if (world.getTileEntity(pos) instanceof TileEntityTritaniumCrate)
		{
			TileEntityTritaniumCrate machine = (TileEntityTritaniumCrate)world.getTileEntity(pos);
			return machine.getInventory();
		}
		return null;
	}
}
