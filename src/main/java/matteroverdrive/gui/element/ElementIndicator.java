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
import matteroverdrive.gui.MOGuiBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by Simeon on 4/12/2015.
 */
public class ElementIndicator extends MOElementBase
{
    private int indication;
    public static final ResourceLocation BG = new ResourceLocation(Reference.PATH_ELEMENTS + "indicator.png");

    public ElementIndicator(MOGuiBase gui, int posX, int posY)
    {
        super(gui, posX, posY,21,5);
        this.texH = 15;
        this.texW = 21;
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
        GL11.glColor3f(1,1,1);
        gui.bindTexture(BG);
        gui.drawSizedTexturedModalRect(posX, posY, 0, 5 * indication, sizeX, sizeY,texW,texH);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {

    }

    public void setIndication(int indication)
    {
        this.indication = indication;
    }
}
