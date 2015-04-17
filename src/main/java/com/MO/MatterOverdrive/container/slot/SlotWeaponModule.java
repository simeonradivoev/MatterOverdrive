package com.MO.MatterOverdrive.container.slot;

import cofh.lib.util.helpers.EnergyHelper;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.weapon.IWeaponModule;
import com.MO.MatterOverdrive.util.MatterHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 4/14/2015.
 */
public class SlotWeaponModule extends MOSlot
{
    int type;

    public SlotWeaponModule(IInventory inventory, int slot, int x, int y,int type)
    {
        super(inventory, slot, x, y);
        this.type = type;
    }

    @Override
    public boolean isValid(ItemStack stack)
    {
        switch (type)
        {
            case Reference.MODULE_BATTERY:
                return EnergyHelper.isEnergyContainerItem(stack);
            default:
                if(MatterHelper.isWeaponModule(stack))
                {
                    if (((IWeaponModule)stack.getItem()).getSlot(stack) == type)
                    {
                        return true;
                    }
                }
                return false;
        }
    }
}
