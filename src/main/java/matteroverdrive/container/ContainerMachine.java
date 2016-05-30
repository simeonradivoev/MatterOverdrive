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

import matteroverdrive.api.container.IMachineWatcher;
import matteroverdrive.container.slot.SlotInventory;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.inventory.UpgradeSlot;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.util.MOInventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 4/9/2015.
 */
public class ContainerMachine<T extends MOTileEntityMachine> extends MOBaseContainer implements IMachineWatcher
{
	protected T machine;
	EntityPlayer entityPlayer;
	private int progressScaled;

	public ContainerMachine()
	{
		super();
	}

	public ContainerMachine(InventoryPlayer inventory, T machine)
	{
		super(inventory);
		this.machine = machine;
		entityPlayer = inventory.player;
		init(inventory);
	}

	protected void init(InventoryPlayer inventory)
	{

	}

	@Override
	public void addListener(IContainerListener icrafting)
	{
		super.addListener(icrafting);
		if (icrafting instanceof EntityPlayerMP)
		{
			machine.addWatcher(this);
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		if (playerIn instanceof EntityPlayerMP)
		{
			machine.removeWatcher(this);
		}
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		if (this.machine != null)
		{
			short progressScaled = (short)(this.machine.getProgress() * Short.MAX_VALUE);
			for (IContainerListener listener : this.listeners)
			{
				if (this.progressScaled != progressScaled)
				{
					listener.sendProgressBarUpdate(this, 0, progressScaled);
				}
			}

			this.progressScaled = progressScaled;
		}
	}

	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int slot, int newValue)
	{
		if (slot == 0)
		{
			this.progressScaled = newValue;
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}

	public void addUpgradeSlots(Inventory inventory)
	{
		addUpgradeSlots(inventory, 0, 0);
	}

	public void addUpgradeSlots(Inventory inventory, int x, int y)
	{
		int upgradeSlotIndex = 0;

		for (int i = 0; i < inventory.getSizeInventory(); i++)
		{
			if (inventory.getSlot(i) instanceof UpgradeSlot)
			{
				addSlotToContainer(new SlotInventory(inventory, inventory.getSlot(i), x + (upgradeSlotIndex % 5) * 24, y + (upgradeSlotIndex / 5) * 24));
				upgradeSlotIndex++;
			}
		}
	}

	public void addAllSlotsFromInventory(Inventory inventory)
	{
		for (matteroverdrive.data.inventory.Slot slot : inventory.getSlots())
		{
			addSlotToContainer(new SlotInventory(inventory, inventory.getSlot(slot.getId()), 0, 0));
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotID)
	{
		ItemStack itemstack = null;
		Slot slot = this.inventorySlots.get(slotID);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (slotID < machine.getSizeInventory())
			{
				putInPlayerInventory(itemstack1);
			}
			else if (slotID >= machine.getSizeInventory())
			{
				tryAndPutInMachineSlots(itemstack1, machine);
			}

			if (itemstack1.stackSize == 0)
			{
				slot.putStack(null);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}

			slot.onPickupFromSlot(player, itemstack1);
		}

		return itemstack;
	}

	protected boolean putInPlayerInventory(ItemStack itemStack)
	{
		return MOInventoryHelper.mergeItemStack(inventorySlots, itemStack, machine.getSizeInventory(), inventorySlots.size() - machine.getSizeInventory(), true, true);
	}

	protected boolean tryAndPutInMachineSlots(ItemStack itemStack, IInventory inventory)
	{
		return MOInventoryHelper.mergeItemStack(inventorySlots, itemStack, 0, inventory.getSizeInventory(), false, true);
	}

	public T getMachine()
	{
		return machine;
	}

	public float getProgress()
	{
		return (float)progressScaled / (float)Short.MAX_VALUE;
	}

	@Override
	public EntityPlayer getPlayer()
	{
		return entityPlayer;
	}

	@Override
	public void onWatcherAdded(MOTileEntityMachine machine)
	{

	}

	@Override
	public boolean isWatcherValid()
	{
		if (entityPlayer instanceof EntityPlayerMP)
		{
			return ((EntityPlayerMP)entityPlayer).getServerWorld().getPlayerEntityByUUID(entityPlayer.getUniqueID()) != null;
		}
		return false;
	}
}
