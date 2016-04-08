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

import matteroverdrive.Reference;
import matteroverdrive.api.events.MOEventTransport;
import matteroverdrive.api.events.anomaly.MOEventGravitationalAnomalyConsume;
import matteroverdrive.data.quest.PlayerQuestData;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOExtendedProperties;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.util.MatterHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.EnumSet;

/**
 * Created by Simeon on 5/26/2015.
 */
public class EntityHandler
{
   /* @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event)
    {
        if (event.entity instanceof EntityPlayer)
        {
            if (AndroidPlayer.get((EntityPlayer) event.entity) == null)
            {
                AndroidPlayer.register((EntityPlayer) event.entity);
            }
            if (MOPlayerCapabilityProvider.GetExtendedCapability((EntityPlayer)event.entity) == null)
            {
                MOExtendedProperties.register((EntityPlayer)event.entity);
            }
        }
    }*/

    @SubscribeEvent
    public void onLivingFallEvent(LivingFallEvent event)
    {
        if (event.getEntityLiving() instanceof EntityPlayer)
        {
            AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(event.getEntityLiving());
            if (androidPlayer.isAndroid())
                androidPlayer.onEntityFall(event);
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (!event.getEntity().worldObj.isRemote && event.getEntity() instanceof EntityPlayer) {
            MOPlayerCapabilityProvider.GetAndroidCapability(event.getEntity()).sync(EnumSet.allOf(AndroidPlayer.DataType.class));
            MOPlayerCapabilityProvider.GetExtendedCapability(event.getEntity()).sync(EnumSet.allOf(PlayerQuestData.DataType.class));
        }
    }

    @SubscribeEvent
    public void onEntityJump(LivingEvent.LivingJumpEvent event)
    {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(event.getEntityLiving());
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
        AndroidPlayer newAndroidPlayer =MOPlayerCapabilityProvider.GetAndroidCapability(event.getEntityPlayer());
        AndroidPlayer oldAndroidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(event.getOriginal());
        if (newAndroidPlayer != null && oldAndroidPlayer != null) {
            newAndroidPlayer.copy(oldAndroidPlayer);
            if (event.isWasDeath()) {
                newAndroidPlayer.onPlayerRespawn();
            }
            newAndroidPlayer.sync(EnumSet.allOf(AndroidPlayer.DataType.class));
        }
        MOExtendedProperties newExtendedProperties = MOPlayerCapabilityProvider.GetExtendedCapability(event.getEntityPlayer());
        MOExtendedProperties oldExtenderDProperties = MOPlayerCapabilityProvider.GetExtendedCapability(event.getOriginal());
        if (newExtendedProperties != null && oldExtenderDProperties != null)
        {
            newExtendedProperties.copy(oldExtenderDProperties);
            newExtendedProperties.sync(EnumSet.allOf(PlayerQuestData.DataType.class));
        }
    }

    @SubscribeEvent
    public void onEntityAttack(LivingAttackEvent event)
    {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            MOPlayerCapabilityProvider.GetAndroidCapability(event.getEntityLiving()).triggerEventOnStats(event);
        }
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent deathEvent)
    {
        if (deathEvent.getSource() != null && deathEvent.getSource().getEntity() instanceof EntityPlayer)
        {
            MOExtendedProperties extendedProperties = MOPlayerCapabilityProvider.GetExtendedCapability(deathEvent.getSource().getEntity());
            extendedProperties.onEvent(deathEvent);
        }
    }

    @SubscribeEvent
    public void onEntityHurt(LivingHurtEvent event)
    {
        if (event.getEntityLiving() instanceof EntityPlayer)
        {
            AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(event.getEntityLiving());
            if (androidPlayer.isAndroid())
                androidPlayer.onEntityHurt(event);
        }
    }

    @SubscribeEvent
    public void onEntityItemPickup(EntityItemPickupEvent event)
    {
        if (event.getEntityLiving() != null)
        {
            if (event.getItem().getEntityItem() != null && MatterHelper.containsMatter(event.getItem().getEntityItem()))
            {
                for (int i = 0;i < 9;i++)
                {
                    if (event.getEntityPlayer().inventory.getStackInSlot(i) != null && event.getEntityPlayer().inventory.getStackInSlot(i).getItem() == MatterOverdriveItems.portableDecomposer)
                    {
                        MatterOverdriveItems.portableDecomposer.decomposeItem(event.getEntityPlayer().inventory.getStackInSlot(i),event.getItem().getEntityItem());
                    }
                }
            }
            MOExtendedProperties extendedProperties = MOPlayerCapabilityProvider.GetExtendedCapability(event.getEntityPlayer());
            if (extendedProperties != null)
            {
                extendedProperties.onEvent(event);
            }
        }
    }

    @SubscribeEvent
    public void onEntityTransport(MOEventTransport eventTransport)
    {
        if (eventTransport.getEntity() instanceof EntityPlayer)
        {
            MOExtendedProperties extendedProperties = MOPlayerCapabilityProvider.GetExtendedCapability(eventTransport.getEntity());
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
            MOExtendedProperties extendedProperties = MOPlayerCapabilityProvider.GetExtendedCapability(event.entity);
            if (extendedProperties != null)
            {
                extendedProperties.onEvent(event);
            }
        }
    }

    @SubscribeEvent
    public void OnAttachCapability(AttachCapabilitiesEvent.Entity event)
    {
        if (event.getEntity() instanceof EntityPlayer)
        {
            event.addCapability(new ResourceLocation(Reference.MOD_NAME,"MOPlayer"),new MOPlayerCapabilityProvider((EntityPlayer) event.getEntity()));
        }
    }
}
