package matteroverdrive.container.slot;

import matteroverdrive.init.MatterOverdriveItems;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 4/6/2015.
 */
public class SlotShielding extends MOSlot
{
    public SlotShielding(IInventory inventory, int index, int x, int y) {

        super(inventory, index, x, y);
    }

    @Override
    public boolean isValid(ItemStack stack)
    {
        return stack.getItem() == MatterOverdriveItems.tritanium_plate;
    }

    @Override
    public int getSlotStackLimit()
    {
        return 5;
    }
}
