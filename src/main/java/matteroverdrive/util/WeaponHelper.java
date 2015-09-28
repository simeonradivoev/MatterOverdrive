/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.util;

import cofh.lib.gui.GuiColor;
import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeaponModule;
import matteroverdrive.items.weapon.module.WeaponModuleColor;
import net.minecraft.item.ItemStack;

import java.util.List;
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
        if (module != null && MatterHelper.isWeaponModule(module))
        {
            Object value = ((IWeaponModule)module.getItem()).getValue(module);
            if (value instanceof GuiColor)
            {
                return (GuiColor)value;
            }
        }
        return WeaponModuleColor.defaultColor;
    }

    public static double getStatMultiply(int stat,ItemStack weapon)
    {
        double multiply = 1;

        if (MatterHelper.isWeapon(weapon))
        {
            Map<Integer,Double> stats;
            List<ItemStack> itemStacks = MOInventoryHelper.getStacks(weapon);
            if (itemStacks != null) {
                for (ItemStack module : itemStacks) {
                    stats = getStatsFromModule(module, weapon);
                    if (stats != null) {
                        if (stats.containsKey(stat)) {
                            multiply *= stats.get(stat);
                        }
                    }
                }
            }
        }
        return multiply;
    }

    public static boolean hasStat(int stat,ItemStack weapon)
    {
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
                        return true;
                    }
                }
            }
        }
        return false;
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
