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

package matteroverdrive.matter_network.packets;

import cofh.lib.util.position.BlockPosition;
import matteroverdrive.api.network.IMatterNetworkConnection;
import matteroverdrive.api.network.IMatterNetworkDispatcher;
import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.api.network.MatterNetworkTaskState;
import matteroverdrive.matter_network.MatterNetworkPacket;
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

    public MatterNetworkTaskPacket(IMatterNetworkDispatcher sender,MatterNetworkTask task,byte queueID,ForgeDirection port,NBTTagCompound filter)
    {
        this(sender.getPosition(), task.getId(),queueID,port,filter);
    }

    public MatterNetworkTaskPacket(BlockPosition sender,long taskID,byte queueID,ForgeDirection port)
    {
        this(sender, taskID,queueID,port,null);
    }

    public MatterNetworkTaskPacket(BlockPosition sender,long taskID,byte queueID,ForgeDirection port,NBTTagCompound filter)
    {
        super(sender,port);
        this.taskID = taskID;
        this.queueID = queueID;
        this.filter = filter;
    }

    public MatterNetworkTaskPacket copy(IMatterNetworkConnection connection)
    {
        MatterNetworkTaskPacket newPacket = new MatterNetworkTaskPacket(senderPos,taskID,queueID,senderPos.orientation,filter);
        newPacket.path = new HashSet<>(path);
        addToPath(connection,ForgeDirection.UNKNOWN);
        return newPacket;
    }

    public MatterNetworkTask getTask(World world)
    {
        IMatterNetworkConnection sender = getSender(world);
        if (sender != null && sender instanceof IMatterNetworkDispatcher) {
            return ((IMatterNetworkDispatcher)(sender)).getTaskQueue(queueID).getWithID(taskID);
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
        return queueID >= (byte)0 && getTask(world) != null && getTask(world).getState() != MatterNetworkTaskState.INVALID;
    }

    @Override
    public String getName()
    {
        return "Task Packet";
    }
}
