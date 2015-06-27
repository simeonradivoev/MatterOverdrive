package com.MO.MatterOverdrive.container.slot;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 4/8/2015.
 */
public class MOSlot extends Slot
{
    boolean isVisible = true;

    @SideOnly(Side.CLIENT)
    public boolean func_111238_b()
    {
        return isVisible;
    }

    public MOSlot(IInventory inventory, int slot, int x, int y) {
        super(inventory, slot, x, y);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    @Override
    public boolean isItemValid(ItemStack itemStack)
    {
        return isValid(itemStack);
    }

    public boolean isValid(ItemStack itemStack)
    {
        return true;
    }

    public void setVisible(boolean visible)
    {
        this.isVisible = visible;
    }
}
