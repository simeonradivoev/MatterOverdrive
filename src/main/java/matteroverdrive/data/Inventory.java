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
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Simeon on 3/16/2015.
 */
public class Inventory implements IInventory
{
    final List<Slot> slots;
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
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");
            if (nbttagcompound1.hasKey("id"))
            {
                setInventorySlotContents(b0, ItemStack.loadItemStackFromNBT(nbttagcompound1));
            }else
            {
                setInventorySlotContents(b0,null);
            }
        }
    }

    public void writeToNBT(NBTTagCompound compound,boolean toDisk)
    {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < getSizeInventory(); ++i)
        {
            writeSlotToNBT(nbttaglist,i,toDisk);
        }

        if (nbttaglist.tagCount() > 0)
        {
            compound.setTag("Items", nbttaglist);
        }
    }

    protected void writeSlotToNBT(NBTTagList nbttaglist,int slotId,boolean toDisk)
    {
        Slot slot = getSlot(slotId);
        if (slot != null)
        {
            if (toDisk && slot.getItem() != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) slotId);
                if (slot.getItem() != null)
                {
                    slot.getItem().writeToNBT(nbttagcompound1);
                }
                nbttaglist.appendTag(nbttagcompound1);
            }
            else if (!toDisk && slot.sendsToClient())
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) slotId);
                if (slot.getItem() != null)
                    slot.getItem().writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
    }

    @Override
    public int getSizeInventory()
    {
        return slots.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return slots.get(slot).getItem();
    }

    @Override
    public ItemStack decrStackSize(int slotId, int size)
    {
        Slot slot = getSlot(slotId);
        if (slot != null && slot.getItem() != null)
        {
            ItemStack itemstack;

            if (slot.getItem().stackSize <= size)
            {
                itemstack = slot.getItem();
                slot.setItem(null);

                return itemstack;
            }
            else
            {
                itemstack = slot.getItem().splitStack(size);

                if (slot.getItem().stackSize == 0)
                {
                    slot.setItem(null);
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
    public ItemStack removeStackFromSlot(int index)
    {
        ItemStack itemStack = getSlot(index).getItem();
        getSlot(index).setItem(null);
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack item)
    {
        getSlot(slot).setItem(item);

        if(item != null && item.stackSize > this.getInventoryStackLimit())
        {
            item.stackSize = this.getInventoryStackLimit();
        }
    }

    public void addItem(ItemStack itemStack)
    {
        for (int i = 0;i < slots.size();i++)
        {
            Slot slot = getSlot(i);
            if (slot.isValidForSlot(itemStack))
            {
                if (slot.getItem() == null)
                {
                    slot.setItem(itemStack);
                    return;
                }else if (ItemStack.areItemStacksEqual(slot.getItem(),itemStack) && slot.getItem().stackSize < slot.getItem().getMaxStackSize())
                {
                    int newStackSize = Math.min(slot.getItem().stackSize+itemStack.stackSize,slot.getItem().getMaxStackSize());
                    int leftStackSize =  slot.getItem().stackSize + itemStack.stackSize - newStackSize;
                    slot.getItem().stackSize = newStackSize;
                    if (leftStackSize <= 0)
                        return;

                    itemStack.stackSize=newStackSize;
                }
            }
        }
    }

    public void clearItems()
    {
        for (Slot slot : slots)
        {
            slot.setItem(null);
        }
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public boolean hasCustomName()
    {
        return name != null && !name.isEmpty();
    }

    @Override
    public IChatComponent getDisplayName()
    {
        return new ChatComponentText(this.name);
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
    public void openInventory(EntityPlayer entityPlayer)
    {

    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer)
    {

    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack item)
    {
        if (slotID >= 0 && slotID < getSizeInventory() && getSlot(slotID) != null)
        {
            Slot slot = getSlot(slotID);
            if (slot.getItem() != null)
            {
                if (slot.getItem().stackSize <= slot.getMaxStackSize())
                {
                    return slot.isValidForSlot(item);
                }else
                {
                    return false;
                }
            }
            return slot.isValidForSlot(item);
        }
        return true;
    }

    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {

    }

    @Override
    public int getFieldCount()
    {
        return 0;
    }

    @Override
    public void clear()
    {
        for (Slot slot : slots)
        {
            slot.setItem(null);
        }
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
