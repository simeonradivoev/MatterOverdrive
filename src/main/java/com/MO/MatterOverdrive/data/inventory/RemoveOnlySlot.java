package com.MO.MatterOverdrive.data.inventory;

import cofh.lib.gui.slot.SlotRemoveOnly;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 3/17/2015.
 */
public class RemoveOnlySlot extends Slot
{
    @Override
    public boolean isValidForSlot(ItemStack itemStack)
    {
        return false;
    }

    @Override
    boolean isEqual(net.minecraft.inventory.Slot slot)
    {
        return slot instanceof SlotRemoveOnly;
    }
}
