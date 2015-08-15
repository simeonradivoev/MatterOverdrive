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

import matteroverdrive.client.render.entity.EntityFakePlayer;
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
    public static EntityFakePlayer renderEntity;

    public StarMapEntityRenderer(Minecraft mc)
    {
        super(mc,mc.getResourceManager());
        this.mc = mc;
    }

    public void setStarMap(TileEntityMachineStarMap starMap)
    {
        this.starMap = starMap;
    }

    public void renderWorld(float ticks, long time)
    {
        if (starMap == null || mc.thePlayer == null || mc.thePlayer.isPlayerSleeping()){
            super.updateCameraAndRender(ticks);
            return;
        }

        if (renderEntity == null)
        {
            renderEntity = new EntityFakePlayer(mc.theWorld,mc.thePlayer.getGameProfile());
        }

        float lastFov = mc.gameSettings.fovSetting;
        boolean lastHideGui = mc.gameSettings.hideGUI;
        EntityLivingBase lastRenderViewEntity = mc.renderViewEntity;

        if (starMap != null)
        {
            mc.renderViewEntity = renderEntity;
            mc.gameSettings.hideGUI = true;

            renderEntity.rotationYaw = -45;
            renderEntity.rotationPitch = 18;
            renderEntity.prevRotationPitch = renderEntity.rotationPitch;
            renderEntity.prevRotationYaw = renderEntity.rotationYaw;
            if (starMap.getZoomLevel() == 0)
            {
                renderEntity.posX = starMap.xCoord - 2.5;
                renderEntity.posY = starMap.yCoord + 4;
                renderEntity.posZ = starMap.zCoord - 2.5;
            }
            else if (starMap.getZoomLevel() == 1)
            {
                renderEntity.posX = starMap.xCoord - 1.6;
                renderEntity.posY = starMap.yCoord + 2.8;
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
                renderEntity.posY = starMap.yCoord + 3.0;
                renderEntity.posZ = starMap.zCoord - (maxDistance * 3);
            }
            else
            {
                renderEntity.posX = starMap.xCoord - 1.4;
                renderEntity.posY = starMap.yCoord + 2.5;
                renderEntity.posZ = starMap.zCoord - 1.4;
            }

            updateFakeViewEntity();
        }
        super.renderWorld(ticks,time);
        mc.renderViewEntity = lastRenderViewEntity;
        mc.gameSettings.hideGUI = lastHideGui;
        mc.gameSettings.fovSetting = lastFov;
    }

    private void updateFakeViewEntity()
    {
        renderEntity.prevPosX = renderEntity.lastTickPosX = renderEntity.posX;
        renderEntity.prevPosY = renderEntity.lastTickPosY = renderEntity.posY;
        renderEntity.prevPosZ = renderEntity.lastTickPosZ = renderEntity.posZ;

        renderEntity.prevRotationPitch = renderEntity.rotationPitch;
        renderEntity.prevRotationYaw = renderEntity.rotationYaw;
    }
}
