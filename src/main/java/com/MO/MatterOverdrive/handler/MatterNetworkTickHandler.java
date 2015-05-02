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
    public static final String MAX_BROADCASTS_KEY = "Max_Broadcasts_Per_Tick";

    private int max_broadcasts;
    int lastID = 0;

    public MatterNetworkTickHandler(MOConfigurationHandler configuration)
    {
        configuration.load();
        this.max_broadcasts = configuration.config.getInt(MAX_BROADCASTS_KEY,MOConfigurationHandler.CATEGORY_MATTER_NETWORK,128,0,1024,"The maximum amount of network packet broadcasts per tick.");
        configuration.save();
    }

    public MatterNetworkTickHandler(int max_broadcasts)
    {
        this.max_broadcasts = max_broadcasts;
    }

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
}
