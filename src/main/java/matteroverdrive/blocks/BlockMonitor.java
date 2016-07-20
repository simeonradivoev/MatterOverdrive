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

import matteroverdrive.blocks.includes.MOBlockMachine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 11/22/2015.
 */
public abstract class BlockMonitor<TE extends TileEntity> extends MOBlockMachine<TE>
{
	protected int depth;

	public BlockMonitor(Material material, String name)
	{
		super(material, name);
		setHasRotation();
		setHardness(20.0F);
		this.setResistance(9.0f);
		this.setHarvestLevel("pickaxe", 2);
		//setBlockBounds(0, 0, 0, 1, 1, 1);
		lightValue = 10;
		depth = 5;
	}

    /*@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
    {
        if (side == meta)
        {
            MatterOverdriveIcons.Monitor_back.setType(0);
            return MatterOverdriveIcons.Monitor_back;
        }
        else if (side == MOBlockHelper.getOppositeSide(meta))
        {
            return MatterOverdriveIcons.Network_port_square;
        }
        return MatterOverdriveIcons.Base;
    }*/

	// TODO: 3/25/2016 Find how to set bounds based on state
	/*@Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
    {
        if (world.getBlockState(pos).getBlock() == this)
        {
            EnumFacing direction = world.getBlockState(pos).getValue(MOBlock.PROPERTY_DIRECTION);
            float pixel = 1f / 16f;

            if (direction == EnumFacing.EAST)
            {
                this.se(0, 0, 0, depth * pixel, 1, 1);
            } else if (direction == EnumFacing.WEST)
            {
                this.setBlockBounds(1 - depth * pixel, 0, 0, 1, 1, 1);
            } else if (direction == EnumFacing.SOUTH)
            {
                this.setBlockBounds(0, 0, 0, 1, 1, depth * pixel);
            } else if (direction == EnumFacing.NORTH)
            {
                this.setBlockBounds(0, 0, 1 - depth * pixel, 1, 1, 1);
            } else
            {
                this.setBlockBounds(0, 0, 0, 1, 1, depth * pixel);
            }
        }
    }

    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0, 0, 0, 1, 1, depth * (1f / 16f));
    }
    */

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World worldIn, BlockPos pos)
	{
		//this.setBlockBoundsBasedOnState(worldIn, pos);
		return super.getCollisionBoundingBox(state, worldIn, pos);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
	{
		//this.setBlockBoundsBasedOnState(worldIn,pos);
		return super.getSelectedBoundingBox(state, worldIn, pos);
	}

	@Override
	public RayTraceResult collisionRayTrace(IBlockState state, World worldIn, BlockPos pos, Vec3d start, Vec3d end)
	{
		//this.setBlockBoundsBasedOnState(worldIn,pos);
		return super.collisionRayTrace(state, worldIn, pos, start, end);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
}
