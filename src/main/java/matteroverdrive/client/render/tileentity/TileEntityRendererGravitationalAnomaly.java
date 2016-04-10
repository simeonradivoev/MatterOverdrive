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
import matteroverdrive.tile.TileEntityGravitationalAnomaly;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.glu.Sphere;

import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

/**
 * Created by Simeon on 5/12/2015.
 */
public class TileEntityRendererGravitationalAnomaly extends TileEntitySpecialRenderer
{
	public static final ResourceLocation core = new ResourceLocation(Reference.PATH_BLOCKS + "gravitational_anomaly_core.png");
	public static final ResourceLocation glow = new ResourceLocation(Reference.PATH_BLOCKS + "gravitational_anomaly_glow.png");
	public static final ResourceLocation black = new ResourceLocation(Reference.PATH_BLOCKS + "black.png");

	private final Sphere sphere_model;

	public TileEntityRendererGravitationalAnomaly()
	{
		sphere_model = new Sphere();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float ticks, int destroyStage)
	{
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		long time = Minecraft.getMinecraft().theWorld.getWorldTime();
		float speed = ((TileEntityGravitationalAnomaly)tileEntity).getBreakStrength();
		double resonateSpeed = 0.2;
		double radius = ((TileEntityGravitationalAnomaly)tileEntity).getEventHorizon();

		radius = radius * Math.sin(time * resonateSpeed) * 0.1 + radius * 0.9;

		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();

		GlStateManager.translate(x + 0.5, y + 0.5, z + 0.5);
		GlStateManager.scale(radius, radius, radius);

		GlStateManager.disableCull();
		GlStateManager.disableTexture2D();
		GlStateManager.color(0, 0, 0, 1);
		sphere_model.draw((float)0.5, 8, 8);
		GlStateManager.enableTexture2D();
		GlStateManager.enableCull();

		GlStateManager.enableBlend();
		GlStateManager.scale(2, 2, 2);
		GlStateManager.rotate(player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * ticks, 0, -1, 0);
		GlStateManager.rotate(player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * ticks, 1, 0, 0);
		GlStateManager.rotate(time * speed, 0, 0, 1);
		GlStateManager.translate(-0.5, -0.5, 0);
		GlStateManager.color(1, 1, 1);
		GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		bindTexture(core);
		RenderUtils.drawPlane(1);
		bindTexture(glow);
		RenderUtils.drawPlane(1);

		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}
