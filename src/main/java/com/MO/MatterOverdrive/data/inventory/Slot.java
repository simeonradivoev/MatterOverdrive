package com.MO.MatterOverdrive.data.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 3/16/2015.
 */
public class Slot
{
    private ItemStack item;
    private int id;
    private boolean drops = true;
    public boolean isValidForSlot(ItemStack item)
    {
        return true;
    }
    public ResourceLocation getTexture()
    {
        return null;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    boolean isEqual(net.minecraft.inventory.Slot slot)
    {
        return true;
    }

    public boolean drops() {
        return drops;
    }

    public void setDrops(boolean drops) {
        this.drops = drops;
    }
}
