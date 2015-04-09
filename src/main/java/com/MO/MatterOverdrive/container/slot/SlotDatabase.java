package com.MO.MatterOverdrive.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.MO.MatterOverdrive.util.MatterHelper;

public class SlotDatabase extends MOSlot
{
	public SlotDatabase(IInventory inventory, int index, int x, int y) {

		super(inventory, index, x, y);
    }

	@Override
	public boolean isValid(ItemStack stack) {

		return MatterHelper.isMatterScanner(stack);
	}
}
