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

import cofh.lib.util.position.BlockPosition;
import matteroverdrive.Reference;
import matteroverdrive.api.network.IMatterNetworkClient;
import matteroverdrive.api.network.IMatterNetworkConnection;
import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.matter_network.MatterNetworkPacket;
import matteroverdrive.matter_network.packets.MatterNetworkRequestPacket;
import matteroverdrive.matter_network.packets.MatterNetworkResponsePacket;
import matteroverdrive.matter_network.packets.MatterNetworkTaskPacket;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 7/15/2015.
 */
public abstract class MatterNetworkComponentClient<T extends IMatterNetworkClient> implements IMatterNetworkClient
{
    T rootClient;

    public MatterNetworkComponentClient(T rootClient)
    {
        this.rootClient = rootClient;
    }

    public boolean manageBasicRequestPacketsResponses(IMatterNetworkConnection connection, World world, MatterNetworkRequestPacket packet, ForgeDirection direction)
    {
        IMatterNetworkConnection sender = packet.getSender(world);

        if (packet.getRequestType() == Reference.PACKET_REQUEST_NEIGHBOR_CONNECTION || packet.getRequestType() == Reference.PACKET_REQUEST_CONNECTION) {

            if (packet.getRequest() instanceof Class)
            {
                if (((Class)packet.getRequest()).isInstance(connection))
                {
                    if (sender instanceof IMatterNetworkClient) {
                        MatterNetworkResponsePacket responsePacket = new MatterNetworkResponsePacket(connection, Reference.PACKET_RESPONCE_VALID, packet.getRequestType(), null,direction);

                        if (((IMatterNetworkClient) sender).canPreform(responsePacket)) {
                            ((IMatterNetworkClient) sender).queuePacket(responsePacket, packet.getSenderPort());

                        }
                    }
                    return true;
                }
            } else {
                if (sender instanceof IMatterNetworkClient) {
                    MatterNetworkResponsePacket responsePacket = new MatterNetworkResponsePacket(connection, Reference.PACKET_RESPONCE_VALID, packet.getRequestType(), null,direction);
                    if (((IMatterNetworkClient) sender).canPreform(responsePacket)) {
                        ((IMatterNetworkClient) sender).queuePacket(responsePacket, packet.getSenderPort());

                    }
                }
                return true;
            }
        }
        return false;
    }

    protected abstract void manageResponsesQueuing(MatterNetworkResponsePacket packet);
    protected abstract void manageTaskPacketQueuing(MatterNetworkTaskPacket packet,MatterNetworkTask task);
    protected abstract void manageRequestsQueuing(MatterNetworkRequestPacket packet);

    public void manageBasicPacketsQueuing(IMatterNetworkConnection connection,World world,MatterNetworkPacket packet,ForgeDirection direction)
    {
        packet.addToPath(rootClient, direction);

        if (packet instanceof MatterNetworkRequestPacket)
        {
            manageRequestsQueuing((MatterNetworkRequestPacket)packet);
            manageBasicRequestPacketsResponses(connection, world, (MatterNetworkRequestPacket) packet, direction);
        }
        else if (packet instanceof MatterNetworkResponsePacket)
        {
            manageResponsesQueuing((MatterNetworkResponsePacket)packet);
        }
        else if (packet instanceof MatterNetworkTaskPacket)
        {
            manageTaskPacketQueuing((MatterNetworkTaskPacket) packet, ((MatterNetworkTaskPacket) packet).getTask(world));
        }
    }

    @Override
    public boolean canPreform(MatterNetworkPacket packet)
    {
        if (packet.getReceiverPos() != null)
        {
            return packet.getReceiverPos().equals(getPosition());
        }
        return true;
    }

    //region Getters and Setters
    @Override
    public BlockPosition getPosition()
    {
        return rootClient.getPosition();
    }

    @Override
    public boolean canConnectFromSide(ForgeDirection side)
    {
        return rootClient.canConnectFromSide(side);
    }
    //endregion
}
