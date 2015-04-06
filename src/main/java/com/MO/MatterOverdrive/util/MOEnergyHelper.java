package com.MO.MatterOverdrive.util;

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
}
