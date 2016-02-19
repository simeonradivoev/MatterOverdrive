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
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.gui.MOGuiMachine;
import matteroverdrive.gui.element.*;
import matteroverdrive.machines.components.ComponentConfigs;
import matteroverdrive.machines.configs.*;
import matteroverdrive.util.MOStringHelper;

/**
 * Created by Simeon on 8/16/2015.
 */
public class AutoConfigPage extends ElementBaseGroup
{
    private final ComponentConfigs componentConfigs;
    protected final MOGuiMachine machineGui;

    public AutoConfigPage(MOGuiMachine gui, int posX, int posY, int width, int height)
    {
        super(gui, posX, posY, width, height);
        this.componentConfigs = gui.getMachine().getConfigs();
        this.machineGui = gui;
    }

    @Override
    public void init()
    {
        super.init();
        int yPos = 0;
        for (IConfigProperty configProperty : componentConfigs.getValues().values())
        {
            if (configProperty instanceof ConfigPropertyBoolean)
            {
                ElementCheckbox elementCheckbox = new ElementCheckbox(gui,this,0,yPos,configProperty.getKey(),(boolean)configProperty.getValue());
                elementCheckbox.setCheckboxLabel(MOStringHelper.translateToLocal(configProperty.getUnlocalizedName()));
                addElement(elementCheckbox);
                yPos+= elementCheckbox.getHeight() + 4;
            }
            else if (configProperty instanceof ConfigPropertyInteger)
            {
                if (configProperty instanceof ConfigPropertyStringList)
                {
                    ElementStates elementStates = new ElementStates(gui,this,0,yPos,configProperty.getKey(),((ConfigPropertyStringList) configProperty).getList());
                    elementStates.setSelectedState((Integer)configProperty.getValue());
                    elementStates.setLabel(MOStringHelper.translateToLocal(configProperty.getUnlocalizedName()));
                    elementStates.setTextColor(Reference.COLOR_HOLO.getColor());
                    addElement(elementStates);
                    yPos += elementStates.getHeight() + 4;
                }else
                {
                    ElementIntegerField elementIntegerField = new ElementIntegerField(gui,this,0,yPos,20,((ConfigPropertyInteger) configProperty).getMin(),((ConfigPropertyInteger) configProperty).getMax());
                    elementIntegerField.setNumber((Integer) configProperty.getValue());
                    elementIntegerField.setName(configProperty.getKey());
                    elementIntegerField.setLabel(MOStringHelper.translateToLocal(configProperty.getUnlocalizedName()));
                    elementIntegerField.setLabelColor(Reference.COLOR_HOLO.getColor());
                    elementIntegerField.init();
                    addElement(elementIntegerField);
                    yPos += elementIntegerField.getHeight() + 4;
                }
            }else if (configProperty instanceof ConfigPropertyString)
            {
                MOElementTextField textField = new MOElementTextField(gui,this,8,yPos + 2,sizeX,20);
                textField.setMaxLength(((ConfigPropertyString) configProperty).getMaxLength());
                textField.setName(configProperty.getKey());
                textField.setText(configProperty.getValue().toString());
                textField.setBackground(MOElementButtonScaled.HOVER_TEXTURE_DARK);
                textField.setTextOffset(8,4);
                if (((ConfigPropertyString) configProperty).getPattern() != null)
                    textField.setFilter(((ConfigPropertyString) configProperty).getPattern(),false);
                addElement(textField);
                yPos += textField.getHeight() + 4;
            }
        }
    }

    @Override
    public void handleElementButtonClick(MOElementBase element,String name, int mouseButton)
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
                if (name.equals(configProperty.getKey()))
                {
                    int state = 0;
                    if (element instanceof ElementStates)
                    {
                        state = mouseButton;
                    }else if (element instanceof ElementIntegerField)
                    {
                        state = ((ElementIntegerField) element).getNumber();
                    }
                    configProperty.setValue(state);
                    componentConfigs.getMachine().sendConfigsToServer(true);
                }
            }
        }
    }

    @Override
    public void textChanged(String elementName, String text, boolean typed)
    {
        for (IConfigProperty configProperty : componentConfigs.getValues().values())
        {
            if (configProperty instanceof ConfigPropertyString)
            {
                if (elementName.equals(configProperty.getKey()))
                {
                    configProperty.setValue(text);
                    componentConfigs.getMachine().sendConfigsToServer(true);
                }
            }
        }
    }
}
