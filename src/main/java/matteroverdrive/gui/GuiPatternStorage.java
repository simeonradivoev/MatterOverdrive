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

import matteroverdrive.Reference;
import matteroverdrive.container.ContainerFactory;
import matteroverdrive.container.slot.MOSlot;
import matteroverdrive.container.slot.SlotInventory;
import matteroverdrive.data.inventory.PatternStorageSlot;
import matteroverdrive.gui.element.ElementInventorySlot;
import matteroverdrive.gui.element.MOElementEnergy;
import matteroverdrive.tile.TileEntityMachinePatternStorage;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

/**
 * Created by Simeon on 3/27/2015.
 */
public class GuiPatternStorage extends MOGuiMachine<TileEntityMachinePatternStorage>
{
    MOElementEnergy energyElement;

    public GuiPatternStorage(InventoryPlayer playerInventory,TileEntityMachinePatternStorage patternStorage)
    {
        super(ContainerFactory.createMachineContainer(patternStorage,playerInventory),patternStorage);
        name = "pattern_storage";
        energyElement = new MOElementEnergy(this,176,39,patternStorage.getEnergyStorage());
        energyElement.setTexture(Reference.TEXTURE_ENERGY_METER, 32, 64);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        pages.get(0).addElement(energyElement);
        AddPatternStorageSlots(inventorySlots, pages.get(0));
        AddMainPlayerSlots(inventorySlots, pages.get(0));
        AddHotbarPlayerSlots(inventorySlots, this);
    }

    public void AddPatternStorageSlots(Container container,GuiElementList list)
    {
        int slotXIndex = 0;
        for (int i = 0;i < container.inventorySlots.size();i++)
        {
            if(container.inventorySlots.get(i) instanceof SlotInventory && ((SlotInventory) container.inventorySlots.get(i)).getSlot() instanceof PatternStorageSlot)
            {
                int x = (slotXIndex % 3 * 24) + 77;
                int y = (slotXIndex / 3) * 24 + 37;
                list.addElement(new ElementInventorySlot(this, (MOSlot)container.inventorySlots.get(i),x,y,22,22, "big",machine.getInventoryContainer().getSlot(i).getHoloIcon()));
                slotXIndex++;
            }
        }
    }

}
