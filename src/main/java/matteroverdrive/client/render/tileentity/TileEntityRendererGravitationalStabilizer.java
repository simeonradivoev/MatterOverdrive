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
import matteroverdrive.tile.TileEntityGravitationalAnomaly;
import matteroverdrive.tile.TileEntityMachineGravitationalStabilizer;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_ONE;

/**
 * Created by Simeon on 5/12/2015.
 */
public class TileEntityRendererGravitationalStabilizer extends TileEntitySpecialRenderer<TileEntityMachineGravitationalStabilizer>
{
    public static final ResourceLocation beam = new ResourceLocation(Reference.PATH_FX + "physbeam.png");

    @Override
    public void renderTileEntityAt(TileEntityMachineGravitationalStabilizer stabilizer, double x, double y, double z, float ticks,int destroyStage)
    {
        if (stabilizer.getHit() != null) {
            RayTraceResult hit = stabilizer.getHit();
            TileEntity tileEntityHit = stabilizer.getWorld().getTileEntity(hit.getBlockPos());


            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);

            long time = stabilizer.getWorld().getWorldTime();
            double pulseSize = Math.sin(time * 0.2) * 0.001;
            Vector3f source = new Vector3f(stabilizer.getPos().getX(), stabilizer.getPos().getY(), stabilizer.getPos().getZ());
            Vector3f destination = new Vector3f((float)hit.hitVec.xCoord, (float)hit.hitVec.yCoord, (float)hit.hitVec.zCoord);
            Vector3f dir = Vector3f.sub(destination, source, null);
            Vector3f dirC = Vector3f.cross(dir,new Vector3f(1,0,1),null);
            float distance = dir.length();
            dir.normalise(dir);
            Vector3f front = new Vector3f(0, 0, -1);
            Vector3f c = Vector3f.cross(dir, front, null);
            double omega = Math.acos(Vector3f.dot(dir, front));


            GlStateManager.enableBlend();
            GlStateManager.disableCull();
            GlStateManager.disableLighting();
            RenderUtils.disableLightmap();

            GlStateManager.blendFunc(GL_ONE, GL_ONE);
            GlStateManager.color((float) stabilizer.getBeamColorR(), (float)stabilizer.getBeamColorG(), (float)stabilizer.getBeamColorB());
            bindTexture(beam);

            GlStateManager.pushMatrix();
            GlStateManager.scale(dirC.x * pulseSize + 1, dirC.y * pulseSize + 1, dirC.z * pulseSize + 1);
            GlStateManager.translate(dir.x * distance / 2, dir.y * distance / 2, dir.z * distance / 2);
            GlStateManager.scale(dir.x * distance + 1, dir.y * distance + 1, dir.z * distance + 1);
            GlStateManager.rotate((float)(omega * (180 / Math.PI)), c.x, c.y, c.z);
            GlStateManager.rotate(90, 0, 1, 0);
            GlStateManager.translate(-0.5, -0.5, 0);
            RenderUtils.drawPlaneWithUV(1, 1, 0, 0, distance / 2, 1);
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            GlStateManager.translate(dir.x * distance / 2, dir.y * distance / 2, dir.z * distance / 2);
            GlStateManager.scale((destination.x - source.x) + 1, (destination.y - source.y) + 1, (destination.z - source.z) + 1);
            GlStateManager.rotate((float)(omega * (180 / Math.PI)), c.x, c.y, c.z);
            GlStateManager.rotate(90, 0, 0, 1);
            GlStateManager.rotate(90, 0, 1, 0);
            GlStateManager.translate(-0.5, -0.5, 0);
            RenderUtils.drawPlaneWithUV(1, 1, 0, 0, distance / 2, 1);
            GlStateManager.popMatrix();

            GlStateManager.enableCull();
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            RenderUtils.enableLightmap();
            GlStateManager.popMatrix();

            if (tileEntityHit != null && tileEntityHit instanceof TileEntityGravitationalAnomaly)
                renderScreen(x, y, z, stabilizer, (TileEntityGravitationalAnomaly) tileEntityHit);
        }
    }


    public void renderScreen(double x, double y, double z, TileEntityMachineGravitationalStabilizer stabilizer, TileEntityGravitationalAnomaly anomaly) {

        EnumFacing side = anomaly.getWorld().getBlockState(stabilizer.getPos()).getValue(MOBlock.PROPERTY_DIRECTION).getOpposite();

        RenderUtils.beginDrawinngBlockScreen(x, y, z, side, Reference.COLOR_HOLO,stabilizer);

        List<String> infos = new ArrayList<>();
        anomaly.addInfo(anomaly.getWorld(), anomaly.getPos().getX(),anomaly.getPos().getY(),anomaly.getPos().getZ(), infos);
        RenderUtils.drawScreenInfoWithGlobalAutoSize(infos.toArray(new String[infos.size()]), Reference.COLOR_HOLO, side, 10, 10, 4);

        RenderUtils.endDrawinngBlockScreen();
    }

    private FontRenderer fontRenderer()
    {
        return Minecraft.getMinecraft().fontRendererObj;
    }

}
