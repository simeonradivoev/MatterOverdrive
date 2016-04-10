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

import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.MOStringHelper;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Simeon on 8/30/2015.
 */
public class GuideCategory
{
	private String name;
	private Set<MOGuideEntry> entries;
	private String holoIcon;

	public GuideCategory(String name)
	{
		this.name = name;
		entries = new HashSet<>();
	}

	public String getName()
	{
		return name;
	}

	public String getDisplayName()
	{
		return MOStringHelper.translateToLocal(String.format("guide.category.%s.name", name));
	}

	public void addEntry(MOGuideEntry entry)
	{
		entries.add(entry);
	}

	public HoloIcon getHoloIcon()
	{
		return ClientProxy.holoIcons.getIcon(holoIcon);
	}

	public GuideCategory setHoloIcon(String holoIcon)
	{
		this.holoIcon = holoIcon;
		return this;
	}

	public Set<MOGuideEntry> getEntries()
	{
		return entries;
	}
}
