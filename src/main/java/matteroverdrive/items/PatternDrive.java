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
import matteroverdrive.data.ItemPattern;
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
import net.minecraftforge.common.util.Constants;

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
            ItemPattern[] patterns = getPatterns(itemstack);
            for (int i = 0;i < patterns.length;i++)
            {
                ItemStack stack = patterns[i].toItemStack(false);
                String displayName;
                try {
                    displayName = stack.getDisplayName();
                }
                catch (Exception e)
                {
                    displayName = "Unknown";
                }

                if (MatterHelper.getMatterAmountFromItem(stack) > 0)
                {
                    infos.add(MatterDatabaseHelper.getPatternInfoColor(patterns[i].getProgress()) + displayName + " [" + patterns[i].getProgress() + "%]");
                }
                else
                {
                    infos.add(EnumChatFormatting.RED + "[Invalid] " + MatterDatabaseHelper.getPatternInfoColor(patterns[i].getProgress()) + displayName + " [" + patterns[i].getProgress() + "%]");
                }
                if (i > 8)
                {
                    infos.add(EnumChatFormatting.YELLOW + String.format("...and %s more patterns.",patterns.length-9));
                    break;
                }
            }
        }
    }

    @Override
    public void InitTagCompount(ItemStack stack)
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setShort(MatterDatabaseHelper.CAPACITY_TAG_NAME, (short) capacity);
        NBTTagList itemList = new NBTTagList();
        tagCompound.setTag(MatterDatabaseHelper.ITEMS_TAG_NAME,itemList);
        stack.setTagCompound(tagCompound);
    }

    @Override
    public ItemPattern[] getPatterns(ItemStack storage)
    {
        TagCompountCheck(storage);
        NBTTagList tagList = storage.getTagCompound().getTagList(MatterDatabaseHelper.ITEMS_TAG_NAME, 10);
        ItemPattern[] patterns = new ItemPattern[tagList.tagCount()];
        for (int i = 0;i < tagList.tagCount();i++)
        {
            ItemPattern pattern = new ItemPattern(tagList.getCompoundTagAt(i));
            patterns[i] = pattern;
        }
        return patterns;
    }

    @Override
    public boolean addItem(ItemStack storage, ItemStack itemStack,int initialAmount,boolean simulate)
    {
        TagCompountCheck(storage);

        NBTTagList patternsTagList = storage.getTagCompound().getTagList(MatterDatabaseHelper.ITEMS_TAG_NAME, Constants.NBT.TAG_COMPOUND);
        if(patternsTagList.tagCount() < getCapacity(storage))
        {
            if(MatterHelper.CanScan(itemStack))
            {
                int itemProgress = MatterDatabaseHelper.getItemStackProgress(storage, itemStack);

                if(itemProgress < MatterDatabaseHelper.MAX_ITEM_PROGRESS)
                {
                    if (!simulate)
                    {
                        MatterDatabaseHelper.addProgressToPatternStorage(storage, itemStack, initialAmount,false);
                    }
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public ItemPattern getPattern(ItemStack storage, ItemStack item)
    {
        TagCompountCheck(storage);
        return  MatterDatabaseHelper.getPatternFromStorage(storage, item);
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
