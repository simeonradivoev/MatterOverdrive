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

package matteroverdrive.api.starmap;

import matteroverdrive.starmap.data.Planet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

/**
 * Created by Simeon on 6/24/2015.
 */
public interface IBuildable
{
    boolean canBuild(ItemStack building,Planet planet,List<String> info);
    int getBuildLength(ItemStack building, Planet planet);
    long getBuildStart(ItemStack building);
    void setBuildStart(ItemStack building,long buildStart);
    boolean isReadyToBuild(World world,ItemStack stack,Planet planet);
    boolean isOwner(ItemStack ship,EntityPlayer player);
    UUID getOwnerID(ItemStack stack);
    void setOwner(ItemStack ship,UUID ownerID);
    /*default double getBuildPercent(ItemStack stack,Planet planet,World world)
    {
        return 1 - (double)((getBuildStart(stack) + getBuildLength(stack,planet)) - world.getTotalWorldTime()) / (double)getBuildLength(stack,planet);
    }*/
    default long getRemainingBuildTimeTicks(ItemStack stack,Planet planet,World world)
    {
        return (getBuildStart(stack) + getBuildLength(stack,planet)) - world.getTotalWorldTime();
    }
}
