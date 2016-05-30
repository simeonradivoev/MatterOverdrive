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

package matteroverdrive.data.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Simeon on 11/19/2015.
 */
public class GenPositionWorldData extends WorldSavedData
{
	final Map<String, List<WorldPosition2D>> positions;

	public GenPositionWorldData(String name)
	{
		super(name);
		positions = new HashMap<>();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound)
	{
		for (Object key : nbtTagCompound.getKeySet())
		{
			List<WorldPosition2D> pos2D = new ArrayList<>();
			NBTTagList tagList = nbtTagCompound.getTagList(key.toString(), Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < tagList.tagCount(); i++)
			{
				pos2D.add(new WorldPosition2D(tagList.getCompoundTagAt(i)));
			}
			positions.put(key.toString(), pos2D);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound)
	{
		for (Map.Entry<String, List<WorldPosition2D>> entry : positions.entrySet())
		{
			NBTTagList tagList = new NBTTagList();
			for (WorldPosition2D worldPosition2D : entry.getValue())
			{
				NBTTagCompound worldPositionTag = new NBTTagCompound();
				worldPosition2D.writeToNBT(worldPositionTag);
				tagList.appendTag(worldPositionTag);
			}
			nbtTagCompound.setTag(entry.getKey(), tagList);
		}
		return nbtTagCompound;
	}

	public boolean isFarEnough(String name, int x, int y, int distance)
	{
		List<WorldPosition2D> positions = this.positions.get(name);
		if (positions != null)
		{
			for (WorldPosition2D worldPosition2D : positions)
			{
				if (worldPosition2D.manhattanDistance(x, y) < distance)
				{
					return false;
				}
			}
		}
		return true;
	}

	public double getNearestDistance(String name, Vec3d pos)
	{
		List<WorldPosition2D> positions = this.positions.get(name);
		double lastDistance = -1;
		double tempDist;

		if (positions != null)
		{
			for (WorldPosition2D worldPosition2D : positions)
			{
				tempDist = new Vec3d(worldPosition2D.x, pos.yCoord, worldPosition2D.z).distanceTo(pos);
				if (lastDistance < 0 || tempDist < lastDistance)
				{
					lastDistance = tempDist;
				}
			}
		}
		return lastDistance;
	}

	public void addPosition(String name, WorldPosition2D position2D)
	{
		List<WorldPosition2D> pos = positions.get(name);
		if (pos == null)
		{
			pos = new ArrayList<>();
			positions.put(name, pos);
		}
		pos.add(position2D);
		markDirty();
	}

	public List<WorldPosition2D> getPositions(String name)
	{
		return positions.get(name);
	}
}
