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
import matteroverdrive.init.MatterOverdriveBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Simeon on 11/30/2015.
 */
public class MOWorldGenCargoShip extends MOWorldGenBuilding
{
    private static final int MIN_DISTANCE_APART = 4096;

    public MOWorldGenCargoShip(String name)
    {
        super(name, new ResourceLocation(Reference.PATH_WORLD_TEXTURES + "cargo_ship.png"), 58, 23);
        for (BlockDecorative blockDecorative : BlockDecorative.decorativeBlocks)
        {
            addMapping(blockDecorative.getBlockColor(0),blockDecorative);
        }
        addMapping(0xdb9c3a, MatterOverdriveBlocks.holoSign);
        addMapping(0x5fffbe,MatterOverdriveBlocks.transporter);
        addMapping(0xd2fb50,MatterOverdriveBlocks.forceGlass);
        addMapping(0xdc01d8,Blocks.wooden_pressure_plate);
        addMapping(0xfc6b34,new BlockMapping(true,Blocks.gold_ore,Blocks.iron_ore,Blocks.coal_ore,MatterOverdriveBlocks.tritaniumOre));
        addMapping(0xd1626,MatterOverdriveBlocks.fusionReactorIO);
        addMapping(0x1b2ff7,MatterOverdriveBlocks.network_pipe);
        addMapping(0x1f2312,MatterOverdriveBlocks.tritaniumCrate);
        addMapping(0xab4824,Blocks.fence);
        addMapping(0x68d738,Blocks.carpet);
        addMapping(0xbdea8f,Blocks.ladder);
        addMapping(0xeff73d,MatterOverdriveBlocks.network_switch);
        addMapping(0xa8ed1c,MatterOverdriveBlocks.heavy_matter_pipe);
        addMapping(0x4b285d,Blocks.oak_stairs);
        addMapping(0xcfd752,MatterOverdriveBlocks.network_router);
        addMapping(0x4d8dd3,MatterOverdriveBlocks.pattern_monitor);
        addMapping(0x6b3534,Blocks.bed);
        addMapping(0xff00ff,Blocks.air);
    }

    @Override
    protected void onGeneration(Random random, World world, int x, int y, int z)
    {

    }

    @Override
    public int getMetaFromColor(int color,Random random)
    {
        return 255-getAlphaFromColor(color);
    }

    @Override
    protected boolean isLocationValid(World world,int x,int y,int z)
    {
        y = Math.min(y + 86,world.getHeight() - 18);
        return world.getBlock(x,y,z) == Blocks.air
                && world.getBlock(x + layerWidth,y,z) == Blocks.air
                && world.getBlock(x,y,z + layerHeight) == Blocks.air
                && world.getBlock(x + layerWidth,y,z + layerHeight) == Blocks.air
                && world.getBlock(x,y + 16,z) == Blocks.air
                && world.getBlock(x + layerWidth,y + 16,z) == Blocks.air
                && world.getBlock(x,y + 16,z + layerHeight) == Blocks.air
                && world.getBlock(x + layerWidth,y + 16,z + layerHeight) == Blocks.air;
    }

    @Override
    protected boolean shouldGenerate(Random random,World world, int x, int y, int z)
    {
        return (world.provider.dimensionId == 0 || world.provider.dimensionId == 1) && isFarEnoughFromOthers(world,x,z,MIN_DISTANCE_APART) && random.nextDouble() < 0.1;
    }

    @Override
    public void onGenerationWorkerCreated(WorldGenBuildingWorker worldGenBuildingWorker)
    {
        setyOffset(86);
    }

    @Override
    public void onBlockPlace(World world, Block block, int x, int y, int z, Random random, int color)
    {

    }
}
