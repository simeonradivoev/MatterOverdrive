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

package matteroverdrive.gui.pages.starmap;

import matteroverdrive.gui.GuiStarMap;
import matteroverdrive.gui.element.ElementBaseGroup;
import matteroverdrive.gui.element.ElementGroupList;
import matteroverdrive.gui.element.starmap.ElementPlanetEntry;
import matteroverdrive.gui.events.IListHandler;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.Star;
import matteroverdrive.tile.TileEntityMachineStarMap;

/**
 * Created by Simeon on 6/21/2015.
 */
public class PageStar extends ElementBaseGroup implements IListHandler
{
	TileEntityMachineStarMap starMap;
	ElementGroupList planetList;

	public PageStar(GuiStarMap gui, int posX, int posY, int width, int height, TileEntityMachineStarMap starMap)
	{
		super(gui, posX, posY, width, height);
		planetList = new ElementGroupList(gui, this, 16, 16, sizeX, sizeY - 100 - 32);
		planetList.setName("Stars");
		planetList.resetSmoothScroll();
		//planetList.textColor = Reference.COLOR_HOLO.getColor();
		//planetList.selectedTextColor = Reference.COLOR_HOLO_YELLOW.getColor();
		this.starMap = starMap;
	}

	private void loadPlanets()
	{
		planetList.init();
		Star star = GalaxyClient.getInstance().getTheGalaxy().getStar(starMap.getDestination());
		if (star != null)
		{
			for (Planet planet : star.getPlanets())
			{
				planetList.addElement(new ElementPlanetEntry((GuiStarMap)gui, planetList, 128 + 64, 32, planet));

				if (starMap.getDestination().equals(planet))
				{
					planetList.setSelectedIndex(planetList.getElements().size() - 1);
				}
			}
		}
		planetList.limitScroll();
		planetList.update(0, 0, 0);
	}

	@Override
	public void init()
	{
		super.init();
		planetList.setSize(sizeX, sizeY - 100 - 32);
		addElement(planetList);
		loadPlanets();

	}

	@Override
	public void ListSelectionChange(String name, int selected)
	{

	}

	@Override
	public void update(int mouseX, int mouseY, float partialTicks)
	{
		super.update(mouseX, mouseY, partialTicks);
	}
}
