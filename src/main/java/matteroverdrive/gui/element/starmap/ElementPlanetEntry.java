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

package matteroverdrive.gui.element.starmap;

import cofh.lib.gui.GuiColor;
import matteroverdrive.api.starmap.GalacticPosition;
import matteroverdrive.api.starmap.IShip;
import matteroverdrive.data.ScaleTexture;
import matteroverdrive.gui.GuiStarMap;
import matteroverdrive.gui.element.ElementGroupList;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String name = spaceBody.getName();
        GuiStarMap guiStarMap = (GuiStarMap)gui;
        if (guiStarMap.getMachine().getGalaxyPosition().equals(planet))
        {
            name = "@ " + EnumChatFormatting.ITALIC + name;
        }

        if (Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode || GalaxyClient.getInstance().canSeePlanetInfo(planet, Minecraft.getMinecraft().thePlayer))

            RenderUtils.drawString(name, posX + 16, posY + 10, color, multiply);
        else
            RenderUtils.drawString(Minecraft.getMinecraft().standardGalacticFontRenderer,name,posX + 16,posY + 10,color,multiply);
    }

    @Override
    protected Map<IIcon,Integer> getIcons(Planet planet)
    {
        HashMap<IIcon,Integer> icons = new HashMap<>();
        IIcon shipIcon = ClientProxy.holoIcons.getIcon("icon_shuttle");
        icons.put(shipIcon,0);

        if (planet.isOwner(Minecraft.getMinecraft().thePlayer))
        {
            if (planet.isHomeworld())
            {
                icons.put(ClientProxy.holoIcons.getIcon("home_icon"),-1);
            }
            if (planet.getBuildings().size() > 0)
            {
                icons.put(ClientProxy.holoIcons.getIcon("factory"), planet.getBuildings().size());
            }
        }

        for (ItemStack ship : planet.getFleet())
        {
            if (((IShip)ship.getItem()).isOwner(ship,Minecraft.getMinecraft().thePlayer))
            {
                icons.put(shipIcon,icons.get(shipIcon)+1);
                break;
            }
        }
        return icons;
    }

    @Override
    public void addTooltip(List<String> list)
    {

    }

    @Override
    protected boolean canTravelTo(Planet planet, EntityPlayer player)
    {
        return !((GuiStarMap)gui).getMachine().getGalaxyPosition().equals(planet);
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
    public ScaleTexture getBG(Planet planet)
    {
        GuiStarMap guiStarMap = (GuiStarMap)gui;
        if (guiStarMap.getMachine().getGalaxyPosition().equals(planet))
        {
            return BG_MIDDLE_DOWN;
        }
        return BG;
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
