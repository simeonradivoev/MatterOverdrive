package com.MO.MatterOverdrive.handler;

import com.MO.MatterOverdrive.client.RenderParticlesHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.world.World;

/**
 * Created by Simeon on 4/26/2015.
 */
public class TickHandler
{
    private MatterNetworkTickHandler networkTick;
    private VersionCheckerHandler versionCheckerHandler;
    private PlayerEventHandler playerEventHandler;
    private boolean worldStartFired = false;

    public TickHandler(MOConfigurationHandler configurationHandler,PlayerEventHandler playerEventHandler)
    {
        networkTick = new MatterNetworkTickHandler();
        this.playerEventHandler = playerEventHandler;
        versionCheckerHandler = new VersionCheckerHandler();

        configurationHandler.subscribe(versionCheckerHandler);
        configurationHandler.subscribe(networkTick);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        versionCheckerHandler.onPlayerTick(event);
    }

    //Called when the client ticks.
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {

    }

    //Called when the server ticks. Usually 20 ticks a second.
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event)
    {
        playerEventHandler.onServerTick(event);
    }

    //Called when a new frame is displayed (See fps)
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {

    }

    //Called when the world ticks
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event)
    {
        if (!worldStartFired)
        {
            onWorldStart(event.side,event.world);
            worldStartFired = true;
        }
        networkTick.onWorldTick(event);
    }

    public void onWorldStart(Side side,World world)
    {

    }
}
