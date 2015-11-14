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

package matteroverdrive.container;

import matteroverdrive.container.slot.SlotInventory;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.inventory.InscriberSlot;
import matteroverdrive.data.inventory.Slot;
import matteroverdrive.tile.TileEntityInscriber;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

/**
 * Created by Simeon on 11/12/2015.
 */
public class ContainerInscriber extends ContainerMachine<TileEntityInscriber>
{
    public ContainerInscriber()
    {
        super();
    }

    public ContainerInscriber(InventoryPlayer inventory, TileEntityInscriber machine)
    {
        super(inventory,machine);
        onCraftMatrixChanged(machine);
    }

    @Override
    public void init(InventoryPlayer inventory)
    {
        Inventory machineInventory = machine.getInventoryContainer();
        for (matteroverdrive.data.inventory.Slot slot : machineInventory.getSlots())
        {
            if (slot instanceof InscriberSlot)
            {
                addSlotToContainer(new InputSlot(machineInventory, slot, 0, 0));
            }
            else
            {
                addSlotToContainer(new SlotInventory(machineInventory, slot, 0, 0));
            }
        }
        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 89, true, true);
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventory)
    {
        machine.calculateRecipe();
    }

    private class InputSlot extends SlotInventory
    {
        public InputSlot(IInventory inventory, Slot slot, int x, int y) {
            super(inventory, slot, x, y);
        }

        @Override
        public void onSlotChanged()
        {
            machine.calculateRecipe();
        }
    }
}
