package com.MO.MatterOverdrive.tile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by Simeon on 3/6/2015.
 */
public interface IMOTileEntity
{
    void onAdded(World world,int x,int y,int z);
    void onPlaced(World world,EntityLivingBase entityLiving);
    void onDestroyed();
    void onNeighborBlockChange();
    void writeToDropItem(ItemStack itemStack);
    void readFromPlaceItem(ItemStack itemStack);
}
