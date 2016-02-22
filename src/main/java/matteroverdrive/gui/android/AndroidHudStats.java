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

package matteroverdrive.gui.android;

import matteroverdrive.Reference;
import matteroverdrive.api.inventory.IEnergyPack;
import matteroverdrive.api.weapon.IWeapon;
import matteroverdrive.client.data.Color;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import java.text.DecimalFormat;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 9/9/2015.
 */
public class AndroidHudStats extends AndroidHudElement
{
    public static final ResourceLocation top_element_bg = new ResourceLocation(Reference.PATH_ELEMENTS + "android_bg_element.png");

    public AndroidHudStats(AndroidHudPosition position,String name)
    {
        super(position,name, 174, 32);
    }

    @Override
    public boolean isVisible(AndroidPlayer android) {
        return true;
    }

    @Override
    public void drawElement(AndroidPlayer androidPlayer, ScaledResolution resolution, float ticks)
    {
        GlStateManager.disableAlpha();

        double energy_perc = (double) androidPlayer.getEnergyStored() / (double) androidPlayer.getMaxEnergyStored();
        double health_perc = androidPlayer.getPlayer().getHealth() / androidPlayer.getPlayer().getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue();
        int x = 0;
        int y = 0;
        if (this.getPosition().y > 0.5)
        {
            y = -48;
        }

        if (getPosition().y == 0 || getPosition().y == 1)
        {
            x = 12 - (int)(24 * getPosition().x);
            y = 12 - (int)(24 * getPosition().y);

            GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE);

            RenderUtils.applyColorWithAlpha(baseColor);
            mc.renderEngine.bindTexture(top_element_bg);
            RenderUtils.drawPlane(x, y+(getHeight(resolution,androidPlayer)-11)*getPosition().y,0,174,11);
            y += 10 - 5 * getPosition().y;
            x += 5;

            int statsX = x;
            statsX -= (getWidthIconWithPercent(health_perc,18) + getWidthIconWithPercent(energy_perc,20) + getWidthIconWithPercent(androidPlayer.getSpeedMultiply(),16)) * getPosition().x;
            statsX += 165 * getPosition().x;

            statsX += renderIconWithPercent("health", health_perc, statsX, y,0,0, false, Reference.COLOR_HOLO_RED, baseColor, 18, 18);
            statsX += renderIconWithPercent("battery", energy_perc, statsX, y,0,-2, false, Reference.COLOR_HOLO_RED, baseColor, 20, 20);
            renderIconWithPercent("person", androidPlayer.getSpeedMultiply(), statsX, y,0,1, false, baseColor, baseColor, 14, 14);

            int weaponX = x;
            weaponX -= (getAmmoBoxWidth(androidPlayer) + getHeatWidth(androidPlayer)) * getPosition().x;
            weaponX += 165 * getPosition().x;

            y += 20;
            weaponX += renderAmmoBox(androidPlayer,weaponX,y,false,baseColor);
            renderHeat(androidPlayer,weaponX,y,false,baseColor);
        }
        else if (getPosition() == AndroidHudPosition.MIDDLE_LEFT || getPosition() == AndroidHudPosition.MIDDLE_RIGHT)
        {
            x = 12 - (int)(24 * getPosition().x);

            //drawBackground(x,y,androidPlayer,resolution);
            GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE);

            RenderUtils.applyColorWithAlpha(baseColor);
            GlStateManager.pushMatrix();
            GlStateManager.translate(x+11+(getWidth(resolution,androidPlayer)-11)*getPosition().x,y,0);
            GlStateManager.rotate(90,0,0,1);
            mc.renderEngine.bindTexture(top_element_bg);
            RenderUtils.drawPlane(0, 0,0,174,11);
            GlStateManager.popMatrix();

            y += 86;
            int ySize = 24 + 22 + 24;
            int ammoWidth = getAmmoBoxWidth(androidPlayer);
            int heatWidth = getHeatWidth(androidPlayer);
            int ammoHeight = ammoWidth == 0 ? 0 : 24;
            int heatHeight = heatWidth == 0 ? 0 : 24;
            ySize += ammoHeight;
            ySize += heatHeight;
            y -= ySize/2;
            //y += 40;

