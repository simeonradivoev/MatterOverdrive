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

package matteroverdrive.gui;

import cofh.lib.gui.GuiBase;
import matteroverdrive.Reference;
import matteroverdrive.data.ScaleTexture;
import matteroverdrive.gui.element.ElementSlot;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by Simeon on 11/7/2015.
 */
public class GuiTritaniumCrate extends GuiBase
{
    ScaleTexture background;

    public GuiTritaniumCrate(IInventory upperChestInventory, IInventory lowerChestInventory) {
        super(new ContainerChest(upperChestInventory, lowerChestInventory));
        background = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "base_gui_hotbar.png"),92,77);
        background.setOffsets(57, 34, 42, 34);
        this.xSize = 256;
        this.ySize = 256;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        for (Slot slot : (List<Slot>)inventorySlots.inventorySlots)
        {
            ElementSlot s = new ElementSlot(this,guiLeft + slot.xDisplayPosition-1,guiTop + slot.yDisplayPosition-1,18,18,"small");
            addElement(s);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int x, int y) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        //RenderUtils.drawSizeableBackground(guiLeft, guiTop, xSize, ySize, texW, texH, texture, this.zLevel, 57);
        background.render(guiLeft,guiTop,xSize,ySize);

        mouseX = x - guiLeft;
        mouseY = y - guiTop;

        drawElements(partialTick, false);
        drawTabs(partialTick, false);
    }
}
