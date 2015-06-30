package matteroverdrive.gui.element.starmap;

import cofh.lib.gui.GuiColor;
import matteroverdrive.Reference;
import matteroverdrive.gui.GuiStarMap;
import matteroverdrive.gui.element.ElementGroupList;
import matteroverdrive.starmap.data.GalacticPosition;
import matteroverdrive.starmap.data.Quadrant;
import matteroverdrive.util.RenderUtils;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 6/27/2015.
 */
public class ElementQuadrantEntry extends ElementAbstractStarMapEntry<Quadrant>
{
    public ElementQuadrantEntry(GuiStarMap gui, ElementGroupList groupList, int width, int height, Quadrant spaceBody) {
        super(gui, groupList, width, height, spaceBody);
    }

    @Override
    protected void drawElementName(Quadrant quadrant, GuiColor color, float multiply)
    {
        RenderUtils.drawString(spaceBody.getName(), posX + 16, posY + 10, color, multiply);
    }

    @Override
    protected boolean canTravelTo(Quadrant planet, EntityPlayer player)
    {
        return true;
    }

    @Override
    protected boolean canView(Quadrant spaceBody, EntityPlayer player) {
        return true;
    }

    @Override
    public float getMultiply(Quadrant quadrant)
    {
        GuiStarMap guiStarMap = (GuiStarMap)gui;
        if (guiStarMap.getMachine().getDestination().equals(quadrant))
        {
            return  1;
        }else if (guiStarMap.getMachine().getGalaxyPosition().equals(quadrant))
        {
            return  0.5f;
        }
        return 0.1f;
    }

    @Override
    protected GuiColor getSpaceBodyColor(Quadrant planet)
    {
        return Reference.COLOR_HOLO;
    }

    @Override
    boolean isSelected(Quadrant quadrant)
    {
        return ((GuiStarMap)gui).getMachine().getDestination().equals(quadrant);
    }

    @Override
    protected void onViewPress()
    {
        ((GuiStarMap) gui).setPage(1);
    }

    @Override
    protected void onTravelPress()
    {
        ((GuiStarMap) gui).getMachine().setGalaxticPosition(new GalacticPosition(spaceBody));
        ((GuiStarMap) gui).getMachine().SyncCommandsToServer();
    }

    @Override
    protected void onSelectPress() {
        ((GuiStarMap) gui).getMachine().setDestination(new GalacticPosition(spaceBody));
        ((GuiStarMap) gui).getMachine().SyncCommandsToServer();
    }
}
