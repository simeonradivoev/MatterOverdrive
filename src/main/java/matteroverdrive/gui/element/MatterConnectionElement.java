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
import matteroverdrive.Reference;
import matteroverdrive.util.RenderUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by Simeon on 3/14/2015.
 */
public class MatterConnectionElement extends MOElementBase
{
    public static final ResourceLocation texture = new ResourceLocation(Reference.PATH_ELEMENTS + "side_slot_bg.png");

    int id;
    int count;

    @Override
    public void addTooltip(List<String> list)
    {
        list.add(StatCollector.translateToLocal(Item.getItemById(id).getUnlocalizedName() + ".name") + " [" + count + "]");
    }

    public MatterConnectionElement(GuiBase gui,int id,int count)
    {
        this(gui, 22, 22, id, count);
    }

    public MatterConnectionElement(GuiBase gui, int width, int height,int id,int count) {
        super(gui,0,0, width, height);

        this.id = id;
        this.count = count;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        GL11.glColor3f(1, 1, 1);
        RenderUtils.bindTexture(texture);
        gui.drawSizedTexturedModalRect(posX, posY, 0, 0, 22, 22, 22, 22);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        RenderUtils.renderStack(posX + 2, posY + 2, new ItemStack(Item.getItemById(id)));
        gui.getFontRenderer().drawStringWithShadow(Integer.toString(count),posX + 8,posY + 24, 0xFFFFFF);
    }
}
