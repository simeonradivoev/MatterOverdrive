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
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Simeon on 12/6/2015.
 */
public class ElementQuestRewards extends MOElementBase
{
	List<ItemStack> itemStacks;

	public ElementQuestRewards(MOGuiBase gui, int posX, int posY, List<ItemStack> itemStacks)
	{
		super(gui, posX, posY);
		setItemStacks(itemStacks);
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
	public void drawBackground(int mouseX, int mouseY, float v)
	{

	}

	@Override
	public void drawForeground(int mouseX, int mouseY)
	{
		for (int i = 0; i < itemStacks.size(); i++)
		{
			RenderUtils.renderStack(posX + 18 * i, posY + 1, 0, itemStacks.get(i), true);
		}
	}

	public void setItemStacks(List<ItemStack> itemStacks)
	{
		this.itemStacks = itemStacks;
		if (itemStacks != null)
		{
			sizeY = 18;
			sizeX = 18 * itemStacks.size();
		}
	}
}
