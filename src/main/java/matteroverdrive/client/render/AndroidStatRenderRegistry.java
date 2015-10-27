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

package matteroverdrive.client.render;

import matteroverdrive.api.android.IAndroidStatRenderRegistry;
import matteroverdrive.api.android.IBionicStat;
import matteroverdrive.api.events.MOEventRegisterAndroidStatRenderer;
import matteroverdrive.api.renderer.IBioticStatRenderer;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simeon on 7/24/2015.
 */
public class AndroidStatRenderRegistry implements IAndroidStatRenderRegistry
{
    Map<Class<? extends IBionicStat>,Collection<IBioticStatRenderer>> map;

    public AndroidStatRenderRegistry()
    {
        map = new HashMap<>();
    }

    @Override
    public Collection<IBioticStatRenderer> getRendererCollection(Class<? extends IBionicStat> stat)
    {
        return map.get(stat);
    }

    @Override
    public Collection<IBioticStatRenderer> removeAllRenderersFor(Class<? extends IBionicStat> stat) {
        return map.remove(stat);
    }

    @Override
    public boolean registerRenderer(Class<? extends IBionicStat> stat, IBioticStatRenderer renderer)
    {
        if (!MinecraftForge.EVENT_BUS.post(new MOEventRegisterAndroidStatRenderer(stat,renderer))) {
            Collection<IBioticStatRenderer> collection = map.get(stat);
            if (collection == null) {
                collection = new ArrayList<>();
                map.put(stat, collection);
            }
            return collection.add(renderer);
        }
        return false;
    }
}