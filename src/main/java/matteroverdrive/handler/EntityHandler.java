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
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.android.IBionicStat;
import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.network.packet.client.PacketSyncAndroid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

/**
 * Created by Simeon on 5/26/2015.
 */
public class EntityHandler
{
    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event)
    {
        if (event.entity instanceof EntityPlayer && AndroidPlayer.get((EntityPlayer) event.entity) == null)
        {
            AndroidPlayer.register((EntityPlayer) event.entity);
        }
    }

    @SubscribeEvent
    public void onLivingFallEvent(LivingFallEvent event)
    {
        if (event.entity instanceof EntityPlayer)
        {
            AndroidPlayer.onEntityFall(event);
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer)
            AndroidPlayer.get((EntityPlayer) event.entity).sync(PacketSyncAndroid.SYNC_ALL);
    }

    @SubscribeEvent
    public void onEntityJump(LivingEvent.LivingJumpEvent event)
    {
        if (event.entityLiving instanceof EntityPlayer) {
            AndroidPlayer androidPlayer = AndroidPlayer.get((EntityPlayer)event.entityLiving);
            if (androidPlayer != null)
            {
                androidPlayer.onEntityJump(event);

                if (androidPlayer.isAndroid()) {
                    for (IBionicStat stat : MatterOverdrive.statRegistry.getStats()) {
                        int unlockedLevel = androidPlayer.getUnlockedLevel(stat);
                        if (unlockedLevel > 0 && stat.isEnabled(androidPlayer,unlockedLevel)) {
                            stat.onLivingEvent(androidPlayer, unlockedLevel, event);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event)
    {
        AndroidPlayer.get(event.entityPlayer).copy(AndroidPlayer.get(event.original));
        if (event.wasDeath) {
            AndroidPlayer.get(event.entityPlayer).onPlayerRespawn();
        }
        AndroidPlayer.get(event.entityPlayer).sync(PacketSyncAndroid.SYNC_ALL);
    }

    @SubscribeEvent
    public void onEntityAttack(LivingAttackEvent event)
    {
        if (event.entityLiving instanceof EntityPlayer) {
            AndroidPlayer androidPlayer = AndroidPlayer.get((EntityPlayer)event.entityLiving);
            if (androidPlayer != null && androidPlayer.isAndroid()) {
                for (IBionicStat stat : MatterOverdrive.statRegistry.getStats()) {
                    int unlockedLevel = androidPlayer.getUnlockedLevel(stat);
                    if (unlockedLevel > 0 && stat.isEnabled(androidPlayer,unlockedLevel))
                    {
                        stat.onLivingEvent(androidPlayer,unlockedLevel , event);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityHurt(LivingHurtEvent event)
    {
        if (event.entityLiving instanceof EntityPlayer)
        {
            AndroidPlayer androidPlayer = AndroidPlayer.get((EntityPlayer)event.entityLiving);

            if (androidPlayer.isAndroid()) {
                for (IBionicStat stat : MatterOverdrive.statRegistry.getStats()) {
                    int unlockedLevel = androidPlayer.getUnlockedLevel(stat);
                    if (unlockedLevel > 0 && stat.isEnabled(androidPlayer,unlockedLevel)) {
                        stat.onLivingEvent(androidPlayer,unlockedLevel , event);
                    }
                }
            }

            androidPlayer.onEntityHurt(event);
        }
    }
}
