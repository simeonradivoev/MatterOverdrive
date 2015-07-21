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
import matteroverdrive.machines.transporter.TileEntityMachineTransporter;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 5/3/2015.
 */
public class ContainerTransporter extends ContainerMachine<TileEntityMachineTransporter>
{
    public ContainerTransporter(InventoryPlayer inventory, TileEntityMachineTransporter machine)
    {
        super(inventory, machine);
    }

    @Override
    public void init(InventoryPlayer inventory)
    {
        addSlotToContainer(new SlotEnergy(machine.getInventoryContainer(),machine.getEnergySlotID(),8,55));
        super.init(inventory);
        MOContainerHelper.AddPlayerSlots(inventory, this, 45, 89, false, true);
    }
}
