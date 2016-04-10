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

package matteroverdrive.client.render.entity;

import matteroverdrive.Reference;
import matteroverdrive.entity.weapon.PlasmaBolt;
import matteroverdrive.util.RenderUtils;
import matteroverdrive.util.animation.MOEasing;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 7/25/2015.
 */
public class EntityRendererPhaserFire extends Render
{
	private static final ResourceLocation arrowTextures = new ResourceLocation(Reference.PATH_ENTETIES + "PlasmaFire.png");

	public EntityRendererPhaserFire(RenderManager renderManager)
	{
		super(renderManager);
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(PlasmaBolt plasmaBolt, double x, double y, double z, float p_76986_8_, float p_76986_9_)
	{
		this.bindEntityTexture(plasmaBolt);
		RenderUtils.disableLightmap();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
		RenderUtils.applyColorWithMultipy(plasmaBolt.getColor(), MOEasing.Quad.easeOut(plasmaBolt.getLife(), 0, 1, 0.7f));
		GlStateManager.disableLighting();
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.rotate(plasmaBolt.prevRotationYaw + (plasmaBolt.rotationYaw - plasmaBolt.prevRotationYaw) * p_76986_9_ - 90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(plasmaBolt.prevRotationPitch + (plasmaBolt.rotationPitch - plasmaBolt.prevRotationPitch) * p_76986_9_, 0.0F, 0.0F, 1.0F);
		byte b0 = 0;
		float f2 = 0.0F;
		float f3 = 0.5F;
		float f4 = (float)(b0 * 10) / 32.0F;
		float f5 = (float)(5 + b0 * 10) / 32.0F;
		float f6 = 0.0F;
		float f7 = 0.15625F;
		float f8 = (float)(5 + b0 * 10) / 32.0F;
		float f9 = (float)(10 + b0 * 10) / 32.0F;
		float f10 = 0.05625F;
		float renderSize = plasmaBolt.getRenderSize();
		double length = 6 * new Vec3d(plasmaBolt.motionX, plasmaBolt.motionY, plasmaBolt.motionZ).lengthVector() + 10;
		GlStateManager.enableRescaleNormal();
		GlStateManager.disableCull();

		GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(f10, f10, f10);
		GlStateManager.translate(-4.0F, 0.0F, 0.0F);
		GL11.glNormal3f(f10, 0.0F, 0.0F);
		VertexBuffer wr = Tessellator.getInstance().getBuffer();
		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		wr.pos(1, -renderSize, -renderSize).tex((double)f6, (double)f8).endVertex();
		wr.pos(1, -renderSize, renderSize).tex((double)f7, (double)f8).endVertex();
		wr.pos(1, renderSize, renderSize).tex((double)f7, (double)f9).endVertex();
		wr.pos(1, renderSize, -renderSize).tex((double)f6, (double)f9).endVertex();
		Tessellator.getInstance().draw();

		wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		wr.pos(length - 1, -renderSize, renderSize).tex((double)f7, (double)f8).endVertex();
		wr.pos(length - 1, renderSize, renderSize).tex((double)f7, (double)f9).endVertex();
		wr.pos(length - 1, renderSize, -renderSize).tex((double)f6, (double)f9).endVertex();
		wr.pos(length - 1, -renderSize, -renderSize).tex((double)f6, (double)f8).endVertex();
		Tessellator.getInstance().draw();

		for (int i = 0; i < 2; ++i)
		{
			GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
			GL11.glNormal3f(0.0F, 0.0F, f10);
			wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			wr.pos(0, -renderSize, 0.0D).tex((double)f2, (double)f4).endVertex();
			wr.pos(length, -renderSize, 0.0D).tex((double)f3, (double)f4).endVertex();
			wr.pos(length, renderSize, 0.0D).tex((double)f3, (double)f5).endVertex();
			wr.pos(0, renderSize, 0.0D).tex((double)f2, (double)f5).endVertex();
			Tessellator.getInstance().draw();
		}

		//GlStateManager.disableDepth();
		GlStateManager.enableLighting();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		RenderUtils.enableLightmap();
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(PlasmaBolt p_110775_1_)
	{
		return arrowTextures;
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(Entity p_110775_1_)
	{
		return this.getEntityTexture((PlasmaBolt)p_110775_1_);
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	@Override
	public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
	{
		this.doRender((PlasmaBolt)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}
}
