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

import matteroverdrive.api.network.IMatterNetworkFilter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

/**
 * Created by Simeon on 3/11/2015.
 */
public class MatterNetworkHelper
{
	public static NBTTagCompound getFilterFromPositions(BlockPos... positions)
	{
		NBTTagCompound tagCompound = new NBTTagCompound();
		NBTTagList tagList = new NBTTagList();
		for (BlockPos position : positions)
		{
			tagList.appendTag(new NBTTagLong(position.toLong()));
		}
		tagCompound.setTag(IMatterNetworkFilter.CONNECTIONS_TAG, tagList);
		return tagCompound;
	}

	public static NBTTagCompound addPositionsToFilter(NBTTagCompound filter, BlockPos... positions)
	{
		if (filter == null)
		{
			filter = new NBTTagCompound();
		}

		NBTTagList tagList = filter.getTagList(IMatterNetworkFilter.CONNECTIONS_TAG, Constants.NBT.TAG_COMPOUND);
		for (BlockPos position : positions)
		{
			tagList.appendTag(new NBTTagLong(position.toLong()));
		}
		filter.setTag(IMatterNetworkFilter.CONNECTIONS_TAG, tagList);
		return filter;
	}
}
