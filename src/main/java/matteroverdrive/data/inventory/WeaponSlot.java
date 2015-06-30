package matteroverdrive.data.inventory;

import matteroverdrive.util.MatterHelper;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 4/13/2015.
 */
public class WeaponSlot extends Slot
{
    public WeaponSlot(boolean isMainSlot)
    {
        super(isMainSlot);
    }

    public boolean isValidForSlot(ItemStack item)
    {
        return MatterHelper.isWeapon(item);
    }
}
