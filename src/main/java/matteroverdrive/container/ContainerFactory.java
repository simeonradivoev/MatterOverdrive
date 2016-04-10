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

import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 7/23/2015.
 */
public class ContainerFactory
{
	public static <T extends MOTileEntityMachine> ContainerMachine<T> createMachineContainer(T machine, InventoryPlayer inventoryPlayer)
	{
		ContainerMachine<T> containerMachine = new ContainerMachine<>(inventoryPlayer, machine);
		containerMachine.addAllSlotsFromInventory(machine.getInventoryContainer());
		MOContainerHelper.AddPlayerSlots(inventoryPlayer, containerMachine, 45, 89, machine.hasPlayerSlotsMain(), machine.hasPlayerSlotsHotbar());
		return containerMachine;
	}
}
