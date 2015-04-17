package com.MO.MatterOverdrive;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.MO.MatterOverdrive.init.MatterOverdriveItems;

public class MatterOverdriveTab extends CreativeTabs 
{
	Item item;

	public MatterOverdriveTab(String label)
	{
		super(label);
	}
	
	public Item getTabIconItem()
	{
		if (item == null)
			return MatterOverdriveItems.matter_scanner;
		return item;
	}
}
