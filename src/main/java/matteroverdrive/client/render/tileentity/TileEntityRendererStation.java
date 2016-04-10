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
import matteroverdrive.client.data.Color;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.util.IConfigSubscriber;
import matteroverdrive.util.MOLog;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.RenderUtils;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Random;

import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_QUADS;

/**
 * Created by Simeon on 5/27/2015.
 */
public abstract class TileEntityRendererStation<T extends MOTileEntityMachine> extends TileEntitySpecialRenderer<T> implements IConfigSubscriber
{
	public static final ResourceLocation glowTexture = new ResourceLocation(Reference.PATH_FX + "hologram_beam.png");
	final Random fliker;
	protected int shaderProgram;
	protected boolean validShader = true;
	protected Color holoColor;
	protected Color red_holoColor;
	private boolean enableHoloShader = true;

	public TileEntityRendererStation()
	{
		holoColor = Reference.COLOR_HOLO.multiplyWithoutAlpha(0.25f);
		red_holoColor = Reference.COLOR_HOLO_RED.multiplyWithoutAlpha(0.25f);
		fliker = new Random();
	}

	private void drawHoloLights(TileEntity entity, World world, double x, double y, double z, double t)
	{
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL_ONE, GL_ONE);
		GlStateManager.disableLighting();
		GlStateManager.depthMask(false);
		RenderUtils.disableLightmap();
		GlStateManager.disableCull();

		Minecraft.getMinecraft().renderEngine.bindTexture(glowTexture);

		double height = 9f * (1f / 16f);
		double hologramHeight = getLightHeight();
		double topSize = getLightsSize() - 1;

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y + height, z);
		VertexBuffer wr = Tessellator.getInstance().getBuffer();
		wr.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		RenderUtils.applyColor(getHoloColor(entity));

		wr.pos(0, 0, 0).tex(1, 1).endVertex();
		wr.pos(-topSize, hologramHeight, -topSize).tex(1, 0).endVertex();
		wr.pos(1 + topSize, hologramHeight, -topSize).tex(0, 0).endVertex();
		wr.pos(1, 0, 0).tex(0, 1).endVertex();

		wr.pos(1, 0, 0).tex(1, 1).endVertex();
		wr.pos(1 + topSize, hologramHeight, -topSize).tex(1, 0).endVertex();
		wr.pos(1 + topSize, hologramHeight, 1 + topSize).tex(0, 0).endVertex();
		wr.pos(1, 0, 1).tex(0, 1).endVertex();

		wr.pos(1, 0, 1).tex(1, 1).endVertex();
		wr.pos(1 + topSize, hologramHeight, 1 + topSize).tex(1, 0).endVertex();
		wr.pos(-topSize, hologramHeight, 1 + topSize).tex(0, 0).endVertex();
		wr.pos(0, 0, 1).tex(0, 1).endVertex();

		wr.pos(0, 0, 1).tex(1, 1).endVertex();
		wr.pos(-topSize, hologramHeight, 1 + topSize).tex(1, 0).endVertex();
		wr.pos(-topSize, hologramHeight, -topSize).tex(0, 0).endVertex();
		wr.pos(0, 0, 0).tex(0, 1).endVertex();

		Tessellator.getInstance().draw();
		GlStateManager.popMatrix();

		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableCull();
		RenderUtils.enableLightmap();
	}

	protected double getLightHeight()
	{
		return 1;
	}

	protected double getLightsSize()
	{
		return 1.3;
	}

	protected Color getHoloColor(TileEntity entity)
	{
		if (((MOTileEntityMachine)entity).isUseableByPlayer(Minecraft.getMinecraft().thePlayer))
		{
			return holoColor;
		}
		return red_holoColor;
	}

	@Override
	public void renderTileEntityAt(T machine, double x, double y, double z, float ticks, int destroyStage)
	{
		double t = MOMathHelper.noise(machine.getPos().getX() * 0.3, machine.getPos().getY() * 0.3, machine.getPos().getZ() * 0.3);
		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL_ONE, GL_ONE);
		try
		{
			renderHologram(machine, x, y, z, ticks, t);
		}
		catch (ClassCastException e)
		{
			MOLog.warn("Could not cast to desired station class", e);
		}
		catch (Exception e)
		{
			MOLog.warn("Error while render a station", e);
		}
		GlStateManager.popMatrix();

		if (drawHoloLights())
		{
			drawHoloLights(machine, machine.getWorld(), x, y, z, ticks);
		}

		GlStateManager.disableBlend();
	}

	protected boolean drawHoloLights()
	{
		return true;
	}

	protected void rotate(T station, double noise)
	{
		GlStateManager.rotate((Minecraft.getMinecraft().theWorld.getWorldTime() * 0.5f) + (1800 * (float)noise), 0, -1, 0);
	}

	protected boolean isUsable(T station)
	{
		return (station).isUseableByPlayer(Minecraft.getMinecraft().thePlayer);
	}

	protected void renderHologram(T station, double x, double y, double z, float partialTicks, double noise)
	{
		if (!isUsable(station))
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(x + 0.5, y + 0.8, z + 0.5);
			rotate(station, noise);

			GlStateManager.disableCull();
			GlStateManager.disableLighting();
			GlStateManager.scale(0.02, 0.02, 0.02);
			GlStateManager.rotate(180, 1, 0, 0);

			Color color = Reference.COLOR_HOLO_RED.multiplyWithoutAlpha(0.33f);
			String info[] = MOStringHelper.translateToLocal("gui.hologram.access_denied").split(" ");
			for (int i = 0; i < info.length; i++)
			{
				int width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(info[i]);
				GlStateManager.pushMatrix();
				GlStateManager.translate(-width / 2, -32, 0);
				Minecraft.getMinecraft().fontRendererObj.drawString(info[i], 0, i * 10, color.getColor());
				GlStateManager.popMatrix();
			}

			GlStateManager.popMatrix();
		}
	}

	@Override
	public void onConfigChanged(ConfigurationHandler config)
	{
		enableHoloShader = config.getBool("use holo shader", ConfigurationHandler.CATEGORY_CLIENT, true, "Use the custom holo shader for holographic items");
	}
}
