package com.MO.MatterOverdrive.data.inventory;

import com.MO.MatterOverdrive.api.matter.IRecyclable;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 5/15/2015.
 */
public class SlotRecycler extends Slot
{
    public SlotRecycler(boolean isMainSlot) {
        super(isMainSlot);
    }

    public boolean isValidForSlot(ItemStack item)
    {
        if (item.getItem() instanceof IRecyclable)
        {
            return ((IRecyclable) item.getItem()).canRecycle(item);
        }
        return false;
    }
}
