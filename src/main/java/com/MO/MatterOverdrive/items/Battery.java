package com.MO.MatterOverdrive.items;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.items.includes.MOItemEnergyContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class Battery extends MOItemEnergyContainer
{
	private static final int BATTERY_EXTRACT = 800;
	private static final int BATTERY_INPUT = 800;

	public Battery(String name, int capacity) 
	{
		super(name, capacity,BATTERY_INPUT,BATTERY_EXTRACT);
		this.setMaxStackSize(1);
	}

	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tabs, List list)
	{
		ItemStack unpowered = new ItemStack(item);
		ItemStack powered = new ItemStack(item);
		setEnergyStored(powered,getMaxEnergyStored(powered));
		list.add(unpowered);
		list.add(powered);
	}
	
}
