package com.MO.MatterOverdrive.data.network;

import cofh.lib.util.position.BlockPosition;
import com.MO.MatterOverdrive.api.network.IMatterNetworkConnection;
import com.MO.MatterOverdrive.api.network.IMatterNetworkConnectionProxy;
import com.MO.MatterOverdrive.api.network.IMatterNetworkDispatcher;
import com.MO.MatterOverdrive.api.network.MatterNetworkTask;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.HashSet;

/**
 * Created by Simeon on 4/20/2015.
 */
public class MatterNetworkTaskPacket
{
    private int taskID = -1;
    BlockPosition senderPos;
    HashSet<BlockPosition> path;

    public MatterNetworkTaskPacket()
    {
        path = new HashSet<BlockPosition>();
    }

    public MatterNetworkTaskPacket(IMatterNetworkDispatcher sender,int taskID)
    {
        this();
        this.taskID = taskID;
        this.senderPos = sender.getPosition();
    }

    public MatterNetworkTaskPacket(BlockPosition sender,int taskID)
    {
        this.taskID = taskID;
        this.senderPos = sender;
    }

    public MatterNetworkTaskPacket copy(IMatterNetworkConnection connection)
    {
        MatterNetworkTaskPacket newPacket = new MatterNetworkTaskPacket(senderPos,taskID);
        newPacket.path = new HashSet<BlockPosition>(path);
        newPacket.path.add(connection.getPosition());
        return newPacket;
    }

    public MatterNetworkTaskPacket addToPath(IMatterNetworkConnection connection)
    {
        path.add(connection.getPosition());
        return this;
    }

    public boolean hasPassedTrough(IMatterNetworkConnection connection)
    {
        return path.contains(connection.getPosition());
    }

    public IMatterNetworkDispatcher getSender(World world)
    {
        if (world != null)
        {
            TileEntity tileEntity = senderPos.getTileEntity(world);
            if (tileEntity != null && tileEntity instanceof IMatterNetworkConnectionProxy && ((IMatterNetworkConnectionProxy) tileEntity).getMatterNetworkConnection() instanceof IMatterNetworkDispatcher)
                return (IMatterNetworkDispatcher)((IMatterNetworkConnectionProxy) tileEntity).getMatterNetworkConnection();
        }
        return null;
    }

    public MatterNetworkTask getTask(World world)
    {
        IMatterNetworkDispatcher sender = getSender(world);
        if (sender != null) {
            return sender.getQueue().getAt(taskID);
        }
        return null;
    }

    public void readFromNBT(NBTTagCompound tagCompound)
    {
        if (tagCompound != null) {
            senderPos = new BlockPosition(tagCompound);
            taskID = tagCompound.getInteger("TaskID");
        }
    }

    public void writeToNBT(NBTTagCompound tagCompound)
    {
        if (tagCompound != null)
        {
            senderPos.writeToNBT(tagCompound);
            tagCompound.setInteger("TaskID",taskID);
        }
    }

    public boolean isValid(World world)
    {
        return taskID >= 0 && getTask(world) != null;
    }
}
