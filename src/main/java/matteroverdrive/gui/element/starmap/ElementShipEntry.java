package matteroverdrive.gui.element.starmap;

import cofh.lib.gui.GuiColor;
import matteroverdrive.Reference;
import matteroverdrive.gui.GuiStarMap;
import matteroverdrive.gui.element.ElementGroupList;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.data.GalacticPosition;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.TravelEvent;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 6/28/2015.
 */
public class ElementShipEntry extends ElementAbstractStarMapEntry<Planet>
{
    int shipId;
    ItemStack ship;

    public ElementShipEntry(GuiStarMap gui, ElementGroupList groupList, int width, int height,Planet planet, ItemStack ship,int shipId) {
        super(gui, groupList, width, height,planet);
        this.ship = ship;
        this.searchIcon = ClientProxy.holoIcons.getIcon("icon_attack");
        this.shipId = shipId;
    }

    @Override
    protected void drawElementName(Planet planet, GuiColor color, float multiply)
    {
        RenderUtils.renderStack(posX + 10, posY + sizeY / 2 - 8, ship);
        RenderUtils.drawString(Minecraft.getMinecraft().fontRenderer,ship.getDisplayName(),posX + 31,posY + 12,color,multiply);
    }

    @Override
    protected boolean canTravelTo(Planet ship, EntityPlayer player)
    {
        return false;
    }

    @Override
    protected boolean canView(Planet planet,EntityPlayer player)
    {
        for (TravelEvent travelEvent : GalaxyClient.getInstance().getTheGalaxy().getTravelEvents())
        {
            if (travelEvent.getFrom().equals(new GalacticPosition(planet)) && travelEvent.getShipID() == shipId)
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public float getMultiply(Planet ship)
    {
        if (groupList.isSelected(this))
        {
            return  1;
        }
        return 0.1f;
    }

    @Override
    protected GuiColor getSpaceBodyColor(Planet planet)
    {
        return Reference.COLOR_HOLO;
    }

    @Override
    boolean isSelected(Planet planet)
    {
        return groupList.isSelected(this);
    }

    @Override
    protected void onViewPress()
    {
        ((GuiStarMap) gui).getMachine().Attack(((GuiStarMap) gui).getMachine().getGalaxyPosition(),((GuiStarMap) gui).getMachine().getDestination(),shipId);
        //((GuiStarMap) gui).getMachine().SyncCommandsToServer();
    }

    @Override
    protected void onTravelPress()
    {

    }

    @Override
    protected void onSelectPress() {

        ((GuiStarMap) gui).getMachine().SyncCommandsToServer();
    }
}
