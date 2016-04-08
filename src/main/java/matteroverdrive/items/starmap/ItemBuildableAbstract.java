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

package matteroverdrive.items.starmap;

import matteroverdrive.api.starmap.IBuildable;
import matteroverdrive.items.includes.MOBaseItem;
import matteroverdrive.starmap.data.Galaxy;
import matteroverdrive.starmap.data.Planet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.UUID;

/**
 * Created by Simeon on 7/6/2015.
 */
public abstract class ItemBuildableAbstract extends MOBaseItem implements IBuildable
{
    public ItemBuildableAbstract(String name)
    {
        super(name);
    }

    protected abstract int getBuildLengthUnscaled(ItemStack buildableStack, Planet planet);

    @Override
    public long getBuildStart(ItemStack building)
    {
        if (building.hasTagCompound())
        {
            return building.getTagCompound().getLong("BuildStart");
        }
        else
        {
            return 0;
        }
    }

    @Override
    public void setBuildStart(ItemStack building,long buildStart)
    {
        if (!building.hasTagCompound())
        {
            building.setTagCompound(new NBTTagCompound());
        }

        building.getTagCompound().setLong("BuildStart", buildStart);
    }

    @Override
    public boolean isReadyToBuild(World world,ItemStack stack,Planet planet)
    {
        if (stack.hasTagCompound())
        {
            if (getBuildStart(stack) + getBuildLength(stack,planet) < world.getTotalWorldTime())
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isOwner(ItemStack ship, EntityPlayer player)
    {
        if (ship.hasTagCompound())
        {
            if (ship.getTagCompound().hasKey("Owner") && !ship.getTagCompound().getString("Owner").isEmpty()) {
                try {
                    return UUID.fromString(ship.getTagCompound().getString("Owner")).equals(EntityPlayer.getUUID(player.getGameProfile()));
                }
                catch (Exception e)
                {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public UUID getOwnerID(ItemStack stack)
    {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Owner",8))
        {
            try {
                return UUID.fromString(stack.getTagCompound().getString("Owner"));
            }catch (Exception e)
            {
                return null;
            }
        }
        return null;
    }

    @Override
    public void setOwner(ItemStack ship, UUID playerId)
    {
        if (!ship.hasTagCompound())
        {
            ship.setTagCompound(new NBTTagCompound());
        }

        ship.getTagCompound().setString("Owner",playerId.toString());
    }

    @Override
    public int getBuildLength(ItemStack buildableStack, Planet planet)
    {
        return MathHelper.ceiling_double_int(getBuildLengthUnscaled(buildableStack, planet) * Galaxy.GALAXY_BUILD_TIME_MULTIPLY);
    }
}
