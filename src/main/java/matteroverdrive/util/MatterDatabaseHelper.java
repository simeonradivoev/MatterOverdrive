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

package matteroverdrive.util;

import matteroverdrive.api.matter.IMatterDatabase;
import matteroverdrive.api.matter.IMatterPatternStorage;
import matteroverdrive.data.ItemPattern;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

public class MatterDatabaseHelper
{
	public static final int MAX_ITEM_PROGRESS = 100;
	public static final String PROGRESS_TAG_NAME = "scan_progress";
	public static final String ITEMS_TAG_NAME = "items";
	public static final String CAPACITY_TAG_NAME = "Capacity";

    public static void initTagCompound(ItemStack scanner)
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        scanner.setTagCompound(tagCompound);
        initItemListTagCompound(scanner);

    }

	public static void initItemListTagCompound(ItemStack scanner)
	{
		NBTTagList items = new NBTTagList();
		scanner.setTagInfo(ITEMS_TAG_NAME, items);
	}

	public static int getPatternCapacity(ItemStack storage)
	{
		if (storage.getTagCompound() != null)
		{
			return storage.getTagCompound().getShort(MatterDatabaseHelper.CAPACITY_TAG_NAME);
		}
		return 0;
	}

	public static boolean hasFreeSpace(ItemStack storage)
	{
		if (storage != null)
		{
			if(MatterHelper.isMatterPatternStorage(storage))
			{
                ItemPattern[] patterns = getPatternsFromStorage(storage);
                if (patterns.length < MatterDatabaseHelper.getPatternCapacity(storage))
                {
                    return true;
                }
			}

		}
		return false;
	}

	public static ItemStack getFirstFreePatternStorage(IMatterDatabase database)
	{
		ItemStack[] patternStorages = database.getPatternStorageList();

		for (int i = 0;i < patternStorages.length;i++)
		{
			if (patternStorages[i] != null)
			{
				if (hasFreeSpace(patternStorages[i]))
				{
					return patternStorages[i];
				}
			}
		}
		return null;
	}

	public static boolean HasItem(ItemStack storage,ItemStack item)
	{
        ItemPattern[] patterns = getPatternsFromStorage(storage);
        for (int i = 0;i < patterns.length;i++)
        {
            if (areEqual(item,patterns[i].toItemStack(false)))
            {
                return true;
            }
        }
		return false;
	}

    public static void addProgressToPatternStorage(ItemStack patternStorage, ItemStack item, int progress,boolean existingOnly)
    {
        if (!patternStorage.hasTagCompound()) {
            initItemListTagCompound(patternStorage);
        }

        NBTTagList patternsTagList = patternStorage.getTagCompound().getTagList(ITEMS_TAG_NAME, Constants.NBT.TAG_COMPOUND);
        for (int i = 0;i < patternsTagList.tagCount();i++)
        {
            ItemPattern pattern = new ItemPattern(patternsTagList.getCompoundTagAt(i));
            if (areEqual(pattern.toItemStack(false),item))
            {
                int oldProgress = patternsTagList.getCompoundTagAt(i).getByte(PROGRESS_TAG_NAME);
                patternsTagList.getCompoundTagAt(i).setByte(PROGRESS_TAG_NAME,(byte) MathHelper.clamp_int(oldProgress + progress,0,MAX_ITEM_PROGRESS));
                return;
            }
        }

        if (!existingOnly)
        {
            ItemPattern pattern = new ItemPattern(item, progress);
            NBTTagCompound patternTag = new NBTTagCompound();
            pattern.writeToNBT(patternTag);
            patternsTagList.appendTag(patternTag);
        }
    }

	public static int getItemStackProgress(ItemStack storage, ItemStack item)
	{
        ItemPattern itemPattern = getPatternFromStorage(storage,item);
        if (itemPattern != null)
        {
            return itemPattern.getProgress();
        }
		return -1;
	}

	public static ItemPattern getPatternFromStorage(ItemStack patternStorage, ItemStack item)
	{
        ItemPattern[] patterns = getPatternsFromStorage(patternStorage);
		for (int i = 0; i < patterns.length; i++) {
			if (areEqual(item, patterns[i].toItemStack(false)))
				return patterns[i];
		}
		return null;
	}
	public static ItemPattern getPatternWithItemID(ItemStack patternStorage, int id)
	{
        ItemPattern[] patterns = getPatternsFromStorage(patternStorage);
        for (int i = 0; i < patterns.length; i++) {
            if (patterns[i].getItemID() == id)
                return patterns[i];
        }

		return null;
	}

	private static ItemPattern getPatternAt(ItemStack patternStorage, int index)
	{
        ItemPattern itemPattern[] = getPatternsFromStorage(patternStorage);
		return itemPattern[index];
	}


	public static boolean areEqual(ItemStack one,ItemStack two)
	{
        if(one != null && two != null)
        {
            if(one.getItem() == two.getItem())
            {
                if(one.getHasSubtypes() && two.getHasSubtypes())
                {
                    if(one.getItemDamage() == two.getItemDamage())
                    {
                        return true;
                    }
                }
                else
                {
                    return true;
                }
            }
        }
		return false;
	}

	public static boolean areEqual(NBTTagCompound one,NBTTagCompound two)
	{
		if(one == null || two == null)
			return false;

		return areEqual(ItemStack.loadItemStackFromNBT(one),ItemStack.loadItemStackFromNBT(two));
	}

	public static ItemPattern[] getPatternsFromStorage(ItemStack patternStorage)
	{
		if (patternStorage.getItem() instanceof IMatterPatternStorage)
		{
			return ((IMatterPatternStorage) patternStorage.getItem()).getPatterns(patternStorage);
		}
		return null;
	}

	public static ItemStack GetItemStackFromWorld(World world,int x, int y,int z)
	{
		Block b = world.getBlock(x, y, z);

		if(b != null)
		{
			int meta = world.getBlockMetadata(x, y, z);

			if (b == Blocks.skull) {
				TileEntity te = world.getTileEntity(x, y, z);
				meta = ((TileEntitySkull)te).func_145904_a();
				return new ItemStack(b.getItemDropped(meta, new Random(), 0), 1, meta);
			}

			return new ItemStack(b, 1, b.damageDropped(meta));

		}

		return new ItemStack(b);
	}

	public static EnumChatFormatting getPatternInfoColor(int progress)
	{
		EnumChatFormatting color = EnumChatFormatting.GRAY;

		if (progress > 0 && progress <= 20)
			color = EnumChatFormatting.RED;
		else if (progress > 20 && progress <= 40)
			color = EnumChatFormatting.GOLD;
		else if (progress > 40 && progress <= 60)
			color = EnumChatFormatting.YELLOW;
		else if (progress > 40 && progress <= 80)
			color = EnumChatFormatting.AQUA;
		else if (progress > 80 && progress <= 100)
			color = EnumChatFormatting.GREEN;
		else
			color = EnumChatFormatting.GREEN;

		return color;
	}
}
