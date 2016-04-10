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

package matteroverdrive.machines.configs;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Simeon on 8/16/2015.
 */
public class ConfigPropertyBoolean extends ConfigPropertyAbstract
{

	private boolean value;

	public ConfigPropertyBoolean(String key, String unlocalizedName, boolean def)
	{
		super(key, unlocalizedName);
		value = def;
	}

	public ConfigPropertyBoolean(String name, String unlocalizedName)
	{
		super(name, unlocalizedName);
	}

	@Override
	public Object getValue()
	{
		return value;
	}

	@Override
	public void setValue(Object value)
	{
		this.value = (boolean)value;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setBoolean(getUnlocalizedName(), value);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		value = nbt.getBoolean(getUnlocalizedName());
	}

	@Override
	public Class getType()
	{
		return Boolean.class;
	}
}
