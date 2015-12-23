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

package matteroverdrive.gui.element;

import matteroverdrive.client.data.Color;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.container.IButtonHandler;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.RenderUtils;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 6/17/2015.
 */
public class MOElementIconButton extends MOElementButton
{
    HoloIcon icon;
    Color iconColor;

    public MOElementIconButton(MOGuiBase gui, IButtonHandler handler, int posX, int posY, String name, int sheetX, int sheetY, int hoverX, int hoverY, int disabledX, int disabledY, int sizeX, int sizeY, String texture, HoloIcon icon) {
        super(gui, handler, posX, posY, name, sheetX, sheetY, hoverX, hoverY, disabledX, disabledY, sizeX, sizeY, texture);
        this.icon = icon;
    }

    public MOElementIconButton(MOGuiBase gui, IButtonHandler handler, int posX, int posY, String name, int sheetX, int sheetY, int hoverX, int hoverY, int sizeX, int sizeY, String texture,HoloIcon icon) {
        super(gui, handler, posX, posY, name, sheetX, sheetY, hoverX, hoverY, sizeX, sizeY, texture);
        this.icon = icon;
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        if (icon != null)
        {
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            ClientProxy.holoIcons.bindSheet();
            if (iconColor != null)
                RenderUtils.applyColor(iconColor);
            else
                GL11.glColor3f(1,1,1);
            ClientProxy.holoIcons.renderIcon(icon,posX - icon.getOriginalWidth()/2 + sizeX/2,posY - icon.getOriginalHeight()/2 + sizeY/2);
        }
    }

    public void setIconColor(Color iconColor)
    {
        this.iconColor = iconColor;
    }

    public void setIcon(HoloIcon icon)
    {
        this.icon = icon;
    }
}
