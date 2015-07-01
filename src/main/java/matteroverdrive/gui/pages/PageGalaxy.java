package matteroverdrive.gui.pages;

import matteroverdrive.gui.GuiStarMap;
import matteroverdrive.gui.element.ElementBaseGroup;
import matteroverdrive.gui.element.ElementGroupList;
import matteroverdrive.gui.element.starmap.ElementQuadrantEntry;
import matteroverdrive.gui.events.IListHandler;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.data.Quadrant;
import matteroverdrive.tile.TileEntityMachineStarMap;

/**
 * Created by Simeon on 6/27/2015.
 */
public class PageGalaxy extends ElementBaseGroup implements IListHandler
{
    public static int scroll;
    TileEntityMachineStarMap starMap;
    ElementGroupList quadrantList;

    public PageGalaxy(GuiStarMap gui, int posX, int posY, int width, int height,TileEntityMachineStarMap starMap) {
        super(gui, posX, posY, width, height);
        quadrantList = new ElementGroupList(gui,this,0,16,width,228);
        quadrantList.setName("Quadrants");
        quadrantList.setScroll(scroll);
        this.starMap = starMap;
    }

    private void loadStars()
    {
        quadrantList.init();
        for (Quadrant quadrant : GalaxyClient.getInstance().getTheGalaxy().getQuadrants())
        {
            quadrantList.addElement(new ElementQuadrantEntry((GuiStarMap)gui,quadrantList,128+64,32,quadrant));

            if (starMap.getDestination().equals(quadrant)) {
                quadrantList.setSelectedIndex(quadrantList.getElements().size()-1);
            }
        }
        quadrantList.limitScroll();
    }

    @Override
    public void init()
    {
        super.init();
        addElement(quadrantList);
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
        scroll = quadrantList.getScroll();
    }
}
