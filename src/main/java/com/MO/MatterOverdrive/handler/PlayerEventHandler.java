package com.MO.MatterOverdrive.handler;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.entity.AndroidPlayer;
import com.MO.MatterOverdrive.network.packet.client.PacketUpdateMatterRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 5/9/2015.
 */
public class PlayerEventHandler
{
    public List<EntityPlayerMP> players;

    public PlayerEventHandler()
    {
        players = new ArrayList<EntityPlayerMP>();
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (event.player instanceof EntityPlayerMP) {
            if (MatterRegistry.hasComplitedRegistration) {
                if (!MinecraftServer.getServer().isSinglePlayer()) {

                    MatterOverdrive.packetPipeline.sendTo(new PacketUpdateMatterRegistry(MatterRegistry.getEntries()), (EntityPlayerMP) event.player);
                }
            } else {
                players.add((EntityPlayerMP) event.player);
            }
        }
    }

    public void onServerTick(TickEvent.ServerTickEvent event)
    {
        if (MatterRegistry.hasComplitedRegistration)
        {
            for (int i = 0; i < MatterOverdrive.playerEventHandler.players.size();i++)
            {
                MatterOverdrive.packetPipeline.sendTo(new PacketUpdateMatterRegistry(MatterRegistry.getEntries()),MatterOverdrive.playerEventHandler.players.get(i));
            }

            MatterOverdrive.playerEventHandler.players.clear();
        }
    }
}
