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

import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.util.RenderUtils;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.client.renderer.GlStateManager;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 12/6/2015.
 */
public class ElementScrollGroup extends ElementBaseGroup
{
    int contentTotalHeight;
    int scroll;
    float scrollSmooth;
    int scrollSpeed = 10;
    int scrollerColor;

    public ElementScrollGroup(MOGuiBase gui, int posX, int posY, int width, int height)
    {
        super(gui, posX, posY, width, height);
    }

    private void manageDrag(int maxHeight)
    {
        scrollSmooth = MOMathHelper.Lerp(scrollSmooth,scroll,0.1f);
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float gameTicks)
    {
        contentTotalHeight = 0;
        for (MOElementBase element : elements)
        {
            element.setPosition(element.getPosX(), Math.round(contentTotalHeight + scrollSmooth));
            element.setVisible(true);
            contentTotalHeight += element.getHeight();
        }

        manageDrag(Math.max(0,contentTotalHeight-sizeY));

        RenderUtils.beginStencil();
        drawStencil(posX, posY, posX + sizeX, posY + sizeY, 1);
        super.drawBackground(mouseX, mouseY, gameTicks);
        RenderUtils.endStencil();
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        RenderUtils.beginDrawingDepthMask();
        RenderUtils.drawPlane(posX,posY,100,sizeX,sizeY);
        //drawStencil(posX, posY, sizeX + posX, sizeY + posY, 1);
        RenderUtils.beginDepthMasking();
        super.drawForeground(mouseX, mouseY);


        GlStateManager.disableTexture2D();
        RenderUtils.applyColor(scrollerColor);
        if (contentTotalHeight-sizeY > 0)
        {
            int maxScroll = contentTotalHeight-sizeY;
            float scrollPercent = -scrollSmooth / (float) maxScroll;
            int scrollerSize = (int) (((float) sizeY / (float) contentTotalHeight) * sizeY);
            int scrollerY = sizeY - scrollerSize;
            RenderUtils.drawPlane(posX + sizeX - 1, posY + scrollerY * scrollPercent, 0, 1, scrollerSize);
        }
        GlStateManager.enableTexture2D();

        RenderUtils.endDepthMask();
    }

    @Override
    public boolean onMouseWheel(int mouseX, int mouseY, int movement) {

        if (movement > 0) {
            scrollUp();
        } else if (movement < 0) {
            scrollDown();
        }
        return true;
    }

    public void scrollDown()
    {
        scroll-=scrollSpeed;
        scroll = Math.max(scroll,-Math.max(0,contentTotalHeight-sizeY));
    }

    public void scrollUp() {

        if(scroll < 0)
        {
            scroll = Math.min(scroll + scrollSpeed,0);
        }
    }

    public void setScrollerColor(int color)
    {
        scrollerColor = color;
    }

    public int getScroll(){return scroll;}
    public void setScroll(int scroll){this.scroll = scroll;}
}
