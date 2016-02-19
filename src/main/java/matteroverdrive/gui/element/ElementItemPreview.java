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
import matteroverdrive.data.ScaleTexture;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by Simeon on 3/26/2015.
 */
public class ElementItemPreview extends MOElementBase
{
    ScaleTexture background = new ScaleTexture(new ResourceLocation(Reference.PATH_ELEMENTS + "item_preview_bg.png"),40,48).setOffsets(22,22,18,18);
    ItemStack itemStack;
    float itemSize = 2;
    boolean renderOverlay;
    boolean drawTooltip;

    public ElementItemPreview(MOGuiBase gui, int posX, int posY, ItemStack itemStack)
    {
        super(gui, posX, posY);
        this.sizeX = 47;
        this.sizeY = 47;
        this.itemStack = itemStack;
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
    public void addTooltip(List<String> tooltip,int mouseX,int mouseY)
    {
        tooltip.addAll(itemStack.getTooltip(Minecraft.getMinecraft().thePlayer,Minecraft.getMinecraft().gameSettings.advancedItemTooltips));
    }

    public void setItemSize(float itemSize)
    {
        this.itemSize = itemSize;
    }

    public void setItemStack(ItemStack itemStack)
    {
        this.itemStack = itemStack;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        if (background != null)
        {
            background.render(posX,posY,sizeX,sizeY);
        }
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        if(itemStack != null)
        {
            GlStateManager.pushMatrix();
            GL11.glTranslatef(this.posX + sizeX/2 - 9*itemSize,this.posY + sizeY/2 - 9*itemSize,0);
            GlStateManager.scale(itemSize,itemSize,itemSize);
            RenderUtils.renderStack(0, 0,32,itemStack,renderOverlay);
            GlStateManager.popMatrix();
        }
    }

    public void setRenderOverlay(boolean renderOverlay)
    {
        this.renderOverlay = renderOverlay;
    }

    public void setDrawTooltip(boolean drawTooltip)
    {
        this.drawTooltip = drawTooltip;
    }

    public void setBackground(ScaleTexture background)
    {
        this.background = background;
    }
}
