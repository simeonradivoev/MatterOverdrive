package com.MO.MatterOverdrive.gui.slot;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.MO.MatterOverdrive.util.MatterHelper;

public class SlotDatabase extends Slot
{
	public SlotDatabase(IInventory inventory, int index, int x, int y) {

		super(inventory, index, x, y);
    }

	@Override
	public boolean isItemValid(ItemStack stack) {

		return MatterHelper.isDatabaseItem(stack);
	}
}
