package matteroverdrive.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 4/8/2015.
 */
public class SlotRemoveOnly extends MOSlot
{
	public SlotRemoveOnly(IInventory inventory, int slot, int x, int y)
	{
		super(inventory, slot, x, y);
	}

	@Override
	public boolean isValid(ItemStack itemStack)
	{
		return false;
	}
}
