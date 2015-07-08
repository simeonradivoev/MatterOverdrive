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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 7/8/2015.
 */
public class TileEntityRendererChargingStation extends TileEntitySpecialRenderer
{
    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.PATH_BLOCKS + "charging_station.png");
    private IModelCustom model;

    public TileEntityRendererChargingStation()
    {
        model = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.MODEL_CHARGING_STATION));
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks)
    {
        if (tileEntity != null) {
            glPushMatrix();
            glColor3d(1, 1, 1);
            glTranslated(x + 0.5, y, z + 0.5);
            RenderUtils.rotateFromBlock(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
            bindTexture(TEXTURE);
            model.renderPart("Base");
            double colorMultiply = Math.sin(Minecraft.getMinecraft().theWorld.getWorldTime() * 0.2) * 0.2 + 0.8;
            glColor3d(colorMultiply, colorMultiply, colorMultiply);
            glDisable(GL_LIGHTING);
            RenderUtils.disableLightmap();
            model.renderPart("Rod");
            glEnable(GL_LIGHTING);
            glPopMatrix();
        }
    }
}
