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

package matteroverdrive.client.render;

import matteroverdrive.entity.EntityRougeAndroidMob;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.Star;
import matteroverdrive.tile.TileEntityMachineStarMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.EntityLivingBase;

/**
 * Created by Simeon on 6/15/2015.
 */
public class StarMapEntityRenderer extends EntityRenderer
{
    Minecraft mc;
    TileEntityMachineStarMap starMap;
    public static EntityLivingBase lastRenderEntity;
    public static EntityRougeAndroidMob renderEntity;

    public StarMapEntityRenderer(Minecraft mc)
    {
        super(mc,mc.getResourceManager());
        this.mc = mc;
    }

    public void setStarMap(TileEntityMachineStarMap starMap)
    {
        this.starMap = starMap;
    }

    @Override
    public void updateCameraAndRender(float partialTick)
    {
        if (starMap == null || mc.thePlayer == null || mc.thePlayer.isPlayerSleeping()){
            super.updateCameraAndRender(partialTick);
            return;
        }
        if (renderEntity == null)
        {
            renderEntity = new EntityRougeAndroidMob(Minecraft.getMinecraft().theWorld);
        }

        double x,y,z;
        int personMode;
        float fov;
        x = mc.thePlayer.posX;
        y = mc.thePlayer.posY;
        z = mc.thePlayer.posZ;
        personMode = this.mc.gameSettings.thirdPersonView;
        fov = mc.gameSettings.fovSetting;


        //mc.thePlayer.lastTickPosX = mc.thePlayer.posX;
        //mc.thePlayer.lastTickPosY = mc.thePlayer.posY;
        //mc.thePlayer.lastTickPosZ = mc.thePlayer.posZ;
        //mc.thePlayer.rotationYaw = -45;
        //mc.thePlayer.rotationPitch = 18;
        //mc.thePlayer.prevRotationYaw = mc.thePlayer.rotationYaw;
        //mc.thePlayer.prevRotationPitch = mc.thePlayer.rotationPitch;
        this.mc.renderViewEntity = renderEntity;
        renderEntity.rotationYaw = -45;
        renderEntity.rotationPitch = 18;
        renderEntity.prevRotationPitch = renderEntity.rotationPitch;
        renderEntity.prevRotationYaw = renderEntity.rotationYaw;
        if (starMap.getZoomLevel() == 0)
        {
            renderEntity.posX = starMap.xCoord - 2.5;
            renderEntity.posY = starMap.yCoord + 2;
            renderEntity.posZ = starMap.zCoord - 2.5;
        }
        else if (starMap.getZoomLevel() == 1)
        {
            renderEntity.posX = starMap.xCoord - 1.6;
            renderEntity.posY = starMap.yCoord + 1.8;
            renderEntity.posZ = starMap.zCoord - 1.6;
        }
        else if (starMap.getZoomLevel() == 2)
        {
            Star star = starMap.getStar();
            float maxDistance = 0;
            if (star != null) {
                for (Planet planet : star.getPlanets()) {
                    if (maxDistance < planet.getOrbit()) {
                        maxDistance = planet.getOrbit();
                    }
                }
            }
            renderEntity.posX = starMap.xCoord - (maxDistance * 3);
            renderEntity.posY = starMap.yCoord + 1.0;
            renderEntity.posZ = starMap.zCoord - (maxDistance * 3);
        }
        else
        {
            renderEntity.posX = starMap.xCoord - 1.4;
            renderEntity.posY = starMap.yCoord + 1.0;
            renderEntity.posZ = starMap.zCoord - 1.4;
        }
        renderEntity.lastTickPosX = renderEntity.posX;
        renderEntity.lastTickPosY = renderEntity.posY;
        renderEntity.lastTickPosZ = renderEntity.posZ;
        mc.thePlayer.posX = renderEntity.posX;
        mc.thePlayer.posY = renderEntity.posY;
        mc.thePlayer.posZ = renderEntity.posZ;
        mc.thePlayer.lastTickPosX = renderEntity.posX;
        mc.thePlayer.lastTickPosY = renderEntity.posY;
        mc.thePlayer.lastTickPosZ = renderEntity.posZ;
        this.mc.gameSettings.hideGUI = true;
        this.mc.gameSettings.thirdPersonView = 0;
        this.mc.gameSettings.fovSetting = 70;
        super.updateCameraAndRender(1);
        this.mc.gameSettings.fovSetting = fov;
        this.mc.gameSettings.hideGUI = false;
        this.mc.gameSettings.thirdPersonView = personMode;
        this.mc.renderViewEntity = Minecraft.getMinecraft().thePlayer;
        mc.thePlayer.posX = x;
        mc.thePlayer.posY = y;
        mc.thePlayer.posZ = z;
        //mc.thePlayer.lastTickPosX = mc.thePlayer.posX;
        //mc.thePlayer.lastTickPosY = mc.thePlayer.posY;
        //mc.thePlayer.lastTickPosZ = mc.thePlayer.posZ;
        //mc.thePlayer.rotationYaw = yaw;
        //mc.thePlayer.rotationPitch = pitch;
        this.mc.gameSettings.thirdPersonView = personMode;
    }
}
