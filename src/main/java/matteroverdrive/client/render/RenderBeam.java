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

package matteroverdrive.client.render;/* Created by Simeon on 10/18/2015. */

import matteroverdrive.client.data.Color;
import matteroverdrive.util.MOPhysicsHelper;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

import static org.lwjgl.opengl.GL11.GL_QUADS;

@SideOnly(Side.CLIENT)
public abstract class RenderBeam<T extends EntityLivingBase> implements IWorldLastRenderer
{
    protected final Random random = new Random();

    protected abstract boolean shouldRenderBeam(T entity);
    protected abstract void onBeamRaycastHit(RayTraceResult hit, T caster);
    protected abstract void onBeamRender(T caster);
    protected abstract Color getBeamColor(T caster);
    protected abstract ResourceLocation getBeamTexture(T caster);
    protected abstract float getBeamMaxDistance(T caster);
    protected abstract float getBeamThickness(T caster);

    protected boolean renderRaycastedBeam(Vec3d direction, Vec3d offset, T caster)
    {
        return renderRaycastedBeam(caster.getPositionEyes(1), direction, offset, caster);
    }

    protected boolean renderRaycastedBeam(Vec3d position, Vec3d direction, Vec3d offset, T caster)
    {
        double maxDistance = getBeamMaxDistance(caster);

        RayTraceResult hit = MOPhysicsHelper.rayTrace(position, caster.worldObj,maxDistance, 0, new Vec3d(0, 0, 0), false, true, direction, caster);
        if (hit != null && hit.typeOfHit != RayTraceResult.Type.MISS)
        {
            renderBeam(position, hit.hitVec, offset, getBeamColor(caster), getBeamTexture(caster), getBeamThickness(caster), caster);
            onBeamRender(caster);
            onBeamRaycastHit(hit, caster);
            return true;
        }else
        {

            renderBeam(position,position.addVector(direction.xCoord * maxDistance, direction.yCoord * maxDistance, direction.zCoord * maxDistance), offset, getBeamColor(caster), getBeamTexture(caster), getBeamThickness(caster), caster);
            onBeamRender(caster);
        }
        return false;
    }

    protected void renderBeam(Vec3d from, Vec3d to, Vec3d offest, Color color, ResourceLocation texture, float tickness, T viewer)
    {
        if (texture != null)
            Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        RenderUtils.applyColor(color);
        //GlStateManager.disableCull();
        //GlStateManager.enableBlend();
        //GlStateManager.blendFunc(GL_ONE, GL_ONE);
        //GlStateManager.disableLighting();
        double distance = from.subtract(to).lengthVector();
        double v = -viewer.worldObj.getWorldTime() * 0.2;

        GlStateManager.pushMatrix();
        GlStateManager.translate(from.xCoord,from.yCoord,from.zCoord);
        GlStateManager.rotate(-viewer.getRotationYawHead(), 0, 1, 0);
        GlStateManager.rotate(viewer.rotationPitch, 1, 0, 0);
        GlStateManager.translate(offest.xCoord, offest.yCoord, offest.zCoord);
        VertexBuffer wr = Tessellator.getInstance().getBuffer();
        wr.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        wr.pos(tickness, 0, 0).tex(0, v).endVertex();
        wr.pos(tickness, 0, distance).tex(0, v + distance * 1.5).endVertex();
        wr.pos(-tickness, 0, distance).tex(1, v + distance * 1.5).endVertex();
        wr.pos(-tickness, 0, 0).tex(1, v).endVertex();

        wr.pos(0, tickness, 0).tex( 0, v).endVertex();
        wr.pos(0, tickness, distance).tex(0, v + distance * 1.5).endVertex();
        wr.pos(0, -tickness, distance).tex(1, v + distance * 1.5).endVertex();
        wr.pos(0, -tickness, 0).tex( 1, v).endVertex();
        Tessellator.getInstance().draw();
        GlStateManager.popMatrix();

        //GlStateManager.enableCull();
        //GlStateManager.disableBlend();
        //GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }
}
