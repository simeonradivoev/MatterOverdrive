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

import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 9/10/2015.
 */
public class GuideElementDetails extends GuideElementAbstract
{
    List<String> details;


    @Override
    protected void loadContent(MOGuideEntry entry, Element element, int width, int height)
    {
        if(element.hasAttribute("item"))
        {
            ItemStack stack = shortCodeToStack(decodeShortcode(element.getAttribute("item")));
            if (stack != null && stack.getItem() != null && stack.getItem() instanceof MOBaseItem)
            {
                List<String> details = new ArrayList<>();
                ((MOBaseItem) stack.getItem()).addDetails(stack, Minecraft.getMinecraft().thePlayer, details);
                this.details = details;
                for (String detail : details)
                {
                    width = Math.max(getFontRenderer().getStringWidth(detail), width);
                }
                this.height = details.size()*getFontRenderer().FONT_HEIGHT;
            }
        }
    }

    @Override
    public void drawElement(int width, int mouseX, int mouseY)
    {
        for (int i = 0;i < details.size();i++)
        {
            getFontRenderer().drawString(details.get(i),marginLeft,marginTop + i * getFontRenderer().FONT_HEIGHT,color.getColor());
        }
    }
}
