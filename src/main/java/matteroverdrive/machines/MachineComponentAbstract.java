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

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Simeon on 7/19/2015.
 */
public abstract class MachineComponentAbstract<T extends MOTileEntityMachine> implements IMachineComponent
{
    protected final T machine;

    public MachineComponentAbstract(T machine)
    {
        this.machine = machine;
    }

    public T getMachine()
    {
        return machine;
    }

    public World getWorld(){return machine.getWorld();}

    public BlockPos getPos(){return machine.getPos();}
}
