package com.MO.MatterOverdrive.handler;

import com.MO.MatterOverdrive.api.network.IMatterNetworkConnectionProxy;
import com.MO.MatterOverdrive.util.IConfigSubscriber;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;

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
                if (tiles.get(i) instanceof IMatterNetworkConnectionProxy) {
                    broadcastCount += ((IMatterNetworkConnectionProxy) tiles.get(i)).onNetworkTick(event.world, event.phase);

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
