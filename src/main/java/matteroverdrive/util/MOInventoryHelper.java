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

package matteroverdrive.util;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Simeon on 4/14/2015.
 */
public class MOInventoryHelper
{

    public static void setInventorySlotContents(ItemStack container,int slot,ItemStack stack)
    {
        if (stack == null)
        {
            if (!container.hasTagCompound())
            {
                container.setTagCompound(new NBTTagCompound());
            }
            container.getTagCompound().setTag("Slot" + slot, new NBTTagCompound());
        }
        else
        {
            NBTTagCompound itemTag = new NBTTagCompound();
            stack.writeToNBT(itemTag);
            if (!container.hasTagCompound())
                container.setTagCompound(new NBTTagCompound());
            container.getTagCompound().setTag("Slot" + slot, itemTag);
        }
    }

    public static ItemStack decrStackSize(ItemStack container,int slot, int amount)
    {
        if (container.getTagCompound().getCompoundTag("Slot" + slot) == null || container.getTagCompound().getCompoundTag("Slot" + slot).hasNoTags()) {
            return null;
        }
        ItemStack stack = ItemStack.loadItemStackFromNBT(container.getTagCompound().getCompoundTag("Slot" + slot));
        ItemStack retStack = stack.splitStack(amount);
        if (stack.stackSize <= 0) {
            container.getTagCompound().setTag("Slot" + slot, new NBTTagCompound());
        } else {
            NBTTagCompound itemTag = new NBTTagCompound();
            stack.writeToNBT(itemTag);
            container.getTagCompound().setTag("Slot" + slot, itemTag);
        }
        return retStack;
    }

    public static ItemStack getStackInSlot(ItemStack container,int slot)
    {
        if (!container.hasTagCompound() || container.getTagCompound().getCompoundTag("Slot" + slot) == null || container.getTagCompound().getCompoundTag("Slot" + slot).hasNoTags()) {
            return null;
        }
        return ItemStack.loadItemStackFromNBT(container.getTagCompound().getCompoundTag("Slot" + slot));
    }

    public static List<ItemStack> getStacks(ItemStack container)
    {
        if (!container.hasTagCompound())
        {
            return null;
        }

        List<ItemStack> itemStacks = new ArrayList<>();

        Iterator iterator = container.getTagCompound().getKeySet().iterator();

        while (iterator.hasNext())
        {
            String s = (String)iterator.next();
            if (s.startsWith("Slot"))
            {
                NBTBase nbtbase = container.getTagCompound().getTag(s);
                if (nbtbase instanceof NBTTagCompound)
                {
                    ItemStack itemStack = ItemStack.loadItemStackFromNBT((NBTTagCompound)nbtbase);
                    if (itemStack != null)
                    {
                        itemStacks.add(itemStack);
                    }
                }
            }
        }
        return itemStacks;
    }

    public static ItemStack addItemInContainer(Container container, ItemStack itemStack)
    {
        for (int i = 0;i < container.inventorySlots.size();i++)
        {
            if (container.getSlot(i).isItemValid(itemStack))
            {
                if (container.getSlot(i).getStack() == null)
                {
                    container.getSlot(i).putStack(itemStack);
                    if (itemStack.stackSize > itemStack.getMaxStackSize())
                    {
                        itemStack.stackSize = itemStack.getMaxStackSize();
                    }else
                    {
                        return null;
                    }
                }
                else if (ItemStack.areItemStacksEqual(container.getSlot(i).getStack(),itemStack) && container.getSlot(i).getStack().stackSize < container.getSlot(i).getStack().getMaxStackSize())
                {
                    int newStackSize = Math.min(container.getSlot(i).getStack().stackSize+itemStack.stackSize,container.getSlot(i).getStack().getMaxStackSize());
                    int leftStackSize =  container.getSlot(i).getStack().stackSize + itemStack.stackSize - newStackSize;
                    container.getSlot(i).getStack().stackSize = newStackSize;
                    if (leftStackSize <= 0)
                        return null;

                    itemStack.stackSize=newStackSize;
                }
            }
        }
        return itemStack;
    }

