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

package matteroverdrive.gui;

import matteroverdrive.container.ContainerHoloSign;
import matteroverdrive.gui.element.MOElementButton;
import matteroverdrive.gui.element.MOElementTextField;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.tile.TileEntityHoloSign;
import net.minecraft.entity.player.InventoryPlayer;

import java.util.EnumSet;

/**
 * Created by Simeon on 8/15/2015.
 */
public class GuiHoloSign extends MOGuiBase
{
    TileEntityHoloSign sign;
    MOElementTextField textField;

    public GuiHoloSign(InventoryPlayer inventoryPlayer,TileEntityHoloSign sign)
    {
        super(new ContainerHoloSign(inventoryPlayer,sign));
        this.sign = sign;
        textField = new MOElementTextField(this,this,50,36,150,115);
        textField.setBackground(MOElementButton.HOVER_TEXTURE_DARK);
        textField.setMultiline(true);
        textField.setMaxLength((short)1024);
        textField.setTextOffset(4,4);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        addElement(textField);
        textField.setText(sign.getText());
    }

    @Override
    public void ListSelectionChange(String name, int selected)
    {

    }

    @Override
    public void textChanged(String elementName, String text, boolean typed)
    {
        sign.setText(text);
        sign.sendNBTToServer(EnumSet.of(MachineNBTCategory.GUI));
    }
}
