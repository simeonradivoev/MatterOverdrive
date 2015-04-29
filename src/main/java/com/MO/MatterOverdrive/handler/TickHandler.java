package com.MO.MatterOverdrive.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

/**
 * Created by Simeon on 4/26/2015.
 */
public class TickHandler
{
    private MatterNetworkTickHandler networkTick;

    public TickHandler()
    {
        networkTick = new MatterNetworkTickHandler();
    }

    //Called when a player ticks.
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {

    }


    //Called when the client ticks.
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {

    }

    //Called when the server ticks. Usually 20 ticks a second.
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {

    }

    //Called when a new frame is displayed (See fps)
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {

    }

    //Called when the world ticks
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        networkTick.onWorldTick(event);
    }
}
