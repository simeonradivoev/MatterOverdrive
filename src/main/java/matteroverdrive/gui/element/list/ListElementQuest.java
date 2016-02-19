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

package matteroverdrive.gui.element.list;

import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.gui.element.IMOListBoxElement;
import matteroverdrive.gui.element.MOElementListBox;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 11/19/2015.
 */
public class ListElementQuest implements IMOListBoxElement
{
    private QuestStack questStack;
    private EntityPlayer entityPlayer;
    private int width;

    public ListElementQuest(EntityPlayer entityPlayer,QuestStack questStack,int width)
    {
        this.questStack = questStack;
        this.entityPlayer = entityPlayer;
        this.width = width;
    }

    @Override
    public String getName() {
        return questStack.getTitle(entityPlayer);
    }

    @Override
    public int getHeight() {
        return Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT+6;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public Object getValue() {
        return questStack;
    }

    @Override
    public void draw(MOElementListBox listBox, int x, int y, int backColor, int textColor, boolean selected, boolean BG)
    {

        int textWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(getName());
        if (selected) {
            Minecraft.getMinecraft().fontRendererObj.drawString("\u2023 " + getName(), x + width / 2 - textWidth / 2 - 8, y, textColor);
        }else
        {
            Minecraft.getMinecraft().fontRendererObj.drawString(getName(), x + width / 2 - textWidth / 2, y, textColor);
        }
    }

    @Override
    public void drawToolTop(MOElementListBox listBox, int x, int y)
    {

    }
}
