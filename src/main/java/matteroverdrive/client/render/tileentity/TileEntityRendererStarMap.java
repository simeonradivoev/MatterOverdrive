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

import matteroverdrive.api.renderer.ISpaceBodyHoloRenderer;
import matteroverdrive.gui.GuiStarMap;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.data.SpaceBody;
import matteroverdrive.tile.TileEntityMachineStarMap;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;

import static org.lwjgl.opengl.GL11.GL_ONE;

/**
 * Created by Simeon on 6/13/2015.
 */
@SideOnly(Side.CLIENT)
public class TileEntityRendererStarMap extends TileEntityRendererStation<TileEntityMachineStarMap>
{
    @Override
    protected void renderHologram(TileEntityMachineStarMap starMap, double x, double y, double z, float partialTicks, double noise)
    {
        if (!(Minecraft.getMinecraft().currentScreen instanceof GuiStarMap))
        {
            if (isUsable(starMap))
            {
                render(starMap, x, y, z, partialTicks);
            } else
            {
                super.renderHologram(starMap, x, y, z, partialTicks, noise);
            }
        }
    }

    public void render(TileEntityMachineStarMap starMap, double x, double y, double z, float partialTicks)
    {
        renderHologramBase(starMap, x, y, z, partialTicks);
    }

    protected void renderHologramBase(TileEntityMachineStarMap starMap, double x, double y, double z,float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.translate(0.5, 0.5, 0.5);
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        RenderUtils.disableLightmap();
        GlStateManager.blendFunc(GL_ONE, GL_ONE);
        float distance = (float) new Vec3d(x, y, z).lengthVector();

        if (starMap.getActiveSpaceBody() != null)
        {
            Collection<ISpaceBodyHoloRenderer> renderers = ClientProxy.renderHandler.getStarmapRenderRegistry().getStarmapRendererCollection(starMap.getActiveSpaceBody().getClass());
            if (renderers != null)
            {
                for (ISpaceBodyHoloRenderer renderer : renderers)
                {
                    if (renderer.displayOnZoom(starMap.getZoomLevel(), starMap.getActiveSpaceBody()))
                    {
                        SpaceBody spaceBody = starMap.getActiveSpaceBody();
                        if (spaceBody != null)
                        {
                            GlStateManager.translate(0, renderer.getHologramHeight(spaceBody), 0);
                            GlStateManager.pushMatrix();
                            renderer.renderBody(GalaxyClient.getInstance().getTheGalaxy(), spaceBody, starMap, partialTicks, distance);
                            GlStateManager.popMatrix();

                            if (drawHoloLights())
                            {
                                GlStateManager.pushMatrix();
                                Vec3d playerPosition = Minecraft.getMinecraft().getRenderViewEntity().getPositionEyes(partialTicks);
                                playerPosition = new Vec3d(playerPosition.xCoord,0,playerPosition.zCoord);
                                Vec3d mapPosition = new Vec3d(starMap.getPos().getX() + 0.5, 0, starMap.getPos().getZ() + 0.5);
                                Vec3d dir = mapPosition.subtract(playerPosition).normalize();
                                double angle = Math.acos(dir.dotProduct(new Vec3d(1, 0, 0)));
                                if (new Vec3d(0, 1, 0).dotProduct(dir.crossProduct(new Vec3d(1, 0, 0))) < 0)
                                {
                                    angle = Math.PI * 2 - angle;
                                }
                                drawHoloGuiInfo(renderer, spaceBody, starMap, (Math.PI / 2 - angle) * (180 / Math.PI), partialTicks);
                                GlStateManager.popMatrix();
                            }
                        }
                    }
                }
            }
        }
        GlStateManager.popMatrix();
        RenderUtils.enableLightmap();
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
        GlStateManager.disableLighting();
        angle = Math.round(angle / 90d) * 90;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, -renderer.getHologramHeight(spaceBody) + 0.3, 0);
        GlStateManager.rotate((float)angle, 0, 1, 0);
        GlStateManager.translate(1, 0, -0.8);
        GlStateManager.scale(0.01, 0.01, 0.01);
        GlStateManager.rotate(180, 0, 0, 1);
        if (spaceBody != null)
            renderer.renderGUIInfo(GalaxyClient.getInstance().getTheGalaxy(), spaceBody,starMap, partialTicks, 0.5f);
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
    }
}
