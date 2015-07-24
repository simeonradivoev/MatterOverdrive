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

import cofh.lib.gui.GuiColor;
import cofh.lib.render.RenderHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.Reference;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.data.Galaxy;
import matteroverdrive.starmap.data.Quadrant;
import matteroverdrive.starmap.data.SpaceBody;
import matteroverdrive.starmap.data.Star;
import matteroverdrive.tile.TileEntityMachineStarMap;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;

import java.text.DecimalFormat;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 6/17/2015.
 */
@SideOnly(Side.CLIENT)
public class StarMapRendererQuadrant extends StarMapRendererStars {

    @Override
    public void renderBody(Galaxy galaxy, SpaceBody spaceBody, TileEntityMachineStarMap starMap, float partialTicks,float viewerDistance)
    {
        if (spaceBody instanceof Quadrant)
        {
            glLineWidth(1);

            Quadrant quadrant = (Quadrant)spaceBody;
            double distanceMultiply = 5;
            double halfQuadrantSize = quadrant.getSize() * 1.5;
            double x = ((-quadrant.getX()) - quadrant.getSize() / 2) * distanceMultiply;
            double y = (-quadrant.getY()) * distanceMultiply;
            double z = ((-quadrant.getZ()) - quadrant.getSize() / 2) * distanceMultiply;
            glTranslated(x, y, z);
            renderStars(quadrant, starMap, distanceMultiply, distanceMultiply);
        }
    }

    @Override
    public void renderGUIInfo(Galaxy galaxy, SpaceBody spaceBody,TileEntityMachineStarMap starMap, float partialTicks, float opacity)
    {
        glEnable(GL_ALPHA_TEST);
        Star star = galaxy.getStar(starMap.getDestination());
        Star origin = galaxy.getStar(starMap.getGalaxyPosition());
        if (star != null) {
            int planetCount = star.getPlanets().size();
            GuiColor color = Reference.COLOR_HOLO;
            if (planetCount <= 0) {
                color = Reference.COLOR_HOLO_RED;
            }
            RenderUtils.applyColorWithMultipy(color, opacity);
            ClientProxy.holoIcons.bindSheet();
            RenderHelper.renderIcon(0, -32, 0, ClientProxy.holoIcons.getIcon("page_icon_planet"), 24, 24);
            RenderUtils.drawString(String.format("x%s", planetCount), 28, -21, color, opacity);


            DecimalFormat format = new DecimalFormat("#");
            if (GalaxyClient.getInstance().canSeeStarInfo(star,Minecraft.getMinecraft().thePlayer)) {
                RenderUtils.drawString(star.getName(), 0, -52, Reference.COLOR_HOLO, opacity);
            } else {
                RenderUtils.drawString(Minecraft.getMinecraft().standardGalacticFontRenderer, star.getName(), 0, -52, Reference.COLOR_HOLO, opacity);
            }

            ClientProxy.holoIcons.renderIcon("icon_size",48,-28);
            //RenderUtils.drawString(star.getName(), 82, -30, Reference.COLOR_HOLO,opacity);
            RenderUtils.drawString(DecimalFormat.getPercentInstance().format(star.getSize()), 68, -23, Reference.COLOR_HOLO,opacity);

            if (origin != null)
                RenderUtils.drawString(String.format("Distance: %s LY", format.format(origin.getPosition().distanceTo(star.getPosition()) * Galaxy.GALAXY_SIZE_TO_LY)), 0, -42, Reference.COLOR_HOLO, opacity);
        }
        glDisable(GL_ALPHA_TEST);
    }

    @Override
    public boolean displayOnZoom(int zoom, SpaceBody spaceBody) {
        return true;
    }

    @Override
    public double getHologramHeight(SpaceBody spaceBody) {
        return 0.3;
    }
}
