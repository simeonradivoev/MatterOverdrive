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

package matteroverdrive.container.slot;

import cofh.lib.util.helpers.EnergyHelper;
import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeaponModule;
import matteroverdrive.util.WeaponHelper;
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
                if(WeaponHelper.isWeaponModule(stack))
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
