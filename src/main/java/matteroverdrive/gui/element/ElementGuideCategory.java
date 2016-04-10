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

import matteroverdrive.container.IButtonHandler;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.guide.GuideCategory;

/**
 * Created by Simeon on 8/30/2015.
 */
public class ElementGuideCategory extends MOElementButtonScaled
{
	private final GuideCategory category;

	public ElementGuideCategory(MOGuiBase gui, IButtonHandler handler, int posX, int posY, String name, int sizeX, int sizeY, GuideCategory category)
	{
		super(gui, handler, posX, posY, name, sizeX, sizeY);
		this.category = category;
		this.icon = category.getHoloIcon();
		this.setToolTip(category.getDisplayName());
	}

	public GuideCategory getCategory()
	{
		return category;
	}
}
