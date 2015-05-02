package com.MO.MatterOverdrive.matter_network.packets;

import cofh.lib.util.position.BlockPosition;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.network.IMatterNetworkConnection;
import com.MO.MatterOverdrive.api.network.IMatterNetworkDispatcher;
import com.MO.MatterOverdrive.api.network.MatterNetworkTask;
import com.MO.MatterOverdrive.matter_network.MatterNetworkPacket;
import com.MO.MatterOverdrive.matter_network.MatterNetworkPathNode;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashSet;

/**
 * Created by Simeon on 4/20/2015.
 */
public class MatterNetworkTaskPacket extends MatterNetworkPacket
{
    private long taskID;
    private byte queueID = -1;

    public MatterNetworkTaskPacket()
    {
        super();
    }

    public MatterNetworkTaskPacket(IMatterNetworkDispatcher sender,long taskID,byte queueID,ForgeDirection port)
    {
        this(sender.getPosition(), taskID,queueID,port);
    }

    public MatterNetworkTaskPacket(IMatterNetworkDispatcher sender,MatterNetworkTask task,byte queueID,ForgeDirection port)
    {
        this(sender.getPosition(), task.getId(),queueID,port);
    }

    public MatterNetworkTaskPacket(BlockPosition sender,long taskID,byte queueID,ForgeDirection port)
    {
        super(sender,port);
        this.taskID = taskID;
        this.queueID = queueID;

    }

    public MatterNetworkTaskPacket copy(IMatterNetworkConnection connection)
    {
        MatterNetworkTaskPacket newPacket = new MatterNetworkTaskPacket(senderPos,taskID,queueID,senderPos.orientation);
        newPacket.path = new HashSet<MatterNetworkPathNode>(path);
        addToPath(connection,ForgeDirection.UNKNOWN);
        return newPacket;
    }

    public MatterNetworkTask getTask(World world)
    {
        IMatterNetworkConnection sender = getSender(world);
        if (sender != null && sender instanceof IMatterNetworkDispatcher) {
            return ((IMatterNetworkDispatcher)(sender)).getQueue(queueID).getWithID(taskID);
        }
        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        if (tagCompound != null) {
            taskID = tagCompound.getLong("TaskID");
            queueID = tagCompound.getByte("QueueID");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        if (tagCompound != null)
        {
            tagCompound.setLong("TaskID", taskID);
            tagCompound.setByte("QueueID",queueID);
        }
    }

    public boolean isValid(World world)
    {
        return queueID >= (byte)0 && getTask(world) != null && getTask(world).getState() != Reference.TASK_STATE_INVALID;
    }

    @Override
    public String getName()
    {
        return "Task Packet";
    }
}
