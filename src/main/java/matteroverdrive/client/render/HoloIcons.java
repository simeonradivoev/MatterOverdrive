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
import matteroverdrive.fx.MOEntityFX;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.IIconCreator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simeon on 6/15/2015.
 */
@SideOnly(Side.CLIENT)
public class HoloIcons {
    private final Map<String, HoloIcon> iconMap;
    public final TextureMap textureMap;
    public int sheetSize = 256;
    public final ResourceLocation sheet = new ResourceLocation(Reference.MOD_ID, "textures/gui/holo_icons.png");

    public HoloIcons() {
        iconMap = new HashMap<>();
        textureMap = new TextureMap("textures");
        Minecraft.getMinecraft().renderEngine.loadTickableTexture(sheet,textureMap);
    }

    public void registerIcons() {
        reg("holo_home", 14);
        reg("holo_dotted_circle", 20);
        reg("holo_factory", 14);
        reg("person", 18);
        reg("android_slot_arms", 16);
        reg("android_slot_chest", 16);
        reg("android_slot_head", 16);
        reg("android_slot_legs", 16);
        reg("android_slot_other", 16);
        reg("barrel", 16);
        reg("battery", 16);
        reg("color", 16);
        reg("factory", 16);
        reg("module", 16);
        reg("sights", 16);
        reg("shielding", 16);
        reg("scanner", 16);
        reg("upgrade", 16);
        reg("decompose", 16);
        reg("pattern_storage", 16);
        reg("home_icon", 14);
        reg("page_icon_home", 14);
        reg("page_icon_tasks", 15);
        reg("page_icon_upgrades", 12);
        reg("page_icon_config", 16);
        reg("page_icon_search", 16);
        reg("page_icon_info", 16);
        reg("page_icon_galaxy", 11);
        reg("page_icon_quadrant", 9);
        reg("page_icon_star", 12);
        reg("page_icon_planet", 16);
        reg("energy", 16);
        reg("arrow_right", 19);
        reg("travel_icon", 18);
        reg("icon_search", 16);
        reg("icon_size", 16);
        reg("icon_shuttle", 16);
        reg("icon_size", 16);
        reg("icon_stats", 16);
        reg("icon_scount_planet", 32);
        reg("icon_attack", 16);
        reg("up_arrow", 7);
        reg("crosshair", 3);
        reg("up_arrow_large", 18);
        reg("android_feature_icon_bg", 22);
        reg("android_feature_icon_bg_active", 22);
        reg("health", 18);
        reg("black_circle", 18);
        reg("connections", 16);
        reg("ammo", 18);
        reg("temperature", 18);
        reg("flash_drive", 16);
        reg("trade", 16);
        reg("mini_quit", 16);
        reg("mini_back", 16);
        reg("tick", 16);
        reg("list", 16);
        reg("grid", 16);
        reg("sort_random", 16);
        reg("minimap_target",21);
        reg("question_mark",20);
        reg("android_feature_icon_bg_black",22);
        reg("smile",16);

        MatterOverdrive.statRegistry.registerIcons(this);
    }

    @SubscribeEvent
    public void onModelBaking(ModelBakeEvent event)
    {
        textureMap.loadSprites(Minecraft.getMinecraft().getResourceManager(), iconRegistry -> {
            for (Map.Entry<String,HoloIcon> entry : iconMap.entrySet())
            {
                TextureAtlasSprite sprite = iconRegistry.registerSprite(new ResourceLocation("mo:gui/items/"+ entry.getKey()));
                entry.getValue().setIcon(sprite);
            }
        });
    }

    private void reg(String iconName, int originalSize) {
        registerIcon(iconName,originalSize);
    }

    public HoloIcon registerIcon(String iconName,int originalSize)
    {
        HoloIcon holoIcon = new HoloIcon(textureMap.registerSprite(new ResourceLocation(Reference.MOD_ID + ":" + iconName)), originalSize, originalSize);
        iconMap.put(iconName, holoIcon);
        return holoIcon;
    }

    public HoloIcon getIcon(String icon) {
        return iconMap.get(icon);
    }

    public void bindSheet() {
        Minecraft.getMinecraft().renderEngine.bindTexture(sheet);
    }

    public void renderIcon(String name, double x, double y) {
        HoloIcon icon = getIcon(name);
        renderIcon(icon, x, y, icon.getOriginalWidth(), icon.getOriginalHeight());
    }

    public void renderIcon(String name, double x, double y, int width, int height) {
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

    public static void tessalateParticleIcon(MOEntityFX.ParticleIcon icon, double x, double y, double z, float size, Color color)
    {
        RenderUtils.tessalateParticle(Minecraft.getMinecraft().getRenderViewEntity(), icon, size, new Vec3(x, y, z), color);
    }

    public static void tessalateStaticIcon(MOEntityFX.ParticleIcon icon, double x, double y, double z, float size, Color color)
    {
        tessalateStaticIcon(icon, x, y, z, size, color, 1);
    }

    public static void tessalateStaticIcon(MOEntityFX.ParticleIcon icon, double x, double y, double z, float size, Color color, float multiply)
    {
        float halfSize = size / 2;
        float uMin = icon.getMinU();
        float uMax = icon.getMaxU();
        float vMin = icon.getMinV();
        float vMax = icon.getMaxV();

        WorldRenderer wr = Tessellator.getInstance().getWorldRenderer();
        wr.pos(x - halfSize, y - halfSize, z).tex(uMax, vMax).color(color.getFloatR() * multiply, color.getFloatG() * multiply, color.getFloatB() * multiply, color.getFloatA()).endVertex();
        wr.pos(x + halfSize,y - halfSize,z).tex(uMin,vMax).color(color.getFloatR() * multiply, color.getFloatG() * multiply, color.getFloatB() * multiply, color.getFloatA()).endVertex();
        wr.pos(x + halfSize,y + halfSize,z).tex(uMin,vMin).color(color.getFloatR() * multiply, color.getFloatG() * multiply, color.getFloatB() * multiply, color.getFloatA()).endVertex();
        wr.pos(x - halfSize,y + halfSize,z).tex(uMax,vMin).color(color.getFloatR() * multiply, color.getFloatG() * multiply, color.getFloatB() * multiply, color.getFloatA()).endVertex();
    }
}
