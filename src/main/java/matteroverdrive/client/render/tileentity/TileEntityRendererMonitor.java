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

import matteroverdrive.Reference;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 11/22/2015.
 */
public abstract class TileEntityRendererMonitor extends TileEntitySpecialRenderer
{
    public static ResourceLocation screenTextureBack = new ResourceLocation(Reference.PATH_BLOCKS + "pattern_monitor_holo_back.png");
    public static ResourceLocation screenTextureGlow = new ResourceLocation(Reference.PATH_FX + "holo_monitor_glow.png");

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float ticks)
    {
        glPushMatrix();

        int meta = tileEntity.getWorldObj().getBlockMetadata(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
        ForgeDirection direction = ForgeDirection.getOrientation(meta);

        glDisable(GL_LIGHTING);
        glDisable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_COLOR);
        RenderUtils.disableLightmap();

        RenderUtils.beginDrawinngBlockScreen(x, y, z, direction, Reference.COLOR_HOLO, tileEntity, -0.65,1f);
        glTranslated(0, 0, -0.05);
        drawScreen(tileEntity,ticks);
        RenderUtils.endDrawinngBlockScreen();

        glDisable(GL_BLEND);
        glEnable(GL_CULL_FACE);
        glEnable(GL_LIGHTING);
        glPopMatrix();
    }

    public abstract void drawScreen(TileEntity tileEntity,float ticks);
}
