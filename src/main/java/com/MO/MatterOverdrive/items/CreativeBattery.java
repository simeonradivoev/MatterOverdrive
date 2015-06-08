package com.MO.MatterOverdrive.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cofh.lib.util.helpers.EnergyHelper;

import com.MO.MatterOverdrive.Reference;

import java.util.List;

public class CreativeBattery extends Battery
{
	private static final int BATTERY_EXTRACT = 8192;
	private static final int BATTERY_INPUT = 8192;

	public CreativeBattery(String name, int capacity) 
	{
		super(name, capacity);
		this.setMaxStackSize(1);
		this.setMaxExtract(BATTERY_EXTRACT);
		this.setMaxReceive(BATTERY_INPUT);
	}

	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tabs, List list)
	{
		ItemStack unpowered = new ItemStack(item);
		list.add(unpowered);
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
