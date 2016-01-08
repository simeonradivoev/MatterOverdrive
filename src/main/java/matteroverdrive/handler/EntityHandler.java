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
import matteroverdrive.api.events.MOEventTransport;
import matteroverdrive.api.events.anomaly.MOEventGravitationalAnomalyConsume;
import matteroverdrive.data.quest.PlayerQuestData;
import matteroverdrive.entity.player.AndroidPlayer;
import matteroverdrive.entity.player.MOExtendedProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import java.util.EnumSet;

/**
 * Created by Simeon on 5/26/2015.
 */
public class EntityHandler
{
    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event)
    {
        if (event.entity instanceof EntityPlayer)
        {
            if (AndroidPlayer.get((EntityPlayer) event.entity) == null)
            {
                AndroidPlayer.register((EntityPlayer) event.entity);
            }
            if (MOExtendedProperties.get((EntityPlayer)event.entity) == null)
            {
                MOExtendedProperties.register((EntityPlayer)event.entity);
            }
        }
    }

    @SubscribeEvent
    public void onLivingFallEvent(LivingFallEvent event)
    {
        if (event.entityLiving instanceof EntityPlayer)
        {
            AndroidPlayer androidPlayer = AndroidPlayer.get((EntityPlayer)event.entityLiving);
            if (androidPlayer.isAndroid())
                androidPlayer.onEntityFall(event);
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer) {
            AndroidPlayer.get((EntityPlayer) event.entity).sync(EnumSet.allOf(AndroidPlayer.DataType.class));
            MOExtendedProperties.get((EntityPlayer) event.entity).sync(EnumSet.allOf(PlayerQuestData.DataType.class));
        }
    }

    @SubscribeEvent
    public void onEntityJump(LivingEvent.LivingJumpEvent event)
    {
        if (event.entityLiving instanceof EntityPlayer) {
            AndroidPlayer androidPlayer = AndroidPlayer.get((EntityPlayer)event.entityLiving);
            if (androidPlayer != null && androidPlayer.isAndroid())
            {
                androidPlayer.onEntityJump(event);
                androidPlayer.triggerEventOnStats(event);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event)
    {
        AndroidPlayer newAndroidPlayer = AndroidPlayer.get(event.entityPlayer);
        AndroidPlayer oldAndroidPlayer = AndroidPlayer.get(event.original);
        if (newAndroidPlayer != null && oldAndroidPlayer != null) {
            newAndroidPlayer.copy(oldAndroidPlayer);
            if (event.wasDeath) {
                newAndroidPlayer.onPlayerRespawn();
            }
            newAndroidPlayer.sync(EnumSet.allOf(AndroidPlayer.DataType.class));
        }
        MOExtendedProperties newExtendedProperties = MOExtendedProperties.get(event.entityPlayer);
        MOExtendedProperties oldExtenderDProperties = MOExtendedProperties.get(event.original);
        if (newExtendedProperties != null && oldExtenderDProperties != null)
        {
            newExtendedProperties.copy(oldExtenderDProperties);
            newExtendedProperties.sync(EnumSet.allOf(PlayerQuestData.DataType.class));
        }
    }

    @SubscribeEvent
    public void onEntityAttack(LivingAttackEvent event)
    {
        if (event.entityLiving instanceof EntityPlayer) {
            AndroidPlayer.get((EntityPlayer)event.entityLiving).triggerEventOnStats(event);
        }
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent deathEvent)
    {
        if (deathEvent.source != null && deathEvent.source.getEntity() instanceof EntityPlayer)
        {
            MOExtendedProperties extendedProperties = MOExtendedProperties.get((EntityPlayer) deathEvent.source.getEntity());
            extendedProperties.onEvent(deathEvent);
        }
    }

    @SubscribeEvent
    public void onEntityHurt(LivingHurtEvent event)
    {
        if (event.entityLiving instanceof EntityPlayer)
        {
            AndroidPlayer androidPlayer = AndroidPlayer.get((EntityPlayer)event.entityLiving);
            if (androidPlayer.isAndroid())
                androidPlayer.onEntityHurt(event);
        }
    }

    @SubscribeEvent
    public void onEntityItemPickup(EntityItemPickupEvent event)
    {
        if (event.entityPlayer != null)
        {
            MOExtendedProperties extendedProperties = MOExtendedProperties.get(event.entityPlayer);
            if (extendedProperties != null)
            {
                extendedProperties.onEvent(event);
            }
        }
    }

    @SubscribeEvent
    public void onEntityTransport(MOEventTransport eventTransport)
    {
        if (eventTransport.entity instanceof EntityPlayer)
        {
            MOExtendedProperties extendedProperties = MOExtendedProperties.get((EntityPlayer)eventTransport.entity);
            if (extendedProperties != null)
            {
                extendedProperties.onEvent(eventTransport);
            }
        }
    }

    @SubscribeEvent
    public void onEntityAnomalyConsume(MOEventGravitationalAnomalyConsume.Post event)
    {
        if (event.entity instanceof EntityPlayer)
        {
            MOExtendedProperties extendedProperties = MOExtendedProperties.get((EntityPlayer)event.entity);
            if (extendedProperties != null)
            {
                extendedProperties.onEvent(event);
            }
        }
    }
}
