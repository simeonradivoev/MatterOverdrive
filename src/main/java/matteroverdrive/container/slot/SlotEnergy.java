package matteroverdrive.container.slot;

import cofh.lib.util.helpers.EnergyHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 4/8/2015.
 */
public class SlotEnergy extends MOSlot
{
    public SlotEnergy(IInventory inventory, int index, int x, int y)
    {
        super(inventory, index, x, y);
    }

    @Override
    public boolean isValid(ItemStack itemStack)
    {
        return EnergyHelper.isEnergyContainerItem(itemStack);
    }
}
