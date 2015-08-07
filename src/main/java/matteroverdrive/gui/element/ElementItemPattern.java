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
import matteroverdrive.data.ScaleTexture;
import matteroverdrive.util.MatterDatabaseHelper;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by Simeon on 4/27/2015.
 */
public class ElementItemPattern extends ElementSlot
{
    ScaleTexture texture;
    NBTTagCompound tagCompound;
    ItemStack itemStack;
    int amount = 0;

    public ElementItemPattern(GuiBase gui,NBTTagCompound tagCompound,String bgType,int width,int height)
    {
        super(gui, 0, 0, width, height, bgType);
        this.texture = new ScaleTexture(getTexture(bgType),width,height).setOffsets(2,2,2,2);
        this.tagCompound = tagCompound;
        itemStack = MatterDatabaseHelper.GetItemStackFromNBT(tagCompound);
        this.name = "";
        if (tagCompound != null) {
            if (itemStack != null)
                this.name = itemStack.getDisplayName();
        }
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        if (tagCompound != null)
        {
            RenderUtils.renderStack(posX + 3, posY + 3, itemStack);

            if (amount > 0) {
                GL11.glPushMatrix();
                GL11.glTranslatef(0, 0, 100);
                gui.drawCenteredString(getFontRenderer(), Integer.toString(amount), posX + 17, posY + 12, 0xFFFFFF);
                GL11.glPopMatrix();
            }
            RenderHelper.enableGUIStandardItemLighting();
        }
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        texture.Render(posX, posY, sizeX, sizeY);
    }

    @Override
    public void addTooltip(List<String> list)
    {
        if (tagCompound != null)
        {
            if (itemStack != null) {
                list.addAll(itemStack.getTooltip(Minecraft.getMinecraft().thePlayer, false));
                String name = list.get(0);
                int progress = MatterDatabaseHelper.GetProgressFromNBT(tagCompound);
                name = MatterDatabaseHelper.getPatternInfoColor(progress) + name + " [" + progress + "%]";
                list.set(0, name);
            }
        }
    }

    public NBTTagCompound getTagCompound()
    {
        return tagCompound;
    }

    public void setTagCompound(NBTTagCompound tagCompound)
    {
        this.tagCompound = tagCompound;
        itemStack = MatterDatabaseHelper.GetItemStackFromNBT(tagCompound);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    public ScaleTexture getTexture()
    {
        return texture;
    }
}
