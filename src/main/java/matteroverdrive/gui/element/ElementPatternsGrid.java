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

import matteroverdrive.data.ItemPattern;
import matteroverdrive.gui.MOGuiBase;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Simeon on 4/27/2015.
 */
public class ElementPatternsGrid extends ElementGrid
{
    String filter = "";

    public ElementPatternsGrid(MOGuiBase guiBase, int x, int y, int width, int height)
    {
        super(guiBase, x, y, width, height, width);
        setMargins(0,0,4,0);
    }


    public void updateStackList(List<ItemPattern> patterns)
    {
        ItemStack stack;
        elements.clear();

        for (ItemPattern pattern : patterns)
        {
            addElement(new ElementMonitorItemPattern(gui, pattern, gui));
        }
    }

    public void setFilter(String filter)
    {
        this.filter = filter;
    }

    public boolean shouldBeDisplayed(MOElementBase element)
    {
        if (element.getName() != null) {
            return element.getName().toLowerCase().contains(filter.toLowerCase());
        }
        return false;
    }
}
