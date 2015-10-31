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

package matteroverdrive.client.render.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.api.renderer.ISpaceBodyHoloRenderer;
import matteroverdrive.container.ContainerStarMap;
import matteroverdrive.gui.GuiStarMap;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.data.SpaceBody;
import matteroverdrive.tile.TileEntityMachineStarMap;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3;

import java.util.Collection;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 6/13/2015.
 */
@SideOnly(Side.CLIENT)
public class TileEntityRendererStarMap extends TileEntityRendererStation<TileEntityMachineStarMap>
{
    @Override
    protected void renderHologram(TileEntityMachineStarMap starMap, double x, double y, double z, float partialTicks, double noise)
    {
        if (isUsable(starMap))
        {
            if (Minecraft.getMinecraft().thePlayer.openContainer == null || !(Minecraft.getMinecraft().thePlayer.openContainer instanceof ContainerStarMap) || ((ContainerStarMap)Minecraft.getMinecraft().thePlayer.openContainer).getMachine() != starMap) {

            }
            else
            {
                glClearColor(0, 0, 0, 0);
                glClear(GL_COLOR_BUFFER_BIT);
            }

            renderHologramBase(starMap, x, y, z, partialTicks);
        }else
        {
            super.renderHologram(starMap, x, y, z, partialTicks, noise);
        }
    }

    protected void renderHologramBase(TileEntityMachineStarMap starMap, double x, double y, double z,float partialTicks) {
        glPushMatrix();
        glTranslated(x, y, z);
        glTranslated(0.5, 0.5, 0.5);
        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        RenderUtils.disableLightmap();
        glBlendFunc(GL_ONE, GL_ONE);
        float distance = (float) Vec3.createVectorHelper(x, y, z).lengthVector();

        Collection<ISpaceBodyHoloRenderer> renderers = ClientProxy.renderHandler.getStarmapRenderRegistry().getStarmapRendererCollection(starMap.getActiveSpaceBody().getClass());
        if (renderers != null)
        {
            for (ISpaceBodyHoloRenderer renderer : renderers)
            {
                if (renderer.displayOnZoom(starMap.getZoomLevel(),starMap.getActiveSpaceBody()))
                {
                    SpaceBody spaceBody = starMap.getActiveSpaceBody();
                    if (spaceBody != null)
					{
                        glTranslated(0, renderer.getHologramHeight(spaceBody), 0);
                        glPushMatrix();
                        renderer.renderBody(GalaxyClient.getInstance().getTheGalaxy(), spaceBody,starMap, partialTicks, distance);
                        glPopMatrix();

                        if (drawHoloLights())
						{
                            glPushMatrix();
                            Vec3 playerPosition = Minecraft.getMinecraft().renderViewEntity.getPosition(partialTicks);
                            playerPosition.yCoord = 0;
                            Vec3 mapPosition = Vec3.createVectorHelper(starMap.xCoord + 0.5, 0, starMap.zCoord + 0.5);
                            Vec3 dir = playerPosition.subtract(mapPosition).normalize();
                            double angle = Math.acos(dir.dotProduct(Vec3.createVectorHelper(1, 0, 0)));
                            if (Vec3.createVectorHelper(0, 1, 0).dotProduct(dir.crossProduct(Vec3.createVectorHelper(1, 0, 0))) < 0)
                            {
                                angle = Math.PI * 2 - angle;
                            }
                            drawHoloGuiInfo(renderer, spaceBody,starMap, (Math.PI / 2 - angle) * (180 / Math.PI), partialTicks);
                            glPopMatrix();
                        }
                    }
                }
            }
        }
        glPopMatrix();
    }

    @Override
    protected boolean drawHoloLights()
    {
        return !(Minecraft.getMinecraft().currentScreen instanceof GuiStarMap);
    }

    @Override
    protected double getLightHeight()
    {
        return 1;
    }

    @Override
    protected double getLightsSize()
    {
        return 2;
    }

    public void drawHoloGuiInfo(ISpaceBodyHoloRenderer renderer,SpaceBody spaceBody,TileEntityMachineStarMap starMap,double angle,float partialTicks)
    {
        angle = Math.round(angle / 90d) * 90;
        glPushMatrix();
        glTranslated(0, -renderer.getHologramHeight(spaceBody) + 0.3, 0);
        glRotated(angle, 0, 1, 0);
        glTranslated(1, 0, -0.8);
        glScaled(0.01, 0.01, 0.01);
        glRotated(180, 0, 0, 1);
        if (spaceBody != null)
            renderer.renderGUIInfo(GalaxyClient.getInstance().getTheGalaxy(), spaceBody,starMap, partialTicks, 0.5f);
        glPopMatrix();
    }
}
