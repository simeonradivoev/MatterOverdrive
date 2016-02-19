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

package matteroverdrive.guide.infograms;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.guide.GuideElementAbstract;
import matteroverdrive.guide.MOGuideEntry;
import matteroverdrive.util.MOLog;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.w3c.dom.Element;

/**
 * Created by Simeon on 8/29/2015.
 */
public class InfogramCreates extends GuideElementAbstract
{
    private static final ResourceLocation background = new ResourceLocation(Reference.PATH_ELEMENTS + "guide_info_creates.png");
    ItemStack from;
    ItemStack to;

    @Override
    public void drawElement(int width,int mouseX,int mouseY)
    {
        bindTexture(background);
        GlStateManager.color(1,1,1);
        RenderUtils.drawPlane(marginLeft,marginTop,0,115,36);
        if (from != null)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(marginLeft + 5,marginTop + 5,0);
            GlStateManager.scale(1.5,1.5,1.5);
            RenderUtils.renderStack(0, 0, from);
            GlStateManager.popMatrix();
        }
        if (to != null)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(marginLeft + 86, marginTop + 5, 0);
            GlStateManager.scale(1.5, 1.5, 1.5);
            RenderUtils.renderStack(0, 0, to);
            GlStateManager.popMatrix();
        }
    }

    @Override
    protected void loadContent(MOGuideEntry entry, Element element, int width, int height)
    {
        if (element.hasAttribute("to"))
        {
            to = shortCodeToStack(decodeShortcode(element.getAttribute("to")));
            if (to == null)
            {
                MOLog.warn("Invalid to Itemstack in infogram of type create: %s",element.hasAttribute("to"));
            }
        }
        else
        {
            MOLog.warn("There is no to Itemstack in infogram of type create");
        }

        if (element.hasAttribute("from"))
        {
            from = shortCodeToStack(decodeShortcode(element.getAttribute("from")));
            if (from == null)
            {
                MOLog.warn("Invalid from Itemstack in infogram of type create: %",element.getAttribute("from"));
            }
        }else
        {
            if (entry.getStackIcons().length > 0)
                from = entry.getStackIcons()[0];
        }
        this.height = 36+16;
        this.width = 100;
    }
}
