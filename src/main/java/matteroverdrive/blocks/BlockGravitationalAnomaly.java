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

import matteroverdrive.api.IScannable;
import matteroverdrive.blocks.includes.MOBlockContainer;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.tile.TileEntityGravitationalAnomaly;
import matteroverdrive.util.IConfigSubscriber;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Simeon on 5/11/2015.
 */
public class BlockGravitationalAnomaly extends MOBlockContainer implements IScannable, IConfigSubscriber
{
	public BlockGravitationalAnomaly(Material material, String name)
	{
		super(material, name);
		// TODO: 3/25/2016 Find how to set block bounds
		//setBlockBounds(0.3f,0.3f,0.3f,0.6f,0.6f,0.6f);
		setBlockUnbreakable();
		setResistance(6000000.0F);
		disableStats();
	}

	@Override
	public boolean isNormalCube(IBlockState blockState)
	{
		return false;
	}

	@Override
	public RayTraceResult collisionRayTrace(IBlockState state, World worldIn, BlockPos pos, Vec3d start, Vec3d end)
	{
		//this.setBlockBoundsBasedOnState(worldIn,pos);
		return super.collisionRayTrace(state, worldIn, pos, start, end);
	}

	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
	{
		return true;
	}

    /*@Override
	public void setBlockBoundsBasedOnState(IBlockStateIBlockAccess world, BlockPos pos)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null && tileEntity instanceof TileEntityGravitationalAnomaly)
        {
            double range = ((TileEntityGravitationalAnomaly) tileEntity).getEventHorizon();
            range = Math.max(range,0.4);
            float rangeMin = (float)(0.5 - (range/2));
            float rangeMax = (float)(0.5 + (range/2));
            setBlockBounds(rangeMin, rangeMin, rangeMin, rangeMax, rangeMax, rangeMax);
        }
    }*/

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World worldIn, BlockPos pos)
	{
		return null;
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

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityGravitationalAnomaly();
	}

	@Override
	public void addInfo(World world, double x, double y, double z, List<String> infos)
	{
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		if (tileEntity != null && tileEntity instanceof TileEntityGravitationalAnomaly)
		{
			((TileEntityGravitationalAnomaly)tileEntity).addInfo(world, x, y, z, infos);
		}
	}

	@Override
	public void onScan(World world, double x, double y, double z, EntityPlayer player, ItemStack scanner)
	{

	}

	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity)
	{
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.INVISIBLE;
	}

	@Override
	public void onConfigChanged(ConfigurationHandler config)
	{
		TileEntityGravitationalAnomaly.BLOCK_ENTETIES = config.getBool(ConfigurationHandler.KEY_GRAVITATIONAL_ANOMALY_BLOCK_ENTITIES, ConfigurationHandler.CATEGORY_SERVER + "." + getUnlocalizedName().substring(5), true, "Should the blocks drop entities or be directly consumed when destroyed by the gravitational anomaly");
		TileEntityGravitationalAnomaly.FALLING_BLOCKS = config.getBool(ConfigurationHandler.KEY_GRAVITATIONAL_ANOMALY_FALLING_BLOCKS, ConfigurationHandler.CATEGORY_SERVER + "." + getUnlocalizedName().substring(5), true, "Should blocks be turned into falling blocks when broken");
		TileEntityGravitationalAnomaly.VANILLA_FLUIDS = config.getBool(ConfigurationHandler.KEY_GRAVITATIONAL_ANOMALY_VANILLA_FLUIDS, ConfigurationHandler.CATEGORY_SERVER + "." + getUnlocalizedName().substring(5), true, "Should vanilla fluid block such as water and lava be consumed by the anomaly");
		TileEntityGravitationalAnomaly.FORGE_FLUIDS = config.getBool(ConfigurationHandler.KEY_GRAVITATIONAL_ANOMALY_FORGE_FLUIDS, ConfigurationHandler.CATEGORY_SERVER + "." + getUnlocalizedName().substring(5), true, "Should other mod fluid blocks be consumed by the anomaly");
		TileEntityGravitationalAnomaly.BLOCK_DESTRUCTION = config.getBool("block destruction", ConfigurationHandler.CATEGORY_SERVER + "." + getUnlocalizedName().substring(5), true, "Should the gravitational anomaly destroy blocks");
		TileEntityGravitationalAnomaly.GRAVITATION = config.getBool("gravitational pull", ConfigurationHandler.CATEGORY_SERVER + "." + getUnlocalizedName().substring(5), true, "Should the gravitational entity pull entities towards it");
	}
}
