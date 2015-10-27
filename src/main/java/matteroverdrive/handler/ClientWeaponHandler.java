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

import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.api.weapon.IWeapon;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simeon on 7/31/2015.
 */
@SideOnly(Side.CLIENT)
public class ClientWeaponHandler
{
    Map<IWeapon,Integer> shotTracker;

    public ClientWeaponHandler()
    {
        shotTracker = new HashMap<>();
    }

    public void registerWeapon(IWeapon weapon)
    {
        shotTracker.put(weapon,0);
    }

    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (!Minecraft.getMinecraft().isGamePaused() && Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null)
        {
            for (IWeapon item : shotTracker.keySet())
            {
                int oldTime = shotTracker.get(item);
                if (oldTime > 0) {
                    shotTracker.put(item, oldTime-1);
                }
            }

            manageWeaponView();
        }
    }

    private void manageWeaponView()
    {
        for (Object playerObj : Minecraft.getMinecraft().theWorld.playerEntities)
        {
            EntityPlayer player = (EntityPlayer)playerObj;
            ItemStack currentitem = player.getCurrentEquippedItem();
            if (currentitem != null && currentitem.getItem() instanceof IWeapon && ((IWeapon) currentitem.getItem()).isAlwaysEquipped(currentitem))
            {
                if (player == Minecraft.getMinecraft().thePlayer && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0)
                {
                    //this disables the use animation of the weapon in first person
                    //to enable custom animations
                    currentitem.setItemDamage(0);
                }else
                {
                    //this allows the item to play the bow use animation when in 3rd person mode
                    currentitem.setItemDamage(1);
                    player.setItemInUse(currentitem,100);
                }
            }
        }
    }

    public boolean shootDelayPassed(IWeapon item)
    {
        return shotTracker.get(item) <= 0;
    }
    public void addShootDelay(IWeapon item)
    {
        if (shotTracker.containsKey(item))
            shotTracker.put(item,shotTracker.get(item) + item.getShootCooldown());
    }
    public void addReloadDelay(IWeapon weapon,int delay)
    {
        if (shotTracker.containsKey(weapon))
            shotTracker.put(weapon,shotTracker.get(weapon) + delay);
    }
    public float getEquippedWeaponAccuracyPercent(EntityPlayer entityPlayer)
    {
        if (entityPlayer.getHeldItem() != null && entityPlayer.getHeldItem().getItem() instanceof IWeapon)
        {
            return ((IWeapon) entityPlayer.getHeldItem().getItem()).getAccuracy(entityPlayer.getHeldItem(), entityPlayer, ((IWeapon) entityPlayer.getHeldItem().getItem()).isWeaponZoomed(entityPlayer.getHeldItem())) / ((IWeapon) entityPlayer.getHeldItem().getItem()).getMaxHeat(entityPlayer.getHeldItem());
        }
        return 0;
    }
}