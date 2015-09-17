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

import cofh.lib.gui.GuiColor;
import cofh.lib.render.RenderHelper;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.android.IBionicStat;
import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.proxy.ClientProxy;
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
                drawAndroidPart(android.getStackInSlot(i), baseColor, getX(count), getY(count));
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
                        drawBioticStat(stat,android, level, Reference.COLOR_HOLO_RED, getX(count), getY(count));
                    }else
                    {
                        drawBioticStat(stat,android, level, baseColor, getX(count), getY(count));
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
            RenderUtils.drawPlane(0, Math.ceil((count/(double)STATS_PER_ROW))*24 + 4, 0, 174, 11);
        }
        else
        {
            mc.renderEngine.bindTexture(AndroidHudStats.top_element_bg);
            RenderUtils.drawPlane(0, 10, 0, 174, 11);
        }

        lastHeightCount = count;
    }

    private void drawAndroidPart(ItemStack stack,GuiColor color,int x,int y)
    {
        drawNormalBG(color, x, y);
        glEnable(GL_BLEND);
        glColor4f(1, 1, 1, 0.5f);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        RenderHelper.bindItemTexture(stack);
        RenderUtils.renderStack(x + 3, y + 3, stack);
        glDisable(GL_BLEND);
    }

    private void drawBioticStat(IBionicStat stat,AndroidPlayer androidPlayer,int level,GuiColor color,int x,int y)
    {
        if (stat.isActive(androidPlayer,level))
            drawActiveBG(color,x,y);
        else
            drawNormalBG(color, x, y);
        glEnable(GL_BLEND);
        ClientProxy.holoIcons.bindSheet();
        RenderHelper.renderIcon(x + 2, y + 2, 0, stat.getIcon(level), 18, 18);
        glDisable(GL_BLEND);
    }

    private void drawNormalBG(GuiColor color,int x,int y)
    {
        glDisable(GL_ALPHA_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        RenderUtils.applyColorWithAlpha(color);
        ClientProxy.holoIcons.renderIcon("android_feature_icon_bg", x, y, 22, 22);
        glDisable(GL_BLEND);
        glEnable(GL_ALPHA_TEST);
    }

    private void drawActiveBG(GuiColor color,int x,int y)
    {
        glDisable(GL_ALPHA_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA,GL_ONE);
        RenderUtils.applyColorWithAlpha(color);
        ClientProxy.holoIcons.renderIcon("android_feature_icon_bg_active", x, y, 22, 22);
        glDisable(GL_BLEND);
        glEnable(GL_ALPHA_TEST);
    }

    private int getX(int count)
    {
        if (getPosition().x == 1) {
            return (int) (getPosition().x * (174 - 24)) - (12 + (24 * (count % STATS_PER_ROW)));
        }else if (getPosition().x == 0)
        {
            return 12 + (24 * (count % STATS_PER_ROW));
        }else
        {
            return (24 * STATS_PER_ROW) - (12 + (24 * (count % STATS_PER_ROW)));
        }
    }

    private int getY(int count)
    {
        if (getPosition().y == 1)
        {
            return -24 * (count / STATS_PER_ROW-2)-20;
        }else
        {
            return 23 + 24 * (count / STATS_PER_ROW);
        }
    }

    @Override
    public int getHeight(ScaledResolution resolution)
    {
        return 23 + 24 * (int)Math.ceil(lastHeightCount / (double)STATS_PER_ROW) + 24;
    }
}
