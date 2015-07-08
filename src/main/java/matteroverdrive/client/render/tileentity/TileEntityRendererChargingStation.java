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

import cofh.lib.gui.GuiColor;
import matteroverdrive.Reference;
import matteroverdrive.init.MatterOverdriveIcons;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.Face;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.WavefrontObject;
import org.lwjgl.util.vector.Matrix4f;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 7/8/2015.
 */
public class TileEntityRendererChargingStation extends TileEntitySpecialRenderer
{
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
            float colorMul = (float)(Math.sin(Minecraft.getMinecraft().theWorld.getWorldTime()*0.2));
            glColor3d(colorMul, colorMul, colorMul);
            glTranslated(x, y, z);
            bindTexture(TextureMap.locationBlocksTexture);
            glDisable(GL_LIGHTING);
            RenderUtils.disableLightmap();
            Tessellator.instance.startDrawingQuads();
            Matrix4f mat = new Matrix4f();
            RenderUtils.rotateFromBlock(mat,tileEntity.getWorldObj(),tileEntity.xCoord,tileEntity.yCoord,tileEntity.zCoord);
            RenderUtils.tesseleteModelAsBlock(mat, ((WavefrontObject) model).groupObjects.get(1), MatterOverdriveIcons.charging_station, 0, 0, 0, (int)(colorMul * 20) + 220,false,null);
            Tessellator.instance.draw();
            glEnable(GL_LIGHTING);
            glPopMatrix();
        }
    }
}
