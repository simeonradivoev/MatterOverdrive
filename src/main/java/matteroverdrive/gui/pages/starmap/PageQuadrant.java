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

import cofh.lib.gui.GuiColor;
import matteroverdrive.client.render.tileentity.starmap.StarMapRendererStars;
import matteroverdrive.gui.GuiStarMap;
import matteroverdrive.gui.element.ElementBaseGroup;
import matteroverdrive.gui.element.ElementGroupList;
import matteroverdrive.gui.element.starmap.ElementStarEntry;
import matteroverdrive.gui.events.IListHandler;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.data.Quadrant;
import matteroverdrive.starmap.data.Star;
import matteroverdrive.tile.TileEntityMachineStarMap;
import net.minecraft.client.Minecraft;

/**
 * Created by Simeon on 6/18/2015.
 */
public class PageQuadrant extends ElementBaseGroup implements IListHandler {

    public static int scroll;
    TileEntityMachineStarMap starMap;
    ElementGroupList starList;

    public PageQuadrant(GuiStarMap gui, int posX, int posY, int width, int height,TileEntityMachineStarMap starMap) {
        super(gui, posX, posY, width, height);
        starList = new ElementGroupList(gui,this,16,16,width,height-160);
        starList.setName("Stars");
        starList.setScroll(scroll);
        this.starMap = starMap;
    }

    private void loadStars()
    {
        starList.init();
        Quadrant quadrant = GalaxyClient.getInstance().getTheGalaxy().getQuadrant(starMap.getDestination());
        if (quadrant != null) {
            for (Star star : quadrant.getStars()) {
                GuiColor color = StarMapRendererStars.getStarColor(star, Minecraft.getMinecraft().thePlayer);
                starList.addElement(new ElementStarEntry((GuiStarMap) gui, starList, 128 + 64, 32, star));

                if (starMap.getDestination().equals(star)) {
                    starList.setSelectedIndex(starList.getElements().size() - 1);
                }
            }
        }
        starList.limitScroll();
    }

    @Override
    public void init()
    {
        super.init();
        addElement(starList);
        loadStars();

    }

    @Override
    public void ListSelectionChange(String name, int selected)
    {

    }

    @Override
    public void update(int mouseX, int mouseY)
    {
        super.update(mouseX,mouseY);
        scroll = starList.getScroll();
    }
}
