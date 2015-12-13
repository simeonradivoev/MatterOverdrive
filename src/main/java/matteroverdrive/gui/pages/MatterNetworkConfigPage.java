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

import matteroverdrive.container.slot.MOSlot;
import matteroverdrive.gui.MOGuiMachine;
import matteroverdrive.gui.element.ElementInventorySlot;
import matteroverdrive.gui.element.MOElementButton;
import matteroverdrive.gui.element.MOElementTextField;
import matteroverdrive.gui.events.ITextHandler;
import matteroverdrive.machines.components.ComponentConfigs;
import matteroverdrive.machines.components.ComponentMatterNetworkConfigs;


/**
 * Created by Simeon on 7/17/2015.
 */
public class MatterNetworkConfigPage extends AutoConfigPage implements ITextHandler
{
    ComponentMatterNetworkConfigs componentMatterNetworkConfigs;
    ElementInventorySlot filterSlot;
    MOElementTextField destinationTextField;

    public MatterNetworkConfigPage(MOGuiMachine gui, int posX, int posY, int width, int height,ComponentMatterNetworkConfigs componentMatterNetworkConfigs,ComponentConfigs configurable) {
        super(gui, posX, posY, width, height,configurable);
        destinationTextField = new MOElementTextField(gui,this,64,54,96,16);
        destinationTextField.setName("Destination");
        destinationTextField.setBackground(MOElementButton.HOVER_TEXTURE_DARK);
        destinationTextField.setTextOffset(4, 3);
        this.componentMatterNetworkConfigs = componentMatterNetworkConfigs;
        filterSlot = new ElementInventorySlot(gui,(MOSlot)machineGui.inventorySlots.getSlot(componentMatterNetworkConfigs.getDestinationFilterSlot()),164,50,22,22,"big");
    }

    @Override
    public void init()
    {
        super.init();
        addElement(destinationTextField);
        if (componentMatterNetworkConfigs != null) {
            if (componentMatterNetworkConfigs.getDestinationFilter() != null)
                destinationTextField.setText(componentMatterNetworkConfigs.getDestinationFilter());
            addElement(filterSlot);
        }
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        super.drawForeground(mouseX, mouseY);
        getFontRenderer().drawString("Destination Address:", 64, 38, 0xFFFFFF);
    }

    @Override
    public void textChanged(String elementName, String text, boolean typed)
    {
        if (elementName.equals("Destination"))
        {
            if (componentMatterNetworkConfigs != null) {
                componentMatterNetworkConfigs.setDestinationFilter(text);
                machineGui.getMachine().sendConfigsToServer(false);
            }
        }
    }

    @Override
    public void update(int mouseX, int mouseY)
    {
        super.update(mouseX,mouseY);
        int x = destinationTextField.getPosX() + destinationTextField.getWidth() + 10;
        /*for (ElementSlot slot : networkConfigSlots)
        {
            slot.setPosition(x,destinationTextField.getPosY() + 6);
            x += slot.getWidth();
        }*/
    }
}
