package com.MO.MatterOverdrive.container.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 4/8/2015.
 */
public class SlotRemoveOnly extends MOSlot
{
    public SlotRemoveOnly(IInventory inventory, int slot, int x, int y) {
        super(inventory, slot, x, y);
    }

    @Override
    public boolean isValid(ItemStack itemStack)
    {
        return false;
    }
}
