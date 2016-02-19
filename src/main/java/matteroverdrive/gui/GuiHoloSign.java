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

import matteroverdrive.container.ContainerFactory;
import matteroverdrive.container.MOBaseContainer;
import matteroverdrive.gui.element.MOElementButton;
import matteroverdrive.gui.element.MOElementTextField;
import matteroverdrive.gui.pages.AutoConfigPage;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.tile.TileEntityHoloSign;
import net.minecraft.entity.player.InventoryPlayer;

import java.util.EnumSet;

/**
 * Created by Simeon on 8/15/2015.
 */
public class GuiHoloSign extends MOGuiMachine<TileEntityHoloSign>
{
    MOElementTextField textField;
    AutoConfigPage configPage;

    public GuiHoloSign(InventoryPlayer inventoryPlayer,TileEntityHoloSign sign)
    {
        super(ContainerFactory.createMachineContainer(sign,inventoryPlayer),sign);
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
        pages.get(0).addElement(textField);
        textField.setText(machine.getText());
    }

    @Override
    public void registerPages(MOBaseContainer container,TileEntityHoloSign machine)
    {
        super.registerPages(container, machine);
        configPage = new AutoConfigPage(this,48,32,xSize-76,ySize);
        elements.remove(pages.get(1));
        pages.set(1, configPage);
    }

    @Override
    public void textChanged(String elementName, String text, boolean typed)
    {
        machine.setText(text);
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();
        machine.sendNBTToServer(EnumSet.of(MachineNBTCategory.GUI),true,false);
    }
}
