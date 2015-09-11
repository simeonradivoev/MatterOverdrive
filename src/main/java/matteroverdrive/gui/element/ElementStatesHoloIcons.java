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

import cofh.lib.gui.GuiBase;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.container.IButtonHandler;

/**
 * Created by Simeon on 8/28/2015.
 */
public class ElementStatesHoloIcons extends MOElementButtonScaled
{
    HoloIcon[] states;
    int selectedState;

    public ElementStatesHoloIcons(GuiBase gui, IButtonHandler buttonHandler, int posX, int posY, int sizeX, int sizeY, String name, HoloIcon[] states)
    {
        super(gui, buttonHandler, posX, posY,name,sizeX,sizeY);
        this.states = states;
    }

    public HoloIcon[] getStates()
    {
        return states;
    }

    public void setStates(HoloIcon[] states)
    {
        this.states = states;
    }

    public void setSelectedState(int selectedState)
    {
        this.selectedState = selectedState;
        this.icon = states[selectedState];
    }

    public int getSelectedState()
    {
        return selectedState;
    }

    @Override
    public void onAction(int mouseX, int mouseY,int mouseButton)
    {
        selectedState++;
        if (selectedState >= states.length)
            selectedState = 0;

        if (selectedState < states.length)
            icon = states[selectedState];


        buttonHandler.handleElementButtonClick(this,name,selectedState);
    }
}
