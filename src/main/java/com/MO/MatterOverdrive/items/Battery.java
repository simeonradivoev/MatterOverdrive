package com.MO.MatterOverdrive.items;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.items.includes.MOItemEnergyContainer;

public class Battery extends MOItemEnergyContainer
{
	private static final int BATTERY_EXTRACT = 32;
	private static final int BATTERY_INPUT = 32;

	public Battery(String name, int capacity) 
	{
		super(name, capacity,BATTERY_INPUT,BATTERY_EXTRACT);
		this.setMaxStackSize(1);
	}
	
}
