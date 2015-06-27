package com.MO.MatterOverdrive.data;

import com.MO.MatterOverdrive.util.MOInventoryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 4/14/2015.
 */
public class ItemInventoryWrapper implements IInventory
{
    ItemStack inventory;
    int size;
    boolean dirty;

    public ItemInventoryWrapper(ItemStack itemStack,int size)
    {
        this.inventory = itemStack;
        this.size = size;
    }

    @Override
    public int getSizeInventory()
    {
        return size;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
       return MOInventoryHelper.getStackInSlot(inventory,slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount)
    {
        return MOInventoryHelper.decrStackSize(inventory,slot,amount);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        MOInventoryHelper.setInventorySlotContents(inventory,slot,stack);
    }

    @Override
    public String getInventoryName() {
        return inventory.getDisplayName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty()
    {
        dirty = true;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return true;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        return true;
    }
}
