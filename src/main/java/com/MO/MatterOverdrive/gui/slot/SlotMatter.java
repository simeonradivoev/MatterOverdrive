package com.MO.MatterOverdrive.gui.slot;

import com.MO.MatterOverdrive.util.MatterHelper;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cofh.lib.util.helpers.EnergyHelper;

public class SlotMatter extends Slot
{
	public SlotMatter(IInventory inventory, int index, int x, int y) {

		super(inventory, index, x, y);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {

		return MatterHelper.containsMatter(stack);
	}
}
