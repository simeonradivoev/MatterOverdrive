package matteroverdrive.gui.pages;

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
