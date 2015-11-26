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

import matteroverdrive.data.world.GenPositionWorldData;
import matteroverdrive.data.world.WorldPosition2D;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.init.MatterOverdriveBlocks;
import matteroverdrive.tile.TileEntityGravitationalAnomaly;
import matteroverdrive.util.IConfigSubscriber;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.config.Property;

import java.util.HashSet;
import java.util.Random;

/**
 * Created by Simeon on 5/18/2015.
 */
public class WorldGenGravitationalAnomaly extends WorldGenerator implements IConfigSubscriber
{
    private HashSet<Integer> blacklist = new HashSet<>();
    private HashSet<Integer> whitelist = new HashSet<>();
    private float defaultChance;
    private float chance;
    private int minMatter;
    private int maxMatter;
    private String name;

    public WorldGenGravitationalAnomaly(String name,float chance,int minMatter,int maxMatter)
    {
        this.defaultChance = chance;
        this.chance = chance;
        this.minMatter = minMatter;
        this.maxMatter = maxMatter;
        this.name = name;
    }

    @Override
    public boolean generate(World world, Random random, int x, int y, int z)
    {
        if (isWorldValid(world) && random.nextFloat() < chance)
        {
            if (world.setBlock(x,y,z, MatterOverdriveBlocks.gravitational_anomaly))
            {
                TileEntityGravitationalAnomaly anomaly = new TileEntityGravitationalAnomaly(minMatter + random.nextInt(maxMatter - minMatter));
                world.setTileEntity(x,y,z,anomaly);
                GenPositionWorldData data = MOWorldGen.getWorldPositionData(world);
                data.addPosition(name,new WorldPosition2D(x,y));
            }
        }
        return false;
    }

    private boolean isWorldValid(World world)
    {
        if (whitelist.size() > 0)
        {
            return whitelist.contains(world.provider.dimensionId) && !blacklist.contains(world.provider.dimensionId);
        }

        return !blacklist.contains(world.provider.dimensionId);
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config)
    {
        chance = config.config.getFloat(ConfigurationHandler.KEY_GRAVITATIONAL_ANOMALY_SPAWN_CHANCE, ConfigurationHandler.CATEGORY_WORLD_GEN + "." + "gravitational_anomaly",defaultChance,0,1,"Spawn Chance of Gravity Anomaly pre chunk");
        loadWhitelist(config);
        loadBlacklist(config);
    }

    private void loadWhitelist(ConfigurationHandler configurationHandler)
    {
        whitelist.clear();
        Property whitelistProp = configurationHandler.config.get(ConfigurationHandler.CATEGORY_WORLD_GEN + "." + "gravitational_anomaly","whitelist",new int[]{-1,0,2});
        whitelistProp.comment = "Gravitational Anomaly Dimension ID whitelist";
        int[] dimentions = whitelistProp.getIntList();
        for (int i = 0;i < dimentions.length;i++)
        {
            whitelist.add(dimentions[i]);
        }
    }

    private void loadBlacklist(ConfigurationHandler configurationHandler)
    {
        blacklist.clear();
        Property blacklistProp = configurationHandler.config.get(ConfigurationHandler.CATEGORY_WORLD_GEN + "." + "gravitational_anomaly","blacklist",new int[]{});
        blacklistProp.comment = "Gravitational Anomaly Dimension ID blacklist";
        int[] dimentions = blacklistProp.getIntList();
        for (int i = 0;i < dimentions.length;i++)
        {
            blacklist.add(dimentions[i]);
        }
    }
}
