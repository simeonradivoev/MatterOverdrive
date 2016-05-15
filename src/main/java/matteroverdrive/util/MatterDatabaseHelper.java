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

import com.mojang.realmsclient.gui.ChatFormatting;
import matteroverdrive.api.matter.IMatterDatabase;
import matteroverdrive.api.matter.IMatterPatternStorage;
import matteroverdrive.data.matter_network.ItemPattern;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

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
			if (MatterHelper.isMatterPatternStorage(storage))
			{
				IMatterPatternStorage patternStorage = (IMatterPatternStorage)storage.getItem();
				for (int i = 0; i < patternStorage.getCapacity(storage); i++)
				{
					ItemPattern itemPattern = patternStorage.getPatternAt(storage, i);
					if (itemPattern == null)
					{
						return true;
					}
				}
			}

		}
		return false;
	}

	public static ItemStack getFirstFreePatternStorage(IMatterDatabase database)
	{
		ItemStack[] patternStorages = database.getPatternStorageList();

		for (ItemStack patternStorage : patternStorages)
		{
			if (patternStorage != null)
			{
				if (hasFreeSpace(patternStorage))
				{
					return patternStorage;
				}
			}
		}
		return null;
	}

	public static void addProgressToPatternStorage(ItemStack patternStorage, ItemStack item, int progress, boolean existingOnly)
	{
		if (!patternStorage.hasTagCompound())
		{
			initItemListTagCompound(patternStorage);
		}

		NBTTagList patternsTagList = patternStorage.getTagCompound().getTagList(ITEMS_TAG_NAME, Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < patternsTagList.tagCount(); i++)
		{
			ItemPattern pattern = new ItemPattern(patternsTagList.getCompoundTagAt(i));
			if (areEqual(pattern.toItemStack(false), item))
			{
				int oldProgress = patternsTagList.getCompoundTagAt(i).getByte(PROGRESS_TAG_NAME);
				patternsTagList.getCompoundTagAt(i).setByte(PROGRESS_TAG_NAME, (byte)MathHelper.clamp_int(oldProgress + progress, 0, MAX_ITEM_PROGRESS));
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
		ItemPattern itemPattern = getPatternFromStorage(storage, item);
		if (itemPattern != null)
		{
			return itemPattern.getProgress();
		}
		return -1;
	}

	public static ItemPattern getPatternFromStorage(ItemStack patternStorage, ItemStack item)
	{
		IMatterPatternStorage storage = (IMatterPatternStorage)patternStorage.getItem();
		for (int i = 0; i < storage.getCapacity(patternStorage); i++)
		{
			ItemPattern pattern = storage.getPatternAt(patternStorage, i);
			if (pattern != null && pattern.equals(item))
			{
				return pattern;
			}
		}
		return null;
	}


	public static boolean areEqual(ItemStack one, ItemStack two)
	{
		if (one != null && two != null)
		{
			if (one.getItem() == two.getItem())
			{
				if (one.getHasSubtypes() && two.getHasSubtypes())
				{
					if (one.getItemDamage() == two.getItemDamage())
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

	public static boolean areEqual(NBTTagCompound one, NBTTagCompound two)
	{
		if (one == null || two == null)
		{
			return false;
		}

		return areEqual(ItemStack.loadItemStackFromNBT(one), ItemStack.loadItemStackFromNBT(two));
	}

	public static ItemStack GetItemStackFromWorld(World world, BlockPos pos)
	{
		IBlockState blockState = world.getBlockState(pos);
		Item item = Item.getItemFromBlock(blockState.getBlock());
		if (item != null)
		{
			return new ItemStack(item, 1, blockState.getBlock().getMetaFromState(blockState));
		}
		return null;
	}

	public static TextFormatting getPatternInfoColor(int progress)
	{
		TextFormatting color;

		if (progress > 0 && progress <= 20)
		{
			color = TextFormatting.RED;
		}
		else if (progress > 20 && progress <= 40)
		{
			color = TextFormatting.GOLD;
		}
		else if (progress > 40 && progress <= 60)
		{
			color = TextFormatting.YELLOW;
		}
		else if (progress > 40 && progress <= 80)
		{
			color = TextFormatting.AQUA;
		}
		else if (progress > 80 && progress <= 100)
		{
			color = TextFormatting.GREEN;
		}
		else
		{
			color = TextFormatting.GREEN;
		}

		return color;
	}
}
