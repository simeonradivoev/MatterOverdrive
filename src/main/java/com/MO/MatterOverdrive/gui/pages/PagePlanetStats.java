package com.MO.MatterOverdrive.gui.pages;

import com.MO.MatterOverdrive.gui.GuiStarMap;
import com.MO.MatterOverdrive.gui.element.ElementBaseGroup;
import com.MO.MatterOverdrive.gui.element.ElementGroupList;
import com.MO.MatterOverdrive.gui.element.starmap.ElementShipEntry;
import com.MO.MatterOverdrive.gui.events.IListHandler;
import com.MO.MatterOverdrive.starmap.GalaxyClient;
import com.MO.MatterOverdrive.starmap.data.Planet;
import com.MO.MatterOverdrive.tile.TileEntityMachineStarMap;

/**
 * Created by Simeon on 6/28/2015.
 */
public class PagePlanetStats extends ElementBaseGroup implements IListHandler
{
    TileEntityMachineStarMap starMap;
    ElementGroupList shipList;

    public PagePlanetStats(GuiStarMap gui, int posX, int posY, int width, int height,TileEntityMachineStarMap starMap)
    {
        super(gui, posX, posY, width, height);
        this.starMap = starMap;
        shipList = new ElementGroupList(gui,this,16,16,186,256);
    }

    @Override
    public void init()
    {
        super.init();
        addElement(shipList);
        loadShips();
    }

    public void loadShips()
    {
        shipList.init();
        Planet planet = GalaxyClient.getInstance().getTheGalaxy().getPlanet(starMap.getGalaxyPosition());
        if (planet != null)
        {
            for (int i = 0;i < planet.getFleet().size();i++)
            {
                shipList.addElement(new ElementShipEntry((GuiStarMap)gui,shipList,186,32,planet,planet.getFleet().get(i),i));
            }
        }
    }

    @Override
    public void ListSelectionChange(String name, int selected)
    {

    }
}
