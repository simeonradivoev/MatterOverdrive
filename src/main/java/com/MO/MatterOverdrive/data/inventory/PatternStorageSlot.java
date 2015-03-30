package com.MO.MatterOverdrive.data.inventory;

import com.MO.MatterOverdrive.api.matter.IMatterPatternStorage;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 3/27/2015.
 */
public class PatternStorageSlot extends Slot
{
    public boolean isValidForSlot(ItemStack item)
    {
        return item.getItem() instanceof IMatterPatternStorage;
    }
}
