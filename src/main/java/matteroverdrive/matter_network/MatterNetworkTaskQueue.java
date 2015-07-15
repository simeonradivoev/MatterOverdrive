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

package matteroverdrive.matter_network;

import matteroverdrive.api.network.IMatterNetworkConnection;
import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.api.network.MatterNetworkTaskState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;


/**
 * Created by Simeon on 4/19/2015.
 */
public class MatterNetworkTaskQueue<T extends MatterNetworkTask> extends MatterNetworkQueue<T>
{
    public MatterNetworkTaskQueue(IMatterNetworkConnection connection, int capacity)
    {
        super("Tasks", connection, capacity);
    }

    public void drop()
    {
        for (MatterNetworkTask task : elements)
        {
            task.setState(MatterNetworkTaskState.INVALID);
        }

        elements.clear();
    }

    public T dropWithID(long id)
    {
        for (int i = 0;i < elements.size();i++)
        {
            if (elements.get(i).getId() == id)
            {
                return elements.remove(i);
            }
        }
        return null;
    }

    public void tickAllAlive(World world,boolean alive)
    {
        for (int i = 0;i < elements.size();i++)
        {
            if (elements.get(i).isValid(world)) {
                elements.get(i).setAlive(alive);
            }
        }
    }

    public T getWithID(long id)
    {
        for (int i = 0;i < elements.size();i++)
        {
            if (elements.get(i).getId() == id)
            {
                return elements.get(i);
            }
        }
        return null;
    }

    @Override
    protected void readElementFromNBT(NBTTagCompound tagCompound, MatterNetworkTask element)
    {
        element.readFromNBT(tagCompound);
    }

    @Override
    protected void writeElementToNBT(NBTTagCompound tagCompound, MatterNetworkTask element)
    {
        element.writeToNBT(tagCompound);
        tagCompound.setInteger("Type", MatterNetworkRegistry.getTaskID(element.getClass()));
    }

    @Override
    protected Class getElementClassFromNBT(NBTTagCompound tagCompound)
    {
        return MatterNetworkRegistry.getTaskClass(tagCompound.getInteger("Type"));
    }
}
