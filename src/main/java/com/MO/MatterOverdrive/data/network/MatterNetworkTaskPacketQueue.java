package com.MO.MatterOverdrive.data.network;

import com.MO.MatterOverdrive.api.network.IMatterNetworkConnectionProxy;
import com.MO.MatterOverdrive.api.network.MatterNetworkTask;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 4/21/2015.
 */
public class MatterNetworkTaskPacketQueue
{
    IMatterNetworkConnectionProxy entity;
    protected List<MatterNetworkTaskPacket> tasks;
    int capacity = 0;

    public MatterNetworkTaskPacketQueue(IMatterNetworkConnectionProxy entity, int capacity)
    {
        this.entity = entity;
        tasks = new ArrayList<MatterNetworkTaskPacket>(capacity);
        this.capacity = capacity;
    }

    public boolean queuePacket(MatterNetworkTaskPacket task)
    {
        if (remaintingCapacity() > 0) {
            if (tasks.size() > 0) {
                try {
                    tasks.add(tasks.size() - 1, task);
                    return true;
                } catch (Exception e) {
                    FMLLog.severe("Could not add to Taks Queue: ", e);
                    return false;
                }
            } else {
                return tasks.add(task);
            }
        }
        return false;
    }

    public MatterNetworkTaskPacket dequeuePacket()
    {
        if (tasks.size() > 0)
        {
            return tasks.remove(tasks.size() - 1);
        }
        return null;
    }

    public MatterNetworkTaskPacket getAt(int i)
    {
        if (i >= 0 && i < tasks.size())
        {
            return tasks.get(i);
        }
        return null;
    }

    public int size()
    {
        return tasks.size();
    }

    public int remaintingCapacity()
    {
        return capacity - tasks.size();
    }

    public void readFromNBT(World world, NBTTagCompound tagCompound)
    {
        if (tagCompound == null || world == null)
            return;

        tasks.clear();
        NBTTagList tagList = tagCompound.getTagList("TaskPackets",10);
        for (int i = 0; i < tagList.tagCount();i++)
        {
            MatterNetworkTaskPacket packet = new MatterNetworkTaskPacket();
            packet.readFromNBT(tagList.getCompoundTagAt(i));
            tasks.add(packet);
        }
    }

    public void writeToNBT(World world, NBTTagCompound tagCompound)
    {
        NBTTagList taskList = new NBTTagList();
        for (MatterNetworkTaskPacket packet : tasks)
        {
            NBTTagCompound task = new NBTTagCompound();
            packet.writeToNBT(task);
            taskList.appendTag(task);
        }
        tagCompound.setTag("TaskPackets",taskList);
    }
}
