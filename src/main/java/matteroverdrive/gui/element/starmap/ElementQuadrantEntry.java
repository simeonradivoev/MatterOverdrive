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

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.starmap.GalacticPosition;
import matteroverdrive.client.data.Color;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.gui.GuiStarMap;
import matteroverdrive.gui.element.ElementGroupList;
import matteroverdrive.network.packet.server.starmap.PacketStarMapClientCommands;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.starmap.data.Quadrant;
import matteroverdrive.starmap.data.Star;
import matteroverdrive.tile.TileEntityMachineStarMap;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Simeon on 6/27/2015.
 */
public class ElementQuadrantEntry extends ElementAbstractStarMapEntry<Quadrant>
{
    public ElementQuadrantEntry(GuiStarMap gui, ElementGroupList groupList, int width, int height, Quadrant spaceBody) {
        super(gui, groupList, width, height, spaceBody);
    }

    @Override
    protected void drawElementName(Quadrant quadrant, Color color, float multiply)
    {
        RenderUtils.drawString(spaceBody.getName(), posX + 16, posY + 10, color, multiply);
    }

    @Override
    protected Map<HoloIcon,Integer> getIcons(Quadrant spaceBody)
    {
        HashMap<HoloIcon,Integer> icons = new HashMap<>();
        HoloIcon homeIcon = ClientProxy.holoIcons.getIcon("home_icon");
        for (Star star : spaceBody.getStars())
        {
            if (star.isClaimed(Minecraft.getMinecraft().thePlayer) >= 2)
            {
                icons.put(homeIcon,-1);
            }
        }
        return icons;
    }

    @Override
    protected boolean canTravelTo(Quadrant quadrant, EntityPlayer player)
    {
        return false;
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
    protected Color getSpaceBodyColor(Quadrant planet)
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
        gui.setPage(1);
    }

    @Override
    protected void onTravelPress()
    {
        TileEntityMachineStarMap starMap = ((GuiStarMap) gui).getMachine();
        MatterOverdrive.packetPipeline.sendToServer(new PacketStarMapClientCommands(starMap,starMap.getZoomLevel(),new GalacticPosition(spaceBody),starMap.getDestination()));
    }

    @Override
    protected void onSelectPress() {
        TileEntityMachineStarMap starMap = ((GuiStarMap) gui).getMachine();
        MatterOverdrive.packetPipeline.sendToServer(new PacketStarMapClientCommands(starMap,starMap.getZoomLevel(),starMap.getGalaxyPosition(),new GalacticPosition(spaceBody)));
    }
}
