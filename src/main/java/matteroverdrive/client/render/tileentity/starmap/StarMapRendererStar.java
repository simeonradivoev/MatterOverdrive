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

package matteroverdrive.client.render.tileentity.starmap;

import com.google.common.base.Function;
import matteroverdrive.Reference;
import matteroverdrive.client.data.Color;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.data.Galaxy;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.SpaceBody;
import matteroverdrive.starmap.data.Star;
import matteroverdrive.tile.TileEntityMachineStarMap;
import matteroverdrive.util.RenderUtils;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 6/17/2015.
 */
@SideOnly(Side.CLIENT)
public class StarMapRendererStar extends StarMapRendererAbstract
{

	public static IBakedModel star_model;

	public StarMapRendererStar()
	{
		super();
		try
		{
			IModel model = OBJLoader.INSTANCE.loadModel(new ResourceLocation(Reference.PATH_MODEL + "block/sphere.obj"));
			star_model = model.bake(model.getDefaultState(), DefaultVertexFormats.ITEM, new Function<ResourceLocation, TextureAtlasSprite>()
			{
				@Nullable
				@Override
				public TextureAtlasSprite apply(@Nullable ResourceLocation input)
				{
					return Minecraft.getMinecraft().getTextureMapBlocks().registerSprite(input);
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void renderBody(Galaxy galaxy, SpaceBody spaceBody, TileEntityMachineStarMap starMap, float partialTicks, float distance)
	{
		if (spaceBody instanceof Star)
		{

			glLineWidth(1);

			Star star = (Star)spaceBody;
			random.setSeed(star.getSeed());

			double time = Minecraft.getMinecraft().theWorld.getWorldTime();

			GlStateManager.pushMatrix();
			GlStateManager.scale(star.getSize(), star.getSize(), star.getSize());

			ClientProxy.renderHandler.getRenderParticlesHandler().bindSheet();
			Tessellator.getInstance().getBuffer().begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
			RenderUtils.tessalateParticle(Minecraft.getMinecraft().getRenderViewEntity(), star_icon, star.getSize(), new Vec3d(0, 0, 0), Reference.COLOR_HOLO_YELLOW.getFloatR() * 0.1f, Reference.COLOR_HOLO_YELLOW.getFloatG() * 0.1f, Reference.COLOR_HOLO_YELLOW.getFloatB() * 0.1f, Reference.COLOR_HOLO_YELLOW.getFloatA() * 0.1f);
			Tessellator.getInstance().draw();

			Color starColor = new Color(star.getColor()).multiplyWithoutAlpha(0.25f * (1f / distance));
			RenderUtils.applyColor(starColor);
			glPolygonMode(GL_FRONT, GL_LINE);
			GlStateManager.disableTexture2D();
			double s = 0.2 + Math.sin(time * 0.01) * 0.01;
			GlStateManager.scale(s, s, s);

			Tessellator.getInstance().getBuffer().begin(GL_QUADS, DefaultVertexFormats.ITEM);
			renderSphere(starColor.getColor());
			Tessellator.getInstance().draw();
			glPolygonMode(GL_FRONT, GL_POINT);
			glPointSize(10 / Math.max(3, distance));

			Tessellator.getInstance().getBuffer().begin(GL_QUADS, DefaultVertexFormats.ITEM);
			renderSphere(starColor.getColor());
			Tessellator.getInstance().draw();
			Tessellator.getInstance().getBuffer().begin(GL_QUADS, DefaultVertexFormats.ITEM);
			if (Minecraft.getMinecraft().theWorld.getWorldTime() % 120 > 80)
			{
				float t = ((Minecraft.getMinecraft().theWorld.getWorldTime() % 120) - 80) / 40f;
				s = MOMathHelper.easeIn(t, 0.0, 10, 1);
				GlStateManager.scale(1 + s, 1 + s, 1 + s);
				renderSphere(starColor.multiplyWithoutAlpha(1 - t).getColor());
			}
			Tessellator.getInstance().draw();
			GlStateManager.popMatrix();
			glPolygonMode(GL_FRONT, GL_LINE);

			int planetID = 0;
			for (Planet planet : star.getPlanets())
			{
				float sizeMultiply = 1;
				if (starMap.getDestination().equals(planet))
				{
					sizeMultiply = 1.2f;
				}

				GlStateManager.disableAlpha();
				Color planetColor = Planet.getGuiColor(planet);
				random.setSeed(planet.getSeed());

				GlStateManager.pushMatrix();
				float axisRotation = random.nextInt(30) - 15;
				GlStateManager.rotate(axisRotation, 1, 0, 0);
				double radius = planet.getOrbit() * 2 + (star.getSize() / 2 + 0.1);
				float planetSize = planet.getSize();
				drawPlanetOrbit(planet, radius);

				GlStateManager.translate(Math.sin(time * 0.001 + 10 * planetID) * radius, 0, Math.cos(time * 0.001 + 10 * planetID) * radius);

				glPolygonMode(GL_FRONT, GL_FILL);
				GlStateManager.enableTexture2D();

				Tessellator.getInstance().getBuffer().begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

				if (starMap.getDestination().equals(planet))
				{
					RenderUtils.tessalateParticle(Minecraft.getMinecraft().getRenderViewEntity(), selectedIcon, planet.getSize() * 0.15f * sizeMultiply, new Vec3d(0, 0, 0), planetColor);
				}

				if (starMap.getGalaxyPosition().equals(planet))
				{
					RenderUtils.tessalateParticle(Minecraft.getMinecraft().getRenderViewEntity(), currentIcon, planet.getSize() * 0.25f, new Vec3d(0, 0, 0), planetColor);
				}

				Tessellator.getInstance().draw();

				GlStateManager.pushMatrix();
				GlStateManager.rotate(-axisRotation, 1, 0, 0);
				RenderUtils.rotateTo(Minecraft.getMinecraft().getRenderViewEntity());
				drawPlanetInfo(planet);
				GlStateManager.popMatrix();
				glPolygonMode(GL_FRONT, GL_LINE);
				GlStateManager.disableTexture2D();

				RenderUtils.applyColorWithMultipy(planetColor, 0.3f * (1f / distance));
				GlStateManager.rotate(100, 1, 0, 0);
				GlStateManager.rotate((float)time * 2, 0, 0, 1);
				sphere.draw(planetSize * 0.1f * sizeMultiply, (int)(16 + planetSize * 2), (int)(8 + planetSize * 2));
				planetID++;
				GlStateManager.popMatrix();
			}
			GlStateManager.enableTexture2D();
			glPolygonMode(GL_FRONT, GL_FILL);
		}
	}

	@Override
	public void renderGUIInfo(Galaxy galaxy, SpaceBody spaceBody, TileEntityMachineStarMap starMap, float partialTicks, float opacity)
	{
		if (spaceBody instanceof Star)
		{
			RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, opacity);
			GlStateManager.enableAlpha();

			Planet planet = galaxy.getPlanet(starMap.getDestination());
			if (planet != null)
			{
				if (GalaxyClient.getInstance().canSeePlanetInfo(planet, Minecraft.getMinecraft().thePlayer))
				{
					RenderUtils.drawString(planet.getName(), 72, -42, Reference.COLOR_HOLO, opacity);
				}
				else
				{
					RenderUtils.drawString(Minecraft.getMinecraft().standardGalacticFontRenderer, planet.getName(), 72, -42, Reference.COLOR_HOLO, opacity);
				}

				RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, opacity);
				ClientProxy.holoIcons.renderIcon("icon_size", 72, -28);
				RenderUtils.drawString(DecimalFormat.getPercentInstance().format(planet.getSize()), 92, -23, Reference.COLOR_HOLO, opacity);

				GlStateManager.disableTexture2D();
				glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
				random.setSeed(planet.getSeed());
				VertexBuffer wr = Tessellator.getInstance().getBuffer();
				wr.begin(GL_QUADS, DefaultVertexFormats.POSITION);
				for (int i = 0; i < 10; i++)
				{
					double step = 64d / 10d;
					double x = step * i;
					double y = -10;
					double height = 64 * (0.5 * random.nextGaussian() + 1d) / 2d;
					wr.pos(x, y, 0).endVertex();
					wr.pos(x + step - 1, y, 0).endVertex();
					wr.pos(x + step - 1, y - height, 0).endVertex();
					wr.pos(x, y - height, 0).endVertex();
				}
				Tessellator.getInstance().draw();
				GlStateManager.enableTexture2D();
			}
		}
	}

	@Override
	public boolean displayOnZoom(int zoom, SpaceBody spaceBody)
	{
		return true;
	}

	@Override
	public double getHologramHeight(SpaceBody spaceBody)
	{
		return 1.5;
	}

	private void drawPlanetInfo(Planet planet)
	{
		GlStateManager.translate(0, planet.getSize() * 0.13f + 0.05f, 0);
		GlStateManager.scale(0.005, 0.005, 0.005);
		GlStateManager.rotate(180, 0, 0, 1);
		if (GalaxyClient.getInstance().canSeePlanetInfo(planet, Minecraft.getMinecraft().thePlayer))
		{
			int width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(planet.getName());
			Minecraft.getMinecraft().fontRendererObj.drawString(planet.getName(), -width / 2, 0, Planet.getGuiColor(planet).getColor());

			if (planet.isHomeworld(Minecraft.getMinecraft().thePlayer))
			{
				width = Minecraft.getMinecraft().fontRendererObj.getStringWidth("[Home]");
				Minecraft.getMinecraft().fontRendererObj.drawString(TextFormatting.GOLD + "[Home]", -width / 2, -10, 0xFFFFFF);
			}
		}
		else
		{
			int width = Minecraft.getMinecraft().standardGalacticFontRenderer.getStringWidth(planet.getName());
			Minecraft.getMinecraft().standardGalacticFontRenderer.drawString(planet.getName(), -width / 2, 0, Planet.getGuiColor(planet).getColor());

			if (planet.hasOwner())
			{
				EntityPlayer owner = Minecraft.getMinecraft().theWorld.getPlayerEntityByUUID(planet.getOwnerUUID());
				if (owner != null)
				{
					String info = String.format("[%s]", owner.getDisplayName().getFormattedText());
					width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(info);
					Minecraft.getMinecraft().fontRendererObj.drawString(TextFormatting.GOLD + info, -width / 2, -10, 0xFFFFFF);
				}
			}
		}
	}

	private void drawPlanetOrbit(Planet planet, double radius)
	{
		GlStateManager.disableTexture2D();
		glPolygonMode(GL_FRONT, GL_LINE);
		VertexBuffer wr = Tessellator.getInstance().getBuffer();
		wr.begin(GL_LINES, DefaultVertexFormats.POSITION);
		//Tessellator.instance.startDrawing(GL_LINES);
		wr.color(Planet.getGuiColor(planet).getFloatR() * 0.1f, Planet.getGuiColor(planet).getFloatG() * 0.1f, Planet.getGuiColor(planet).getFloatB() * 0.1f, Planet.getGuiColor(planet).getFloatA() * 0.1f);
		for (int i = 0; i < 32; i++)
		{
			double angleStep = (Math.PI * 2) / 32;
			wr.pos(Math.sin(angleStep * i) * radius, 0, Math.cos(angleStep * i) * radius).endVertex();
			wr.pos(Math.sin(angleStep * (i + 1)) * radius, 0, Math.cos(angleStep * (i + 1)) * radius).endVertex();
		}
		Tessellator.getInstance().draw();
	}

	private void bindTexture(ResourceLocation location)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(location);
	}

	public void renderSphere(int color)
	{
		List<BakedQuad> quadList = star_model.getQuads(null, null, 0);
		for (BakedQuad quad : quadList)
		{
			Tessellator.getInstance().getBuffer().addVertexData(quad.getVertexData());
			Tessellator.getInstance().getBuffer().putColor4(color);
			//LightUtil.renderQuadColorSlow(Tessellator.getInstance().getBuffer(),quad,color);
			//LightUtil.renderQuadColor(Tessellator.getInstance().getBuffer(),quad,new Color(color).getColor()+0x00ff);
		}
	}
}
