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

package matteroverdrive.guide;

import matteroverdrive.util.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import org.w3c.dom.Element;

import java.util.Random;

/**
 * Created by Simeon on 8/31/2015.
 */
public class GuideElementPreview extends GuideElementAbstract
{
    private static Random random = new Random();
    double size = 1;
    ItemStack itemStack;

    @Override
    public void drawElement(int width,int mouseX,int mouseY)
    {
        if (itemStack != null)
        {
            GlStateManager.pushMatrix();

            if (textAlign == 1)
                GlStateManager.translate(width/2 - 8*size,0,0);
            else if (textAlign == 2)
                GlStateManager.translate(width - 16*size,0,0);

            GlStateManager.translate(marginLeft,marginTop,0);
            GlStateManager.scale(size, size, size);
            RenderUtils.renderStack(0, 0,0, itemStack,false);
            GlStateManager.popMatrix();
        }
    }

    @Override
    protected void loadContent(MOGuideEntry entry, Element element, int width, int height) {
        if (element.hasAttribute("item"))
        {
            itemStack = shortCodeToStack(decodeShortcode(element.getAttribute("item")));
        }else if (entry.getStackIcons() != null && entry.getStackIcons().length > 0)
        {
            int index = random.nextInt(entry.getStackIcons().length);
            if (element.hasAttribute("index"))
            {
                index = Integer.parseInt(element.getAttribute("index"));
            }
            index = MathHelper.clamp_int(index,0,entry.getStackIcons().length-1);
            itemStack = entry.getStackIcons()[index];
        }
        if (element.hasAttribute("size"))
        {
            size = Double.parseDouble(element.getAttribute("size"));
        }
        this.height = (int)(16*size);
        this.width = (int)(16*size);
    }
}
