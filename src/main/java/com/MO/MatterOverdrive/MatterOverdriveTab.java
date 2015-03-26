package com.MO.MatterOverdrive;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.MO.MatterOverdrive.init.MatterOverdriveItems;

public class MatterOverdriveTab extends CreativeTabs 
{
	public MatterOverdriveTab(String label) 
	{
		super(label);
		//this.setBackgroundImageName("matterOverdrive_cp.png");
	}
	
	public Item getTabIconItem()
	{
		return MatterOverdriveItems.matter_scanner;
	}
}
