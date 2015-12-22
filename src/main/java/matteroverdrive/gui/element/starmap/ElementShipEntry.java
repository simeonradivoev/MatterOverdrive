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
import matteroverdrive.Reference;
import matteroverdrive.api.starmap.IShip;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.gui.GuiStarMap;
import matteroverdrive.gui.element.ElementGroupList;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Map;

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
        RenderUtils.drawString(Minecraft.getMinecraft().fontRenderer, ship.getDisplayName(), posX + 31, posY + 12, color, multiply);
    }

    @Override
    protected Map<HoloIcon,Integer> getIcons(Planet spaceBody)
    {
        return null;
    }

    @Override
    protected boolean canTravelTo(Planet ship, EntityPlayer player)
    {
        return false;
    }

    @Override
    protected boolean canView(Planet planet,EntityPlayer player)
    {
        if (ship.getItem() instanceof IShip && ((IShip) ship.getItem()).isOwner(ship,Minecraft.getMinecraft().thePlayer))
        {
            Planet to = GalaxyClient.getInstance().getTheGalaxy().getPlanet(((GuiStarMap) gui).getMachine().getDestination());
            if (to != null && to != planet) {
                return to.canAddShip(ship, player);
            }
        }
        return false;
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
        if (canView(planet,Minecraft.getMinecraft().thePlayer)) {
            return Reference.COLOR_HOLO;
        }else
        {
            return Reference.COLOR_HOLO_RED;
        }
    }

    @Override
    boolean isSelected(Planet planet)
    {
        return groupList.isSelected(this);
    }

    @Override
    protected void onViewPress() {
        ((GuiStarMap) gui).getMachine().Attack(((GuiStarMap) gui).getMachine().getGalaxyPosition(), ((GuiStarMap) gui).getMachine().getDestination(), shipId);
        //((GuiStarMap) gui).getMachine().SyncCommandsToServer();
    }

    @Override
    protected void onTravelPress()
    {

    }

    @Override
    protected void onSelectPress() {

        //((GuiStarMap) gui).getMachine().SyncCommandsToServer();
    }
}
