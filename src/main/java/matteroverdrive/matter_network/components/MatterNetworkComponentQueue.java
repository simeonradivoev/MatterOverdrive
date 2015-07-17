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
import cpw.mods.fml.common.gameevent.TickEvent;
import matteroverdrive.Reference;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.api.network.MatterNetworkTaskState;
import matteroverdrive.matter_network.MatterNetworkPacket;
import matteroverdrive.matter_network.packets.MatterNetworkBroadcastPacket;
import matteroverdrive.matter_network.packets.MatterNetworkRequestPacket;
import matteroverdrive.matter_network.packets.MatterNetworkTaskPacket;
import matteroverdrive.matter_network.packets.MatterNetworkResponsePacket;
import matteroverdrive.tile.TileEntityMachinePacketQueue;
import matteroverdrive.util.MatterNetworkHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 7/15/2015.
 */
public class MatterNetworkComponentQueue extends MatterNetworkComponentClient<TileEntityMachinePacketQueue> {

    public static int[] directions = {0,1,2,3,4,5};
    private TimeTracker broadcastTracker;

    public MatterNetworkComponentQueue(TileEntityMachinePacketQueue queue)
    {
        super(queue);
        broadcastTracker = new TimeTracker();
    }

    @Override
    protected void manageResponsesQueuing(MatterNetworkResponsePacket packet) {

    }

    @Override
    protected void manageTaskPacketQueuing(MatterNetworkTaskPacket packet, MatterNetworkTask task) {

    }

    @Override
    protected void manageRequestsQueuing(MatterNetworkRequestPacket packet) {

    }

    @Override
    public boolean canPreform(MatterNetworkPacket packet)
    {
        if (super.canPreform(packet))
        {
            return rootClient.getRedstoneActive();
        }
        return false;
    }

    @Override
    public void queuePacket(MatterNetworkPacket packet, ForgeDirection from)
    {
        if (packet.isValid(rootClient.getWorldObj()))
        {
            if (packet instanceof MatterNetworkTaskPacket && !isInValidState(((MatterNetworkTaskPacket) packet).getTask(rootClient.getWorldObj()))) {
                return;
            }
            if (packet instanceof MatterNetworkBroadcastPacket)
            {
                if (manageBroadcastPacket((MatterNetworkBroadcastPacket)packet,from))
                    return;
            }
            else if (packet instanceof MatterNetworkResponsePacket)
            {
                if (manageResponsePackets((MatterNetworkResponsePacket)packet,from))
                    return;
            }

            if (rootClient.getPacketQueue().queue(packet))
            {
                packet.addToPath(rootClient, from);
                broadcastTracker.markTime(rootClient.getWorldObj());
                rootClient.ForceSync();
            }
        }
    }

    protected int handlePacketBroadcast(World world,MatterNetworkPacket packet)
    {
        boolean foundReceiver = false;
        int broadcastCount = 0;

        if (packet.isGuided()) {
            //check if it already has an established connection to reciver
            for (int i = 0; i < rootClient.getConnections().length; i++) {
                if (rootClient.getConnection(i).equals(packet.getReceiverPos()))
                {
                    if (MatterNetworkHelper.broadcastTaskInDirection(rootClient.getWorldObj(), packet, rootClient, ForgeDirection.getOrientation(directions[i])))
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

                if (MatterNetworkHelper.broadcastTaskInDirection(rootClient.getWorldObj(), packet, rootClient, ForgeDirection.getOrientation(directions[i]))) {
                    broadcastCount++;
                }
            }
        }
        return broadcastCount;
    }

    boolean manageResponsePackets(MatterNetworkResponsePacket packet,ForgeDirection direction)
    {
        if (packet.getResponseType() == Reference.PACKET_RESPONCE_VALID && packet.getRequestType() == Reference.PACKET_REQUEST_NEIGHBOR_CONNECTION)
        {
            rootClient.setConnection(direction.ordinal(),packet.getSender(rootClient.getWorldObj()).getPosition());
            rootClient.ForceSync();
            return true;
        }
        return false;
    }

    boolean manageBroadcastPacket(MatterNetworkBroadcastPacket packet,ForgeDirection direction)
    {
        if ((packet.getBroadcastType() == Reference.PACKET_BROADCAST_CONNECTION))
        {
            rootClient.setConnection(direction.ordinal(),packet.getSender(rootClient.getWorldObj()).getPosition());
            rootClient.ForceSync();
            return true;
        }
        return false;
    }

    private int getBroadcastDelay()
    {
        return MathHelper.round(TileEntityMachinePacketQueue.BROADCAST_DELAY * rootClient.getUpgradeMultiply(UpgradeTypes.Speed));
    }

    private boolean isInValidState(MatterNetworkTask task)
    {
        if (task != null) {
            return task.getState() == MatterNetworkTaskState.WAITING;
        }
        return false;
    }

    @Override
    public int onNetworkTick(World world, TickEvent.Phase phase)
    {
        int broadcastCount = 0;
        if (phase == TickEvent.Phase.END)
        {
            rootClient.getPacketQueue().tickAllAlive(world, true);

            if (broadcastTracker.hasDelayPassed(rootClient.getWorldObj(), getBroadcastDelay()))
            {
                MatterNetworkPacket packet = rootClient.getPacketQueue().dequeue();
                if (packet != null)
                {
                    if (packet.isValid(rootClient.getWorldObj())) {

                        broadcastCount += handlePacketBroadcast(world,packet);
                    }

                    rootClient.ForceSync();
                }
            }
        }
        return broadcastCount;
    }
}
