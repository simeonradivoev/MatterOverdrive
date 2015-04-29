package com.MO.MatterOverdrive.handler;

import com.MO.MatterOverdrive.api.network.IMatterNetworkConnectionProxy;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;

import java.util.List;

/**
 * Created by Simeon on 4/26/2015.
 */
public class MatterNetworkTickHandler
{
    public static final int MAX_BROADCAST_COUNT_PER_TICK = 128;
    int lastID = 0;

    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.side == Side.SERVER) {

            int broadcastCount = 0;
            List tiles = event.world.loadedTileEntityList;

            for (int i = lastID; i < tiles.size(); i++) {
                if (tiles.get(i) instanceof IMatterNetworkConnectionProxy) {
                    broadcastCount += ((IMatterNetworkConnectionProxy) tiles.get(i)).onNetworkTick(event.world, event.phase);

                    if (broadcastCount >= MAX_BROADCAST_COUNT_PER_TICK) {
                        return;
                    }
                }
            }

            lastID = 0;
        }
    }
}
