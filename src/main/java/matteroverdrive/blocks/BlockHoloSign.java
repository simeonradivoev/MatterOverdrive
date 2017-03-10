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

import matteroverdrive.api.machines.IDismantleable;
import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.tile.TileEntityHoloSign;
import matteroverdrive.util.MOInventoryHelper;
import matteroverdrive.util.MachineHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;

/**
 * Created by Simeon on 8/15/2015.
 */
public class BlockHoloSign extends BlockMonitor<TileEntityHoloSign> implements IDismantleable
{

	public BlockHoloSign(Material material, String name)
	{
		super(material, name);
		//setBoundingBox(new AxisAlignedBB(0, 1, 0, 1, 14/16d, 1));
		this.isBlockContainer = true;
		this.setHardness(20f);
		setHasRotation();
	}

	/*    @Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (side == meta)
        {
            MatterOverdriveIcons.Monitor_back.setType(0);
            return MatterOverdriveIcons.Monitor_back;
        }
        return MatterOverdriveIcons.Base;
    }*/

    /*@Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        this.blockIcon = MatterOverdriveIcons.Monitor_back;
        iconConnectedTexture = new IconConnectedTexture(this.blockIcon);
    }*/


    /*@Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(world, x, y, z);
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public int getRenderType()
    {
        return MOBlockRenderer.renderID;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean getBlocksMovement(IBlockAccess world, int x, int y, int z)
    {
        return true;
    }*/

	@Override
	public Class<TileEntityHoloSign> getTileEntityClass()
	{
		return TileEntityHoloSign.class;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state)
	{
		return new TileEntityHoloSign();
	}

	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		boolean flag;
		EnumFacing l = world.getBlockState(pos).getValue(MOBlock.PROPERTY_DIRECTION);
		flag = true;

		IBlockState nState = world.getBlockState(pos.offset(l));
		if (nState.getMaterial().isSolid())
		{
			flag = false;
		}

		if (flag && world instanceof World)
		{
			this.dropBlockAsItem((World)world, pos, world.getBlockState(pos), 0);
			((World)world).setBlockToAir(pos);
		}

		super.onNeighborChange(world, pos, neighbor);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		return MachineHelper.canOpenMachine(worldIn, pos, playerIn, true, "alert.no_rights");
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		return MachineHelper.canRemoveMachine(world, player, pos, willHarvest) && world.setBlockToAir(pos);
	}

	@Override
	public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, BlockPos pos, boolean returnDrops)
	{
		IBlockState blockState = world.getBlockState(pos);
		ItemStack blockItem = new ItemStack(getItemDropped(blockState, world.rand, 1));

		if (!returnDrops)
		{
			dropBlockAsItem(world, pos, blockState, 0);
		}
		else
		{
			MOInventoryHelper.insertItemStackIntoInventory(player.inventory, blockItem, EnumFacing.DOWN);
		}

		ArrayList<ItemStack> list = new ArrayList<>();
		list.add(blockItem);
		return list;
	}

	@Override
	public boolean canDismantle(EntityPlayer entityPlayer, World world, BlockPos pos)
	{
		return true;
	}
}
