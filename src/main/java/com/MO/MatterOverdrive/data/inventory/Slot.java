package com.MO.MatterOverdrive.data.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 3/16/2015.
 */
public class Slot
{
    private ItemStack item;
    private int id;
    private boolean drops = true;
    private boolean isMainSlot = false;

    public Slot(boolean isMainSlot)
    {
        this.isMainSlot = isMainSlot;
    }

    public boolean isValidForSlot(ItemStack item)
    {
        return true;
    }
    public IIcon getTexture()
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

    public boolean isMainSlot()
    {
        return isMainSlot;
    }
    public void setMainSlot(boolean mainSlot)
    {
        this.isMainSlot = mainSlot;
    }
}
