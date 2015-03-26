package com.MO.MatterOverdrive.items;

import net.minecraft.item.ItemStack;
import cofh.lib.util.helpers.EnergyHelper;

import com.MO.MatterOverdrive.Reference;

public class CreativeBattery extends Battery
{
	private static final int BATTERY_EXTRACT = 512;
	private static final int BATTERY_INPUT = 512;

	public CreativeBattery(String name, int capacity) 
	{
		super(name, capacity);
		this.setMaxStackSize(1);
		this.setMaxExtract(BATTERY_EXTRACT);
		this.setMaxReceive(BATTERY_INPUT);
	}
	
	@Override
	public int getEnergyStored(ItemStack container) 
	{
		return capacity;
	}
	
	@Override
	protected void setEnergyStored(ItemStack container,int amount)
	{
		EnergyHelper.setDefaultEnergyTag(container, capacity);
	}
	
	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) 
	{
		return Math.min(this.maxExtract, maxExtract);
	}
	
	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) 
	{
		return 0;
	}

}
