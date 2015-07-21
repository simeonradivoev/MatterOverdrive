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

package matteroverdrive.data.inventory;

import matteroverdrive.util.MatterHelper;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 4/13/2015.
 */
public class WeaponSlot extends Slot
{
    public WeaponSlot(boolean isMainSlot)
    {
        super(isMainSlot);
    }

    public boolean isValidForSlot(ItemStack item)
    {
        return MatterHelper.isWeapon(item);
    }

    @Override
    public int getMaxStackSize(){return 1;}
}
