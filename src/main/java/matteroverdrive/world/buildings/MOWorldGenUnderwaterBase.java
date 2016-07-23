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

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.blocks.BlockDecorative;
import matteroverdrive.entity.monster.EntityMutantScientist;
import matteroverdrive.init.MatterOverdriveBlocks;
import matteroverdrive.world.MOImageGen;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.NoiseGeneratorSimplex;

import java.util.Random;

/**
 * Created by Simeon on 11/26/2015.
 */
public class MOWorldGenUnderwaterBase extends MOWorldGenBuilding
{
	private static final int MIN_DISTANCE_APART = 2048;
	final NoiseGeneratorSimplex noise;

	public MOWorldGenUnderwaterBase(String name)
	{
		super(name, new ResourceLocation(Reference.PATH_WORLD_TEXTURES + "underwater_base.png"), 43, 43);
		validSpawnBlocks = new Block[] {Blocks.WATER};
		setyOffset(-24);
		noise = new NoiseGeneratorSimplex(new Random());
		for (BlockDecorative blockDecorative : BlockDecorative.decorativeBlocks)
		{
			addMapping(blockDecorative.getBlockColor(0), blockDecorative);
		}
		addMapping(0xdc979c, Blocks.TALLGRASS);
		addMapping(0x77d1b6, Blocks.RED_FLOWER);
		addMapping(0xd2fb50, MatterOverdrive.blocks.forceGlass);
		addMapping(0xc1e4e, Blocks.FARMLAND);
		addMapping(0xa7ac65, MatterOverdrive.blocks.tritaniumCrate); //orange crate
		addMapping(0xd6a714, Blocks.STAINED_GLASS);
		addMapping(0x2c5ae9, MatterOverdrive.blocks.weapon_station);
		addMapping(0xacd8c, MatterOverdrive.blocks.androidStation);
		addMapping(0x7018f9, MatterOverdrive.blocks.tritaniumCrate); //light blue
		addMapping(0x4657cc, MatterOverdrive.blocks.tritaniumCrate); //lime
		addMapping(0x1f2312, MatterOverdrive.blocks.tritaniumCrate); //white
		addMapping(0xd3371d, MatterOverdrive.blocks.machine_hull);
		addMapping(0x3640f9, Blocks.STONE_BUTTON);
		addMapping(0xeff73d, MatterOverdrive.blocks.network_switch);
		addMapping(0x5a6388, MatterOverdrive.blocks.boundingBox);
		addMapping(0xbf19a9, Blocks.GRASS);
		addMapping(0xc05e5e, Blocks.FLOWER_POT);
		addMapping(0x4d8dd3, MatterOverdrive.blocks.pattern_monitor);
		addMapping(0xdb9c3a, MatterOverdrive.blocks.holoSign);
		addMapping(0x68b68c, MatterOverdrive.blocks.matter_analyzer);
		addMapping(0x2cb0c7, MatterOverdrive.blocks.starMap);
		addMapping(0x1b2ff7, MatterOverdrive.blocks.network_pipe);
		addMapping(0x5eaab, MatterOverdrive.blocks.tritaniumCrate);
		addMapping(0x11003e, MatterOverdrive.blocks.chargingStation);
		addMapping(0xb31e83, Blocks.CARROTS);
		addMapping(0xc78e77, MatterOverdrive.blocks.replicator);
		addMapping(0x338a42, Blocks.POTATOES);
		addMapping(0xbdea8f, Blocks.LADDER);
		addMapping(0x4d12f4, MatterOverdrive.blocks.pattern_storage);
		addMapping(0xf7d20b, Blocks.SAPLING);
		addMapping(0x854b38, Blocks.IRON_DOOR);
		addMapping(0xff00ff, Blocks.AIR);
	}

	@Override
	public int getMetaFromColor(int color, Random random)
	{
		return 255 - getAlphaFromColor(color);
	}

	@Override
	public MOImageGen.ImageGenWorker getNewWorkerInstance()
	{
		return new WorldGenBuildingWorker();
	}

	@Override
	protected void onGeneration(Random random, World world, BlockPos pos, WorldGenBuildingWorker worker)
	{

	}

	@Override
	public boolean isLocationValid(World world, BlockPos pos)
	{
		return isPointDeepEnough(world, pos) && isPointDeepEnough(world, pos.add(layerWidth, 0, 0)) && isPointDeepEnough(world, pos.add(layerWidth, 0, layerHeight)) && isPointDeepEnough(world, pos.add(0, 0, layerHeight));
	}

	protected boolean isPointDeepEnough(World world, BlockPos pos)
	{
		int blocksInWater = 0;
		while (pos.getY() > 0)
		{
			if (world.getBlockState(pos).getBlock() == Blocks.WATER || world.getBlockState(pos).getBlock() == Blocks.FLOWING_WATER)
			{
				blocksInWater++;
			}
			else
			{
				return blocksInWater > 26;
			}
			pos = pos.add(0, -1, 0);
		}
		return false;
	}

	@Override
	public boolean shouldGenerate(Random random, World world, BlockPos pos)
	{
		//deep_ocean biome
		return world.getBiome(pos).equals(Biome.REGISTRY.getObject(new ResourceLocation("minecraft", "deep_ocean"))) && isFarEnoughFromOthers(world, pos.getX(), pos.getZ(), MIN_DISTANCE_APART);
	}

	@Override
	public void onGenerationWorkerCreated(WorldGenBuildingWorker worker)
	{
		super.onGenerationWorkerCreated(worker);
		int offset = 0;
		int yo = worker.getPos().getY();
		while (yo > 0)
		{
			if (worker.getWorld().isSideSolid(new BlockPos(worker.getPos().getX() + layerWidth / 2, yo, worker.getPos().getZ() + layerHeight / 2), EnumFacing.UP))
			{
				break;
			}
			else
			{
				offset--;
			}
			yo--;
		}
		setyOffset(offset);
	}

	@Override
	public void onBlockPlace(World world, IBlockState block, BlockPos pos, Random random, int color, MOImageGen.ImageGenWorker worker)
	{
		if (block == MatterOverdrive.blocks.starMap)
		{
			EntityMutantScientist mutantScientist = new EntityMutantScientist(world);
			mutantScientist.enablePersistence();
			mutantScientist.setPosition(pos.getX(), pos.getY() + 2, pos.getZ());
			mutantScientist.setCustomNameTag("Mitko'Urrr");
			world.spawnEntityInWorld(mutantScientist);
		}
	}

}
