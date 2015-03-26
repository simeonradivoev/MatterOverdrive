package com.MO.MatterOverdrive.util;

import java.text.DecimalFormat;

public class MOEnergyHelper 
{
    public static final String ENERGY_UNIT = " RF";

	public static String formatEnergy(int energy,int capacity)
	{
		DecimalFormat formatter = new DecimalFormat("#,###");
		return "Power: " + formatter.format(energy) + "/" + formatter.format(capacity) + ENERGY_UNIT;
	}

    public static String formatEnergy(int energy)
    {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return "Power: " + formatter.format(energy) + ENERGY_UNIT;
    }
}
