package com.MO.MatterOverdrive.util;

import cofh.lib.gui.GuiColor;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.weapon.IWeaponModule;
import com.MO.MatterOverdrive.items.WeaponColorModule;
import net.minecraft.item.ItemStack;

import java.util.Map;

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

    public static double getStatMultiply(int stat,ItemStack weapon)
    {
        double multiply = 1;

        if (MatterHelper.isWeapon(weapon))
        {
            Map<Integer,Double> stats;
            for (ItemStack module : MOInventoryHelper.getStacks(weapon))
            {
                stats = getStatsFromModule(module,weapon);
                if (stats != null)
                {
                    if (stats.containsKey(stat))
                    {
                        multiply *= stats.get(stat);
                    }
                }
            }
        }
        return multiply;
    }

    public static Map<Integer,Double> getStatsFromModule(ItemStack module,ItemStack weapon)
    {
        if (weapon != null && module != null && MatterHelper.isWeapon(weapon) && MatterHelper.isWeaponModule(module))
        {
            Object mapObject = ((IWeaponModule)module.getItem()).getValue(module);
            if (mapObject instanceof Map)
            {
                return (Map<Integer,Double>)mapObject;
            }
        }
        return null;
    }

    public static Map<Integer,Double> getStatsFromModule(int module,ItemStack weapon)
    {
        if (weapon != null && MatterHelper.isWeapon(weapon))
        {
            ItemStack m = getModuleAtSlot(module,weapon);
            return getStatsFromModule(m,weapon);
        }
        return null;
    }
}
