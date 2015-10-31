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

import matteroverdrive.container.slot.MOSlot;
import matteroverdrive.container.slot.SlotWeaponModule;
import matteroverdrive.tile.TileEntityWeaponStation;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 4/13/2015.
 */
public class ContainerWeaponStation extends ContainerMachine<TileEntityWeaponStation>
{
    public ContainerWeaponStation(InventoryPlayer playerInventory, TileEntityWeaponStation machine)
    {
        this.machine = machine;
        init(playerInventory);
    }

    @Override
    protected void init(InventoryPlayer inventory)
    {
        addSlotToContainer(new MOSlot(machine, machine.INPUT_SLOT, 8, 55));
        for (int i = 0;i < 5;i++)
        {
            addSlotToContainer(new SlotWeaponModule(machine, i, 0, 0, i));
        }

        addUpgradeSlots(machine.getInventoryContainer());
        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 150, true, true);
    }

}
