package com.MO.MatterOverdrive.items;

import com.MO.MatterOverdrive.api.matter.IMatterPatternStorage;
import com.MO.MatterOverdrive.items.includes.MOBaseItem;
import com.MO.MatterOverdrive.util.MatterDatabaseHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.List;

/**
 * Created by Simeon on 3/27/2015.
 */
public class PatternDrive extends MOBaseItem implements IMatterPatternStorage
{
    int capacity;

    public PatternDrive(String name, int capacity)
    {
        super(name);
        this.capacity = capacity;
        this.setMaxStackSize(1);
    }

    @Override
    public boolean hasDetails(ItemStack itemStack)
    {
        return true;
    }

    @Override
    public void addDetails(ItemStack itemstack, EntityPlayer player, List infos)
    {
        if(itemstack.hasTagCompound())
        {
            NBTTagList list = MatterDatabaseHelper.GetItemsTagList(itemstack);
            if (list != null) {
                for (int i = 0;i < list.tagCount();i++)
                {
                    infos.add(MatterDatabaseHelper.GetItemStackFromNBT(list.getCompoundTagAt(i)).getDisplayName() + " [" + MatterDatabaseHelper.GetProgressFromNBT(list.getCompoundTagAt(i)) + "%]");
                }
            }
        }
    }

    public void InitTagCompount(ItemStack stack)
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setShort(MatterDatabaseHelper.CAPACITY_TAG_NAME, (short) capacity);
        NBTTagList itemList = new NBTTagList();
        tagCompound.setTag(MatterDatabaseHelper.ITEMS_TAG_NAME,itemList);
        stack.setTagCompound(tagCompound);
    }

    @Override
    public NBTTagList getItemsAsNBT(ItemStack storage)
    {
        TagCompountCheck(storage);
        return storage.getTagCompound().getTagList(MatterDatabaseHelper.ITEMS_TAG_NAME,10);
    }

    @Override
    public boolean addItem(ItemStack storage, ItemStack itemStack,int initialAmount)
    {
        TagCompountCheck(storage);

        NBTTagList itemList = getItemsAsNBT(storage);
        if(itemList.tagCount() < getCapacity(storage))
        {
            return MatterDatabaseHelper.Register(storage,itemStack,initialAmount);
        }
        return false;
    }

    @Override
    public NBTTagCompound getItemAsNBT(ItemStack storage, ItemStack item)
    {
        TagCompountCheck(storage);
        return  MatterDatabaseHelper.GetItemAsNBT(storage,item);
    }

    @Override
    public int getCapacity(ItemStack item)
    {
        TagCompountCheck(item);
        return item.getTagCompound().getShort(MatterDatabaseHelper.CAPACITY_TAG_NAME);
    }
}