    public static ItemStack insertItemStackIntoInventory(IInventory inventory, ItemStack itemStack, EnumFacing side) {
        if(itemStack != null && inventory != null) {
            int var3 = itemStack.stackSize;
            if(inventory instanceof ISidedInventory) {
                ISidedInventory var4 = (ISidedInventory)inventory;
                int[] var5 = var4.getSlotsForFace(side);
                if(var5 == null) {
                    return itemStack;
                }

                int var6;
                for(var6 = 0; var6 < var5.length && itemStack != null; ++var6) {
                    if(var4.canInsertItem(var5[var6], itemStack, side)) {
                        ItemStack var7 = inventory.getStackInSlot(var5[var6]);
                        if(ItemStack.areItemStacksEqual(itemStack,var7)) {
                            itemStack = addToOccupiedInventorySlot(var4, var5[var6], itemStack, var7);
                        }
                    }
                }

                for(var6 = 0; var6 < var5.length && itemStack != null; ++var6) {
                    if(inventory.getStackInSlot(var5[var6]) == null && var4.canInsertItem(var5[var6], itemStack, side)) {
                        itemStack = addToEmptyInventorySlot(var4, var5[var6], itemStack);
                    }
                }
            } else {
                int var8 = inventory.getSizeInventory();

                int var9;
                for(var9 = 0; var9 < var8 && itemStack != null; ++var9) {
                    ItemStack var10 = inventory.getStackInSlot(var9);
                    if(ItemStack.areItemStacksEqual(itemStack,var10)) {
                        itemStack = addToOccupiedInventorySlot(inventory, var9, itemStack, var10);
                    }
                }

                for(var9 = 0; var9 < var8 && itemStack != null; ++var9) {
                    if(inventory.getStackInSlot(var9) == null) {
                        itemStack = addToEmptyInventorySlot(inventory, var9, itemStack);
                    }
                }
            }

            if(itemStack == null || itemStack.stackSize != var3) {
                inventory.markDirty();
            }

            return itemStack;
        } else {
            return null;
        }
    }

    public static ItemStack addToOccupiedInventorySlot(IInventory inventory, int slot, ItemStack one, ItemStack two) {
        int maxSize = Math.min(inventory.getInventoryStackLimit(), one.getMaxStackSize());
        if(one.stackSize + two.stackSize > maxSize) {
            int remanningSize = maxSize - two.stackSize;
            two.stackSize = maxSize;
            one.stackSize -= remanningSize;
            inventory.setInventorySlotContents(slot, two);
            return one;
        } else {
            two.stackSize += Math.min(one.stackSize, maxSize);
            inventory.setInventorySlotContents(slot, two);
            return maxSize >= one.stackSize?null:one.splitStack(one.stackSize - maxSize);
        }
    }

    public static ItemStack addToEmptyInventorySlot(IInventory inventory, int slot, ItemStack itemStack) {
        if(!inventory.isItemValidForSlot(slot, itemStack)) {
            return itemStack;
        } else {
            int inventoryStackLimit = inventory.getInventoryStackLimit();
            ItemStack newItemStack = ItemStack.copyItemStack(itemStack);
            newItemStack.stackSize = Math.min(itemStack.stackSize, inventoryStackLimit);
            inventory.setInventorySlotContents(slot, newItemStack);

            return inventoryStackLimit >= itemStack.stackSize?null:itemStack.splitStack(itemStack.stackSize - inventoryStackLimit);
        }
    }

    public static boolean mergeItemStack(List<Slot> var0, ItemStack var1, int var2, int var3, boolean var4) {
        return mergeItemStack(var0, var1, var2, var3, var4, true);
    }

    public static boolean mergeItemStack(List<Slot> slots, ItemStack itemStack, int var2, int var3, boolean var4, boolean var5) {
        boolean var6 = false;
        int var7 = !var4?var2:var3 - 1;
        int var8 = !var4?1:-1;
        Slot var9;
        ItemStack var10;
        int var11;
        if(itemStack.isStackable()) {
            for(; itemStack.stackSize > 0 && (!var4 && var7 < var3 || var4 && var7 >= var2); var7 += var8) {
                var9 = slots.get(var7);
                var10 = var9.getStack();
                if(var9.isItemValid(itemStack) && var10 != null && var10.getItem().equals(itemStack.getItem()) && (!itemStack.getHasSubtypes() || itemStack.getItemDamage() == var10.getItemDamage()) && ItemStack.areItemStackTagsEqual(itemStack, var10)) {
                    var11 = var10.stackSize + itemStack.stackSize;
                    int var12 = Math.min(itemStack.getMaxStackSize(), var9.getSlotStackLimit());
                    if(var11 <= var12) {
                        itemStack.stackSize = 0;
                        var10.stackSize = var11;
                        var9.onSlotChanged();
                        var6 = true;
                    } else if(var10.stackSize < var12) {
                        itemStack.stackSize -= var12 - var10.stackSize;
                        var10.stackSize = var12;
                        var9.onSlotChanged();
                        var6 = true;
                    }
                }
            }
        }

        if(itemStack.stackSize > 0) {
            for(var7 = !var4?var2:var3 - 1; itemStack.stackSize > 0 && (!var4 && var7 < var3 || var4 && var7 >= var2); var7 += var8) {
                var9 = slots.get(var7);
                var10 = var9.getStack();
                if(var9.isItemValid(itemStack) && var10 == null) {
                    var11 = var5?Math.min(itemStack.getMaxStackSize(), var9.getSlotStackLimit()):var9.getSlotStackLimit();
                    var10 = itemStack.splitStack(Math.min(itemStack.stackSize, var11));
                    var9.putStack(var10);
                    var9.onSlotChanged();
                    var6 = true;
                }
            }
        }

        return var6;
    }
}
