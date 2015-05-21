package com.MO.MatterOverdrive.api.matter;

import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 5/15/2015.
 */
public interface IRecyclable
{
    ItemStack getOutput(ItemStack from);
    int getRecycleMatter(ItemStack stack);
    boolean canRecycle(ItemStack stack);
}
