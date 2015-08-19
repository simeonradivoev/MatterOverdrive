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

import cofh.lib.gui.element.ElementBase;
import matteroverdrive.gui.MOGuiMachine;
import matteroverdrive.gui.element.ElementBaseGroup;
import matteroverdrive.gui.element.ElementCheckbox;
import matteroverdrive.gui.element.ElementStates;
import matteroverdrive.machines.components.ComponentConfigs;
import matteroverdrive.machines.configs.ConfigPropertyBoolean;
import matteroverdrive.machines.configs.ConfigPropertyInteger;
import matteroverdrive.machines.configs.ConfigPropertyStringList;
import matteroverdrive.machines.configs.IConfigProperty;
import matteroverdrive.util.MOStringHelper;

/**
 * Created by Simeon on 8/16/2015.
 */
public class AutoConfigPage extends ElementBaseGroup
{
    protected ComponentConfigs componentConfigs;
    protected MOGuiMachine machineGui;
    protected int xStart = 48;
    protected int yStart = 36;

    public AutoConfigPage(MOGuiMachine gui, int posX, int posY, int width, int height,ComponentConfigs configurable)
    {
        super(gui, posX, posY, width, height);
        this.componentConfigs = configurable;
        this.machineGui = gui;
    }

    @Override
    public void init()
    {
        super.init();
        int yPos = yStart;
        for (IConfigProperty configProperty : componentConfigs.getValues().values())
        {
            if (configProperty instanceof ConfigPropertyBoolean)
            {
                ElementCheckbox elementCheckbox = new ElementCheckbox(gui,this,xStart,yPos,configProperty.getKey(),(boolean)configProperty.getValue());
                elementCheckbox.setCheckboxLabel(MOStringHelper.translateToLocal(configProperty.getUnlocalizedName()));
                addElement(elementCheckbox);
                yPos+= elementCheckbox.getHeight() + 4;
            }
            else if (configProperty instanceof ConfigPropertyInteger)
            {
                if (configProperty instanceof ConfigPropertyStringList)
                {
                    ElementStates elementStates = new ElementStates(gui,this,xStart,yPos,configProperty.getKey(),((ConfigPropertyStringList) configProperty).getList());
                    elementStates.setSelectedState((Integer)configProperty.getValue());
                    elementStates.setLabel(MOStringHelper.translateToLocal(configProperty.getUnlocalizedName()));
                    addElement(elementStates);
                    yPos += elementStates.getHeight() + 4;
                }
            }
        }
    }

    @Override
    public void handleElementButtonClick(ElementBase element,String name, int mouseButton)
    {
        for (IConfigProperty configProperty : componentConfigs.getValues().values()) {
            if (configProperty instanceof ConfigPropertyBoolean) {
                if (name.equals(configProperty.getKey()) && element instanceof ElementCheckbox)
                {
                    configProperty.setValue(((ElementCheckbox) element).getState());
                    componentConfigs.getMachine().sendConfigsToServer(true);
                }
            }else if (configProperty instanceof ConfigPropertyInteger)
            {
                if (name.equals(configProperty.getKey()) && element instanceof ElementStates)
                {
                    configProperty.setValue(mouseButton);
                    componentConfigs.getMachine().sendConfigsToServer(true);
                }
            }
        }
    }

    public void setStart(int x,int y)
    {
        this.xStart = x;
        this.yStart = y;
    }
}
