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

package matteroverdrive.machines.components;

import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.data.Inventory;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.machines.MachineComponentAbstract;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.configs.IConfigProperty;
import matteroverdrive.machines.configs.IConfigurable;
import matteroverdrive.machines.events.MachineEvent;
import net.minecraft.nbt.NBTTagCompound;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simeon on 8/16/2015.
 */
public class ComponentConfigs extends MachineComponentAbstract<MOTileEntityMachine> implements IConfigurable
{
	private final Map<String, IConfigProperty> propertyMap;

	public ComponentConfigs(MOTileEntityMachine machine)
	{
		super(machine);
		propertyMap = new HashMap<>();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
	{
		if (categories.contains(MachineNBTCategory.CONFIGS))
		{
			for (IConfigProperty property : propertyMap.values())
			{
				property.readFromNBT(nbt);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk)
	{
		if (categories.contains(MachineNBTCategory.CONFIGS))
		{
			for (IConfigProperty property : propertyMap.values())
			{
				property.writeToNBT(nbt);
			}
		}
	}

	@Override
	public void registerSlots(Inventory inventory)
	{

	}

	@Override
	public boolean isAffectedByUpgrade(UpgradeTypes type)
	{
		return false;
	}

	@Override
	public boolean isActive()
	{
		return false;
	}

	@Override
	public void onMachineEvent(MachineEvent event)
	{

	}

	@Override
	public Map<String, IConfigProperty> getValues()
	{
		return propertyMap;
	}

	@Override
	public IConfigProperty getProperty(String name)
	{
		return propertyMap.get(name);
	}

	public void addProperty(IConfigProperty property)
	{
		propertyMap.put(property.getKey(), property);
	}

	public boolean getBoolean(String key, boolean def)
	{
		IConfigProperty property = propertyMap.get(key);
		if (property != null && property.getType() == Boolean.class)
		{
			return (Boolean)property.getValue();
		}
		return def;
	}

	public Integer getInteger(String key, int def)
	{
		IConfigProperty property = propertyMap.get(key);
		if (property != null && property.getType().equals(Integer.class))
		{
			return (Integer)property.getValue();
		}
		return def;
	}

	public Integer getEnum(String key, int def)
	{
		IConfigProperty property = propertyMap.get(key);
		if (property != null && property.getType().equals(Enum.class))
		{
			return (Integer)property.getValue();
		}
		return def;
	}

	public String getString(String key, String def)
	{
		IConfigProperty property = propertyMap.get(key);
		if (property != null && property.getType().equals(String.class))
		{
			return (String)property.getValue();
		}
		return def;
	}
}
