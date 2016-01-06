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
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.weapon.IWeapon;
import matteroverdrive.network.packet.bi.PacketFirePlasmaShot;
import matteroverdrive.network.packet.bi.PacketWeaponTick;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simeon on 7/31/2015.
 */
@SideOnly(Side.CLIENT)
public class ClientWeaponHandler extends CommonWeaponHandler
{
    private static final float RECOIL_RESET_SPEED = 0.03f;
    public static float ZOOM_TIME;
    public static float RECOIL_TIME;
    public static float RECOIL_AMOUNT;
    private Map<IWeapon,Integer> shotTracker;
    private float lastMouseSensitivity;

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

    @SideOnly(Side.CLIENT)
    public void onTick(TickEvent.RenderTickEvent event)
    {
        if (Minecraft.getMinecraft().thePlayer != null)
        {
            EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;

            if (entityPlayer.getHeldItem() != null && entityPlayer.getHeldItem().getItem() instanceof IWeapon)
            {
                ZOOM_TIME = MOMathHelper.Lerp(ZOOM_TIME, ((IWeapon) entityPlayer.getHeldItem().getItem()).isWeaponZoomed(entityPlayer,entityPlayer.getHeldItem()) ? 1f : 0, event.renderTickTime*0.2f);

            }
            else
            {
                ZOOM_TIME = MOMathHelper.Lerp(ZOOM_TIME, 0, 0.2f);
            }

            if (ZOOM_TIME == 0)
            {
                lastMouseSensitivity = Minecraft.getMinecraft().gameSettings.mouseSensitivity;
            }else
            {
                if (entityPlayer.getHeldItem() != null && entityPlayer.getHeldItem().getItem() instanceof IWeapon)
                {
                    Minecraft.getMinecraft().gameSettings.mouseSensitivity = lastMouseSensitivity * (1 - (ZOOM_TIME * ((IWeapon) entityPlayer.getHeldItem().getItem()).getZoomMultiply(entityPlayer,entityPlayer.getHeldItem())));
                }else
                {
                    Minecraft.getMinecraft().gameSettings.mouseSensitivity = lastMouseSensitivity;
                }
            }

            if (RECOIL_TIME > 0)
            {
                RECOIL_TIME = Math.max(0,RECOIL_TIME - RECOIL_RESET_SPEED);
            }
        }
    }

    @SubscribeEvent
    public void onFovUpdate(FOVUpdateEvent event)
    {
        if (Minecraft.getMinecraft().thePlayer.getHeldItem() != null && Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() instanceof IWeapon) {
            event.newfov -= event.fov * ZOOM_TIME * ((IWeapon) Minecraft.getMinecraft().thePlayer.getHeldItem().getItem()).getZoomMultiply(Minecraft.getMinecraft().thePlayer,Minecraft.getMinecraft().thePlayer.getHeldItem());
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
                    player.setItemInUse(currentitem,player.getItemInUseCount());
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void sendWeaponTickToServer(World world, PacketFirePlasmaShot firePlasmaShot)
    {
        MatterOverdrive.packetPipeline.sendToServer(new PacketWeaponTick(world.getWorldTime(),firePlasmaShot));
    }

    public boolean shootDelayPassed(IWeapon item)
    {
        return shotTracker.get(item) <= 0;
    }
    public void addShootDelay(IWeapon item,ItemStack weaponStack)
    {
        if (shotTracker.containsKey(item))
            shotTracker.put(item,shotTracker.get(item) + item.getShootCooldown(weaponStack));
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
            return ((IWeapon) entityPlayer.getHeldItem().getItem()).getAccuracy(entityPlayer.getHeldItem(), entityPlayer, ((IWeapon) entityPlayer.getHeldItem().getItem()).isWeaponZoomed(entityPlayer,entityPlayer.getHeldItem())) / ((IWeapon) entityPlayer.getHeldItem().getItem()).getMaxHeat(entityPlayer.getHeldItem());
        }
        return 0;
    }
}
