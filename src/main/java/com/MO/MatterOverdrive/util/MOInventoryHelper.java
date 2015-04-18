package com.MO.MatterOverdrive.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    public static List<ItemStack> getStacks(ItemStack container)
    {
        if (!container.hasTagCompound())
        {
            return null;
        }

        List<ItemStack> itemStacks = new ArrayList<ItemStack>();

        Iterator iterator = container.getTagCompound().func_150296_c().iterator();

        while (iterator.hasNext())
        {
            String s = (String)iterator.next();
            if (s.startsWith("Slot"))
            {
                NBTBase nbtbase = (NBTBase)container.getTagCompound().getTag(s);
                if (nbtbase instanceof NBTTagCompound)
                {
                    itemStacks.add(ItemStack.loadItemStackFromNBT((NBTTagCompound)nbtbase));
                }
            }
        }
        return itemStacks;
    }
}
