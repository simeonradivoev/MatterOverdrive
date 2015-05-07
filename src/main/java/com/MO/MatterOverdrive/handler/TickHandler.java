package com.MO.MatterOverdrive.handler;

import com.MO.MatterOverdrive.handler.thread.CheckVersion;
import com.MO.MatterOverdrive.handler.thread.RegisterItemsFromRecipes;
import com.MO.MatterOverdrive.init.MatterOverdriveMatter;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

/**
 * Created by Simeon on 4/26/2015.
 */
public class TickHandler
{
    private MatterNetworkTickHandler networkTick;
    private VersionCheckerHandler versionCheckerHandler;
    private boolean worldStartFired = false;

    public TickHandler(MOConfigurationHandler configurationHandler)
    {
        networkTick = new MatterNetworkTickHandler(configurationHandler);
        versionCheckerHandler = new VersionCheckerHandler();
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        versionCheckerHandler.onPlayerTick(event);
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
        if (!worldStartFired)
        {
            onWorldStart(event.side,event.world);
            worldStartFired = true;
        }
        networkTick.onWorldTick(event);
    }

    public void onWorldStart(Side side,World world)
    {
        Thread registerItemsThread = new Thread(new RegisterItemsFromRecipes());
        registerItemsThread.run();
    }
}
