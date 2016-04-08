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

package matteroverdrive.client.render.tileentity.starmap;

import matteroverdrive.Reference;
import matteroverdrive.client.data.Color;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.data.*;
import matteroverdrive.tile.TileEntityMachineStarMap;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 6/17/2015.
 */
@SideOnly(Side.CLIENT)
public class StarMapRenderGalaxy extends StarMapRendererStars
{
    @Override
    public void renderBody(Galaxy galaxy, SpaceBody spaceBody, TileEntityMachineStarMap starMap, float partialTicks,float viewerDistance)
    {
        double distanceMultiply = 2;
        GlStateManager.depthMask(false);

        glLineWidth(1);

        Tessellator.getInstance().getBuffer().begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        for (Quadrant quadrant : galaxy.getQuadrants())
        {
            renderStars(quadrant, starMap,distanceMultiply,2);
        }
        Tessellator.getInstance().draw();
        GlStateManager.disableTexture2D();


        for (int i = 0;i < galaxy.getTravelEvents().size();i++)
        {
            TravelEvent travelEvent = galaxy.getTravelEvents().get(i);
            if (travelEvent.isValid(GalaxyClient.getInstance().getTheGalaxy()))
            {

                Vec3d from = GalaxyClient.getInstance().getTheGalaxy().getPlanet(travelEvent.getFrom()).getStar().getPosition(2);
                Vec3d to = GalaxyClient.getInstance().getTheGalaxy().getPlanet(travelEvent.getTo()).getStar().getPosition(2);
                Vec3d dir = from.subtract(to);
                double percent = travelEvent.getPercent(starMap.getWorld());

                RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, 0.5f);
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
                GlStateManager.pushMatrix();
                GlStateManager.translate(from.xCoord + dir.xCoord * percent, from.yCoord + dir.yCoord * percent, from.zCoord + dir.zCoord * percent);
                RenderUtils.rotateTowards(new Vec3d(-1, 0, 0.0), dir.normalize(), new Vec3d(0, 1, 0));
                RenderUtils.drawShip(0, 0, 0, 0.02);
                GlStateManager.popMatrix();

                glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);
                RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO_PURPLE, 0.5f);
                glBegin(GL_LINE_STRIP);
                glVertex3d(from.xCoord, from.yCoord, from.zCoord);
                glVertex3d(to.xCoord, to.yCoord, to.zCoord);
                glEnd();
            }
        }
        GlStateManager.enableTexture2D();
    }

    @Override
    public void renderGUIInfo(Galaxy galaxy, SpaceBody spaceBody,TileEntityMachineStarMap starMap, float partialTicks, float opacity)
    {
        GlStateManager.enableAlpha();
        int ownedSystemCount = galaxy.getOwnedSystemCount(Minecraft.getMinecraft().thePlayer);
        int enemySystemCount = galaxy.getEnemySystemCount(Minecraft.getMinecraft().thePlayer);
        int freeSystemCount = galaxy.getStarCount() - ownedSystemCount - enemySystemCount;
        Color color = Reference.COLOR_HOLO_GREEN;
        RenderUtils.applyColorWithMultipy(color, opacity);
        ClientProxy.holoIcons.renderIcon("page_icon_star", 0, -30);
        RenderUtils.drawString(String.format("x%s", ownedSystemCount), 24, -23, color, opacity);

        color = Reference.COLOR_HOLO_RED;
        RenderUtils.applyColorWithMultipy(color, opacity);
        ClientProxy.holoIcons.renderIcon("page_icon_star", 64, -30);
        RenderUtils.drawString(String.format("x%s", enemySystemCount), 88, -23, color, opacity);

        color = Reference.COLOR_HOLO;
        RenderUtils.applyColorWithMultipy(color, opacity);
        ClientProxy.holoIcons.renderIcon("page_icon_star", 128, -30);
        RenderUtils.drawString(String.format("x%s", freeSystemCount), 152, -23, color, opacity);

        for (int i = 0; i < galaxy.getTravelEvents().size(); i++)
        {
            TravelEvent travelEvent = galaxy.getTravelEvents().get(i);
            if (travelEvent.isValid(GalaxyClient.getInstance().getTheGalaxy())) {
                Planet from = GalaxyClient.getInstance().getTheGalaxy().getPlanet(travelEvent.getFrom());
                Planet to = GalaxyClient.getInstance().getTheGalaxy().getPlanet(travelEvent.getTo());

                RenderUtils.drawString(String.format("%s -> %s : %s", from.getName(), to.getName(), MOStringHelper.formatRemainingTime(galaxy.getTravelEvents().get(i).getTimeRemainning(starMap.getWorld()) / 20)), 0, -48 - i * 10, Reference.COLOR_HOLO, opacity);
            }
        }
        GlStateManager.disableAlpha();
    }

    @Override
    public boolean displayOnZoom(int zoom, SpaceBody spaceBody) {
        return true;
    }

    @Override
    public double getHologramHeight(SpaceBody spaceBody) {
        return 2.5;
    }

}
