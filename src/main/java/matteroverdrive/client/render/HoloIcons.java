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

package matteroverdrive.client.render;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.client.data.Color;
import matteroverdrive.util.MOLog;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.ITextureMapPopulator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simeon on 6/15/2015.
 */
@SideOnly(Side.CLIENT)
public class HoloIcons implements ITextureMapPopulator
{
	public final TextureMap textureMap;
	public final ResourceLocation sheet = new ResourceLocation(Reference.MOD_ID, "textures/gui/holo_icons.png");
	private final Map<String, HoloIcon> iconMap;
	public int sheetSize = 256;

	public HoloIcons()
	{
		iconMap = new HashMap<>();
		textureMap = new TextureMap("textures/gui/items", this);
		Minecraft.getMinecraft().renderEngine.loadTickableTexture(sheet, textureMap);
		try
		{
			textureMap.loadTexture(Minecraft.getMinecraft().getResourceManager());
		}
		catch (IOException e)
		{
			MOLog.log(Level.ERROR, e, "There was a problem while creating Holo Icons texture sheet");
		}
	}

	public static void tessalateParticleIcon(TextureAtlasSprite icon, double x, double y, double z, float size, Color color)
	{
		RenderUtils.tessalateParticle(Minecraft.getMinecraft().getRenderViewEntity(), icon, size, new Vec3d(x, y, z), color);
	}

	public static void tessalateStaticIcon(TextureAtlasSprite icon, double x, double y, double z, float size, Color color)
	{
		tessalateStaticIcon(icon, x, y, z, size, color, 1);
	}

	public static void tessalateStaticIcon(TextureAtlasSprite icon, double x, double y, double z, float size, Color color, float multiply)
	{
		float halfSize = size / 2;
		float uMin = icon.getMinU();
		float uMax = icon.getMaxU();
		float vMin = icon.getMinV();
		float vMax = icon.getMaxV();

		VertexBuffer wr = Tessellator.getInstance().getBuffer();
		wr.pos(x - halfSize, y - halfSize, z).tex(uMax, vMax).color(color.getFloatR() * multiply, color.getFloatG() * multiply, color.getFloatB() * multiply, color.getFloatA()).endVertex();
		wr.pos(x + halfSize, y - halfSize, z).tex(uMin, vMax).color(color.getFloatR() * multiply, color.getFloatG() * multiply, color.getFloatB() * multiply, color.getFloatA()).endVertex();
		wr.pos(x + halfSize, y + halfSize, z).tex(uMin, vMin).color(color.getFloatR() * multiply, color.getFloatG() * multiply, color.getFloatB() * multiply, color.getFloatA()).endVertex();
		wr.pos(x - halfSize, y + halfSize, z).tex(uMax, vMin).color(color.getFloatR() * multiply, color.getFloatG() * multiply, color.getFloatB() * multiply, color.getFloatA()).endVertex();
	}

	private void reg(TextureMap textureMap, String iconName, int originalSize)
	{
		registerIcon(textureMap, iconName, originalSize);
	}

	public HoloIcon registerIcon(TextureMap textureMap, String iconName, int originalSize)
	{
		HoloIcon holoIcon = new HoloIcon(textureMap.registerSprite(new ResourceLocation(Reference.MOD_ID, iconName)), originalSize, originalSize);
		iconMap.put(iconName, holoIcon);
		return holoIcon;
	}

	public HoloIcon getIcon(String icon)
	{
		return iconMap.get(icon);
	}

	public void bindSheet()
	{
		Minecraft.getMinecraft().renderEngine.bindTexture(sheet);
	}

	public void renderIcon(String name, double x, double y)
	{
		HoloIcon icon = getIcon(name);
		renderIcon(icon, x, y, icon.getOriginalWidth(), icon.getOriginalHeight());
	}

	public void renderIcon(String name, double x, double y, int width, int height)
	{
		renderIcon(getIcon(name), x, y, width, height);
	}

	public void renderIcon(HoloIcon icon, double x, double y)
	{
		renderIcon(icon, x, y, icon.getOriginalWidth(), icon.getOriginalHeight());
	}

	public void renderIcon(HoloIcon icon, double x, double y, int width, int height)
	{
		if (icon != null)
		{
			bindSheet();
			RenderUtils.renderIcon(x, y, 0, icon.getIcon(), width, height);
		}
	}

	@Override
	public void registerSprites(TextureMap ir)
	{
		reg(ir, "holo_home", 14);
		reg(ir, "holo_dotted_circle", 20);
		reg(ir, "holo_factory", 14);
		reg(ir, "person", 18);
		reg(ir, "android_slot_arms", 16);
		reg(ir, "android_slot_chest", 16);
		reg(ir, "android_slot_head", 16);
		reg(ir, "android_slot_legs", 16);
		reg(ir, "android_slot_other", 16);
		reg(ir, "barrel", 16);
		reg(ir, "battery", 16);
		reg(ir, "color", 16);
		reg(ir, "factory", 16);
		reg(ir, "module", 16);
		reg(ir, "sights", 16);
		reg(ir, "shielding", 16);
		reg(ir, "scanner", 16);
		reg(ir, "upgrade", 16);
		reg(ir, "decompose", 16);
		reg(ir, "pattern_storage", 16);
		reg(ir, "home_icon", 14);
		reg(ir, "page_icon_home", 14);
		reg(ir, "page_icon_tasks", 15);
		reg(ir, "page_icon_upgrades", 12);
		reg(ir, "page_icon_config", 16);
		reg(ir, "page_icon_search", 16);
		reg(ir, "page_icon_info", 16);
		reg(ir, "page_icon_galaxy", 11);
		reg(ir, "page_icon_quadrant", 9);
		reg(ir, "page_icon_star", 12);
		reg(ir, "page_icon_planet", 16);
		reg(ir, "energy", 16);
		reg(ir, "arrow_right", 19);
		reg(ir, "travel_icon", 18);
		reg(ir, "icon_search", 16);
		reg(ir, "icon_size", 16);
		reg(ir, "icon_shuttle", 16);
		reg(ir, "icon_size", 16);
		reg(ir, "icon_stats", 16);
		reg(ir, "icon_scount_planet", 32);
		reg(ir, "icon_attack", 16);
		reg(ir, "up_arrow", 7);
		reg(ir, "crosshair", 3);
		reg(ir, "up_arrow_large", 18);
		reg(ir, "android_feature_icon_bg", 22);
		reg(ir, "android_feature_icon_bg_active", 22);
		reg(ir, "health", 18);
		reg(ir, "black_circle", 18);
		reg(ir, "connections", 16);
		reg(ir, "ammo", 18);
		reg(ir, "temperature", 18);
		reg(ir, "flash_drive", 16);
		reg(ir, "trade", 16);
		reg(ir, "mini_quit", 16);
		reg(ir, "mini_back", 16);
		reg(ir, "tick", 16);
		reg(ir, "list", 16);
		reg(ir, "grid", 16);
		reg(ir, "sort_random", 16);
		reg(ir, "minimap_target", 21);
		reg(ir, "question_mark", 20);
		reg(ir, "android_feature_icon_bg_black", 22);
		reg(ir, "smile", 16);

		MatterOverdrive.statRegistry.registerIcons(ir, this);
	}
}
