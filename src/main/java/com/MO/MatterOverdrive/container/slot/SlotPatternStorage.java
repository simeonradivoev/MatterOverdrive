package com.MO.MatterOverdrive.container.slot;

import com.MO.MatterOverdrive.util.MatterHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 3/27/2015.
 */
public class SlotPatternStorage extends Slot
{
    public SlotPatternStorage(IInventory inventory, int index, int x, int y) {

        super(inventory, index, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {

        return MatterHelper.isMatterPatternStorage(stack);
    }
}
