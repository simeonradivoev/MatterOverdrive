package com.MO.MatterOverdrive.handler;

import com.MO.MatterOverdrive.handler.thread.CheckVersion;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.server.MinecraftServer;

/**
 * Created by Simeon on 5/7/2015.
 */
public class VersionCheckerHandler
{
    private boolean updateInfoDisplayed = false;
    int lastPoll = 400;

    //Called when a player ticks.
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.START)
        {
            return;
        }

        if (MinecraftServer.getServer() != null && MinecraftServer.getServer().isServerRunning())
        {
            if (!MinecraftServer.getServer().getConfigurationManager().func_152596_g(event.player.getGameProfile()))
            {
                return;
            }
        }

        if (lastPoll > 0)
        {
            --lastPoll;
            return;
        }
        lastPoll = 400;

        if (!updateInfoDisplayed)
        {
            updateInfoDisplayed = true;
            Thread updateCheck = new Thread(new CheckVersion(event.player));
            updateCheck.run();
        }
    }
}
