package com.MO.MatterOverdrive.util;

import cofh.lib.gui.GuiColor;
import cofh.lib.gui.container.InventoryContainerItemWrapper;
import cofh.lib.util.helpers.InventoryHelper;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.weapon.IWeaponModule;
import com.MO.MatterOverdrive.data.ItemInventoryWrapper;
import com.MO.MatterOverdrive.items.WeaponColorModule;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * Created by Simeon on 4/14/2015.
 */
public class WeaponHelper
{
    public static ItemStack getModuleAtSlot(int slot,ItemStack weapon) {
        if (MatterHelper.isWeapon(weapon))
        {
            return MOInventoryHelper.getStackInSlot(weapon,slot);
        }
        return null;
    }

    public static void setModuleAtSlot(int slot,ItemStack weapon,ItemStack module)
    {
        if (MatterHelper.isWeapon(weapon))
        {
            MOInventoryHelper.setInventorySlotContents(weapon,slot,module);
        }
    }

    public static GuiColor getColor(ItemStack weapon)
    {
        ItemStack module = getModuleAtSlot(Reference.MODULE_COLOR,weapon);
        if (module != null && MatterHelper.isWeaponModule(weapon))
        {
            Object value = ((IWeaponModule)module.getItem()).getValue(module);
            if (value instanceof GuiColor)
            {
                return (GuiColor)value;
            }
        }
        return WeaponColorModule.defaultColor;
    }
}
