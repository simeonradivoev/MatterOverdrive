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

package matteroverdrive.api.weapon;

import io.netty.buffer.ByteBuf;

/**
 * Created by Simeon on 10/22/2015.
 */
public class WeaponShot
{
	protected int seed;
	protected float damage;
	protected float accuracy;
	protected int color;
	protected int range;
	protected int count = 1;

	public WeaponShot(WeaponShot shot)
	{
		this(shot.seed, shot.damage, shot.accuracy, shot.color, shot.range);
	}

	public WeaponShot(ByteBuf buf)
	{
		this(buf.readInt(), buf.readFloat(), buf.readFloat(), buf.readInt(), buf.readShort());
		setCount(buf.readByte());
	}

	public WeaponShot(int seed, float damage, float accuracy, int color, int range)
	{
		this.seed = seed;
		this.damage = damage;
		this.accuracy = accuracy;
		this.color = color;
		this.range = range;
	}

	public void writeTo(ByteBuf buf)
	{
		buf.writeInt(seed);
		buf.writeFloat(damage);
		buf.writeFloat(accuracy);
		buf.writeInt(color);
		buf.writeShort(range);
		buf.writeByte(count);
	}

	public int getSeed()
	{
		return seed;
	}

	public void setSeed(int seed)
	{
		this.seed = seed;
	}

	public float getDamage()
	{
		return damage;
	}

	public void setDamage(float damage)
	{
		this.damage = damage;
	}

	public float getAccuracy()
	{
		return accuracy;
	}

	public void setAccuracy(float accuracy)
	{
		this.accuracy = accuracy;
	}

	public int getColor()
	{
		return color;
	}

	public void setColor(int color)
	{
		this.color = color;
	}

	public int getRange()
	{
		return range;
	}

	public void setRange(int range)
	{
		this.range = range;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}
}
