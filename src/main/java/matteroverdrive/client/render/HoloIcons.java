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

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.GuiColor;
import cofh.lib.gui.element.ElementButton;
import cofh.lib.render.RenderHelper;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

/**
 * Created by Simeon on 6/15/2015.
 */
public class HoloIcons
{
    public TextureMap textureMap;
    public int SheetSize = 256;
    public ResourceLocation sheet = new ResourceLocation(Reference.MOD_ID,"textures/gui/holo_icons.png");

    public String icons[] = new String[]{"holo_home","holo_dotted_circle","holo_factory","holo_galaxy","person",
            "android_slot_arms","android_slot_chest","android_slot_head",
            "android_slot_legs","android_slot_other","barrel","battery","color","factory","module","sights",
            "shielding","scanner","upgrade","decompose","pattern_storage","home_icon","page_icon_home",
            "page_icon_tasks","page_icon_upgrades","page_icon_config","page_icon_search","page_icon_info",
            "page_icon_galaxy","page_icon_quadrant","page_icon_star","page_icon_planet","energy","arrow_right",
            "travel_icon","icon_search","icon_size","icon_shuttle","icon_stats","icon_scount_planet","icon_attack",
            "up_arrow","crosshair","up_arrow_large","android_feature_icon_bg","android_feature_icon_bg_active",
            "health","black_circle","connections","ammo","temperature","flash_drive","trade","mini_quit","mini_back"};


    public HoloIcons()
    {
        textureMap = new TextureMap(4,"textures/gui/items");
        Minecraft.getMinecraft().renderEngine.loadTextureMap(sheet,textureMap);
    }

    public void registerIcons(TextureMap textureMap)
    {
        for (String icon : icons)
        {
            textureMap.registerIcon(Reference.MOD_ID + ":" + icon);
        }

        MatterOverdrive.statRegistry.registerIcons(textureMap);
    }

    public IIcon getIcon(String icon)
    {
        return textureMap.getAtlasSprite(Reference.MOD_ID + ":" + icon);
    }

    public void bindSheet()
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(sheet);
    }

    public void renderIcon(String name,int x,int y)
    {
        bindSheet();
        IIcon icon = getIcon(name);
        RenderHelper.renderIcon(x, y, 0, icon, icon.getIconWidth(), icon.getIconHeight());
    }

    public void renderIcon(String name,int x,int y,int width,int height)
    {
        bindSheet();
        IIcon icon = getIcon(name);
        RenderHelper.renderIcon(x,y,0,icon,width,height);
    }

    public static void tessalateParticleIcon(IIcon icon,double x,double y,double z,float size,GuiColor color)
    {
        RenderUtils.tessalateParticle(Minecraft.getMinecraft().renderViewEntity, icon, size, Vec3.createVectorHelper(x, y, z), color);
    }

    public static void tessalateStaticIcon(IIcon icon,double x,double y,double z,float size,GuiColor color)
    {
        tessalateStaticIcon(icon,x,y,z,size,color,1);
    }

    public static void tessalateStaticIcon(IIcon icon,double x,double y,double z,float size,GuiColor color,float multiply)
    {
        float halfSize = size/2;
        float uMin = icon.getMinU();
        float uMax = icon.getMaxU();
        float vMin = icon.getMinV();
        float vMax = icon.getMaxV();

        Tessellator.instance.setColorRGBA_F(color.getFloatR() * multiply, color.getFloatG() * multiply, color.getFloatB() * multiply, color.getFloatA());
        Tessellator.instance.addVertexWithUV(x - halfSize, y - halfSize, z, uMax, vMax);
        Tessellator.instance.addVertexWithUV(x + halfSize,y - halfSize,z,uMin,vMax);
        Tessellator.instance.addVertexWithUV(x + halfSize,y + halfSize,z,uMin,vMin);
        Tessellator.instance.addVertexWithUV(x - halfSize,y + halfSize,z,uMax,vMin);
    }

    public ElementButton getIconButton(GuiBase gui,String name,IIcon icon,int x,int y,int sizeX,int sizeY)
    {
        ElementButton button = new ElementButton(gui,x,y,name, (int)(icon.getMinU() * 256),(int)(icon.getMinV() * 256),(int)(icon.getMinU() * 256),(int)(icon.getMinV() * 256),sizeX,sizeY,"");
        button.setTexture(sheet.toString(),256,256);
        return button;
    }
}
