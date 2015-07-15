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

import matteroverdrive.Reference;
import matteroverdrive.api.network.IMatterNetworkClient;
import matteroverdrive.api.network.IMatterNetworkConnection;
import matteroverdrive.matter_network.packets.MatterNetworkRequestPacket;
import matteroverdrive.matter_network.packets.MatterNetworkResponsePacket;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 7/15/2015.
 */
public abstract class MatterNetworkComponentClient implements IMatterNetworkClient
{
    public boolean manageRequestPackets(IMatterNetworkConnection connection,World world,MatterNetworkRequestPacket packet,ForgeDirection direction)
    {
        IMatterNetworkConnection sender = packet.getSender(world);

        if (packet.getRequestType() == Reference.PACKET_REQUEST_NEIGHBOR_CONNECTION || packet.getRequestType() == Reference.PACKET_REQUEST_CONNECTION) {

            if (packet.getRequest() instanceof Class)
            {
                if (((Class)packet.getRequest()).isInstance(connection))
                {
                    if (sender instanceof IMatterNetworkClient) {
                        MatterNetworkResponsePacket responcePacket = new MatterNetworkResponsePacket(connection, Reference.PACKET_RESPONCE_VALID, packet.getRequestType(), null,direction);

                        if (((IMatterNetworkClient) sender).canPreform(responcePacket)) {
                            ((IMatterNetworkClient) sender).queuePacket(responcePacket, packet.getSenderPort());

                        }
                    }
                    return true;
                }
            } else {
                if (sender instanceof IMatterNetworkClient) {
                    MatterNetworkResponsePacket responcePacket = new MatterNetworkResponsePacket(connection, Reference.PACKET_RESPONCE_VALID, packet.getRequestType(), null,direction);
                    if (((IMatterNetworkClient) sender).canPreform(responcePacket)) {
                        ((IMatterNetworkClient) sender).queuePacket(responcePacket, packet.getSenderPort());

                    }
                }
                return true;
            }
        }
        return false;
    }
}
