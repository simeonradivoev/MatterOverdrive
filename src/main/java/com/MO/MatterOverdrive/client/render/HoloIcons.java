package com.MO.MatterOverdrive.client.render;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.GuiColor;
import cofh.lib.gui.element.ElementButton;
import cofh.lib.render.RenderHelper;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.data.HoloIcon;
import com.MO.MatterOverdrive.data.IconHolder;
import com.MO.MatterOverdrive.handler.AndroidStatRegistry;
import com.MO.MatterOverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
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
            "travel_icon","icon_search","icon_size","icon_shuttle"};

    public HoloIcons()
    {
        textureMap = new TextureMap(4,"textures/gui/items");
        Minecraft.getMinecraft().renderEngine.loadTextureMap(sheet,textureMap);
    }

    public void registerIcons(TextureMap textureMap)
    {
        for (int i = 0;i < icons.length;i++)
        {
            textureMap.registerIcon(Reference.MOD_ID + ":" + icons[i]);
        }

        AndroidStatRegistry.registerIcons(textureMap);
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
        RenderHelper.renderIcon(x,y,0,icon,icon.getIconWidth(),icon.getIconHeight());
    }

    public static void tessalateParticleIcon(IIcon icon,double x,double y,double z,float size,GuiColor color)
    {
        RenderUtils.tessalateParticle(Minecraft.getMinecraft().renderViewEntity, icon, size, Vec3.createVectorHelper(x,y,z),color);
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
