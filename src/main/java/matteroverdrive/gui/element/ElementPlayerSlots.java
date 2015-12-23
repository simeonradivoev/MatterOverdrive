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

import java.util.List;

/**
 * Created by Simeon on 3/16/2015.
 */
public class ElementPlayerSlots extends MOElementBase
{
    public ElementPlayerSlots(MOGuiBase gui, int posX, int posY)
    {
        super(gui, posX, posY);
        this.setTexture(Reference.PATH_ELEMENTS + "slot_bg.png",18,18);
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
        gui.bindTexture(texture);

        for(int i = 0; i < 3; i++)
        {
            for(int j = 0;j < 9;j++)
            {
                drawTexturedModalRect(posX + j * 18,posY + i * 18,0,0,18,18);
            }
        }

        for(int i = 0;i < 9;i++)
        {
            drawTexturedModalRect(posX + i * 18,posY + 58,0,0,18,18);
        }
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {

    }
}
