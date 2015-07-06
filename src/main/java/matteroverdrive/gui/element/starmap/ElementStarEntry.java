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
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String name = spaceBody.getName();
        GuiStarMap guiStarMap = (GuiStarMap)gui;
        if (guiStarMap.getMachine().getGalaxyPosition().equals(star))
        {
            name = "@ " + EnumChatFormatting.ITALIC + name;
        }

        if (Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode || GalaxyClient.getInstance().canSeeStarInfo(star,Minecraft.getMinecraft().thePlayer))
        {
            RenderUtils.drawString(name, posX + 16, posY + 10, color, multiply);
        }else
        {
            RenderUtils.drawString(Minecraft.getMinecraft().standardGalacticFontRenderer,name, posX + 16, posY + 10, color, multiply);
        }
    }

    @Override
    protected Map<IIcon,Integer> getIcons(Star star)
    {
        HashMap<IIcon,Integer> icons = new HashMap<>();
        IIcon homeIcon = ClientProxy.holoIcons.getIcon("home_icon");
        IIcon shipIcon = ClientProxy.holoIcons.getIcon("icon_shuttle");
        IIcon factoryIcon = ClientProxy.holoIcons.getIcon("factory");
        icons.put(shipIcon,0);
        icons.put(factoryIcon,0);
        for (Planet planet : star.getPlanets())
        {
            if (planet.isOwner(Minecraft.getMinecraft().thePlayer))
            {
                if (planet.isHomeworld())
                {
                    icons.put(homeIcon,-1);
                }
                if (planet.getBuildings().size() > 0)
                {
                    icons.put(factoryIcon,icons.get(factoryIcon)+1);
                }
            }
            for (ItemStack ship : planet.getFleet())
            {
                if (((IShip)ship.getItem()).isOwner(ship,Minecraft.getMinecraft().thePlayer))
                {
                    icons.put(shipIcon,icons.get(shipIcon)+1);
                }
            }
        }
        return icons;
    }

    @Override
    protected boolean canTravelTo(Star star, EntityPlayer player)
    {
        return true;
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


