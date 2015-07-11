package matteroverdrive.container.slot;

import matteroverdrive.util.MatterHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

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
