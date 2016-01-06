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

package matteroverdrive.handler.weapon;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.events.weapon.MOEventEnergyWeapon;
import matteroverdrive.entity.player.AndroidPlayer;
import matteroverdrive.network.packet.bi.PacketFirePlasmaShot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simeon on 12/7/2015.
 */
public class CommonWeaponHandler
{
    private static final PacketFirePlasmaShot.BiHandler firePlasmaShotHandler = new PacketFirePlasmaShot.BiHandler();
    Map<EntityPlayer, Long> weaponTimestamps;

    public CommonWeaponHandler()
    {
        weaponTimestamps = new HashMap<>();
    }

    public void addTimestamp(EntityPlayer player, long timestamp)
    {
        weaponTimestamps.put(player, timestamp);
    }

    public boolean hasTimestamp(EntityPlayer player)
    {
        return weaponTimestamps.containsKey(player);
    }

    public long getTimestamp(EntityPlayer entityPlayer)
    {
        return weaponTimestamps.get(entityPlayer);
    }

    public void handlePlasmaShotFire(EntityPlayer entityPlayer, PacketFirePlasmaShot plasmaShot, long timeStamp)
    {
        int delay = (int) (timeStamp - getTimestamp(entityPlayer));
        firePlasmaShotHandler.handleServerShot(entityPlayer, plasmaShot, delay);
        MatterOverdrive.packetPipeline.sendToAllAround(plasmaShot, entityPlayer, plasmaShot.getShot().getRange() + 64);
    }

    @SubscribeEvent
    public void onEnergyWeaponEvent(MOEventEnergyWeapon eventEnergyWeapon)
    {
        if (eventEnergyWeapon.entityLiving != null && eventEnergyWeapon.entityLiving instanceof EntityPlayer)
        {
            AndroidPlayer androidPlayer = AndroidPlayer.get((EntityPlayer)eventEnergyWeapon.entityLiving);
            if (androidPlayer != null)
            {
                androidPlayer.onWeaponEvent(eventEnergyWeapon);
            }
        }
    }
}
