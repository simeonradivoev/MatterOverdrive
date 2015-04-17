package com.MO.MatterOverdrive.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Simeon on 4/14/2015.
 */
public class MOInventoryHelper
{

    public static void setInventorySlotContents(ItemStack container,int slot,ItemStack stack)
    {
        if (stack == null)
        {
            container.getTagCompound().setTag("Slot" + slot, new NBTTagCompound());
        }
        else
        {
            NBTTagCompound itemTag = new NBTTagCompound();
            stack.writeToNBT(itemTag);
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
}
