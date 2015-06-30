package matteroverdrive.gui.pages;

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
        starList = new ElementGroupList(gui,this,0,16,128+64,228);
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
