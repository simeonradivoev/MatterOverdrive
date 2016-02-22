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
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.client.gui.FontRenderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Simeon on 11/21/2015.
 */
public class ElementTextList extends MOElementBase
{
    boolean isUnicode;
    List<String> lines;
    int textColor;

    public ElementTextList(MOGuiBase gui, int posX, int posY, int width, int textColor, boolean isUnicode) {
        super(gui, posX, posY, width, 0);
        lines = new ArrayList<>();
        this.textColor = textColor;
        this.isUnicode = isUnicode;
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
    public void addTooltip(List<String> var1, int mouseX, int mouseY)
    {

    }

    @Override
    public void drawBackground(int i, int i1, float v)
    {

    }

    @Override
    public FontRenderer getFontRenderer()
    {
        return ClientProxy.moFontRender;
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        boolean unicode = getFontRenderer().getUnicodeFlag();
        getFontRenderer().setUnicodeFlag(isUnicode);
        for (int i = 0;i < lines.size();i++)
        {
            getFontRenderer().drawString(lines.get(i), posX, posY + i * getLineHeight(), textColor);
        }
        getFontRenderer().setUnicodeFlag(unicode);
        sizeY = lines.size() * getFontRenderer().FONT_HEIGHT;
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

    public void addLines(Collection<String> lines)
    {
        this.lines.addAll(lines);
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
