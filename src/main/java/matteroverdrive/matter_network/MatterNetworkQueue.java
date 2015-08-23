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

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.network.IMatterNetworkConnection;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 7/15/2015.
 */
public abstract class MatterNetworkQueue <T>
{
    private IMatterNetworkConnection connection;
    protected List<T> elements;
    int capacity = 0;
    String name;

    public MatterNetworkQueue(String name,IMatterNetworkConnection connection, int capacity)
    {
        this.name = name;
        this.connection = connection;
        elements = new ArrayList<>(capacity);
        this.capacity = capacity;
    }

    public boolean queue(T element)
    {
        if (elements.size() > 0)
        {
            try
            {
                elements.add(elements.size(),element);
                return true;
            }
            catch (Exception e)
            {
				MatterOverdrive.log.error("Could not add element to queue", e);
                return false;
            }
        }
        else
        {
            return elements.add(element);
        }

    }

    public T dropAt(int i)
    {
        if (i < elements.size()) {
            return elements.remove(i);
        }
        return null;
    }

    public T dequeue()
    {
        if (elements.size() > 0)
        {
            return elements.remove(0);
        }
        return null;
    }

    public T peek()
    {
        if (elements.size() > 0)
        {
            return elements.get(0);
        }
        return null;
    }

    public int getLastIndex()
    {
        if (elements.size() > 0)
        {
            return elements.size()-1;
        }
        return -1;
    }

    public T getAt(int i)
    {
        if (i >= 0 && i < elements.size())
        {
            return elements.get(i);
        }
        return null;
    }

    public boolean remove(T task)
    {
        return elements.remove(task);
    }

    public int size()
    {
        return elements.size();
    }

    public int remaintingCapacity()
    {
        return capacity - elements.size();
    }

    public void readFromNBT(NBTTagCompound tagCompound)
    {
        if (tagCompound == null)
            return;

        elements.clear();
        NBTTagList tagList = tagCompound.getTagList(name,10);
        for (int i = 0; i < tagList.tagCount();i++)
        {
            try
            {
                T element = (T)getElementClassFromNBT(tagList.getCompoundTagAt(i)).newInstance();
                readElementFromNBT(tagList.getCompoundTagAt(i),element);
                elements.add(element);
            }
            catch (InstantiationException e)
            {
                MatterOverdrive.log.log(Level.ERROR,e,"There was a problem while loading a packet of type %s",getElementClassFromNBT(tagList.getCompoundTagAt(i)));
            }
            catch (IllegalAccessException e)
            {
                MatterOverdrive.log.log(Level.ERROR, e, "There was a problem while loading a packet of type %s", getElementClassFromNBT(tagList.getCompoundTagAt(i)));
            }
        }
    }

    protected abstract void readElementFromNBT(NBTTagCompound tagCompound,T element);
    protected abstract void writeElementToNBT(NBTTagCompound tagCompound,T element);
    protected abstract Class getElementClassFromNBT(NBTTagCompound tagCompound);

    public void writeToNBT(NBTTagCompound tagCompound)
    {
        NBTTagList taskList = new NBTTagList();
        for (T element : elements)
        {
            NBTTagCompound taskNBT = new NBTTagCompound();
            writeElementToNBT(taskNBT, element);
            taskList.appendTag(taskNBT);
        }
        tagCompound.setTag(name,taskList);
    }
    public IMatterNetworkConnection getConnection()
    {
        return connection;
    }
}
