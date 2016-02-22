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

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.weapon.IWeapon;
import matteroverdrive.api.weapon.WeaponShot;
import matteroverdrive.entity.weapon.PlasmaBolt;
import matteroverdrive.items.weapon.EnergyWeapon;
import matteroverdrive.network.packet.bi.PacketFirePlasmaShot;
import matteroverdrive.network.packet.bi.PacketWeaponTick;
import matteroverdrive.util.WeaponHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IntHashMap;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Simeon on 7/31/2015.
 */
@SideOnly(Side.CLIENT)
public class ClientWeaponHandler extends CommonWeaponHandler
{
    private static final float RECOIL_RESET_SPEED = 0.03f;
    private static final float CAMERA_RECOIL_RESET_SPEED = 0.03f;
    public static float ZOOM_TIME;
    public static float RECOIL_TIME;
    public static float RECOIL_AMOUNT;
    public static float CAMERA_RECOIL_TIME;
    public static float CAMERA_RECOIL_AMOUNT;
    private final Map<IWeapon,Integer> shotTracker;
    private float lastMouseSensitivity;
    private final IntHashMap<PlasmaBolt> plasmaBolts;
    private int nextShotID;
    private boolean hasChangedSensitivity = false;
    private final Random cameraRecoilRandom = new Random();

    public ClientWeaponHandler()
    {
        shotTracker = new HashMap<>();
        plasmaBolts = new IntHashMap<>();
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
        if (Minecraft.getMinecraft().thePlayer != null && event.phase.equals(TickEvent.Phase.END))
        {
            EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;

            if (entityPlayer.getHeldItem() != null && entityPlayer.getHeldItem().getItem() instanceof IWeapon)
            {
                if (((IWeapon) entityPlayer.getHeldItem().getItem()).isWeaponZoomed(entityPlayer,entityPlayer.getHeldItem()))
                {
                    ZOOM_TIME = Math.min(ZOOM_TIME+(event.renderTickTime*0.1f),1);
                }else
                {
                    ZOOM_TIME = Math.max(ZOOM_TIME-(event.renderTickTime*0.1f),0);
                }
            }
            else
            {
                ZOOM_TIME = Math.max(ZOOM_TIME-(event.renderTickTime*0.2f),0);
            }

            if (ZOOM_TIME == 0)
            {
                if (hasChangedSensitivity)
                {
                    hasChangedSensitivity = false;
                    Minecraft.getMinecraft().gameSettings.mouseSensitivity = lastMouseSensitivity;
                }else
                {
                    lastMouseSensitivity = Minecraft.getMinecraft().gameSettings.mouseSensitivity;
                }
            }else if (ZOOM_TIME != 0)
            {
                if (entityPlayer.getHeldItem() != null && entityPlayer.getHeldItem().getItem() instanceof IWeapon)
                {
                    hasChangedSensitivity = true;
                    Minecraft.getMinecraft().gameSettings.mouseSensitivity = lastMouseSensitivity * (1f - (ZOOM_TIME * ((IWeapon) entityPlayer.getHeldItem().getItem()).getZoomMultiply(entityPlayer,entityPlayer.getHeldItem())));
                }else
                {
                    hasChangedSensitivity = true;
                    Minecraft.getMinecraft().gameSettings.mouseSensitivity = lastMouseSensitivity;
                }
            }


            if (RECOIL_TIME > 0)
            {
                RECOIL_TIME = Math.max(0,RECOIL_TIME - RECOIL_RESET_SPEED);
            }

            if (CAMERA_RECOIL_TIME > 0)
            {
                CAMERA_RECOIL_TIME = Math.max(0,CAMERA_RECOIL_TIME - CAMERA_RECOIL_RESET_SPEED);
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
    public void setRecoil(float amount, float time,float viewRecoilMultiply)
    {
        RECOIL_AMOUNT = amount;
        RECOIL_TIME = time;
        Minecraft.getMinecraft().thePlayer.rotationPitch -= amount * viewRecoilMultiply;
    }

    public void setCameraRecoil(float amount,float time)
    {
        CAMERA_RECOIL_AMOUNT = amount * (cameraRecoilRandom.nextBoolean() ? -1 : 1);
        CAMERA_RECOIL_TIME = time;
    }
    public float getEquippedWeaponAccuracyPercent(EntityPlayer entityPlayer)
    {
        if (entityPlayer.getHeldItem() != null && entityPlayer.getHeldItem().getItem() instanceof IWeapon)
        {
            return ((IWeapon) entityPlayer.getHeldItem().getItem()).getAccuracy(entityPlayer.getHeldItem(), entityPlayer, ((IWeapon) entityPlayer.getHeldItem().getItem()).isWeaponZoomed(entityPlayer,entityPlayer.getHeldItem())) / ((IWeapon) entityPlayer.getHeldItem().getItem()).getMaxHeat(entityPlayer.getHeldItem());
        }
        return 0;
    }

    public void addPlasmaBolt(PlasmaBolt plasmaBolt)
    {
        plasmaBolts.addKey(plasmaBolt.getEntityId(),plasmaBolt);
    }

    public void removePlasmaBolt(PlasmaBolt plasmaBolt)
    {
        plasmaBolts.removeObject(plasmaBolt.getEntityId());
    }

    public PlasmaBolt getPlasmaBolt(int id)
    {
        return plasmaBolts.lookup(id);
    }

    public int getNextShotID()
    {
        return nextShotID++;
    }

    public WeaponShot getNextShot(ItemStack weaponStack, EnergyWeapon energyWeapon, EntityLivingBase shooter, boolean zoomed)
    {
        return new WeaponShot(getNextShotID(),energyWeapon.getWeaponScaledDamage(weaponStack,shooter),energyWeapon.getAccuracy(weaponStack,shooter,zoomed), WeaponHelper.getColor(weaponStack),energyWeapon.getRange(weaponStack));
    }
}
