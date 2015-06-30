package matteroverdrive.container.slot;

import matteroverdrive.util.MatterHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 3/27/2015.
 */
public class SlotPatternStorage extends MOSlot
{
    public SlotPatternStorage(IInventory inventory, int index, int x, int y) {

        super(inventory, index, x, y);
    }

    @Override
    public boolean isValid(ItemStack stack) {

        return MatterHelper.isMatterPatternStorage(stack);
    }
}
