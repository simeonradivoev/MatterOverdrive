package com.MO.MatterOverdrive.util;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.text.DecimalFormat;

public class MOEnergyHelper 
{
    public static final String ENERGY_UNIT = " RF";

	public static String formatEnergy(int energy,int capacity)
	{
		return MOStringHelper.formatNUmber(energy) + " / " + MOStringHelper.formatNUmber(capacity) + ENERGY_UNIT;
	}

    public static String formatEnergy(int energy)
    {
        return "Charge: " + MOStringHelper.formatNUmber(energy) + ENERGY_UNIT;
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
