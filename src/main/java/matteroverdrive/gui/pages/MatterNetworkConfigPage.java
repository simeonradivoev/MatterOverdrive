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

package matteroverdrive.gui.pages;

import matteroverdrive.Reference;
import matteroverdrive.api.network.IMatterNetworkBroadcaster;
import matteroverdrive.gui.MOGuiMachine;
import matteroverdrive.gui.element.MOElementButton;
import matteroverdrive.gui.element.MOElementTextField;
import matteroverdrive.gui.events.ITextHandler;
import matteroverdrive.util.MOStringHelper;

/**
 * Created by Simeon on 7/17/2015.
 */
public class MatterNetworkConfigPage extends ConfigPage implements ITextHandler
{
    IMatterNetworkBroadcaster broadcaster;
    MOElementTextField destinationTextField;

    public MatterNetworkConfigPage(MOGuiMachine gui, int posX, int posY, int width, int height,IMatterNetworkBroadcaster broadcaster) {
        super(gui, posX, posY, width, height);
        destinationTextField = new MOElementTextField(gui,this,64,84,128,16);
        destinationTextField.setName("Destination");
        destinationTextField.setBackground(MOElementButton.HOVER_TEXTURE_DARK);
        destinationTextField.setTextOffset(4, 3);
        this.broadcaster = broadcaster;
    }

    @Override
    public void init()
    {
        super.init();
        addElement(destinationTextField);
        if (broadcaster.getDestinationFilter() != null)
            destinationTextField.setText(broadcaster.getDestinationFilter());
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        super.drawForeground(mouseX, mouseY);
        getFontRenderer().drawString("Destination Address:", 64, 70, 0xFFFFFF);
    }

    @Override
    public void textChanged(String elementName, String text, boolean typed)
    {
        if (elementName == "Destination") {
            broadcaster.setDestinationFilter(text);
            machineGui.getMachine().sendConfigsToServer();
        }
    }
}
