package com.MO.MatterOverdrive.items;

import com.MO.MatterOverdrive.items.includes.MOBaseItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

public class MatterDust extends MOBaseItem
{

	public MatterDust(String name) 
	{
		super(name);
		
	}

	@Override
	public boolean hasDetails(ItemStack itemStack)
	{
		return true;
	}

	@Override
	public void addDetails(ItemStack itemstack, EntityPlayer player, List infos)
	{
		infos.add("Put in a furnace, to refine");
	}

}
