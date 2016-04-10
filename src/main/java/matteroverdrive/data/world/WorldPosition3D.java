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

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

import java.io.Serializable;

/**
 * Created by Simeon on 11/19/2015.
 */
public class WorldPosition3D implements Serializable
{
	int x, y, z;

	public WorldPosition3D(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public WorldPosition3D(NBTTagCompound tagCompound)
	{
		readFromNBT(tagCompound);
	}

	public WorldPosition3D(ByteBuf byteBuf)
	{
		readFromBuffer(byteBuf);
	}

	public void writeToNBT(NBTTagCompound tagCompound)
	{
		tagCompound.setInteger("wp_x", x);
		tagCompound.setInteger("wp_y", y);
		tagCompound.setInteger("wp_z", z);
	}

	public void writeToBuffer(ByteBuf byteBuf)
	{
		byteBuf.writeInt(x);
		byteBuf.writeInt(y);
		byteBuf.writeInt(z);
	}

	public void readFromNBT(NBTTagCompound tagCompound)
	{
		x = tagCompound.getInteger("wp_x");
		y = tagCompound.getInteger("wp_y");
		z = tagCompound.getInteger("wp_z");
	}

	public void readFromBuffer(ByteBuf byteBuf)
	{
		x = byteBuf.readInt();
		y = byteBuf.readInt();
		z = byteBuf.readInt();
	}

	public int hashCode()
	{
		return this.x & 4095 | this.y & '\uff00' | this.z & 16773120;
	}
}
