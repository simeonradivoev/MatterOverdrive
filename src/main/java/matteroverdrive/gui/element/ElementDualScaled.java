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

import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.util.RenderUtils;

import java.util.List;

/**
 * Created by Simeon on 12/23/2015.
 */
public class ElementDualScaled extends MOElementBase
{
    public int quantity;
    public int mode;
    public boolean background = true;

    public ElementDualScaled(MOGuiBase gui, int posX, int posY) {

        super(gui, posX, posY);
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

    public ElementDualScaled setBackground(boolean background) {

        this.background = background;
        return this;
    }

    public ElementDualScaled setMode(int mode) {

        this.mode = mode;
        return this;
    }

    public ElementDualScaled setQuantity(int quantity) {

        this.quantity = quantity;
        return this;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks) {

        RenderUtils.bindTexture(texture);

        if (background) {
            drawTexturedModalRect(posX, posY, 0, 0, sizeX, sizeY);
        }
        switch (mode) {
            case 0:
                // vertical bottom -> top
                drawTexturedModalRect(posX, posY + sizeY - quantity, sizeX, sizeY - quantity, sizeX, quantity);
                return;
            case 1:
                // horizontal left -> right
                drawTexturedModalRect(posX, posY, sizeX, 0, quantity, sizeY);
                return;
            case 2:
                // horizontal right -> left
                drawTexturedModalRect(posX + sizeX - quantity, posY, sizeX + sizeX - quantity, 0, quantity, sizeY);
                return;
        }
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {

    }
}
