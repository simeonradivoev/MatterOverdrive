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
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.weapon.IWeapon;
import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.network.packet.client.PacketSyncAndroid;
import matteroverdrive.network.packet.client.PacketUpdateMatterRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
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
        players = new ArrayList<>();
        versionCheckerHandler = new VersionCheckerHandler();

        configurationHandler.subscribe(versionCheckerHandler);
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (event.player instanceof EntityPlayerMP) {
            if (MatterOverdrive.matterRegistry.hasComplitedRegistration) {
                if (!MinecraftServer.getServer().isSinglePlayer()) {

                    MatterOverdrive.packetPipeline.sendTo(new PacketUpdateMatterRegistry(MatterOverdrive.matterRegistry.getEntries()), (EntityPlayerMP) event.player);

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
            if (event.side == Side.CLIENT) {
                onAndroidTickClient(player, event);

            } else {
                onAndroidServerTick(player, event);
            }
        }

        //used to stop the item refreshing when using weapons and changing their data
        if(event.player.worldObj.isRemote) {
            if (event.player.getItemInUse() != null && event.player.getItemInUse().getItem() instanceof IWeapon) {
                ItemStack itemstack = event.player.inventory.getCurrentItem();

                if (itemstack != null && Minecraft.getMinecraft().currentScreen == null) {
                    if (event.player.getItemInUse().isItemEqual(itemstack)) {
                        event.player.setItemInUse(itemstack,event.player.getItemInUseCount());
                    } else {
                        System.out.println(String.format("%s not equal to %s", itemstack, event.player.getItemInUse()));
                    }
                } else {
                    event.player.clearItemInUse();
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void onAndroidTickClient(AndroidPlayer androidPlayer,TickEvent.PlayerTickEvent event)
    {
        if (event.player == Minecraft.getMinecraft().thePlayer)
        {
            androidPlayer.onPlayerTick(event);
        }
    }

    private void onAndroidServerTick(AndroidPlayer androidPlayer,TickEvent.PlayerTickEvent event)
    {
        androidPlayer.onPlayerTick(event);
    }

    @SubscribeEvent
    public void onPlayerLoadFromFile(net.minecraftforge.event.entity.player.PlayerEvent.LoadFromFile event)
    {
        AndroidPlayer player = AndroidPlayer.get(event.entityPlayer);
        if (player != null) {
            player.onPlayerLoad(event);
        }
    }

    @SubscribeEvent
    public void onStartTracking(net.minecraftforge.event.entity.player.PlayerEvent.StartTracking event)
    {
        if (event.target instanceof EntityPlayer)
        {
            AndroidPlayer androidPlayer = AndroidPlayer.get((EntityPlayer)event.target);
            if (androidPlayer != null && androidPlayer.isAndroid())
            {
                androidPlayer.sync(event.entityPlayer, PacketSyncAndroid.SYNC_ALL,false);
            }
        }
    }

    public void onServerTick(TickEvent.ServerTickEvent event)
    {
        if (MatterOverdrive.matterRegistry.hasComplitedRegistration)
        {
            for (int i = 0; i < MatterOverdrive.playerEventHandler.players.size();i++)
            {
                MatterOverdrive.packetPipeline.sendTo(new PacketUpdateMatterRegistry(MatterOverdrive.matterRegistry.getEntries()),MatterOverdrive.playerEventHandler.players.get(i));
            }

            MatterOverdrive.playerEventHandler.players.clear();
        }
    }
}
