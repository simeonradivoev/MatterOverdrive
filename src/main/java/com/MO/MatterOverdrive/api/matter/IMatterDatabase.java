package com.MO.MatterOverdrive.api.matter;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public interface IMatterDatabase 
{
	ItemStack[] getItems(ItemStack database);
	NBTTagList getItemNBTCompounds(ItemStack database);
	boolean hasItem(ItemStack database,int id);
	boolean hasItem(ItemStack database,ItemStack item);
	ItemStack getItem(ItemStack database,int id);
	ItemStack getItem(ItemStack database,ItemStack item);
	NBTTagCompound getItemAsNBT(ItemStack database,int id);
}
