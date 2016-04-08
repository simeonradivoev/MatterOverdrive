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
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.NoiseGeneratorSimplex;

import java.util.Random;

/**
 * Created by Simeon on 11/26/2015.
 */
public class MOWorldGenUnderwaterBase extends MOWorldGenBuilding
{
    private static final int MIN_DISTANCE_APART = 2048;
    final NoiseGeneratorSimplex noise;

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
        addMapping(0xa7ac65,MatterOverdriveBlocks.tritaniumCrate); //orange crate
        addMapping(0xd6a714,Blocks.stained_glass);
        addMapping(0x2c5ae9,MatterOverdriveBlocks.weapon_station);
        addMapping(0xacd8c,MatterOverdriveBlocks.androidStation);
        addMapping(0x7018f9,MatterOverdriveBlocks.tritaniumCrate); //light blue
        addMapping(0x4657cc,MatterOverdriveBlocks.tritaniumCrate); //lime
        addMapping(0x1f2312,MatterOverdriveBlocks.tritaniumCrate); //white
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
        addMapping(0x5eaab,MatterOverdriveBlocks.tritaniumCrate);
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
    public MOImageGen.ImageGenWorker getNewWorkerInstance()
    {
        return new WorldGenBuildingWorker();
    }

    @Override
    protected void onGeneration(Random random, World world, BlockPos pos,WorldGenBuildingWorker worker) {

    }

    @Override
    public boolean isLocationValid(World world,BlockPos pos)
    {
        return isPointDeepEnough(world, pos) && isPointDeepEnough(world, pos.add(layerWidth,0,0)) && isPointDeepEnough(world, pos.add(layerWidth,0,layerHeight)) && isPointDeepEnough(world, pos.add(0,0,layerHeight));
    }

    protected boolean isPointDeepEnough(World world,BlockPos pos)
    {
        int blocksInWater = 0;
        while (pos.getY() > 0)
        {
            if (world.getBlockState(pos).getBlock() == Blocks.water || world.getBlockState(pos).getBlock() == Blocks.flowing_water)
            {
                blocksInWater++;
            }
            else
            {
                return blocksInWater > 26;
            }
            pos = pos.add(0,-1,0);
        }
        return false;
    }

    @Override
    public boolean shouldGenerate(Random random,World world, BlockPos pos) {
        //deep_ocean biome
        return world.getBiomeGenForCoords(pos).equals(BiomeGenBase.getBiome(24)) && isFarEnoughFromOthers(world,pos.getX(),pos.getZ(),MIN_DISTANCE_APART);
    }

    @Override
    public void onGenerationWorkerCreated(WorldGenBuildingWorker worker) {
        super.onGenerationWorkerCreated(worker);
        int offset = 0;
        int yo = worker.getPos().getY();
        while (yo > 0)
        {
            if (worker.getWorld().isSideSolid(new BlockPos(worker.getPos().getX() + layerWidth/2,yo,worker.getPos().getZ() + layerHeight/2),EnumFacing.UP))
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
    public void onBlockPlace(World world, IBlockState block,BlockPos pos, Random random, int color,MOImageGen.ImageGenWorker worker)
    {
        if (block == MatterOverdriveBlocks.starMap)
        {
            EntityMutantScientist mutantScientist = new EntityMutantScientist(world);
            mutantScientist.enablePersistence();
            mutantScientist.setPosition(pos.getX(),pos.getY()+2,pos.getZ());
            mutantScientist.setCustomNameTag("Mitko'Urrr");
            world.spawnEntityInWorld(mutantScientist);
        }
    }
}
