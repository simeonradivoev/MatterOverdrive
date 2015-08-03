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

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyProvider;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class MOEnergyHelper 
{
    public static final String ENERGY_UNIT = " RF";

	public static String formatEnergy(int energy,int capacity)
	{
		return MOStringHelper.formatNUmber(energy) + " / " + MOStringHelper.formatNUmber(capacity) + ENERGY_UNIT;
	}

    public static String formatEnergy(int energy)
    {
        return formatEnergy("Charge: ",energy);
    }

    public static String formatEnergy(String prefix,int energy)
    {
        return prefix != null ? prefix : "" + MOStringHelper.formatNUmber(energy) + ENERGY_UNIT;
    }

    public static boolean extractExactAmount(IEnergyProvider provider,ForgeDirection direction,int amount,boolean simulate)
    {
        int hasEnergy = provider.getEnergyStored(direction);
        if (hasEnergy >= amount)
        {
            while (amount > 0)
            {
                if (provider.extractEnergy(direction, amount, true) >= 0)
                {
                    amount -= provider.extractEnergy(direction,amount,simulate);
                }
                else
                {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean extractExactAmount(IEnergyContainerItem provider,ItemStack itemStack,int amount,boolean simulate)
    {
        int hasEnergy = provider.getEnergyStored(itemStack);
        if (hasEnergy >= amount)
        {
            while (amount > 0)
            {
                if (provider.extractEnergy(itemStack,amount, true) > 0)
                {
                    amount -= provider.extractEnergy(itemStack,amount,simulate);
                }
                else
                {
                    return false;
                }
            }
        }else
        {
            return false;
        }
        return true;
    }
}
