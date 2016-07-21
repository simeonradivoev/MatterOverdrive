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

import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.List;

/**
 * Created by Simeon on 8/5/2015.
 */
public class TransportFlashDrive extends MOBaseItem
{
	public TransportFlashDrive(String name)
	{
		super(name);
		setMaxStackSize(1);
	}

	public void addDetails(ItemStack itemstack, EntityPlayer player, List<String> infos)
	{
		super.addDetails(itemstack, player, infos);
		if (hasTarget(itemstack))
		{
			BlockPos target = getTarget(itemstack);
			IBlockState state = player.worldObj.getBlockState(target);
			Block block = state.getBlock();
			infos.add(TextFormatting.YELLOW + String.format("[%s] %s", target.toString(), state.getMaterial() != Material.AIR ? block.getLocalizedName() : "Unknown"));
		}
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		IBlockState state = worldIn.getBlockState(pos);
		if (state.getMaterial() != Material.AIR)
		{
			setTarget(stack, pos);
			return EnumActionResult.SUCCESS;
		}
		removeTarget(stack);
		return EnumActionResult.FAIL;
	}

	public void setTarget(ItemStack itemStack, BlockPos pos)
	{
		if (!itemStack.hasTagCompound())
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}

		itemStack.getTagCompound().setLong("target", pos.toLong());
	}

	public void removeTarget(ItemStack itemStack)
	{
		if (itemStack.hasTagCompound())
		{
			itemStack.setTagCompound(null);
		}
	}

	public BlockPos getTarget(ItemStack itemStack)
	{
		if (itemStack.getTagCompound() != null)
		{
			return BlockPos.fromLong(itemStack.getTagCompound().getLong("target"));
		}
		return null;
	}

	public boolean hasTarget(ItemStack itemStack)
	{
		return itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("taget", Constants.NBT.TAG_LONG);
	}
}
