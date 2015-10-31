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

package matteroverdrive.gui.element;

import matteroverdrive.Reference;
import matteroverdrive.api.transport.TransportLocation;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.gui.events.IListHandler;
import matteroverdrive.machines.transporter.TileEntityMachineTransporter;

/**
 * Created by Simeon on 5/5/2015.
 */
public class ElementTransportList extends MOElementListBox
{
    TileEntityMachineTransporter transporter;

    public ElementTransportList(MOGuiBase containerScreen,IListHandler listHandler, int x, int y, int width, int height,TileEntityMachineTransporter transporter)
    {
        super(containerScreen,listHandler, x, y, width, height);
        this.transporter = transporter;
    }

    @Override
    public void DrawElement(int i,int x,int y,int selectedLineColor,int selectedTextColor, boolean selected,boolean BG)
    {
        TransportLocation position = transporter.getPositions().get(i);

        if (BG)
        {
            if (selected && transporter.isLocationValid(position))
                MOElementButton.NORMAL_TEXTURE.render(x,y,getElementWidth(i),getElementHeight(i));
            else {
                MOElementButton.HOVER_TEXTURE_DARK.render(x, y, getElementWidth(i), getElementHeight(i));
            }
        }
        else
        {

            String info = "[ X: " + (position.x + transporter.xCoord) + ", Y: " + (position.y + transporter.yCoord) + ", Z: " + (position.z + transporter.zCoord) + " ]";
            gui.drawCenteredString(getFontRenderer(), position.name, x + getElementWidth(i) / 2, y + getElementHeight(i) / 2 - 4, transporter.isLocationValid(position) ? selectedTextColor : Reference.COLOR_HOLO_RED.getColor());
            //gui.drawCenteredString(getFontRenderer(), EnumChatFormatting.YELLOW + info,x + getElementWidth(i)/2,y + getElementHeight(i) / 2 + 2,0xFFFFFF);
        }
    }

    @Override
    public void drawElementTooltip(int index,int mouseX,int mouseY)
    {

    }

    @Override
    public int getElementHeight(int id)
    {
        return 20;
    }

    @Override
    public int getElementWidth(int id)
    {
        return  sizeX - 4;
    }

    @Override
    protected boolean shouldBeDisplayed(IMOListBoxElement element)
    {
        return true;
    }

    @Override
    public IMOListBoxElement getElement(int index) {

        return null;
    }

    public int getElementCount() {

        return transporter.getPositions().size();
    }
}
