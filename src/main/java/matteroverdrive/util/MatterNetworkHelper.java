package matteroverdrive.util;

import cofh.lib.util.position.BlockPosition;
import matteroverdrive.Reference;
import matteroverdrive.api.network.*;
import matteroverdrive.matter_network.MatterNetworkPacket;
import matteroverdrive.matter_network.packets.MatterNetworkBroadcastPacket;
import matteroverdrive.matter_network.packets.MatterNetworkRequestPacket;
import matteroverdrive.matter_network.packets.MatterNetworkTaskPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 3/11/2015.
 */
public class MatterNetworkHelper
{
    public static boolean broadcastTaskInDirection(World world, MatterNetworkPacket taskPacket, IMatterNetworkConnection source, ForgeDirection direction) {
        //if the source connection can connect From Side
        if (source.canConnectFromSide(direction)) {
            BlockPosition position = source.getPosition().step(direction);
            ForgeDirection oppositeDirection = direction.getOpposite();
            TileEntity e = position.getTileEntity(world);
            //if there is any connection in that direction
            if (e instanceof IMatterNetworkConnection) {
                IMatterNetworkConnection connection = (IMatterNetworkConnection) e;
                //check if the packet has passed trough the connection or if it can connect from opposite source side
                if (!taskPacket.hasPassedTrough(connection) && connection.canConnectFromSide(oppositeDirection)) {
                    if (connection instanceof IMatterNetworkCable) {
                        //if the connection is a cable
                        IMatterNetworkCable cable = (IMatterNetworkCable) connection;
                        if (cable.isValid()) {
                            cable.broadcast(taskPacket, direction);
                            return true;
                        }
                    } else if (connection instanceof IMatterNetworkClient) {
                        //if the connection is a client
                        IMatterNetworkClient c = (IMatterNetworkClient) connection;
                        if (c.canPreform(taskPacket)) {
                            c.queuePacket(taskPacket, oppositeDirection);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean broadcastTaskInDirection(World world,byte queueID, MatterNetworkTask task, IMatterNetworkDispatcher dispatcher, ForgeDirection direction)
    {
        return broadcastTaskInDirection(world, new MatterNetworkTaskPacket(dispatcher, task,queueID,direction), dispatcher, direction);
    }

    public static void broadcastConnection(World world,IMatterNetworkConnection connection)
    {
        for (int i = 0;i < 6;i++)
        {
            MatterNetworkBroadcastPacket packet = new MatterNetworkBroadcastPacket(connection.getPosition(),Reference.PACKET_BROADCAST_CONNECTION,ForgeDirection.getOrientation(i));
            broadcastTaskInDirection(world, packet, connection, ForgeDirection.getOrientation(i));
        }
    }

    public static void requestNeighborConnections(World world,IMatterNetworkConnection connection)
    {
        for (int i = 0;i < 6;i++)
        {
            MatterNetworkRequestPacket packet = new MatterNetworkRequestPacket(connection,Reference.PACKET_REQUEST_NEIGHBOR_CONNECTION,ForgeDirection.getOrientation(i),null);
            broadcastTaskInDirection(world, packet, connection, ForgeDirection.getOrientation(i));
        }
    }
}
