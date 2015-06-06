package com.MO.MatterOverdrive.handler;

import com.MO.MatterOverdrive.api.inventory.IBionicStat;
import com.MO.MatterOverdrive.entity.AndroidPlayer;
import com.MO.MatterOverdrive.network.packet.client.PacketSyncAndroid;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
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
            AndroidPlayer.get((EntityPlayer) event.entity).sync((EntityPlayer)event.entity, PacketSyncAndroid.SYNC_ALL);
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
                    for (IBionicStat stat : AndroidStatRegistry.stats.values()) {
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
    }

    @SubscribeEvent
    public void onEntityAttack(LivingAttackEvent event)
    {
        if (event.entityLiving instanceof EntityPlayer) {
            AndroidPlayer androidPlayer = AndroidPlayer.get((EntityPlayer)event.entityLiving);
            if (androidPlayer != null && androidPlayer.isAndroid()) {
                for (IBionicStat stat : AndroidStatRegistry.stats.values()) {
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
            androidPlayer.onEntityHurt(event);

            if (androidPlayer != null && androidPlayer.isAndroid()) {
                for (IBionicStat stat : AndroidStatRegistry.stats.values()) {
                    int unlockedLevel = androidPlayer.getUnlockedLevel(stat);
                    if (unlockedLevel > 0 && stat.isEnabled(androidPlayer,unlockedLevel)) {
                        stat.onLivingEvent(androidPlayer,unlockedLevel , event);
                    }
                }
            }
        }
    }
}
