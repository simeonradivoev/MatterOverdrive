package com.MO.MatterOverdrive.tile;

import cpw.mods.fml.common.gameevent.TickEvent;

/**
 * Created by Simeon on 5/17/2015.
 */
public interface IMOTickable
{
    void onServerTick(TickEvent.WorldTickEvent event);
    boolean isInvalid();
    int getPhase();
}
