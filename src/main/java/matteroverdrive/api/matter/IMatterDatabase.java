package matteroverdrive.api.matter;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public interface IMatterDatabase
{
	boolean hasItem(ItemStack item);
	NBTTagList getItemsAsNBT();
	ItemStack[] getItems();
	boolean addItem(ItemStack itemStack,int initialAmount,boolean simulate,StringBuilder info);
	NBTTagCompound getItemAsNBT(ItemStack item);
	ItemStack[] getPatternStorageList();
}
