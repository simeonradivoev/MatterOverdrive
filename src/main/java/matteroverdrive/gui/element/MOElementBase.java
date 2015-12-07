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

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.GuiColor;
import cofh.lib.gui.element.ElementBase;
import matteroverdrive.util.RenderUtils;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by Simeon on 4/8/2015.
 */
public abstract class MOElementBase extends ElementBase
{
    private GuiColor color = new GuiColor(255,255,255);
    public MOElementBase parent;
    public MOElementBase(GuiBase gui, int posX, int posY)
    {
        super(gui, posX, posY);
    }
    public MOElementBase(GuiBase gui, int posX, int posY, int width, int height) {super(gui,posX,posY,width,height);}

    public void updateInfo()
    {

    }

    public void init()
    {

    }

    public void addTooltip(List<String> var1,int mouseX,int mouseY)
    {
        addTooltip(var1);
    }

    public GuiColor getColor() {
        return color;
    }

    public void setColor(int r,int g,int b,int alpha)
    {
        this.color = new GuiColor(r,g,b,alpha);
    }

    public void setColor(GuiColor color)
    {
        this.color = color;
    }

    protected void ApplyColor()
    {
        if (color != null) {
            RenderUtils.applyColor(color);
        }
    }

    protected void ResetColor()
    {
        GL11.glColor3f(1, 1, 1);
    }

    protected int getGlobalX()
    {
        int x = posX;

        if (parent != null)
        {
            x += parent.getGlobalX();
        }
        return x;
    }

    protected int getGlobalY()
    {
        int y = posY;

        if (parent != null)
        {
            y += parent.getGlobalY();
        }
        return y;
    }
}
