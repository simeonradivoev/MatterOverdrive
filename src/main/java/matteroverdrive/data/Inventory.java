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

package matteroverdrive.data;

import matteroverdrive.data.inventory.Slot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Simeon on 3/16/2015.
 */
public class Inventory implements IInventory
{
    List<Slot> slots;
    String name;
    IUsableCondition usableCondition;

    //region Constructors
    public Inventory(String name)
    {
        this(name, new ArrayList<>());
    }

    public Inventory(String name,Collection<Slot> slots)
    {
        this(name, slots, null);
    }

    public Inventory(String name,Collection<Slot> slots, IUsableCondition usableCondition)
    {
        this.slots = new ArrayList<>(slots);
        this.name = name;
        this.usableCondition = usableCondition;
    }


    //endregion

    public int AddSlot(Slot slot)
    {
        if(slots.add(slot))
        {
            slot.setId(slots.size() - 1);
            return slots.size() - 1;
        }
        return 0;
    }

    public void setUsableCondition(IUsableCondition condition)
    {
        this.usableCondition = condition;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        NBTTagList nbttaglist = compound.getTagList("Items", 10);
        for (Slot slot : slots)
        {
            slot.setItem(null);
        }

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            setInventorySlotContents(b0, ItemStack.loadItemStackFromNBT(nbttagcompound1));
        }
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < getSizeInventory(); ++i)
        {
            if (getStackInSlot(i) != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                getStackInSlot(i).writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        compound.setTag("Items", nbttaglist);
    }

    @Override
    public int getSizeInventory()
    {
        return slots.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        if(slot >= 0 && slot < getSizeInventory())
        {
            return slots.get(slot).getItem();
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int size)
    {
        if (this.slots.get(slot) != null && this.slots.get(slot).getItem() != null)
        {
            ItemStack itemstack;

            if (this.slots.get(slot).getItem().stackSize <= size)
            {
                itemstack = this.slots.get(slot).getItem();
                this.slots.get(slot).setItem(null);

                return itemstack;
            }
            else
            {
                itemstack = this.slots.get(slot).getItem().splitStack(size);

                if (this.slots.get(slot).getItem().stackSize == 0)
                {
                    this.slots.get(slot).setItem(null);
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        if(this.slots.get(slot) != null)
        {
            ItemStack itemstack = this.slots.get(slot).getItem();
            this.slots.set(slot, null);
            return itemstack;
        }

        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack item)
    {
        this.slots.get(slot).setItem(item);

        if(item != null && item.stackSize > this.getInventoryStackLimit())
        {
            item.stackSize = this.getInventoryStackLimit();
        }
    }

    public void addItem(ItemStack itemStack)
    {
        for (int i = 0;i < slots.size();i++)
        {
            if (slots.get(i).isValidForSlot(itemStack))
            {
                if (slots.get(i).getItem() == null)
                {
                    slots.get(i).setItem(itemStack);
                    return;
                }else if (ItemStack.areItemStacksEqual(slots.get(i).getItem(),itemStack) && slots.get(i).getItem().stackSize < slots.get(i).getItem().getMaxStackSize())
                {
                    int newStackSize = Math.min(slots.get(i).getItem().stackSize+itemStack.stackSize,slots.get(i).getItem().getMaxStackSize());
                    int leftStackSize =  slots.get(i).getItem().stackSize + itemStack.stackSize - newStackSize;
                    slots.get(i).getItem().stackSize = newStackSize;
                    if (leftStackSize <= 0)
                        return;

                    itemStack.stackSize=newStackSize;
                }
            }
        }
    }

    @Override
    public String getInventoryName() {
        return this.name;
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return name != null && !name.isEmpty();
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void markDirty()
    {

    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return true;

    }

    @Override
    public void openInventory()
    {

    }

    @Override
    public void closeInventory()
    {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack item)
    {
        if (slot >= 0 && slot < getSizeInventory() && slots.get(slot) != null)
        {
            if (slots.get(slot).getItem() != null)
            {
                if (slots.get(slot).getItem().stackSize <= slots.get(slot).getMaxStackSize())
                {
                    return slots.get(slot).isValidForSlot(item);
                }else
                {
                    return false;
                }
            }
            return slots.get(slot).isValidForSlot(item);
        }
        return true;
    }

    public Slot getSlot(int slotID) {
        return slots.get(slotID);
    }

    public int getLastSlotId()
    {
        return slots.size() - 1;
    }

    public List<Slot> getSlots() {
        return slots;
    }
}
