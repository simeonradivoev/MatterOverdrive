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

import matteroverdrive.container.slot.SlotEnergy;
import matteroverdrive.container.slot.SlotInventory;
import matteroverdrive.data.inventory.PatternStorageSlot;
import matteroverdrive.data.inventory.Slot;
import matteroverdrive.tile.TileEntityMachinePatternStorage;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 3/27/2015.
 */
public class ContainerPatternStorage extends ContainerMachine<TileEntityMachinePatternStorage>
{
    public ContainerPatternStorage(InventoryPlayer inventoryPlayer,TileEntityMachinePatternStorage patternStorage)
    {
        super(inventoryPlayer, patternStorage);
    }

    @Override
    public void init(InventoryPlayer inventoryPlayer)
    {
        this.addSlotToContainer(new SlotInventory(machine, machine.getInventoryContainer().getSlot(machine.input_slot), 8, 55));

        int slotXIndex = 0;
        for (Slot slot : machine.getInventoryContainer().getSlots())
        {
            if (slot instanceof PatternStorageSlot)
            {
                int x = (slotXIndex % 3 * 24) + 77;
                int y = (slotXIndex / 3) * 24 + 37;
                this.addSlotToContainer(new SlotInventory(machine,slot,x,y));
                slotXIndex ++;
            }
        }

        this.addSlotToContainer(new SlotEnergy(machine, machine.getEnergySlotID(),8,82));

        super.init(inventoryPlayer);

        MOContainerHelper.AddPlayerSlots(inventoryPlayer, this, 45, 89,true,true);
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_)
    {
        return true;
    }
}
