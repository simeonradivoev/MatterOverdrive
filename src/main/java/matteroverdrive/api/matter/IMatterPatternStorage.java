package matteroverdrive.api.matter;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * Created by Simeon on 3/27/2015.
 */
public interface IMatterPatternStorage
{
    NBTTagList getItemsAsNBT(ItemStack storage);
    boolean addItem(ItemStack storage,ItemStack itemStack,int initialAmount,boolean simulate);
    NBTTagCompound getItemAsNBT(ItemStack storage,ItemStack item);
    int getCapacity(ItemStack item);
}
