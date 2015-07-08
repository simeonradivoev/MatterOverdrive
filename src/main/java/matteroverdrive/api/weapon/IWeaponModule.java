package matteroverdrive.api.weapon;

import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 4/14/2015.
 */
public interface IWeaponModule
{
    int getSlot(ItemStack itemStack);
    Object getValue(ItemStack itemStack);
}
