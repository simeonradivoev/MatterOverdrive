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

import matteroverdrive.Reference;
import matteroverdrive.entity.monster.EntityMeleeRougeAndroidMob;
import matteroverdrive.entity.monster.EntityRangedRogueAndroidMob;
import matteroverdrive.init.MatterOverdriveBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class MOAndroidHouseBuilding extends MOWorldGenBuilding
{
	public MOAndroidHouseBuilding(String name)
	{
		super(name, new ResourceLocation(Reference.PATH_WORLD_TEXTURES + "android_house.png"), 21, 21);
		setyOffset(-2);
		addMapping(0x00fffc, MatterOverdriveBlocks.decorative_beams, MatterOverdriveBlocks.decorative_carbon_fiber_plate, MatterOverdriveBlocks.decorative_white_plate);
		addMapping(0x623200, Blocks.DIRT);
		addMapping(0xffa200, MatterOverdriveBlocks.decorative_floor_tiles);
		addMapping(0xfff600, MatterOverdriveBlocks.decorative_holo_matrix);
		addMapping(0x80b956, Blocks.GRASS);
		addMapping(0x539ac3, MatterOverdriveBlocks.decorative_tritanium_plate);
		addMapping(0xb1c8d5, MatterOverdriveBlocks.decorative_floor_noise, MatterOverdriveBlocks.decorative_floor_tiles_green, MatterOverdriveBlocks.decorative_floot_tile_white);
		addMapping(0x5f6569, MatterOverdriveBlocks.decorative_vent_dark);
		addMapping(0xf1f1f1, Blocks.AIR);
		addMapping(0xe400ff, MatterOverdriveBlocks.starMap);
		addMapping(0x1850ad, MatterOverdriveBlocks.decorative_clean);
		addMapping(0x9553c3, MatterOverdriveBlocks.forceGlass);
		addMapping(0x35d6e0, MatterOverdriveBlocks.replicator);
		addMapping(0x35e091, MatterOverdriveBlocks.network_switch);
		addMapping(0xc8d43d, MatterOverdriveBlocks.tritaniumCrate);
		addMapping(0x2a4071, MatterOverdriveBlocks.androidStation, MatterOverdriveBlocks.weapon_station);
		addMapping(0xa13e5f, MatterOverdriveBlocks.network_pipe);
		addMapping(0xa16a3e, MatterOverdriveBlocks.chargingStation);
		addMapping(0x416173, MatterOverdriveBlocks.decorative_tritanium_plate_stripe);
		addMapping(0x187716, MatterOverdriveBlocks.pattern_monitor);
		addMapping(0xac7c1e, MatterOverdriveBlocks.decorative_vent_bright);
		addMapping(0x007eff, MatterOverdriveBlocks.decorative_stripes);
	}

	@Override
	protected void onGeneration(Random random, World world, BlockPos pos, WorldGenBuildingWorker worker)
	{
		for (int i = 0; i < random.nextInt(3) + 3; i++)
		{
			spawnAndroid(world, random, pos.add(7, i, 10));
		}
		spawnLegendary(world, random, pos.add(12, 4, 10));
	}

	@Override
	public boolean shouldGenerate(Random random, World world, BlockPos pos)
	{
		return world.provider.getDimension() == 0;
	}

	@Override
	public void onBlockPlace(World world, IBlockState block, BlockPos pos, Random random, int color, ImageGenWorker worker)
	{
		// TODO: 3/25/2016 Find how to get chest gen hook
		/*if ((color & 0xffffff) == 0xc8d43d)
		{
            TileEntity inventory = world.getTileEntity(pos);
            if (inventory instanceof IInventory) {
                WeightedRandomChestContent.generateDispenserContents(random,ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).getItems(random), (IInventory) inventory,random.nextInt(10) + 10);
                if (random.nextInt(200) < 10)
                {
                    MOInventoryHelper.insertItemStackIntoInventory((IInventory)inventory, MatterOverdrive.weaponFactory.getRandomDecoratedEnergyWeapon(new WeaponFactory.WeaponGenerationContext(3,null,true)), EnumFacing.DOWN);
                }
            }
        }*/
	}

	public void spawnAndroid(World world, Random random, BlockPos pos)
	{
		if (random.nextInt(100) < 60)
		{
			EntityRangedRogueAndroidMob androidMob = new EntityRangedRogueAndroidMob(world);
			androidMob.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
			world.spawnEntityInWorld(androidMob);
			androidMob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			androidMob.enablePersistence();
		}
		else
		{
			EntityMeleeRougeAndroidMob androidMob = new EntityMeleeRougeAndroidMob(world);
			androidMob.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
			world.spawnEntityInWorld(androidMob);
			androidMob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
			androidMob.enablePersistence();
		}
	}

	public void spawnLegendary(World world, Random random, BlockPos pos)
	{
		EntityRangedRogueAndroidMob legendaryMob = new EntityRangedRogueAndroidMob(world, 3, true);
		legendaryMob.setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
		world.spawnEntityInWorld(legendaryMob);
		legendaryMob.onInitialSpawn(world.getDifficultyForLocation(pos), null);
		legendaryMob.enablePersistence();
	}

	@Override
	public int getMetaFromColor(int color, Random random)
	{
		int alpha = 255 - getAlphaFromColor(color);
		return (int)((alpha / 255d) * 10d);
	}

	@Override
	public WorldGenBuildingWorker getNewWorkerInstance()
	{
		return new WorldGenBuildingWorker();
	}
}
