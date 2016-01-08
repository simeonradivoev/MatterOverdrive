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
import matteroverdrive.api.events.MOEventScan;
import matteroverdrive.api.events.bionicStats.MOEventBionicStat;
import matteroverdrive.api.weapon.IWeapon;
import matteroverdrive.data.quest.PlayerQuestData;
import matteroverdrive.entity.player.AndroidPlayer;
import matteroverdrive.entity.player.MOExtendedProperties;
import matteroverdrive.items.includes.MOBaseItem;
import matteroverdrive.network.packet.client.PacketUpdateMatterRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;

import java.util.ArrayList;
import java.util.EnumSet;
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
        if (player != null && event.phase.equals(TickEvent.Phase.START)) {
            if (event.side == Side.CLIENT) {
                onAndroidTickClient(player, event);

            } else {
                onAndroidServerTick(player, event);
            }
        }

        MOExtendedProperties extendedProperties = MOExtendedProperties.get(event.player);
        if (extendedProperties != null && event.phase.equals(TickEvent.Phase.START))
        {
            if (event.side == Side.CLIENT)
            {
                extendedProperties.update(Side.CLIENT);
            }else
            {
                extendedProperties.update(Side.SERVER);
            }
        }

        //used to stop the item refreshing when using weapons and changing their data
        //this also happens on SMP and stops the beam weapons from working properly
        //Minecraft stops he using of items each time they change their NBT. This makes is so the weapons refresh and jitter.
        if (event.player.isUsingItem() && event.side == Side.CLIENT) {
            ItemStack itemstack = event.player.inventory.getCurrentItem();
            int itemUseCount = event.player.getItemInUseCount();
            if (itemstack != null && itemstack.getItem() instanceof IWeapon && itemUseCount > 0)
            {
                event.player.setItemInUse(itemstack,itemUseCount);

                if (Minecraft.getMinecraft().currentScreen != null && event.player.equals(Minecraft.getMinecraft().thePlayer))
                    event.player.clearItemInUse();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void onAndroidTickClient(AndroidPlayer androidPlayer,TickEvent.PlayerTickEvent event)
    {
        if (event.player == Minecraft.getMinecraft().thePlayer)
        {
            androidPlayer.onAndroidTick(event.side);
        }
    }

    private void onAndroidServerTick(AndroidPlayer androidPlayer,TickEvent.PlayerTickEvent event)
    {
        androidPlayer.onAndroidTick(event.side);
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
                androidPlayer.sync(event.entityPlayer, EnumSet.allOf(AndroidPlayer.DataType.class),false);
            }
            MOExtendedProperties extendedProperties = MOExtendedProperties.get((EntityPlayer)event.target);
            if (extendedProperties != null)
            {
                extendedProperties.sync(EnumSet.allOf(PlayerQuestData.DataType.class));
            }
        }
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent deathEvent)
    {
        if (deathEvent.entityLiving instanceof EntityPlayer)
        {
            if (!((EntityPlayer) deathEvent.entityLiving).worldObj.isRemote)
            {
                MatterOverdrive.proxy.getGoogleAnalytics().sendEventHit(GoogleAnalyticsCommon.EVENT_CATEGORY_ENTITIES,GoogleAnalyticsCommon.EVENT_ACTION_PLAYER_DEATH,deathEvent.source.getDamageType(),((EntityPlayer) deathEvent.entityLiving).ticksExisted / 20,(EntityPlayer)deathEvent.entityLiving);
                AndroidPlayer androidPlayer = AndroidPlayer.get((EntityPlayer)deathEvent.entityLiving);
                if (androidPlayer != null && androidPlayer.isAndroid())
                {
                    androidPlayer.onPlayerDeath(deathEvent);
                }
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

    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event)
    {
        if (event.player != null && !event.player.worldObj.isRemote)
        {
            if (event.crafting != null && event.crafting.getItem() instanceof MOBaseItem)
            {
                MatterOverdrive.proxy.getGoogleAnalytics().sendEventHit(GoogleAnalyticsCommon.EVENT_CATEGORY_ITEMS, GoogleAnalyticsCommon.EVENT_ACTION_CRAFT_ITEMS, event.crafting.getDisplayName(), event.crafting.stackSize, event.player);
            }
            MOExtendedProperties extendedProperties = MOExtendedProperties.get(event.player);
            if (extendedProperties != null)
            {
                extendedProperties.onEvent(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerFlyableFallEvent(PlayerFlyableFallEvent event)
    {
        if (event.entityPlayer != null)
        {
            AndroidPlayer androidPlayer = AndroidPlayer.get(event.entityPlayer);
            if (androidPlayer != null && androidPlayer.isAndroid())
            {
                androidPlayer.triggerEventOnStats(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerScanEvent(MOEventScan event)
    {
        if (event.getSide() == Side.SERVER && event.entityPlayer != null)
        {
            MOExtendedProperties extendedProperties = MOExtendedProperties.get(event.entityPlayer);
            if (extendedProperties != null)
            {
                extendedProperties.onEvent(event);
            }
        }
    }

    /*@SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event)
    {
        MatterOverdrive.proxy.getGoogleAnalytics().sendScreenHit(event.gui != null ? event.gui.getClass().getSimpleName() : "Ingame",null);
    }*/

    @SubscribeEvent
    public void onBioticStatUse(MOEventBionicStat event)
    {
        MatterOverdrive.proxy.getGoogleAnalytics().sendEventHit(GoogleAnalyticsCommon.EVENT_CATEGORY_BIOTIC_STATS,GoogleAnalyticsCommon.EVENT_ACTION_BIOTIC_STAT_USE,event.stat.getDisplayName(event.android,event.level),event.android.getPlayer());
    }
}
