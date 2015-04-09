package com.MO.MatterOverdrive.container.slot;

import com.MO.MatterOverdrive.util.MatterHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 4/9/2015.
 */
public class SlotUpgrade extends  MOSlot
{
    public SlotUpgrade(IInventory inventory, int slot, int x, int y)
    {
        super(inventory, slot, x, y);
    }

    @Override
    public boolean isValid(ItemStack stack)
    {
        return MatterHelper.isUpgrade(stack);
    }

    @Override
    public int getSlotStackLimit()
    {
        return 1;
    }
}
