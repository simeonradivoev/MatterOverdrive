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

import matteroverdrive.api.matter.IMatterPatternStorage;
import matteroverdrive.data.matter_network.ItemPattern;
import matteroverdrive.items.includes.MOBaseItem;
import matteroverdrive.util.MatterDatabaseHelper;
import matteroverdrive.util.MatterHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Simeon on 3/27/2015.
 */
public class PatternDrive extends MOBaseItem implements IMatterPatternStorage
{
	/*private IIcon storageFull;
	private IIcon storagePartiallyFull;*/
	final int capacity;

	public PatternDrive(String name, int capacity)
	{
		super(name);
		this.capacity = capacity;
		this.setMaxStackSize(1);
	}

    /*@Override
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
    }*/

	@Override
	public int getDamage(ItemStack stack)
	{
		if (stack.hasTagCompound())
		{
			if (stack.getTagCompound().getKeySet().size() > 0)
			{
				if (stack.getTagCompound().getKeySet().size() < getCapacity(stack))
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
		if (itemstack.hasTagCompound())
		{
			for (int i = 0; i < getCapacity(itemstack); i++)
			{
				ItemPattern pattern = getPatternAt(itemstack, i);
				if (pattern != null)
				{
					ItemStack stack = pattern.toItemStack(false);
					String displayName;
					try
					{
						displayName = stack.getDisplayName();
					}
					catch (Exception e)
					{
						displayName = "Unknown";
					}

					if (MatterHelper.getMatterAmountFromItem(stack) > 0)
					{
						infos.add(MatterDatabaseHelper.getPatternInfoColor(pattern.getProgress()) + displayName + " [" + pattern.getProgress() + "%]");
					}
					else
					{
						infos.add(TextFormatting.RED + "[Invalid] " + MatterDatabaseHelper.getPatternInfoColor(pattern.getProgress()) + displayName + " [" + pattern.getProgress() + "%]");
					}
				}
			}
		}
	}

	@Override
	public void InitTagCompount(ItemStack stack)
	{
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setShort(MatterDatabaseHelper.CAPACITY_TAG_NAME, (short)capacity);
		NBTTagList itemList = new NBTTagList();
		tagCompound.setTag(MatterDatabaseHelper.ITEMS_TAG_NAME, itemList);
		stack.setTagCompound(tagCompound);
	}

	@Override
	public ItemPattern getPatternAt(ItemStack storage, int slot)
	{
		if (storage.getTagCompound() != null)
		{
			if (slot < getCapacity(storage) && storage.getTagCompound().hasKey("p" + slot))
			{
				ItemPattern pattern = new ItemPattern(storage.getTagCompound().getCompoundTag("p" + slot));
				return pattern;
			}
		}
		return null;
	}

	@Override
	public void setItemPatternAt(ItemStack storage, int slot, ItemPattern itemPattern)
	{
		if (storage.getTagCompound() == null)
		{
			storage.setTagCompound(new NBTTagCompound());
		}

		if (itemPattern != null)
		{
			NBTTagCompound patternTag = new NBTTagCompound();
			itemPattern.writeToNBT(patternTag);
			storage.getTagCompound().setTag("p" + slot, patternTag);
		}
		else
		{
			storage.getTagCompound().removeTag("p" + slot);
		}
	}

	@Override
	public boolean increasePatternProgress(ItemStack itemStack, int slot, int amount)
	{
		return false;
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
