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

import com.mojang.realmsclient.gui.ChatFormatting;
import matteroverdrive.gui.GuiDataPad;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;
import org.w3c.dom.Element;

import java.util.List;

/**
 * Created by Simeon on 8/28/2015.
 */
public class GuideElementText extends GuideElementTextAbstract
{
	TextLine[] lines;

	protected String formatVariableReplace(String variable, String replace)
	{
		return TextFormatting.AQUA + replace + TextFormatting.RESET;
	}

	@Override
	public void drawElement(int width, int mouseX, int mouseY)
	{
		for (int i = 0; i < lines.length; i++)
		{
			int x = 0;
			if (textAlign == 1)
			{
				x = -lines[i].getWidth() / 2 + (width - marginLeft - marginRight) / 2;
			}
			else if (textAlign == 2)
			{
				x = -lines[i].getWidth() + (width - marginLeft - marginRight);
			}

			for (int c = 0; c < lines[i].chunks.size(); c++)
			{
				int y = marginTop + i * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
				TextChunk chunk = lines[i].chunks.get(c);

				if (chunk instanceof TextChunkLink)
				{
					if (mouseX > x && mouseX < x + chunk.getWidth() && mouseY > y && mouseY < y + getFontRenderer().FONT_HEIGHT)
					{
						if (Mouse.isButtonDown(0))
						{
							if (gui instanceof GuiDataPad)
							{
								((TextChunkLink)chunk).onClick((GuiDataPad)gui);
							}
						}

					}
				}


				Minecraft.getMinecraft().fontRendererObj.drawString(lines[i].chunks.get(c).getText(), marginLeft + x, y, color.getColor());
				int w = calculateWidth(null, lines[i].chunks.get(c), null);
				if (c > 0 && c < lines[i].chunks.size() - 1)
				{
					w = calculateWidth(lines[i].chunks.get(c - 1), lines[i].chunks.get(c), lines[i].chunks.get(c + 1));
				}
				x += w;
			}
		}
	}

	@Override
	protected void loadContent(MOGuideEntry entry, Element element, int width, int height)
	{
		List<TextLine> lines = handleTextFormatting(entry, element.getTextContent(), this.width);
		this.lines = new TextLine[lines.size()];
		this.lines = lines.toArray(this.lines);
		this.height = Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT * this.lines.length;
	}
}
