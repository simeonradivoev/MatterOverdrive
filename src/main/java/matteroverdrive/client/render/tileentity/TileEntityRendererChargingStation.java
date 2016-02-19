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

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Simeon on 7/8/2015.
 */
public class TileEntityRendererChargingStation extends TileEntitySpecialRenderer
{
    /*private IModelCustom model;

    public TileEntityRendererChargingStation()
    {
        model = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.MODEL_CHARGING_STATION));
    }*/

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks,int destroyStage)
    {
        /*if (tileEntity != null) {
            glPushMatrix();
            float colorMul = (float)(Math.sin(Minecraft.getMinecraft().theWorld.getWorldTime() * 0.2));
            GlStateManager.color(colorMul, colorMul, colorMul);
            GlStateManager.translate(x, y, z);
            bindTexture(TextureMap.locationBlocksTexture);
            GlStateManager.disableLighting();
            RenderUtils.disableLightmap();
            Tessellator.instance.startDrawingQuads();
            Matrix4f mat = new Matrix4f();
            RenderUtils.rotateFromBlock(mat, tileEntity.getWorld(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
            RenderUtils.tesseleteModelAsBlock(mat, ((WavefrontObject) model).groupObjects.get(1), MatterOverdriveIcons.charging_station, 0, 0, 0, (int)(colorMul * 20) + 220, false, null);
            Tessellator.instance.draw();
            RenderUtils.enableLightmap();
            GlStateManager.enableLighting();
            glPopMatrix();
        }*/
    }
}
