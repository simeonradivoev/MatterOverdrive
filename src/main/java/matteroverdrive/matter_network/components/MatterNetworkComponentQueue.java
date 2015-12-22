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

import cpw.mods.fml.common.gameevent.TickEvent;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.matter_network.MatterNetworkPacket;
import matteroverdrive.matter_network.packets.MatterNetworkBroadcastPacket;
import matteroverdrive.matter_network.packets.MatterNetworkResponsePacket;
import matteroverdrive.network.packet.client.PacketSendQueueFlash;
import matteroverdrive.tile.TileEntityMachinePacketQueue;
import matteroverdrive.util.MatterNetworkHelper;
import matteroverdrive.util.TimeTracker;
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
    public boolean canPreform(MatterNetworkPacket packet)
    {
        return rootClient.getRedstoneActive();
    }

    @Override
    public void queuePacket(MatterNetworkPacket packet, ForgeDirection from)
    {
        if (canPreform(packet) && packet.isValid(getWorldObj()))
        {
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

            if (getPacketQueue(0).queue(packet))
            {
                packet.addToPath(rootClient, from);
                packet.tickAlive(getWorldObj(),true);
                broadcastTracker.markTime(getWorldObj());
                MatterOverdrive.packetPipeline.sendToAllAround(new PacketSendQueueFlash(rootClient), rootClient, 32);
            }
        }
    }

    @Override
    protected void executePacket(MatterNetworkPacket packet)
    {

    }

    protected int handlePacketBroadcast(World world,MatterNetworkPacket packet)
    {
        int broadcastCount = 0;
        for (int i = 0; i < directions.length; i++) {
            if (MatterNetworkHelper.broadcastPacketInDirection(world, packet, rootClient, ForgeDirection.getOrientation(directions[i]))) {
                broadcastCount++;
            }
        }
        return broadcastCount;
    }

    boolean manageResponsePackets(MatterNetworkResponsePacket packet,ForgeDirection direction)
    {
        if (packet.getResponseType() == Reference.PACKET_RESPONCE_VALID && packet.getRequestType() == Reference.PACKET_REQUEST_NEIGHBOR_CONNECTION)
        {
            rootClient.setConnection(direction.ordinal(), packet.getSender(getWorldObj()).getPosition());
            rootClient.forceSync();
            return true;
        }
        return false;
    }

    boolean manageBroadcastPacket(MatterNetworkBroadcastPacket packet,ForgeDirection direction)
    {
        if ((packet.getBroadcastType() == Reference.PACKET_BROADCAST_CONNECTION))
        {
            rootClient.setConnection(direction.ordinal(), packet.getSender(getWorldObj()).getPosition());
            rootClient.forceSync();
            return true;
        }
        return false;
    }

    private int getBroadcastDelay()
    {
        return (int)Math.round(TileEntityMachinePacketQueue.BROADCAST_DELAY * rootClient.getUpgradeMultiply(UpgradeTypes.Speed));
    }

    @Override
    public int onNetworkTick(World world, TickEvent.Phase phase)
    {
        int broadcastCount = 0;
        if (phase == TickEvent.Phase.END)
        {
            for (int i = 0;i < getPacketQueueCount();i++) {
                getPacketQueue(i).tickAllAlive(world, true);

                if (broadcastTracker.hasDelayPassed(world, getBroadcastDelay())) {
                    MatterNetworkPacket packet = getPacketQueue(i).dequeue();
                    if (packet != null) {
                        if (packet.isValid(world)) {

                            broadcastCount += handlePacketBroadcast(world, packet);
                        }
                    }
                }
            }
        }
        return broadcastCount;
    }
}
