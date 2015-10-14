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

package matteroverdrive.api.network;

import matteroverdrive.matter_network.MatterNetworkPacket;
import matteroverdrive.matter_network.MatterNetworkPacketQueue;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 4/19/2015.
 * Used by Machine that can receive and complete Packets.
 */
public interface IMatterNetworkClient extends IMatterNetworkConnection,IMatterNetworkHandler
{
    /**
     * Can the given packet be preformed by the machine.
     * This is also where the packet filter is handled.
     * @param packet The packet.
     * @return can the packet be preformed.
     */
    boolean canPreform(MatterNetworkPacket packet);

    /**
     * Used to queue the received packet into the machine's queue.
     * @param packet the packet being queued.
     * @param from the side from which the packed was received.
     */
    void queuePacket(MatterNetworkPacket packet,ForgeDirection from);

    /**
     * Gets the Packet Queue of the machine at the specified queue ID.
     * @param queueID the ID of the Queue.
     * @return the Packet Queue at the given ID.
     */
    MatterNetworkPacketQueue<MatterNetworkPacket> getPacketQueue(int queueID);

    /**
     * Gets the count of all queues the machine has.
     * @return the number of queues in the machine.
     */
    int getPacketQueueCount();
}
