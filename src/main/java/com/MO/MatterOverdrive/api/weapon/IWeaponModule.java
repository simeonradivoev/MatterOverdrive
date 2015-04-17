package com.MO.MatterOverdrive.api.weapon;

import net.minecraft.item.ItemStack;

import java.util.Objects;

/**
 * Created by Simeon on 4/14/2015.
 */
public interface IWeaponModule
{
    int getSlot(ItemStack itemStack);
    Object getValue(ItemStack itemStack);
}
