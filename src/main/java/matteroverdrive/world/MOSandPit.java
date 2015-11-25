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

import cpw.mods.fml.common.IWorldGenerator;
import matteroverdrive.Reference;
import matteroverdrive.data.world.GenPositionWorldData;
import matteroverdrive.data.world.WorldPosition2D;
import matteroverdrive.init.MatterOverdriveBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.Random;

/**
 * Created by Simeon on 11/18/2015.
 */
public class MOSandPit extends MOImageGen implements IWorldGenerator
{
    private double spawnChance;
    private int airLeeway;
    private String name;

    public MOSandPit(String name,double spawnChance,int airLeeway)
    {
        super(new ResourceLocation(Reference.PATH_WORLD_TEXTURES + "sand_pit.png"), 24);
        this.spawnChance = spawnChance;
        this.airLeeway = airLeeway;
        this.name = name;
        addMapping(0xe1db35, Blocks.sandstone);
        addMapping(0xf1f1f1,Blocks.air);
        addMapping(0xffff00,Blocks.sand);
        addMapping(0xc735e1,Blocks.glowstone);
        addMapping(0x35a2e1,Blocks.water);
        addMapping(0x359ae1, MatterOverdriveBlocks.decorative_tritanium_plate);
        addMapping(0xff8400,MatterOverdriveBlocks.decorative_coils);
        addMapping(0x6b4400,Blocks.fence);
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
    {
        if (world.provider.dimensionId == 0) {
            int XCoord = chunkX * 16;
            int ZCoord = chunkZ * 16;
            int YCoord = world.getHeightValue(XCoord, ZCoord);
            if (world.getBiomeGenForCoords(XCoord,ZCoord) == BiomeGenBase.desert && isFlat(world,XCoord,YCoord,ZCoord))
            {
                if (random.nextDouble() < spawnChance) {
                    generateFromImage(world, random, XCoord, YCoord - 10, ZCoord);
                    GenPositionWorldData data = MOWorldGen.getWorldPositionData(world);
                    data.addPosition(name,new WorldPosition2D(XCoord,ZCoord));
                }
            }
        }
    }

    public boolean isFlat(World world,int x,int y,int z)
    {
        int y10 = world.getHeightValue(x+layerSize,z);
        int y11 = world.getHeightValue(x+layerSize,z+layerSize);
        int y01 = world.getHeightValue(x,z+layerSize);
        if (Math.abs(y-y10) <= airLeeway && Math.abs(y-y11) <= airLeeway && Math.abs(y-y01) <= airLeeway)
        {
            return blockBelowMatches(airLeeway,world,Blocks.sand,x,y,z) && blockBelowMatches(airLeeway,world,Blocks.sand,x+layerSize,y,z) && blockBelowMatches(airLeeway,world,Blocks.sand,x,y,z+layerSize) && blockBelowMatches(airLeeway,world,Blocks.sand,x+layerSize,y,z+layerSize);
        }
        return false;
    }

    private boolean blockBelowMatches(int airLeeway,World world,Block block,int x,int y,int z)
    {
        for (int i = 0;i < airLeeway;i++)
        {
            if (world.getBlock(x,y-i,z) == block)
            {
                return true;
            }
        }
        return false;
    }

    private boolean isPointOnSurface(World world,int x,int y,int z)
    {
        if (world.getBlock(x,y+1,z) == Blocks.air)
        {
            return true;
        }
        return false;
    }

    @Override
    public void placeBlock(World world,int color,int x,int y,int z,int layer,Random random)
    {
        if ((color & 0xffffff) == 0xc735e1)
        {
            Block block = getBlockFromColor(color,random);
            int meta = getMetaFromColor(color);
            if (block != null)
            {
                world.setBlock(x, y, z, block, meta, 3);
                onBlockPlace(world,block,x,y,z,random,color);
            }
        }else
        {
            super.placeBlock(world,color,x,y,z,layer,random);
        }
    }

    @Override
    public void onBlockPlace(World world, Block block, int x, int y, int z, Random random, int color) {

    }
}
