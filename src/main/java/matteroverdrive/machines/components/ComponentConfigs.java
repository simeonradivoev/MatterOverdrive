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

import cpw.mods.fml.relauncher.Side;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.data.Inventory;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.machines.MachineComponentAbstract;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.configs.IConfigProperty;
import matteroverdrive.machines.configs.IConfigurable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simeon on 8/16/2015.
 */
public class ComponentConfigs extends MachineComponentAbstract<MOTileEntityMachine> implements IConfigurable
{
    private Map<String,IConfigProperty> propertyMap;

    public ComponentConfigs(MOTileEntityMachine machine)
    {
        super(machine);
        propertyMap = new HashMap<>();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
    {
        if (categories.contains(MachineNBTCategory.CONFIGS)) {
            for (IConfigProperty property : propertyMap.values()) {
                property.readFromNBT(nbt);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
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
    public void registerSlots(Inventory inventory) {

    }

    @Override
    public void update(MOTileEntityMachine machine) {

    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type) {
        return false;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void onActiveChange(MOTileEntityMachine machine) {

    }

    @Override
    public void onAwake(MOTileEntityMachine machine, Side side) {

    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving, MOTileEntityMachine machine) {

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
        propertyMap.put(property.getKey(),property);
    }

    public boolean getBoolean(String key,boolean def)
    {
        if (propertyMap.containsKey(key))
        {
            if (propertyMap.get(key).getType() == Boolean.class)
            {
                return (Boolean)propertyMap.get(key).getValue();
            }
        }
        return def;
    }

    public Integer getInteger(String key, int def)
    {
        if (propertyMap.containsKey(key))
        {
            if (propertyMap.get(key).getType().equals(Integer.class))
            {
                return (Integer)propertyMap.get(key).getValue();
            }
        }
        return def;
    }

    public Integer getEnum(String key,int def)
    {
        if (propertyMap.containsKey(key))
        {
            if (propertyMap.get(key).getType().equals(Enum.class))
            {
                return (Integer)propertyMap.get(key).getValue();
            }
        }
        return def;
    }
}
