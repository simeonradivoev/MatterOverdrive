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

import net.minecraft.client.renderer.GlStateManager;
import org.w3c.dom.Element;

/**
 * Created by Simeon on 8/30/2015.
 */
public class GuideElementTitle extends GuideElementTextAbstract
{
    String title;
    double size = 3;

    @Override
    public void drawElement(int width,int mouseX,int mouseY)
    {
        GlStateManager.pushMatrix();
        int titleWidth = (int)(getFontRenderer().getStringWidth(title) * size);
        int x = 0;
        if (textAlign == 1)
        {
            x = (width-marginLeft-marginRight)/2 - titleWidth/2;
        }
        else if (textAlign == 2)
        {
            x = (width-marginLeft-marginRight) - titleWidth;
        }
        GlStateManager.translate(x + marginLeft, marginTop, 0);
        GlStateManager.scale(size, size, size);
        getFontRenderer().drawString(title, 0, 0, color.getColor());
        GlStateManager.popMatrix();
    }

    @Override
    protected void loadContent(MOGuideEntry entry, Element element, int width, int height)
    {
        title = handleVariables(element.getTextContent(),entry);
        if (element.hasAttribute("size"))
        {
            size = Double.parseDouble(element.getAttribute("size"));
        }

        this.width = width;
        this.height = (int)(getFontRenderer().FONT_HEIGHT * size);
    }
}
