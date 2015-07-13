package matteroverdrive.matter_network;

import cpw.mods.fml.common.FMLLog;
import matteroverdrive.api.network.IMatterNetworkConnection;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 4/21/2015.
 */
public class MatterNetworkPacketQueue
{
    IMatterNetworkConnection entity;
    protected List<MatterNetworkPacket> tasks;
    int capacity = 0;

    public MatterNetworkPacketQueue(IMatterNetworkConnection entity, int capacity)
    {
        this.entity = entity;
        tasks = new ArrayList<>(capacity);
        this.capacity = capacity;
    }

    public boolean queuePacket(MatterNetworkPacket task)
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

    public void tickAllAlive(World world,boolean alive)
    {
        for (int i = 0;i < tasks.size();i++)
        {
            if (tasks.get(i).isValid(world)) {
                //tasks.get(i).getTask(world).setAlive(alive);
            }
        }
    }

    public MatterNetworkPacket dequeuePacket()
    {
        if (tasks.size() > 0)
        {
            return tasks.remove(0);
        }
        return null;
    }

    public MatterNetworkPacket peek()
    {
        if (tasks.size() > 0)
        {
            return tasks.get(0);
        }
        return null;
    }

    public MatterNetworkPacket getAt(int i)
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
            MatterNetworkPacket packet = null;
            try {
                packet = MatterNetworkRegistry.getPacketClass(tagList.getCompoundTagAt(i).getInteger("Type")).newInstance();
                packet.readFromNBT(tagList.getCompoundTagAt(i));
                tasks.add(packet);

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeToNBT(World world, NBTTagCompound tagCompound)
    {
        NBTTagList taskList = new NBTTagList();
        for (MatterNetworkPacket packet : tasks)
        {
            NBTTagCompound task = new NBTTagCompound();
            packet.writeToNBT(task);
            task.setInteger("Type",MatterNetworkRegistry.getPacketID(packet.getClass()));
            taskList.appendTag(task);
        }
        tagCompound.setTag("TaskPackets",taskList);
    }
}
