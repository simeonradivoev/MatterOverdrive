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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.container.slot.SlotEnergy;
import matteroverdrive.container.slot.SlotInventory;
import matteroverdrive.container.slot.SlotRemoveOnly;
import matteroverdrive.tile.TileEntityMachineReplicator;
import matteroverdrive.util.MOContainerHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerReplicator extends ContainerMachine<TileEntityMachineReplicator>
{
	private int lastReplicateProgress;

	public ContainerReplicator(InventoryPlayer inventory,TileEntityMachineReplicator tileentity)
	{
		super(inventory, tileentity);
	}

	@Override
	public void init(InventoryPlayer inventory)
	{
		this.addSlotToContainer(new SlotRemoveOnly(machine,machine.OUTPUT_SLOT_ID,70,52));
		this.addSlotToContainer(new SlotRemoveOnly(machine,machine.SECOUND_OUTPUT_SLOT_ID,96,52));
		this.addSlotToContainer(new SlotInventory(machine, machine.getInventoryContainer().getSlot(machine.DATABASE_SLOT_ID),8,79));
		this.addSlotToContainer(new SlotInventory(machine, machine.getInventoryContainer().getSlot(this.machine.SHIELDING_SLOT_ID),8,106));
		this.addSlotToContainer(new SlotEnergy(machine, this.machine.getEnergySlotID(),8,133));

		super.init(inventory);
		MOContainerHelper.AddPlayerSlots(inventory, this,45,89,true,true);
	}

	@Override
	public void addCraftingToCrafters(ICrafting icrafting)
	{
		super.addCraftingToCrafters(icrafting);
		icrafting.sendProgressBarUpdate(this, 0, this.machine.replicateProgress);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for(int i = 0;i < this.crafters.size();i++)
		{
			ICrafting icrafting = (ICrafting)this.crafters.get(i);
			
			if(this.lastReplicateProgress != this.machine.replicateProgress)
			{
				icrafting.sendProgressBarUpdate(this, 0, this.machine.replicateProgress);
			}

			this.lastReplicateProgress = this.machine.replicateProgress;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int slot,int newValue)
	{
		if(slot == 0)
			this.machine.replicateProgress = newValue;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) 
	{
		return true;
	}
}
