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

import matteroverdrive.Reference;
import matteroverdrive.client.data.Color;
import matteroverdrive.gui.MOGuiBase;
import net.minecraft.util.ResourceLocation;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 5/17/2015.
 */
public class ElementDoubleCircleBar extends MOElementBase
{
    public static final ResourceLocation BG = new ResourceLocation(Reference.PATH_ELEMENTS + "circle_bar.png");
    public static final ResourceLocation OVERLAY = new ResourceLocation(Reference.PATH_ELEMENTS + "circle_bar_top.png");
    public static final ResourceLocation BACK = new ResourceLocation(Reference.PATH_ELEMENTS + "circle_bar_bottom.png");
    private float progressLeft;
    private float progressRight;

    private Color colorLeft;
    private Color colorRight;

    public ElementDoubleCircleBar(MOGuiBase gui, int posX, int posY, int width, int height, Color color)
    {
        super(gui, posX, posY, width, height);
        colorLeft = color;
        colorRight = color;
    }

    @Override
    public void updateInfo()
    {

    }

    @Override
    public void init()
    {

    }

    @Override
    public void addTooltip(List<String> var1, int mouseX, int mouseY)
    {

    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        gui.bindTexture(BACK);
        glColor4f(1,1,1,1);
        gui.drawSizedTexturedModalRect(posX,posY,0,0,sizeX,sizeY,135,135);

        //left
        gui.bindTexture(BG);
        glAlphaFunc(GL_GREATER,1 - progressLeft);
        if (colorLeft != null)
        glColor4f(colorLeft.getFloatR(),colorLeft.getFloatG(),colorLeft.getFloatB(),colorLeft.getFloatA());
        gui.drawSizedTexturedModalRect(posX,posY,0,0,sizeX / 2,sizeY,135,135);
        glAlphaFunc(GL_GREATER,0.2f);

        //right
        gui.bindTexture(BG);
        glAlphaFunc(GL_GREATER,1 - progressRight);
        if (colorRight != null)
            glColor4f(colorRight.getFloatR(),colorRight.getFloatG(),colorRight.getFloatB(),colorRight.getFloatA());
        gui.drawSizedTexturedModalRect(posX + 135 / 2,posY,135/2,0,sizeX / 2,sizeY,135,135);
        glAlphaFunc(GL_GREATER,0.2f);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        glDisable(GL_ALPHA_TEST);
        glEnable(GL_BLEND);
        gui.bindTexture(OVERLAY);
        glColor4f(1,1,1,1);
        gui.drawSizedTexturedModalRect(posX,posY,0,0,sizeX,sizeY,135,135);
        glDisable(GL_BLEND);
        glEnable(GL_ALPHA_TEST);
    }

    public void setProgressLeft(float progressLeft)
    {
        this.progressLeft = progressLeft;
    }

    public void setProgressRight(float progressRight)
    {
        this.progressRight = progressRight;
    }

    public void setColorLeft(Color color)
    {
        this.colorLeft = color;
    }

    public void setColorRight(Color color)
    {
        this.colorRight = color;
    }

    public float getProgressLeft()
    {
        return progressLeft;
    }

    public float getProgressRight()
    {
        return progressRight;
    }
}
