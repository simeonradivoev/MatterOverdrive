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
import matteroverdrive.Reference;
import matteroverdrive.entity.AndroidPlayer;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
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
    public void drawElement(AndroidPlayer androidPlayer,int mouseX, int mouseY, ScaledResolution resolution, float ticks)
    {
        glDisable(GL_ALPHA_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);

        RenderUtils.applyColorWithAlpha(baseColor);
        mc.renderEngine.bindTexture(top_element_bg);
        RenderUtils.drawPlane(0, 10,0,174,11);

        double energy_perc = (double) androidPlayer.getEnergyStored() / (double) androidPlayer.getMaxEnergyStored();
        double health_perc = androidPlayer.getPlayer().getHealth() / androidPlayer.getPlayer().getMaxHealth();
        int x = 12;

        //region Health
        GuiColor healthColor = RenderUtils.lerp(Reference.COLOR_HOLO_RED, baseColor, (float) Math.min(health_perc,1));
        RenderUtils.applyColorWithAlpha(healthColor);
        ClientProxy.holoIcons.renderIcon("health", x, 22);
        x += 18;
        String info = DecimalFormat.getPercentInstance().format(health_perc);
        mc.fontRenderer.drawString(info, 32, 28, healthColor.getColor());
        x += mc.fontRenderer.getStringWidth(info) + 5;
        //endregion

        //region energy
        GuiColor energyColor = RenderUtils.lerp(Reference.COLOR_HOLO_RED, baseColor, (float) energy_perc);
        RenderUtils.applyColorWithAlpha(energyColor);
        ClientProxy.holoIcons.renderIcon("battery", x, 20, 22, 22);
        x += 22;
        info = DecimalFormat.getPercentInstance().format(energy_perc);
        mc.fontRenderer.drawString(info, x, 28, energyColor.getColor());
        x += mc.fontRenderer.getStringWidth(info) + 5;
        //endregion

        //region speed
        RenderUtils.applyColorWithAlpha(baseColor);
        ClientProxy.holoIcons.renderIcon("person",x,22,18,18);
        x += 18;
        info = DecimalFormat.getPercentInstance().format(androidPlayer.getSpeedMultiply());
        mc.fontRenderer.drawString(info, x, 28, baseColor.getColor());
        //endregion

        glEnable(GL_ALPHA_TEST);
    }
}
