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
import matteroverdrive.container.slot.MOSlot;
import matteroverdrive.data.Inventory;

/**
 * Created by Simeon on 3/16/2015.
 */
public class ElementSlotsList extends ElementBaseGroup
{
    ElementInventorySlot mainSlot;
    int margin = 0;

    public ElementSlotsList(GuiBase gui, int posX, int posY, int width, int height, Inventory inventory, int main)
    {
        super(gui, posX, posY, width, height);
        int index = 0;
        for (int i = 0;i < inventory.getSizeInventory();i++)
        {
            if (inventory.getSlot(i).isMainSlot())
            {
                if (index == main)
                {
                    mainSlot = new ElementInventorySlot(gui, (MOSlot)gui.inventorySlots.getSlot(i),0, 0, 37, 22, "big_main", inventory.getSlot(i).getTexture());
                    mainSlot.setItemOffset(3, 3);
                    addElement(mainSlot);
                }
                else {

                    addElement(new ElementInventorySlot(gui,(MOSlot)gui.inventorySlots.getSlot(i), 0, 0, 22, 22, "big", inventory.getSlot(i).getTexture()));
                }

                index++;
            }
        }
    }

    @Override
    public void update(int mouseX, int mouseY)
    {
        super.update(mouseX, mouseY);

        int height = 0;
        for (int i = 0;i < elements.size();i++)
        {
            elements.get(i).setPosition(0,height);
            height += elements.get(i).getHeight() + margin;
        }
    }

    public void setMargin(int margin)
    {
        this.margin = margin;
    }

    public ElementSlot getMainSlot()
    {
        return mainSlot;
    }
}
