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

import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Simeon on 11/26/2015.
 */
public class WeightedRandomMOWorldGenBuilding extends WeightedRandom.Item
{
    public final MOWorldGenBuilding worldGenBuilding;

    public WeightedRandomMOWorldGenBuilding(MOWorldGenBuilding worldGenBuilding,int weight)
    {
        super(weight);
        this.worldGenBuilding = worldGenBuilding;
    }

    public int getWeight(Random random,World world, int x, int y, int z)
    {
        return worldGenBuilding.shouldGenerate(random,world,x,y,z) && worldGenBuilding.isLocationValid(world,x,y,z) ? itemWeight : Math.max(1,(int)(itemWeight*0.1));
    }
}
