/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.api.matter.IMatterPatternStorage;
import matteroverdrive.items.includes.MOBaseItem;
import matteroverdrive.util.MatterDatabaseHelper;
import matteroverdrive.util.MatterHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Simeon on 3/27/2015.
 */
public class PatternDrive extends MOBaseItem implements IMatterPatternStorage
{
    private IIcon storageFull;
    private IIcon storagePartiallyFull;
    int capacity;

    public PatternDrive(String name, int capacity)
    {
        super(name);
        this.capacity = capacity;
        this.setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister p_94581_1_)
    {
        this.itemIcon = p_94581_1_.registerIcon(this.getIconString());
        this.storageFull = p_94581_1_.registerIcon(this.getIconString() + "_full");
        this.storagePartiallyFull = p_94581_1_.registerIcon(this.getIconString() + "_partially_full");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
        switch (damage)
        {
            case 2:
                return storageFull;
            case 1:
                return storagePartiallyFull;
            default:
                return itemIcon;
        }
    }

    public int getDamage(ItemStack stack)
    {
        if (stack.hasTagCompound())
        {
            if (stack.getTagCompound().func_150296_c().size() > 0)
            {
                if (stack.getTagCompound().func_150296_c().size() < getCapacity(stack))
                {
                    return 1;
                }
                else
                {
                    return 2;
                }
            }
        }
        return 0;
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
            NBTTagList list = getItemsAsNBT(itemstack);
            if (list != null) {
                for (int i = 0;i < list.tagCount();i++)
                {
                    int progress = MatterDatabaseHelper.GetProgressFromNBT(list.getCompoundTagAt(i));
                    String displayName = "Unknown";
                    try {
                        displayName = MatterDatabaseHelper.GetItemStackFromNBT(list.getCompoundTagAt(i)).getDisplayName();
                    }catch (Exception e)
                    {

                    }
                    infos.add(MatterDatabaseHelper.getPatternInfoColor(progress) + displayName + " [" + progress + "%]");
                    if (i > 8)
                    {
                        infos.add(EnumChatFormatting.YELLOW + String.format("...and %s more patterns.",list.tagCount()-9));
                        break;
                    }
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
        return storage.getTagCompound().getTagList(MatterDatabaseHelper.ITEMS_TAG_NAME, 10);
    }

    @Override
    public boolean addItem(ItemStack storage, ItemStack itemStack,int initialAmount,boolean simulate)
    {
        TagCompountCheck(storage);

        NBTTagList itemList = getItemsAsNBT(storage);
        if(itemList.tagCount() < getCapacity(storage))
        {
            if(MatterHelper.CanScan(itemStack))
            {
                int itemProgress = MatterDatabaseHelper.GetItemProgress(storage, itemStack);

                if(itemProgress < MatterDatabaseHelper.MAX_ITEM_PROGRESS)
                {
                    if (!simulate)
                    {
                        MatterDatabaseHelper.writeToNBT(storage, itemStack, initialAmount);
                    }
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public NBTTagCompound getItemAsNBT(ItemStack storage, ItemStack item)
    {
        TagCompountCheck(storage);
        return  MatterDatabaseHelper.GetItemAsNBT(storage, item);
    }

    @Override
    public int getCapacity(ItemStack item)
    {
        TagCompountCheck(item);
        return item.getTagCompound().getShort(MatterDatabaseHelper.CAPACITY_TAG_NAME);
    }

    public void clearStorage(ItemStack itemStack)
    {
        if (MatterHelper.isMatterPatternStorage(itemStack))
        {
            itemStack.setTagCompound(null);
        }
    }

    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        if (player.isSneaking())
        {
            clearStorage(itemStack);
        }
        return itemStack;
    }
}
