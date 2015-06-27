package com.MO.MatterOverdrive.gui.pages;

import com.MO.MatterOverdrive.gui.GuiStarMap;
import com.MO.MatterOverdrive.gui.element.ElementBaseGroup;
import com.MO.MatterOverdrive.gui.element.ElementGroupList;
import com.MO.MatterOverdrive.gui.element.starmap.ElementPlanetEntry;
import com.MO.MatterOverdrive.gui.events.IListHandler;
import com.MO.MatterOverdrive.starmap.GalaxyClient;
import com.MO.MatterOverdrive.starmap.data.Planet;
import com.MO.MatterOverdrive.starmap.data.Star;
import com.MO.MatterOverdrive.tile.TileEntityMachineStarMap;

/**
 * Created by Simeon on 6/21/2015.
 */
public class PageStar extends ElementBaseGroup implements IListHandler
{
    TileEntityMachineStarMap starMap;
    ElementGroupList planetList;

    public PageStar(GuiStarMap gui, int posX, int posY, int width, int height,TileEntityMachineStarMap starMap) {
        super(gui, posX, posY, width, height);
        planetList = new ElementGroupList(gui,this,0,16,128+64,228);
        planetList.setName("Stars");
        //planetList.textColor = Reference.COLOR_HOLO.getColor();
        //planetList.selectedTextColor = Reference.COLOR_HOLO_YELLOW.getColor();
        this.starMap = starMap;
    }

    private void loadPlanets()
    {
        planetList.init();
        Star star = GalaxyClient.getInstance().getTheGalaxy().getStar(starMap.getDestination());
        if (star != null) {
            for (Planet planet : star.getPlanets()) {
                planetList.addElement(new ElementPlanetEntry((GuiStarMap) gui, planetList, 128 + 64, 32, planet));

                if (starMap.getDestination().equals(planet)) {
                    planetList.setSelectedIndex(planetList.getElements().size() - 1);
                }
            }
        }
        planetList.limitScroll();
    }

    @Override
    public void init()
    {
        super.init();
        addElement(planetList);
        loadPlanets();

    }

    @Override
    public void ListSelectionChange(String name, int selected) {

    }

    @Override
    public void update(int mouseX, int mouseY)
    {
        super.update(mouseX,mouseY);
    }
}
