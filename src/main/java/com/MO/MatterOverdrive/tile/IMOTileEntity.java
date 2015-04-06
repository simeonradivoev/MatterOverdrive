package com.MO.MatterOverdrive.tile;

import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 3/6/2015.
 */
public interface IMOTileEntity
{
    void onAdded();
    void onDestroyed();
    void onNeighborBlockChange();
    void writeToDropItem(ItemStack itemStack);
    void readFromPlaceItem(ItemStack itemStack);
}
