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

package matteroverdrive.machines;

import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.data.Inventory;
import matteroverdrive.machines.events.MachineEvent;
import net.minecraft.nbt.NBTTagCompound;

import java.util.EnumSet;

/**
 * Created by Simeon on 7/19/2015.
 */
public interface IMachineComponent
{
    void readFromNBT(NBTTagCompound nbt,EnumSet<MachineNBTCategory> categories);
    void writeToNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk);
    void registerSlots(Inventory inventory);
    boolean isAffectedByUpgrade(UpgradeTypes type);
    boolean isActive();
    void onMachineEvent(MachineEvent event);
}
