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
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.MathHelper;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 11/21/2015.
 */
public class ElementTextList extends MOElementBase
{
    boolean isUnicode;
    List<String> lines;
    int scroll;
    int textColor;

    public ElementTextList(GuiBase gui, int posX, int posY, int width, int height,int textColor,boolean isUnicode) {
        super(gui, posX, posY, width, height);
        lines = new ArrayList<>();
        this.textColor = textColor;
        this.isUnicode = isUnicode;
        this.scroll = 0;
    }

    @Override
    public void drawBackground(int i, int i1, float v)
    {

    }

    @Override
    public FontRenderer getFontRenderer()
    {
        return Minecraft.getMinecraft().fontRenderer;
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        boolean unicode = getFontRenderer().getUnicodeFlag();
        getFontRenderer().setUnicodeFlag(isUnicode);
        for (int i = scroll;i < lines.size();i++)
        {
            if (i <= getHeight()/getLineHeight()+scroll) {
                getFontRenderer().drawString(lines.get(i), posX, posY + (i - scroll) * getLineHeight(), textColor);
            }
        }
        getFontRenderer().setUnicodeFlag(unicode);

        glDisable(GL_TEXTURE_2D);
        RenderUtils.applyColor(textColor);
        int displayLinesCount = getHeight()/getLineHeight();
        if (lines.size() > displayLinesCount) {
            int allLines = lines.size();
            int scrollerSize = (int) (((float) displayLinesCount / (float) allLines) * sizeY);
            int scrollerY = sizeY - scrollerSize;
            int maxScroll = (getLinesHeight() - getHeight()) / getLineHeight();
            float scrollPercent = (float) scroll / (float) maxScroll;
            RenderUtils.drawPlane(posX + sizeX - 1, posY + scrollerY * scrollPercent, 0, 1, scrollerSize);
        }
        glEnable(GL_TEXTURE_2D);
    }

    public boolean onMouseWheel(int mouseX, int mouseY, int movement)
    {
        if (movement > 0)
        {
            scroll = Math.max(0,scroll-1);
        }else
        {
            scroll = Math.max(0,scroll+1);
        }

        scroll = MathHelper.clamp_int(scroll,0,getMaxScroll());

        return true;
    }

    public int getMaxScroll()
    {
        return Math.max(0,(getLinesHeight()-getHeight())/getLineHeight());
    }

    public int getLinesHeight()
    {
        return lines.size()*getLineHeight();
    }

    public int getLineHeight()
    {
        return getFontRenderer().FONT_HEIGHT;
    }

    public void addLine(String line)
    {
        lines.add(line);
    }

    public void setScroll(int scroll)
    {
        this.scroll = MathHelper.clamp_int(scroll,0,getMaxScroll());
    }

    public int getScroll()
    {
        return this.scroll;
    }

    public void setLines(List<String> lines)
    {
        this.lines = lines;
    }

    public void clearLines()
    {
        this.lines.clear();
    }
}
