package matteroverdrive.gui.element.starmap;

import cofh.lib.gui.GuiColor;
import matteroverdrive.api.starmap.IShip;
import matteroverdrive.gui.GuiStarMap;
import matteroverdrive.gui.element.ElementGroupList;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.starmap.data.GalacticPosition;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Simeon on 6/21/2015.
 */
public class ElementPlanetEntry extends ElementAbstractStarMapEntry<Planet>
{
    public ElementPlanetEntry(GuiStarMap gui, ElementGroupList groupList, int width, int height, Planet spaceBody) {
        super(gui, groupList, width, height, spaceBody);
    }

    @Override
    protected void drawElementName(Planet planet, GuiColor color, float multiply)
    {
        if (Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode || planet.isOwner(Minecraft.getMinecraft().thePlayer))
            RenderUtils.drawString(spaceBody.getName(), posX + 16, posY + 10, color, multiply);
        else
            RenderUtils.drawString(Minecraft.getMinecraft().standardGalacticFontRenderer,spaceBody.getName(),posX + 16,posY + 10,color,multiply);

        if (planet.isOwner(Minecraft.getMinecraft().thePlayer) && planet.isHomeworld())
        {
            if (isSelected(planet))
            {
                ClientProxy.holoIcons.renderIcon("home_icon", posX + sizeX + 4, posY + 8);
            }else
            {
                ClientProxy.holoIcons.renderIcon("home_icon", posX + sizeX - 60, posY + 8);
            }
        }

        boolean fleetFlag = false;
        for (ItemStack ship : planet.getFleet())
        {
            if (((IShip)ship.getItem()).isOwner(ship,Minecraft.getMinecraft().thePlayer))
            {
                fleetFlag = true;
                break;
            }
        }
        if (fleetFlag)
        {
            if (isSelected(planet))
            {
                ClientProxy.holoIcons.renderIcon("icon_shuttle", posX + sizeX + 21, posY + 7);
            }else
            {
                ClientProxy.holoIcons.renderIcon("icon_shuttle", posX + sizeX - 44, posY + 7);
            }
        }
    }

    @Override
    public void addTooltip(List<String> list)
    {

    }

    @Override
    protected boolean canTravelTo(Planet planet, EntityPlayer player)
    {
        return planet.isOwner(player);
    }

    @Override
    protected boolean canView(Planet planet, EntityPlayer player)
    {
        if (planet.hasOwner())
        {
            return planet.isOwner(player);
        }
        return true;
    }

    @Override
    public float getMultiply(Planet planet)
    {
        GuiStarMap guiStarMap = (GuiStarMap)gui;
        if (guiStarMap.getMachine().getDestination().equals(planet))
        {
            return  1;
        }else if (guiStarMap.getMachine().getGalaxyPosition().equals(planet))
        {
            return  0.5f;
        }
        return 0.1f;
    }

    @Override
    protected GuiColor getSpaceBodyColor(Planet planet)
    {
        return Planet.getGuiColor(planet);
    }

    @Override
    boolean isSelected(Planet planet)
    {
        return ((GuiStarMap)gui).getMachine().getDestination().equals(planet);
    }

    @Override
    protected void onViewPress()
    {
        ((GuiStarMap) gui).setPage(3);
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
