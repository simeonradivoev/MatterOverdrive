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
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Created by Simeon on 8/15/2015.
 */
public class BlockHoloSign extends BlockMonitor implements IDismantleable, ITileEntityProvider
{

	public BlockHoloSign(Material material, String name)
	{
		super(material, name);
		depth = 2;
		float f = 0.25F;
		float f1 = 1.0F;
		// TODO: 3/26/2016 Find how to set block bounds
		//this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
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
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityHoloSign();
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{
		boolean flag;
		EnumFacing l = worldIn.getBlockState(pos).getValue(MOBlock.PROPERTY_DIRECTION);
		flag = true;

		IBlockState nState = worldIn.getBlockState(pos.offset(l));
		if (nState.getBlock().getMaterial(nState).isSolid())
		{
			flag = false;
		}

		if (flag)
		{
			this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
			worldIn.setBlockToAir(pos);
		}

		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
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
