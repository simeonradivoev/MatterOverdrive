package matteroverdrive.gui.element.starmap;

import cofh.lib.gui.GuiColor;
import matteroverdrive.api.starmap.IShip;
import matteroverdrive.client.render.tileentity.starmap.StarMapRendererStars;
import matteroverdrive.gui.GuiStarMap;
import matteroverdrive.gui.element.ElementGroupList;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.api.starmap.GalacticPosition;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.starmap.data.Star;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 6/20/2015.
 */
public class ElementStarEntry extends ElementAbstractStarMapEntry<Star>
{

    public ElementStarEntry(GuiStarMap gui,ElementGroupList groupList, int width, int height,Star star)
    {
        super(gui,groupList,width,height,star);
    }

    public void addTooltip(List<String> list)
    {

    }

    @Override
    protected void drawElementName(Star star,GuiColor color,float multiply)
    {
        if (Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode || GalaxyClient.getInstance().canSeeStarInfo(star,Minecraft.getMinecraft().thePlayer))
        {
            RenderUtils.drawString(spaceBody.getName(), posX + 16, posY + 10, color, multiply);
        }else
        {
            RenderUtils.drawString(Minecraft.getMinecraft().standardGalacticFontRenderer,spaceBody.getName(), posX + 16, posY + 10, color, multiply);
        }
    }

    @Override
    protected List<IIcon> getIcons(Star star)
    {
        List<IIcon> icons = new ArrayList<>();
        boolean homeworldFlag = false;
        boolean fleetFlag = false;
        boolean buildingsFlag = false;
        for (Planet planet : star.getPlanets())
        {
            if (planet.isOwner(Minecraft.getMinecraft().thePlayer))
            {
                if (planet.isHomeworld())
                {
                    homeworldFlag = true;
                }
                if (planet.getBuildings().size() > 0)
                {
                    buildingsFlag = true;
                }
            }
            for (ItemStack ship : planet.getFleet())
            {
                if (((IShip)ship.getItem()).isOwner(ship,Minecraft.getMinecraft().thePlayer))
                {
                    fleetFlag = true;
                }
            }
        }
        if (homeworldFlag)
        {
            icons.add(ClientProxy.holoIcons.getIcon("home_icon"));
        }
        if (fleetFlag)
        {
            icons.add(ClientProxy.holoIcons.getIcon("icon_shuttle"));
        }
        if (buildingsFlag)
        {
            icons.add(ClientProxy.holoIcons.getIcon("factory"));
        }
        return icons;
    }

    @Override
    protected boolean canTravelTo(Star star, EntityPlayer player)
    {
        return star.isClaimed(player) >= 2;
    }

    @Override
    protected boolean canView(Star spaceBody, EntityPlayer player) {
        return true;
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

    protected void onViewPress()
    {
        ((GuiStarMap) gui).setPage(2);
    }

    @Override
    protected GuiColor getSpaceBodyColor(Star star)
    {
        return StarMapRendererStars.getStarColor(star, Minecraft.getMinecraft().thePlayer);
    }

    @Override
    boolean isSelected(Star star)
    {
        return ((GuiStarMap)gui).getMachine().getDestination().equals(star);
    }

    @Override
    public float getMultiply(Star star)
    {
        GuiStarMap guiStarMap = (GuiStarMap)gui;
        if (guiStarMap.getMachine().getDestination().equals(star))
        {
           return  1;
        }else if (guiStarMap.getMachine().getGalaxyPosition().equals(star))
        {
            return  0.5f;
        }
        return 0.1f;
    }
}


