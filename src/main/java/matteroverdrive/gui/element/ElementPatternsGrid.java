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

import matteroverdrive.api.matter.IMatterDatabase;
import matteroverdrive.data.BlockPos;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.util.MatterDatabaseHelper;
import matteroverdrive.util.MatterHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

import java.util.HashSet;

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


    public void updateStackList(HashSet<BlockPos> positions)
    {
        ItemStack stack;
        elements.clear();

        for (BlockPos position : positions)
        {
            TileEntity entity = position.getTileEntity(Minecraft.getMinecraft().theWorld);

            if (entity instanceof IMatterDatabase) {
                NBTTagList list = ((IMatterDatabase) entity).getItemsAsNBT();

                if (list != null) {

                    for (int p = 0; p < list.tagCount(); p++)
                    {
                        stack = MatterDatabaseHelper.GetItemStackFromNBT(list.getCompoundTagAt(p));
                        if (MatterHelper.getMatterAmountFromItem(stack) > 0) {
                            addElement(new ElementMonitorItemPattern(gui, list.getCompoundTagAt(p), (MOGuiBase) gui));
                        }
                    }
                }
            }
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
