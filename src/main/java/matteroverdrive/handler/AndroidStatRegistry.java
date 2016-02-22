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

package matteroverdrive.handler;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.android.IAndroidStatRegistry;
import matteroverdrive.api.android.IBioticStat;
import matteroverdrive.api.events.MOEventRegisterAndroidStat;
import matteroverdrive.client.render.HoloIcons;
import matteroverdrive.util.MOLog;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.common.MinecraftForge;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Simeon on 5/27/2015.
 */
public class AndroidStatRegistry implements IAndroidStatRegistry
{
    private final HashMap<String,IBioticStat> stats = new HashMap<>();

    @Override
    public boolean registerStat(IBioticStat stat)
    {
        if (stats.containsKey(stat.getUnlocalizedName()))
        {
            MOLog.warn("Stat with the name '%s' is already present!", stat.getUnlocalizedName());
            MatterOverdrive.proxy.getGoogleAnalytics().setExceptionHit("Biotic Stat Registration with existing name");
        }
        else
        {
            if (!MinecraftForge.EVENT_BUS.post(new MOEventRegisterAndroidStat(stat))) {
                stats.put(stat.getUnlocalizedName(), stat);
                return true;
            }
        }
        return false;
    }

    @Override
    public IBioticStat getStat(String name)
    {
        return stats.get(name);
    }

    @Override
    public boolean hasStat(String name)
    {
        return stats.containsKey(name);
    }

    @Override
    public IBioticStat unregisterStat(String statName)
    {
        return stats.remove(statName);
    }

    public void registerIcons(TextureMap textureMap,HoloIcons holoIcons)
    {
        for (IBioticStat stat : stats.values())
        {
            stat.registerIcons(textureMap,holoIcons);
        }
    }

    public Collection<IBioticStat> getStats()
    {
        return stats.values();
    }
}
