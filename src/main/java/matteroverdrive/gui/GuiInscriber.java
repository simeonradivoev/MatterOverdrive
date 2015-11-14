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

import cofh.lib.gui.element.ElementDualScaled;
import cofh.lib.util.helpers.MathHelper;
import matteroverdrive.Reference;
import matteroverdrive.container.ContainerInscriber;
import matteroverdrive.container.ContainerMachine;
import matteroverdrive.gui.element.ElementInventorySlot;
import matteroverdrive.gui.element.ElementSlot;
import matteroverdrive.gui.element.MOElementEnergy;
import matteroverdrive.tile.TileEntityInscriber;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 11/12/2015.
 */
public class GuiInscriber extends MOGuiMachine<TileEntityInscriber>
{
    MOElementEnergy energyElement;
    ElementDualScaled inscribe_progress;
    ElementSlot outputSlot;

    public GuiInscriber(InventoryPlayer inventoryPlayer, TileEntityInscriber machine) {
        super(new ContainerInscriber(inventoryPlayer,machine), machine);
        name="inscriber";
        energyElement = new MOElementEnergy(this,100,39,machine.getEnergyStorage());
        inscribe_progress = new ElementDualScaled(this,32,55);
        outputSlot = new ElementInventorySlot(this,getContainer().getSlotAt(machine.OUTPUT_SLOT_ID),129,55,22,22,"big");

        inscribe_progress.setMode(1);
        inscribe_progress.setSize(24, 16);
        inscribe_progress.setTexture(Reference.TEXTURE_ARROW_PROGRESS, 48, 16);
        energyElement.setTexture(Reference.TEXTURE_ENERGY_METER, 32, 64);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        pages.get(0).addElement(outputSlot);
        pages.get(0).addElement(energyElement);
        this.addElement(inscribe_progress);

        AddMainPlayerSlots(this.inventorySlots, pages.get(0));
        AddHotbarPlayerSlots(this.inventorySlots,this);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_,
                                                   int p_146976_2_, int p_146976_3_)
    {
        super.drawGuiContainerBackgroundLayer(p_146976_1_, p_146976_2_, p_146976_3_);
        inscribe_progress.setQuantity(MathHelper.round((((ContainerMachine)getContainer()).getProgress() * 24)));
    }
}
