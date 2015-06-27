package com.MO.MatterOverdrive.container.slot;

import com.MO.MatterOverdrive.data.Inventory;
import com.MO.MatterOverdrive.data.inventory.Slot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 5/15/2015.
 */
public class SlotInventory extends MOSlot {

    Slot slot;

    public SlotInventory(IInventory inventory, Slot slot, int x, int y)
    {
        super(inventory, slot.getId(), x, y);
        this.slot = slot;
    }

    public boolean isItemValid(ItemStack itemStack)
    {
        return slot.isValidForSlot(itemStack);
    }

    public Slot getSlot()
    {
        return slot;
    }
}
