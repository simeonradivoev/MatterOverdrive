package com.MO.MatterOverdrive.container.slot;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 4/8/2015.
 */
public class MOSlot extends Slot
{
    boolean isVisible = true;

    public MOSlot(IInventory inventory, int slot, int x, int y) {
        super(inventory, slot, x, y);
    }

    @SideOnly(Side.CLIENT)
    public boolean func_111238_b()
    {
        return isVisible;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack itemStack)
    {
        if(isVisible)
        {
            return isValid(itemStack);
        }
        return false;
    }

    public boolean isValid(ItemStack itemStack)
    {
        return true;
    }

    public void setVisible(boolean visible)
    {
        isVisible = visible;
    }
}
