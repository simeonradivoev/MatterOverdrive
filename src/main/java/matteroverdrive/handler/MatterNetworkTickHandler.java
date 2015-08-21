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

import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.network.IMatterNetworkHandler;
import matteroverdrive.util.IConfigSubscriber;
import org.apache.logging.log4j.Level;

import java.util.List;

/**
 * Created by Simeon on 4/26/2015.
 */
public class MatterNetworkTickHandler implements IConfigSubscriber
{
    private int max_broadcasts;
    int lastID = 0;

    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.side == Side.SERVER) {

            int broadcastCount = 0;
            List tiles = event.world.loadedTileEntityList;

            for (int i = lastID; i < tiles.size(); i++) {
                if (tiles.get(i) instanceof IMatterNetworkHandler)
                {
                    try {
                        broadcastCount += ((IMatterNetworkHandler) tiles.get(i)).onNetworkTick(event.world, event.phase);
                    }catch (Exception e)
                    {
                        MatterOverdrive.log.log(Level.FATAL,e,"There was a problem while ticking MatterNetworkHandler %s",tiles.get(i));
                    }

                    if (broadcastCount >= max_broadcasts) {
                        return;
                    }
                }
            }

            lastID = 0;
        }
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config) {
        this.max_broadcasts = config.getInt(ConfigurationHandler.KEY_MAX_BROADCASTS, ConfigurationHandler.CATEGORY_MATTER_NETWORK,128,"The maximum amount of network packet broadcasts per tick.");
    }
}
