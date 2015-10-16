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

package matteroverdrive.matter_network.handlers;

import matteroverdrive.Reference;
import matteroverdrive.matter_network.MatterNetworkPacket;
import matteroverdrive.matter_network.packets.MatterNetworkRequestPacket;
import matteroverdrive.util.MatterNetworkHelper;

/**
 * Created by Simeon on 10/16/2015.
 */
public class PacketHandlerBasicConnections extends AbstractMatterNetworkPacketHandler
{
    @Override
    public void processPacket(MatterNetworkPacket packet, AbstractMatterNetworkPacketHandler.Context context)
    {
        if (packet instanceof MatterNetworkRequestPacket) {
            MatterNetworkRequestPacket requestPacket = (MatterNetworkRequestPacket)packet;
            if (requestPacket.getRequestType() == Reference.PACKET_REQUEST_NEIGHBOR_CONNECTION || requestPacket.getRequestType() == Reference.PACKET_REQUEST_CONNECTION) {

                if (requestPacket.getRequest() instanceof Class) {
                    if (((Class) requestPacket.getRequest()).isInstance(context.connection)) {
                        MatterNetworkHelper.respondToRequest(context.world, context.connection, requestPacket, Reference.PACKET_RESPONCE_VALID, null);
                    }
                } else {
                    MatterNetworkHelper.respondToRequest(context.world, context.connection, requestPacket, Reference.PACKET_RESPONCE_VALID, null);
                }
            }
        }
    }
}
