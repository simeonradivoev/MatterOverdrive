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

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 12/4/2015.
 */
public class GuideElementTooltip extends GuideElementAbstract
{
	ItemStack itemStack;
	List<String> lines;

	public GuideElementTooltip()
	{
		lines = new ArrayList<>();

	}

	@Override
	protected void loadContent(MOGuideEntry entry, Element element, int width, int height)
	{
		if (element.hasAttribute("item"))
		{
			itemStack = shortCodeToStack(decodeShortcode(element.getAttribute("item")));
		}
		else
		{
			itemStack = entry.getStackIcons()[0];
		}

		itemStack.getItem().addInformation(itemStack, Minecraft.getMinecraft().thePlayer, lines, true);
		this.height = lines.size() * getFontRenderer().FONT_HEIGHT;
	}

	@Override
	public void drawElement(int width, int mouseX, int mouseY)
	{
		for (int i = 0; i < lines.size(); i++)
		{
			getFontRenderer().drawString(lines.get(i), x, y + i * getFontRenderer().FONT_HEIGHT, color.getColor());
		}
	}
}
