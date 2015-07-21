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

import cofh.lib.util.helpers.InventoryHelper;
import matteroverdrive.container.slot.SlotInventory;
import matteroverdrive.data.Inventory;
import matteroverdrive.data.inventory.UpgradeSlot;
import matteroverdrive.machines.MOTileEntityMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Simeon on 4/9/2015.
 */
public class ContainerMachine<T extends MOTileEntityMachine> extends MOBaseContainer
{
    protected T machine;

    public ContainerMachine()
    {
        super();
    }

    public ContainerMachine(InventoryPlayer inventory,T machine)
    {
        super(inventory);
        this.machine = machine;
        init(inventory);
    }

    protected void init(InventoryPlayer inventory)
    {
        AddUpgradeSlots(machine.getInventoryContainer(), 77, 52);
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }

    public void AddUpgradeSlots(Inventory inventory,int x, int y)
    {
        int upgradeSlotIndex = 0;

        for (int i = 0;i < inventory.getSizeInventory();i++)
        {
            if (inventory.getSlot(i) instanceof UpgradeSlot)
            {
                addSlotToContainer(new SlotInventory(inventory,inventory.getSlot(i),x + (upgradeSlotIndex % 5) * 24,y + (upgradeSlotIndex / 5) * 24));
                upgradeSlotIndex++;
            }
        }
    }

    public void addAllSlotsFromInventory(Inventory inventory)
    {
        for (matteroverdrive.data.inventory.Slot slot : inventory.getSlots())
        {
            addSlotToContainer(new SlotInventory(inventory,inventory.getSlot(slot.getId()),0,0));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotID)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotID);

        if(slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if(slotID < machine.getSizeInventory())
            {
                putInPlayerInventory(itemstack1);
            }
            else if(slotID >= machine.getSizeInventory())
            {
                tryAndPutInMachineSlots(itemstack1,machine);
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
        return InventoryHelper.mergeItemStack((List<Slot>)inventorySlots,itemStack,machine.getSizeInventory(),inventorySlots.size() - machine.getSizeInventory(),true,true);
    }

    protected boolean tryAndPutInMachineSlots(ItemStack itemStack,IInventory inventory)
    {
        return InventoryHelper.mergeItemStack((List<Slot>)inventorySlots,itemStack,0,inventory.getSizeInventory(),false,true);
    }

    public T getMachine()
    {
        return machine;
    }
}
