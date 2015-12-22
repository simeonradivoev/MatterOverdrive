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

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.android.IBionicStat;
import matteroverdrive.client.data.Color;
import matteroverdrive.entity.player.AndroidPlayer;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 9/9/2015.
 */
public class AndroidHudBionicStats extends AndroidHudElement
{
    public static final int STATS_PER_ROW = 6;
    private int lastHeightCount = 0;

    public AndroidHudBionicStats(AndroidHudPosition position,String name)
    {
        super(position,name, 174, 0);
    }

    @Override
    public boolean isVisible(AndroidPlayer android) {
        return true;
    }

    @Override
    public void drawElement(AndroidPlayer android, int mouseX, int mouseY, ScaledResolution resolution, float ticks)
    {
        int count = 0;
        for (int i = 0; i < android.getSizeInventory(); i++) {
            if (android.getStackInSlot(i) != null) {
                drawAndroidPart(android.getStackInSlot(i), baseColor, getX(count,resolution,android), getY(count,resolution,android));
                count++;
            }
        }

        for (Object object : android.getUnlocked().func_150296_c()) {
            IBionicStat stat = MatterOverdrive.statRegistry.getStat(object.toString());
            if (stat != null) {
                int level = android.getUnlockedLevel(stat);
                if (stat.showOnHud(android, level))
                {
                    if (!stat.isEnabled(android,level))
                    {
                        drawBioticStat(stat,android, level, Reference.COLOR_HOLO_RED, getX(count,resolution,android), getY(count,resolution,android));
                    }else
                    {
                        drawBioticStat(stat,android, level, baseColor, getX(count,resolution,android), getY(count,resolution,android));
                    }

                    count++;
                }
            }
        }


        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA,GL_ONE);

        if (getPosition().y == 1)
        {
            mc.renderEngine.bindTexture(AndroidHudStats.top_element_bg);
            RenderUtils.drawPlane(12 - 24 * getPosition().x, Math.ceil((count/(double)STATS_PER_ROW))*24 + 4, 0, 174, 11);
        }
        else if (getPosition().y == 0.5)
        {
            glPushMatrix();
            glTranslated(22+(getWidth(resolution,android)-24)*getPosition().x,0,0);
            glRotated(90,0,0,1);
            mc.renderEngine.bindTexture(AndroidHudStats.top_element_bg);
            RenderUtils.drawPlane(0, 0, 0, 174, 11);
            glPopMatrix();
        }else
        {
            mc.renderEngine.bindTexture(AndroidHudStats.top_element_bg);
            RenderUtils.drawPlane(12 - 24 * getPosition().x, 10, 0, 174, 11);
        }

        lastHeightCount = count;
    }

    private int getTotalElementCount(AndroidPlayer android)
    {
        int count = 0;
        for (int i = 0; i < android.getSizeInventory(); i++) {
            if (android.getStackInSlot(i) != null) {
                count++;
            }
        }

        for (Object object : android.getUnlocked().func_150296_c()) {
            IBionicStat stat = MatterOverdrive.statRegistry.getStat(object.toString());
            if (stat != null) {
                int level = android.getUnlockedLevel(stat);
                if (stat.showOnHud(android, level))
                {
                    count++;
                }
            }
        }
        return count;
    }

    private void drawAndroidPart(ItemStack stack, Color color, int x, int y)
    {
        drawNormalBG(color, x, y);
        glEnable(GL_BLEND);
        glColor4f(1, 1, 1, 0.5f);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        RenderUtils.renderStack(x + 3, y + 3, stack);
        glDisable(GL_BLEND);
    }

    private void drawBioticStat(IBionicStat stat,AndroidPlayer androidPlayer,int level,Color color,int x,int y)
    {
        if (stat.isActive(androidPlayer,level))
            drawActiveBG(color,x,y);
        else
            drawNormalBG(color, x, y);
        glEnable(GL_BLEND);
        ClientProxy.holoIcons.renderIcon(stat.getIcon(level),x +2,y + 2,18,18);
        if (stat.getDelay(androidPlayer,level) > 0)
        {
            String delay = MOStringHelper.formatRemainingTime(stat.getDelay(androidPlayer, level)/20f,true);
            int delayWidth = mc.fontRenderer.getStringWidth(delay);
            mc.fontRenderer.drawString(delay, x + 22 - delayWidth, y + 22 - mc.fontRenderer.FONT_HEIGHT - 1, Reference.COLOR_HOLO.getColor());
        }
        glDisable(GL_BLEND);
    }

    private void drawNormalBG(Color color,int x,int y)
    {
        glDisable(GL_ALPHA_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
        glColor4f(0,0,0,backgroundAlpha);
        ClientProxy.holoIcons.renderIcon("android_feature_icon_bg_black", x, y, 22, 22);

        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        RenderUtils.applyColorWithAlpha(color);
        ClientProxy.holoIcons.renderIcon("android_feature_icon_bg", x, y, 22, 22);
        glDisable(GL_BLEND);
        glEnable(GL_ALPHA_TEST);
    }

    private void drawActiveBG(Color color,int x,int y)
    {
        glDisable(GL_ALPHA_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
        glColor4f(0,0,0,backgroundAlpha);
        ClientProxy.holoIcons.renderIcon("android_feature_icon_bg_black", x, y, 22, 22);
        glBlendFunc(GL_SRC_ALPHA,GL_ONE);
        RenderUtils.applyColorWithAlpha(color);
        ClientProxy.holoIcons.renderIcon("android_feature_icon_bg_active", x, y, 22, 22);
        glDisable(GL_BLEND);
        glEnable(GL_ALPHA_TEST);
    }

    private int getX(int count,ScaledResolution resolution,AndroidPlayer androidPlayer)
    {
        if (getPosition().y == 0.5)
        {
            return Math.floorDiv(count,(getHeight(resolution,androidPlayer) / 24)) * 24 + 22 - (int)(44 * getPosition().x);
        }
        else
        {
            return 24 * (count % (getWidth(resolution, androidPlayer) / 24)) + 12 - (int) (22 * getPosition().x);
        }
    }

    private int getY(int count,ScaledResolution resolution,AndroidPlayer androidPlayer)
    {
        if (getPosition().y == 0.5)
        {
            return 24 * (count % (getHeight(resolution,androidPlayer) / 24));
        }else
        {
            return Math.floorDiv(count,(getWidth(resolution,androidPlayer) / 24)) * 24 + 22 - (int) (22 * getPosition().y);
        }
    }

    @Override
    public int getHeight(ScaledResolution resolution,AndroidPlayer androidPlayer)
    {
        if (getPosition().y == 0.5)
        {
            return width;
        }
        else
        {
            int count = getTotalElementCount(androidPlayer);
            return (int) Math.ceil(count * 24d / width)*24 + (int)(24*getPosition().y);
        }

    }

    @Override
    public int getWidth(ScaledResolution resolution,AndroidPlayer androidPlayer)
    {
        if (getPosition().y == 0.5)
        {
            int count = getTotalElementCount(androidPlayer);
            return  (int) Math.ceil((count * 24d) / width) * 24;
        }
        else
        {
            return width;
        }
    }
}
