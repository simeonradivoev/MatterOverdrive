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

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.network.packet.client.PacketUpdateMatterRegistry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 5/9/2015.
 */
public class PlayerEventHandler
{
    private VersionCheckerHandler versionCheckerHandler;
    public List<EntityPlayerMP> players;
    public PlayerEventHandler(ConfigurationHandler configurationHandler)
    {
        players = new ArrayList<EntityPlayerMP>();
        versionCheckerHandler = new VersionCheckerHandler();

        configurationHandler.subscribe(versionCheckerHandler);
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

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.side == Side.CLIENT) {
            versionCheckerHandler.onPlayerTick(event);
        }

        AndroidPlayer player = AndroidPlayer.get(event.player);
        if (player != null) {
            player.onPlayerTick(event);
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
