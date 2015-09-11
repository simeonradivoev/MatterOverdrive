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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

/**
 * Created by Simeon on 9/8/2015.
 */
public abstract class AndroidHudElement implements IAndroidHudElement
{
    protected Minecraft mc;
    protected String name;
    protected int posX;
    protected int posY;
    protected int width;
    protected int height;
    protected GuiColor baseColor;
    protected AndroidHudPosition defaultPosition;
    protected AndroidHudPosition hudPosition;

    public AndroidHudElement(AndroidHudPosition defaultPosition,String name,int width,int height)
    {
        this.name = name;
        this.width = width;
        this.height = height;
        mc = Minecraft.getMinecraft();
        baseColor = Reference.COLOR_HOLO;
        hudPosition = this.defaultPosition = defaultPosition;
    }

    @Override
    public int getWidth(ScaledResolution resolution) {
        return width;
    }

    @Override
    public int getHeight(ScaledResolution resolution) {
        return height;
    }

    @Override
    public void setX(int x)
    {
        this.posX = x;
    }

    @Override
    public void setY(int y)
    {
        this.posY = y;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setBaseColor(GuiColor color) {
        this.baseColor = color;
    }

    public AndroidHudPosition getPosition()
    {
        return this.hudPosition;
    }

    public void setHudPosition(AndroidHudPosition position)
    {
        this.hudPosition = position;
    }

    public AndroidHudPosition getDefaultPosition(){return defaultPosition;}
}
