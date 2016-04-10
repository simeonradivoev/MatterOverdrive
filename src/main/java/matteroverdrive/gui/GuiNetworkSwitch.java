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
import matteroverdrive.gui.element.ElementConnections;
import matteroverdrive.tile.TileEntityMachineNetworkSwitch;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 5/1/2015.
 */
public class GuiNetworkSwitch extends MOGuiMachine<TileEntityMachineNetworkSwitch>
{
	ElementConnections connections;

	public GuiNetworkSwitch(InventoryPlayer inventoryPlayer, TileEntityMachineNetworkSwitch entity)
	{
		super(ContainerFactory.createMachineContainer(entity, inventoryPlayer), entity);
		name = "network_switch";
		connections = new ElementConnections(this, 50, 42, xSize - 74, ySize, machine);
	}

	@Override
	public void initGui()
	{
		super.initGui();
		pages.get(0).addElement(connections);
		AddHotbarPlayerSlots(inventorySlots, this);
	}
}
