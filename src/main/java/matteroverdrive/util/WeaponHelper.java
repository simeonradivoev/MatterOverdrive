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

import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeapon;
import matteroverdrive.api.weapon.IWeaponColor;
import matteroverdrive.api.weapon.IWeaponModule;
import matteroverdrive.items.weapon.module.WeaponModuleColor;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Simeon on 4/14/2015.
 */
public class WeaponHelper
{
    public static ItemStack getModuleAtSlot(int slot,ItemStack weapon) {
        if (isWeapon(weapon))
        {
            return MOInventoryHelper.getStackInSlot(weapon,slot);
        }
        return null;
    }

    public static boolean hasModule(int module,ItemStack weapon)
    {
        return MOInventoryHelper.getStackInSlot(weapon,module) != null;
    }

    public static void setModuleAtSlot(int slot,ItemStack weapon,ItemStack module)
    {
        if (isWeapon(weapon) && module != null)
        {
            MOInventoryHelper.setInventorySlotContents(weapon,slot,module);
        }
    }

    public static int getColor(ItemStack weapon)
    {
        ItemStack module = getModuleAtSlot(Reference.MODULE_COLOR,weapon);
        if (module != null && isWeaponModule(module))
        {
            return ((IWeaponColor)module.getItem()).getColor(module,weapon);
        }
        return WeaponModuleColor.defaultColor.getColor();
    }

    public static float modifyStat(int stat,ItemStack weapon,float original)
    {
        if (isWeapon(weapon))
        {
            List<ItemStack> itemStacks = MOInventoryHelper.getStacks(weapon);
            if (itemStacks != null) {
                for (ItemStack module : itemStacks) {
                    if (module != null && module.getItem() instanceof IWeaponModule)
                    {
                        original = ((IWeaponModule) module.getItem()).modifyWeaponStat(stat,module,weapon,original);
                    }
                }
            }
        }
        return original;
    }

    public static boolean hasStat(int stat,ItemStack weapon)
    {
        float statValue = 1f;
        if (isWeapon(weapon))
        {
            for (ItemStack module : MOInventoryHelper.getStacks(weapon))
            {
                if (module != null && module.getItem() instanceof IWeaponModule)
                {
                    statValue = ((IWeaponModule) module.getItem()).modifyWeaponStat(stat,module,weapon,statValue);
                }
            }
        }
        return statValue != 1f;
    }

    public static boolean isWeaponModule(ItemStack itemStack)
    {
        return itemStack != null && itemStack.getItem() != null && itemStack.getItem() instanceof IWeaponModule;
    }

    public static boolean isWeapon(ItemStack itemStack)
    {
        return itemStack != null && itemStack.getItem() != null  && itemStack.getItem() instanceof IWeapon;
    }
}
