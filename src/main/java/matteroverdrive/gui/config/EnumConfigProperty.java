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

package matteroverdrive.gui.config;


import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Simeon on 9/10/2015.
 */
public class EnumConfigProperty extends GuiConfigEntries.SelectValueEntry
{
    public EnumConfigProperty(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement) {
        super(owningScreen, owningEntryList, configElement, getSelectableValues(configElement));
    }

    @Override
    public void updateValueButtonText()
    {
        super.updateValueButtonText();
        btnValue.displayString = configElement.getValidValues()[Integer.parseInt(getCurrentValue())];
    }

    private static Map<Object, String> getSelectableValues(IConfigElement configElement)
    {
        Map<Object, String> selectableValues = new TreeMap<>();

        for (int i = 0;i < configElement.getValidValues().length;i++)
        {
            selectableValues.put(i,configElement.getValidValues()[i]);
        }

        return selectableValues;
    }
}
