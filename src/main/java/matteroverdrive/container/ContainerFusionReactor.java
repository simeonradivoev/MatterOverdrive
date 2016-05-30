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

import matteroverdrive.machines.fusionReactorController.TileEntityMachineFusionReactorController;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 5/17/2015.
 */
public class ContainerFusionReactor extends ContainerMachine<TileEntityMachineFusionReactorController>
{
	private int energyPerTick;

	public ContainerFusionReactor(InventoryPlayer inventory, TileEntityMachineFusionReactorController machine)
	{
		super(inventory, machine);
	}

	@Override
	public void init(InventoryPlayer inventory)
	{
		addAllSlotsFromInventory(machine.getInventoryContainer());
		MOContainerHelper.AddPlayerSlots(inventory, this, 45, 89, false, true);
	}

	@Override
	public void addListener(IContainerListener icrafting)
	{
		super.addListener(icrafting);
		icrafting.sendProgressBarUpdate(this, 1, this.machine.getEnergyPerTick());
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for (IContainerListener listener : this.listeners)
		{
			if (this.energyPerTick != this.machine.getEnergyPerTick())
			{
				listener.sendProgressBarUpdate(this, 1, this.machine.getEnergyPerTick());
			}
		}

		this.energyPerTick = this.machine.getEnergyPerTick();
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int slot, int newValue)
	{
		super.updateProgressBar(slot, newValue);
		if (slot == 1)
		{
			energyPerTick = newValue;
		}
	}

	public int getEnergyPerTick()
	{
		return energyPerTick;
	}
}
