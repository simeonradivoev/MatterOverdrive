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

import matteroverdrive.Reference;
import matteroverdrive.blocks.BlockDecorative;
import matteroverdrive.entity.monster.EntityMutantScientist;
import matteroverdrive.init.MatterOverdriveBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.NoiseGeneratorSimplex;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

/**
 * Created by Simeon on 11/26/2015.
 */
public class MOWorldGenUnderwaterBase extends MOWorldGenBuilding
{
    private static final int MIN_DISTANCE_APART = 2048;
    NoiseGeneratorSimplex noise;

    public MOWorldGenUnderwaterBase(String name) {
        super(name,new ResourceLocation(Reference.PATH_WORLD_TEXTURES + "underwater_base.png"),43,43);
        validSpawnBlocks = new Block[]{Blocks.water};
        setyOffset(-24);
        noise = new NoiseGeneratorSimplex(new Random());
        for (BlockDecorative blockDecorative : BlockDecorative.decorativeBlocks)
        {
            addMapping(blockDecorative.getBlockColor(0),blockDecorative);
        }
        addMapping(0xdc979c,Blocks.tallgrass);
        addMapping(0x77d1b6,Blocks.red_flower);
        addMapping(0xd2fb50, MatterOverdriveBlocks.forceGlass);
        addMapping(0xc1e4e,Blocks.farmland);
        addMapping(0xa7ac65,MatterOverdriveBlocks.tritaniumCrate[14]); //orange crate
        addMapping(0xd6a714,Blocks.stained_glass);
        addMapping(0x2c5ae9,MatterOverdriveBlocks.weapon_station);
        addMapping(0xacd8c,MatterOverdriveBlocks.androidStation);
        addMapping(0x7018f9,MatterOverdriveBlocks.tritaniumCrate[12]); //light blue
        addMapping(0x4657cc,MatterOverdriveBlocks.tritaniumCrate[10]); //lime
        addMapping(0x1f2312,MatterOverdriveBlocks.tritaniumCrate[15]); //white
        addMapping(0xd3371d,MatterOverdriveBlocks.machine_hull);
        addMapping(0x3640f9,Blocks.stone_button);
        addMapping(0xeff73d,MatterOverdriveBlocks.network_switch);
        addMapping(0x5a6388,MatterOverdriveBlocks.boundingBox);
        addMapping(0xbf19a9,Blocks.grass);
        addMapping(0xc05e5e,Blocks.flower_pot);
        addMapping(0x4d8dd3,MatterOverdriveBlocks.pattern_monitor);
        addMapping(0xdb9c3a,MatterOverdriveBlocks.holoSign);
        addMapping(0x68b68c,MatterOverdriveBlocks.matter_analyzer);
        addMapping(0x2cb0c7,MatterOverdriveBlocks.starMap);
        addMapping(0x1b2ff7,MatterOverdriveBlocks.network_pipe);
        addMapping(0x5eaab,MatterOverdriveBlocks.tritaniumCrate[11]);
        addMapping(0x11003e,MatterOverdriveBlocks.chargingStation);
        addMapping(0xb31e83,Blocks.carrots);
        addMapping(0xc78e77,MatterOverdriveBlocks.replicator);
        addMapping(0x338a42,Blocks.potatoes);
        addMapping(0xbdea8f, Blocks.ladder);
        addMapping(0x4d12f4,MatterOverdriveBlocks.pattern_storage);
        addMapping(0xf7d20b,Blocks.sapling);
        addMapping(0x854b38,Blocks.iron_door);
        addMapping(0xff00ff,Blocks.air);
    }

    @Override
    public int getMetaFromColor(int color,Random random)
    {
        return 255-getAlphaFromColor(color);
    }

    @Override
    protected void onGeneration(Random random, World world, int x, int y, int z) {

    }

    @Override
    protected boolean isLocationValid(World world,int x,int y,int z)
    {
        return isPointDeepEnough(world, x, y, z) && isPointDeepEnough(world, x + layerWidth, y, z) && isPointDeepEnough(world, x + layerWidth, y, z + layerHeight) && isPointDeepEnough(world, x, y, z + layerHeight);
    }

    protected boolean isPointDeepEnough(World world,int x,int y,int z)
    {
        int blocksInWater = 0;
        while (y > 0)
        {
            if (world.getBlock(x,y,z) == Blocks.water || world.getBlock(x,y,z) == Blocks.flowing_water)
            {
                blocksInWater++;
            }
            else
            {
                if (blocksInWater > 26)
                {
                    return true;
                }else
                {
                    return false;
                }
            }
            y--;
        }
        return false;
    }

    @Override
    protected boolean shouldGenerate(Random random,World world, int x, int y, int z) {
        return world.getBiomeGenForCoords(x,z).isEqualTo(BiomeGenBase.deepOcean) && isFarEnoughFromOthers(world,x,z,MIN_DISTANCE_APART);
    }

    @Override
    public void onGenerationWorkerCreated(WorldGenBuildingWorker worker) {
        int offset = 0;
        int yo = worker.y;
        while (yo > 0)
        {
            if (worker.world.isSideSolid(worker.x+layerWidth/2,yo,worker.z+layerHeight/2,ForgeDirection.UP))
            {
                break;
            }else
            {
                offset--;
            }
            yo--;
        }
        setyOffset(offset);
    }

    @Override
    public void onBlockPlace(World world, Block block, int x, int y, int z, Random random, int color)
    {
        if (block == MatterOverdriveBlocks.starMap)
        {
            EntityMutantScientist mutantScientist = new EntityMutantScientist(world);
            mutantScientist.func_110163_bv();
            mutantScientist.setPosition(x,y+2,z);
            mutantScientist.setCustomNameTag("Mitko'Urrr");
            world.spawnEntityInWorld(mutantScientist);
        }
    }
}
