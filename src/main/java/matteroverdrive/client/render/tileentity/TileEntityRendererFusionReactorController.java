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
import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.client.data.Color;
import matteroverdrive.machines.fusionReactorController.TileEntityMachineFusionReactorController;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 5/14/2015.
 */
public class TileEntityRendererFusionReactorController extends TileEntitySpecialRenderer<TileEntityMachineFusionReactorController>
{
    public TileEntityRendererFusionReactorController(){

    }

    @Override
    public void renderTileEntityAt(TileEntityMachineFusionReactorController controller, double x, double y, double z, float ticks,int destoryStage)
    {
        if (!controller.isValidStructure()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);

            for (int i = 0; i < TileEntityMachineFusionReactorController.positionsCount; i++) {
                Vec3 pos = controller.getPosition(i,getWorld().getBlockState(controller.getPos()).getValue(MOBlock.PROPERTY_DIRECTION));

                GlStateManager.pushMatrix();
                GlStateManager.translate(pos.xCoord, pos.yCoord, pos.zCoord);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GL_ONE, GL_ONE);
                bindTexture(TileEntityRendererPatternMonitor.screenTextureBack);
                GlStateManager.translate(0.1, 0.1, 0.1);
                RenderUtils.drawCube(0.8, 0.8, 0.8, Reference.COLOR_HOLO);
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();

            }
            GlStateManager.popMatrix();
        }

        renderInfo(x, y, z, controller);
    }

    private void renderInfo(double x, double y, double z, TileEntityMachineFusionReactorController controller) {
        EnumFacing side = controller.getWorld().getBlockState(controller.getPos()).getValue(MOBlock.PROPERTY_DIRECTION);

        Color color = Reference.COLOR_HOLO;
        if (!controller.isValidStructure())
            color = Reference.COLOR_HOLO_RED;

        RenderUtils.beginDrawinngBlockScreen(x, y, z, side, color, controller);

        String[] info = controller.getMonitorInfo().split("\n");

        RenderUtils.drawScreenInfoWithGlobalAutoSize(info, color, side, 10, 10, 4);

        RenderUtils.endDrawinngBlockScreen();

    }

    private FontRenderer fontRenderer()
    {
        return Minecraft.getMinecraft().fontRendererObj;
    }
}
