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

import matteroverdrive.api.matter_network.IMatterNetworkConnection;
import matteroverdrive.api.network.IMatterNetworkFilter;
import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.List;

/**
 * Created by Simeon on 7/21/2015.
 */
public class NetworkFlashDrive extends MOBaseItem implements IMatterNetworkFilter
{

	public NetworkFlashDrive(String name)
	{
		super(name);
		setMaxStackSize(1);
	}

	public void addDetails(ItemStack itemstack, EntityPlayer player, List infos)
	{
		super.addDetails(itemstack, player, infos);
		if (itemstack.hasTagCompound())
		{
			NBTTagList list = itemstack.getTagCompound().getTagList(IMatterNetworkFilter.CONNECTIONS_TAG, Constants.NBT.TAG_LONG);
			for (int i = 0; i < list.tagCount(); i++)
			{
				BlockPos pos = BlockPos.fromLong(((NBTTagLong)list.get(i)).getLong());
				IBlockState block = player.worldObj.getBlockState(pos);
				if (block != null)
				{
					infos.add(String.format("[%s] %s", pos.toString(), block.getBlock() != Blocks.air ? block.getBlock().getLocalizedName() : "Unknown"));
				}
			}
		}
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof IMatterNetworkConnection)
		{
			BlockPos connectionPosition = tileEntity.getPos();
			if (!stack.hasTagCompound())
			{
				stack.setTagCompound(new NBTTagCompound());
			}

			boolean hasPos = false;
			NBTTagList list = stack.getTagCompound().getTagList(IMatterNetworkFilter.CONNECTIONS_TAG, Constants.NBT.TAG_LONG);
			for (int i = 0; i < list.tagCount(); i++)
			{
				BlockPos p = BlockPos.fromLong(((NBTTagLong)list.get(i)).getLong());
				if (p.equals(connectionPosition))
				{
					hasPos = true;
					list.removeTag(i);
					break;
				}
			}

			if (!hasPos)
			{
				list.appendTag(new NBTTagLong(pos.toLong()));
			}

			stack.getTagCompound().setTag(IMatterNetworkFilter.CONNECTIONS_TAG, list);

			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}

	@Override
	public NBTTagCompound getFilter(ItemStack stack)
	{
		return stack.getTagCompound();
	}
}
