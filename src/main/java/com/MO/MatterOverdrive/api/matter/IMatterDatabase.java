package com.MO.MatterOverdrive.api.matter;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public interface IMatterDatabase
{
	boolean hasItem(int id);
	boolean hasItem(String id);
	boolean hasItem(ItemStack item);
	NBTTagList getItemsAsNBT();
	ItemStack[] getItems();
	boolean addItem(ItemStack itemStack);
	NBTTagCompound getItemAsNBT(ItemStack item);
	NBTTagCompound getItemAsNBT(String index);
	boolean increaseProgress(ItemStack item,int amount);
}
