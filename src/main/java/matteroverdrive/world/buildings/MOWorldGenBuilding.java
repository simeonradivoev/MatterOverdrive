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

package matteroverdrive.world.buildings;

import matteroverdrive.data.world.GenPositionWorldData;
import matteroverdrive.data.world.WorldPosition2D;
import matteroverdrive.util.MOLog;
import matteroverdrive.world.MOImageGen;
import matteroverdrive.world.MOWorldGen;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import org.apache.logging.log4j.Level;

import java.util.Random;

/**
 * Created by Simeon on 11/26/2015.
 */
public abstract class MOWorldGenBuilding<T extends MOWorldGenBuilding.WorldGenBuildingWorker> extends MOImageGen<T> implements IMOWorldGenBuilding<T>
{

	protected Block[] validSpawnBlocks;
	int yOffset = -1;
	int maxDistanceToAir = 2;
	String name;

	public MOWorldGenBuilding(String name, ResourceLocation texture, int layerWidth, int layerHeight)
	{
		super(texture, layerWidth, layerHeight);
		this.name = name;
		validSpawnBlocks = new Block[] {Blocks.STONE, Blocks.GRASS, Blocks.DIRT};
	}

	@Override
	public void generate(Random random, BlockPos pos, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider, int layer, int placeNotify, T worker)
	{
		generateFromImage(world, random, pos.add(0, getYOffset(), 0), layer, placeNotify, worker);
	}

	public boolean locationIsValidSpawn(World world, BlockPos pos)
	{
		int distanceToAir = 0;
		IBlockState blockState = world.getBlockState(pos);

		while (blockState.getBlock() != Blocks.AIR)
		{
			if (distanceToAir > getMaxDistanceToAir())
			{
				return false;
			}

			distanceToAir++;
			blockState = world.getBlockState(pos.add(0, distanceToAir, 0));
		}

		pos = pos.add(0, distanceToAir - 1, 0);

		IBlockState block = world.getBlockState(pos);
		IBlockState blockAbove = world.getBlockState(pos.add(0, 1, 0));
		IBlockState blockBelow = world.getBlockState(pos.add(0, -1, 0));

		for (Block x : getValidSpawnBlocks())
		{
			if (blockAbove != Blocks.AIR)
			{
				return false;
			}
			if (block == x)
			{
				return true;
			}
			else if (block == Blocks.SNOW && blockBelow == x)
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public String getName()
	{
		return name;
	}

	protected int getMaxDistanceToAir()
	{
		return maxDistanceToAir;
	}

	public void setMaxDistanceToAir(int maxDistanceToAir)
	{
		this.maxDistanceToAir = maxDistanceToAir;
	}

	protected Block[] getValidSpawnBlocks()
	{
		return validSpawnBlocks;
	}

	public int getYOffset()
	{
		return yOffset;
	}

	public void setyOffset(int yOffset)
	{
		this.yOffset = yOffset;
	}

	protected abstract void onGeneration(Random random, World world, BlockPos pos, T worker);

	public abstract boolean shouldGenerate(Random random, World world, BlockPos pos);

	public boolean isLocationValid(World world, BlockPos pos)
	{
		return locationIsValidSpawn(world, pos) && locationIsValidSpawn(world, pos.add(layerWidth, 0, 0)) && locationIsValidSpawn(world, pos.add(layerWidth, 0, layerHeight)) && locationIsValidSpawn(world, pos.add(0, 0, layerHeight));
	}

	@Override
	public void onGenerationWorkerCreated(T worker)
	{
		worker.setWorldGenBuilding(this);
		GenPositionWorldData data = MOWorldGen.getWorldPositionData(worker.getWorld());
		data.addPosition(getName(), new WorldPosition2D(worker.getPos().getX() + layerWidth / 2, worker.getPos().getZ() + layerHeight / 2));
	}

	public boolean isFarEnoughFromOthers(World world, int x, int z, int minDistance)
	{
		GenPositionWorldData worldData = MOWorldGen.getWorldPositionData(world);
		if (worldData != null)
		{
			return worldData.isFarEnough(getName(), x, z, minDistance);
		}
		return true;
	}


	public static class WorldGenBuildingWorker extends ImageGenWorker
	{
		MOWorldGenBuilding worldGenBuilding;

		public void setWorldGenBuilding(MOWorldGenBuilding worldGenBuilding)
		{
			this.worldGenBuilding = worldGenBuilding;
			this.worldGenBuilding.manageTextureLoading();
		}

		public boolean generate()
		{
			try
			{
				if (currentLayer >= worldGenBuilding.getLayerCount())
				{
					worldGenBuilding.onGeneration(random, getWorld(), getPos(), this);
					return true;
				}
				else
				{
					worldGenBuilding.generate(random, getPos(), getWorld(), getChunkGenerator(), getChunkProvider(), currentLayer, getPlaceNotify(), this);
					currentLayer++;
					return false;
				}

			}
			catch (Exception e)
			{
				MOLog.log(Level.ERROR, e, "There was a problem while generating layer %s of %s", currentLayer, worldGenBuilding.getName());
			}
			return false;
		}
	}
}
