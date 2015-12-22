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

package matteroverdrive.world;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.entity.monster.EntityMeleeRougeAndroidMob;
import matteroverdrive.entity.monster.EntityRangedRogueAndroidMob;
import matteroverdrive.init.MatterOverdriveBlocks;
import matteroverdrive.util.MOInventoryHelper;
import matteroverdrive.util.WeaponFactory;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;

import java.util.Random;

public class MOAndroidHouseBuilding extends MOWorldGenBuilding
{
    public MOAndroidHouseBuilding(String name)
    {
        super(name,new ResourceLocation(Reference.PATH_WORLD_TEXTURES + "android_house.png"),21,21);
        setyOffset(-2);
        addMapping(0x00fffc,MatterOverdriveBlocks.decorative_beams,MatterOverdriveBlocks.decorative_carbon_fiber_plate,MatterOverdriveBlocks.decorative_white_plate);
        addMapping(0x623200,Blocks.dirt);
        addMapping(0xffa200,MatterOverdriveBlocks.decorative_floor_tiles);
        addMapping(0xfff600,MatterOverdriveBlocks.decorative_holo_matrix);
        addMapping(0x80b956,Blocks.grass);
        addMapping(0x539ac3,MatterOverdriveBlocks.decorative_tritanium_plate);
        addMapping(0xb1c8d5,MatterOverdriveBlocks.decorative_floor_noise,MatterOverdriveBlocks.decorative_floor_tiles_green,MatterOverdriveBlocks.decorative_floot_tile_white);
        addMapping(0x5f6569,MatterOverdriveBlocks.decorative_vent_dark);
        addMapping(0xf1f1f1,Blocks.air);
        addMapping(0xe400ff,MatterOverdriveBlocks.starMap);
        addMapping(0x1850ad,MatterOverdriveBlocks.decorative_clean);
        addMapping(0x9553c3,MatterOverdriveBlocks.forceGlass);
        addMapping(0x35d6e0,MatterOverdriveBlocks.replicator);
        addMapping(0x35e091,MatterOverdriveBlocks.network_switch);
        addMapping(0xc8d43d,MatterOverdriveBlocks.tritaniumCrate);
        addMapping(0x2a4071,MatterOverdriveBlocks.androidStation,MatterOverdriveBlocks.weapon_station);
        addMapping(0xa13e5f,MatterOverdriveBlocks.network_pipe);
        addMapping(0xa16a3e,MatterOverdriveBlocks.chargingStation);
        addMapping(0x416173,MatterOverdriveBlocks.decorative_tritanium_plate_stripe);
        addMapping(0x187716,MatterOverdriveBlocks.pattern_monitor);
        addMapping(0xac7c1e,MatterOverdriveBlocks.decorative_vent_bright);
        addMapping(0x007eff,MatterOverdriveBlocks.decorative_stripes);
    }

    @Override
    protected void onGeneration(Random random,World world, int x, int y, int z)
    {
        for (int i = 0; i < random.nextInt(3) + 3; i++) {
            spawnAndroid(world, random, x + 7 + i, y + 4, z + 10);
        }
        spawnLegendary(world, random, x + 12, y + 4, z + 10);
    }

    @Override
    protected boolean shouldGenerate(Random random,World world, int x, int y, int z) {
        return world.provider.dimensionId == 0;
    }

    @Override
    public void onGenerationWorkerCreated(WorldGenBuildingWorker worldGenBuildingWorker) {

    }

    @Override
    public void onBlockPlace(World world, Block block, int x, int y, int z,Random random,int color)
    {
        if ((color & 0xffffff) == 0xc8d43d)
        {
            TileEntity inventory = world.getTileEntity(x,y,z);
            if (inventory instanceof IInventory) {
                WeightedRandomChestContent.generateChestContents(random,ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).getItems(random), (IInventory) inventory,random.nextInt(10) + 10);
                if (random.nextInt(200) < 10)
                {
                    MOInventoryHelper.insertItemStackIntoInventory((IInventory)inventory, MatterOverdrive.weaponFactory.getRandomDecoratedEnergyWeapon(new WeaponFactory.WeaponGenerationContext(3,null,true)),0);
                }
            }
        }
    }

    public void spawnAndroid(World world,Random random,int x,int y,int z)
    {
        if (random.nextInt(100) < 60) {
            EntityRangedRogueAndroidMob androidMob = new EntityRangedRogueAndroidMob(world);
            androidMob.setPosition(x+0.5, y+0.5, z+0.5);
            world.spawnEntityInWorld(androidMob);
            androidMob.onSpawnWithEgg(null);
            androidMob.func_110163_bv();
        }else
        {
            EntityMeleeRougeAndroidMob androidMob = new EntityMeleeRougeAndroidMob(world);
            androidMob.setPosition(x+0.5, y+0.5, z+0.5);
            world.spawnEntityInWorld(null);
            androidMob.onSpawnWithEgg(null);
            androidMob.func_110163_bv();
        }
    }

    public void spawnLegendary(World world,Random random,int x,int y,int z)
    {
        EntityRangedRogueAndroidMob legendaryMob = new EntityRangedRogueAndroidMob(world,3,true);
        legendaryMob.setPosition(x+0.5, y+0.5, z+0.5);
        world.spawnEntityInWorld(legendaryMob);
        legendaryMob.onSpawnWithEgg(null);
        legendaryMob.func_110163_bv();
    }

    @Override
    public int getMetaFromColor(int color,Random random)
    {
        int alpha = 255-getAlphaFromColor(color);
        int side = (int) ((alpha/255d)*10d);
        return side;
    }
}