            x += 11;
            renderIconWithPercent("health", health_perc, x + (int) (((getWidth(resolution,androidPlayer)-getWidthIconWithPercent(health_perc,18)) - 22) * getPosition().x), y,0,0, false, Reference.COLOR_HOLO_RED, baseColor, 18, 18);
            y += 24;
            renderIconWithPercent("battery", energy_perc, x + (int)(((getWidth(resolution,androidPlayer)-getWidthIconWithPercent(energy_perc,20)) - 22) * getPosition().x), y - 2,0,-2, false, Reference.COLOR_HOLO_RED, baseColor, 20, 20);
            y += 22;
            renderIconWithPercent("person", androidPlayer.getSpeedMultiply(), x + (int)(((getWidth(resolution,androidPlayer)-getWidthIconWithPercent(androidPlayer.getSpeedMultiply(),16)) - 22) * getPosition().x), y,0,1, false, baseColor, baseColor, 16, 16);
            y += 24;
            renderAmmoBox(androidPlayer,x + (int) (((getWidth(resolution,androidPlayer)-ammoWidth) - 22) * getPosition().x),y,false,baseColor);
            y += ammoHeight;
            renderHeat(androidPlayer,x + (int) (((getWidth(resolution,androidPlayer)-heatWidth) - 22) * getPosition().x),y,false,baseColor);
        }
        else if (getPosition() == AndroidHudPosition.MIDDLE_CENTER)
        {
            renderIconWithPercent("health", health_perc, x - getWidthIconWithPercent(health_perc,18) - 22, y - 8,0,0, true, Reference.COLOR_HOLO_RED, baseColor, 18, 18);
            renderIconWithPercent("battery", energy_perc, x + 24,y - 9,0,0, false, Reference.COLOR_HOLO_RED, baseColor, 20, 20);
        }

        GlStateManager.enableAlpha();
    }

    private int getWidthIconWithInfo(String info,int iconWidth)
    {
        return iconWidth + ClientProxy.moFontRender.getStringWidth(info) + 4;
    }

    private int getWidthIconWithPercent(double amount,int iconWidth)
    {
        return getWidthIconWithInfo(DecimalFormat.getPercentInstance().format(amount),iconWidth);
    }

    private int renderIconWithPercent(String icon,double amount,int x,int y,int iconOffsetX,int iconOffsetY,boolean leftSided,Color fromColor,Color toColor,int iconWidth,int iconHeight)
    {
        return this.renderIconWithInfo(icon,DecimalFormat.getPercentInstance().format(amount),RenderUtils.lerp(fromColor, toColor, MathHelper.clamp_float((float) amount,0,1)),x,y,iconOffsetX,iconOffsetY,leftSided,iconWidth,iconHeight);
    }

    private int renderIconWithInfo(String icon, String info, Color color, int x, int y, int iconOffsetX, int iconOffsetY, boolean leftSided, int iconWidth, int iconHeight)
    {
        HoloIcon holoIcon = ClientProxy.holoIcons.getIcon(icon);
        int infoWidth = ClientProxy.moFontRender.getStringWidth(info);

        GlStateManager.disableTexture2D();
        GlStateManager.blendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(0,0,0,backgroundAlpha);
        RenderUtils.drawPlane(x,y-1,0,infoWidth + 2 + iconWidth + 2,18+2);
        GlStateManager.enableTexture2D();
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE);
        RenderUtils.applyColorWithAlpha(color);

        if (!leftSided)
        {
            ClientProxy.holoIcons.renderIcon(holoIcon, x + iconOffsetX, y + iconOffsetY,iconWidth,iconHeight);
            ClientProxy.moFontRender.drawString(info, x + iconWidth + 2 + iconOffsetX, y + iconWidth/2 - ClientProxy.moFontRender.FONT_HEIGHT/2 + iconOffsetY, color.getColor());
        }else
        {
            ClientProxy.moFontRender.drawString(info, x + iconOffsetX, y + iconWidth/2 - ClientProxy.moFontRender.FONT_HEIGHT/2 + iconOffsetY, color.getColor());
            ClientProxy.holoIcons.renderIcon(icon,x+infoWidth+2+iconOffsetX,y+iconOffsetY,iconWidth,iconHeight);
        }



        return infoWidth + 2 + iconWidth + 2;
    }

    private int renderAmmoBox(AndroidPlayer androidPlayer,int x,int y,boolean leftSided,Color baseColor)
    {
        if (androidPlayer.getPlayer() != null && androidPlayer.getPlayer().getHeldItem() != null && androidPlayer.getPlayer().getHeldItem().getItem() instanceof IWeapon)
        {
            float percent = (float)((IWeapon)androidPlayer.getPlayer().getHeldItem().getItem()).getAmmo(androidPlayer.getPlayer().getHeldItem()) / (float)((IWeapon)androidPlayer.getPlayer().getHeldItem().getItem()).getMaxAmmo(androidPlayer.getPlayer().getHeldItem());
            int energyPackCount = getEnergyPackCount(androidPlayer.getPlayer());
            return renderIconWithInfo("ammo",DecimalFormat.getPercentInstance().format(percent) + " | " + Integer.toString(energyPackCount), RenderUtils.lerp(Reference.COLOR_HOLO_RED, baseColor, percent),x,y,0,0,leftSided,18,18);
        }
        return 0;
    }

    private int getAmmoBoxWidth(AndroidPlayer androidPlayer)
    {
        if (androidPlayer.getPlayer() != null && androidPlayer.getPlayer().getHeldItem() != null && androidPlayer.getPlayer().getHeldItem().getItem() instanceof IWeapon)
        {
            float percent = (float)((IWeapon)androidPlayer.getPlayer().getHeldItem().getItem()).getAmmo(androidPlayer.getPlayer().getHeldItem()) / (float)((IWeapon)androidPlayer.getPlayer().getHeldItem().getItem()).getMaxAmmo(androidPlayer.getPlayer().getHeldItem());
            int energyPackCount = getEnergyPackCount(androidPlayer.getPlayer());
            return getWidthIconWithInfo(DecimalFormat.getPercentInstance().format(percent) + " | " + Integer.toString(energyPackCount),18);
        }
        return 0;
    }

    private int renderHeat(AndroidPlayer androidPlayer,int x,int y,boolean leftSided,Color baseColor)
    {
        if (androidPlayer.getPlayer() != null && androidPlayer.getPlayer().getHeldItem() != null && androidPlayer.getPlayer().getHeldItem().getItem() instanceof IWeapon)
        {
            if (((IWeapon)androidPlayer.getPlayer().getHeldItem().getItem()).getMaxHeat(androidPlayer.getPlayer().getHeldItem()) > 0) {
                float percent = ((IWeapon)androidPlayer.getPlayer().getHeldItem().getItem()).getHeat(androidPlayer.getPlayer().getHeldItem()) / ((IWeapon)androidPlayer.getPlayer().getHeldItem().getItem()).getMaxHeat(androidPlayer.getPlayer().getHeldItem());
                return renderIconWithPercent("temperature",percent,x,y,0,0,leftSided,baseColor,Reference.COLOR_HOLO_RED,18,18);
            }
        }
        return 0;
    }

    private int getHeatWidth(AndroidPlayer androidPlayer)
    {
        if (androidPlayer.getPlayer() != null && androidPlayer.getPlayer().getHeldItem() != null && androidPlayer.getPlayer().getHeldItem().getItem() instanceof IWeapon)
        {
            if (((IWeapon)androidPlayer.getPlayer().getHeldItem().getItem()).getMaxHeat(androidPlayer.getPlayer().getHeldItem()) > 0) {
                float percent = ((IWeapon)androidPlayer.getPlayer().getHeldItem().getItem()).getHeat(androidPlayer.getPlayer().getHeldItem()) / ((IWeapon)androidPlayer.getPlayer().getHeldItem().getItem()).getMaxHeat(androidPlayer.getPlayer().getHeldItem());
                return getWidthIconWithPercent(percent,18);
            }
        }
        return 0;
    }

    private int getEnergyPackCount(EntityPlayer entityPlayer)
    {
        int energyPackCount = 0;
        for (ItemStack stack : entityPlayer.inventory.mainInventory)
        {
            if (stack != null && stack.getItem() instanceof IEnergyPack)
            {
                energyPackCount += stack.stackSize;
            }
        }
        return energyPackCount;
    }

    @Override
    public int getWidth(ScaledResolution resolution,AndroidPlayer androidPlayer)
    {
        if (getPosition() == AndroidHudPosition.MIDDLE_CENTER)
            return 0;

        if (getPosition().y == 0.5)
        {
            return Math.max(getAmmoBoxWidth(androidPlayer)+16,getWidthIconWithPercent(1000,18));
        }
        return width;
    }

    @Override
    public int getHeight(ScaledResolution resolution,AndroidPlayer androidPlayer)
    {
        if (getPosition() == AndroidHudPosition.MIDDLE_CENTER)
            return 0;

        if (getPosition().y == 0.5)
        {
            return width;
        }
        if (androidPlayer.getPlayer() != null && androidPlayer.getPlayer().getHeldItem() != null && androidPlayer.getPlayer().getHeldItem().getItem() instanceof IWeapon)
        {
            return height + 20;
        }else
        {
            return height;
        }
    }
}
