package matteroverdrive;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import matteroverdrive.init.MatterOverdriveItems;

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
