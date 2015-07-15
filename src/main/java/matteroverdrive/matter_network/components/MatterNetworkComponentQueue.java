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

package matteroverdrive.matter_network.components;

import cofh.lib.util.TimeTracker;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.position.BlockPosition;
import cpw.mods.fml.common.gameevent.TickEvent;
import matteroverdrive.Reference;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.api.network.MatterNetworkTaskState;
import matteroverdrive.matter_network.MatterNetworkPacket;
import matteroverdrive.matter_network.packets.MatterNetworkBroadcastPacket;
import matteroverdrive.matter_network.packets.MatterNetworkRequestPacket;
import matteroverdrive.matter_network.packets.MatterNetworkTaskPacket;
import matteroverdrive.matter_network.packets.MatterNetwrokResponcePacket;
import matteroverdrive.tile.TileEntityMachinePacketQueue;
import matteroverdrive.util.MatterNetworkHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 7/15/2015.
 */
public class MatterNetworkComponentQueue extends MatterNetworkComponentClient {

    public static int[] directions = {0,1,2,3,4,5};
    private TileEntityMachinePacketQueue queue;
    private TimeTracker broadcastTracker;

    public MatterNetworkComponentQueue(TileEntityMachinePacketQueue queue)
    {
        this.queue = queue;
        broadcastTracker = new TimeTracker();
    }

    @Override
    public boolean canPreform(MatterNetworkPacket packet)
    {
        return queue.getRedstoneActive();
    }

    @Override
    public void queuePacket(MatterNetworkPacket packet, ForgeDirection from)
    {
        if (packet.isValid(queue.getWorldObj()))
        {
            if (packet instanceof MatterNetworkTaskPacket && !isInValidState(((MatterNetworkTaskPacket) packet).getTask(queue.getWorldObj()))) {
                return;
            }
            if (packet instanceof MatterNetworkBroadcastPacket)
            {
                if (manageBroadcastPacket((MatterNetworkBroadcastPacket)packet,from))
                    return;
            }
            else if (packet instanceof MatterNetwrokResponcePacket)
            {
                if (manageResponcePackets((MatterNetwrokResponcePacket)packet,from))
                    return;
            }else if (packet instanceof MatterNetworkRequestPacket)
            {
                if (manageRequestPackets(queue,queue.getWorldObj(),(MatterNetworkRequestPacket)packet,from))
                    return;
            }

            if (queue.getQueue().queue(packet)) {
                packet.addToPath(queue, from);
                broadcastTracker.markTime(queue.getWorldObj());
                queue.ForceSync();
            }
        }
    }

    @Override
    public BlockPosition getPosition() {
        return queue.getPosition();
    }

    @Override
    public boolean canConnectFromSide(ForgeDirection side) {
        return queue.canConnectFromSide(side);
    }

    @Override
    public int onNetworkTick(World world, TickEvent.Phase phase) {
        int broadcastCount = 0;
        if (phase.equals(TickEvent.Phase.END))
        {
            queue.getQueue().tickAllAlive(world, true);

            if (broadcastTracker.hasDelayPassed(queue.getWorldObj(), getBroadcastDelay()))
            {
                MatterNetworkPacket packet = queue.getQueue().dequeue();

                if (packet != null)
                {
                    if (packet.isValid(queue.getWorldObj())) {

                        broadcastCount += handlePacketBroadcast(world,packet);
                    }

                    queue.ForceSync();
                }
            }
        }
        return broadcastCount;
    }

    protected int handlePacketBroadcast(World world,MatterNetworkPacket packet)
    {
        boolean foundReceiver = false;
        int broadcastCount = 0;

        if (packet.isGuided()) {
            //check if it already has an established connection to reciver
            for (int i = 0; i < queue.getConnections().length; i++) {
                if (queue.getConnection(i).equals(packet.getReceiverPos()))
                {
                    if (MatterNetworkHelper.broadcastTaskInDirection(queue.getWorldObj(), packet, queue, ForgeDirection.getOrientation(directions[i])))
                    {
                        foundReceiver = true;
                        broadcastCount++;
                    }
                }
            }
        }

        if (!foundReceiver) {
            //if there is no connection to receiver send to all around
            for (int i = 0; i < directions.length; i++) {
                if (packet instanceof MatterNetworkTaskPacket && !isInValidState(((MatterNetworkTaskPacket) packet).getTask(world)))
                    continue;

                if (MatterNetworkHelper.broadcastTaskInDirection(queue.getWorldObj(), packet, queue, ForgeDirection.getOrientation(directions[i]))) {
                    broadcastCount++;
                }
            }
        }
        return broadcastCount;
    }

    boolean manageResponcePackets(MatterNetwrokResponcePacket packet,ForgeDirection direction)
    {
        if (packet.getResponceType() == Reference.PACKET_RESPONCE_VALID && packet.getRequestType() == Reference.PACKET_REQUEST_NEIGHBOR_CONNECTION)
        {
            queue.setConnection(direction.ordinal(),packet.getSender(queue.getWorldObj()).getPosition());
            queue.ForceSync();
            return true;
        }
        return false;
    }

    boolean manageBroadcastPacket(MatterNetworkBroadcastPacket packet,ForgeDirection direction)
    {
        if ((packet.getBroadcastType() == Reference.PACKET_BROADCAST_CONNECTION))
        {
            queue.setConnection(direction.ordinal(),packet.getSender(queue.getWorldObj()).getPosition());
            queue.ForceSync();
            return true;
        }
        return false;
    }

    private int getBroadcastDelay()
    {
        return MathHelper.round(queue.BROADCAST_DELAY * queue.getUpgradeMultiply(UpgradeTypes.Speed));
    }

    private boolean isInValidState(MatterNetworkTask task)
    {
        if (task != null) {
            return task.getState() == MatterNetworkTaskState.WAITING;
        }
        return false;
    }
}
