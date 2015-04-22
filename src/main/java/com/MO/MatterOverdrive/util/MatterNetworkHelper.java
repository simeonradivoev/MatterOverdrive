package com.MO.MatterOverdrive.util;

import cofh.lib.util.position.BlockPosition;
import com.MO.MatterOverdrive.api.network.*;
import com.MO.MatterOverdrive.data.network.MatterNetworkTaskPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 3/11/2015.
 */
public class MatterNetworkHelper
{
    public static void broadcastTaskInDirection(World world, MatterNetworkTaskPacket task, IMatterNetworkConnection source, ForgeDirection direction) {
            //if the source connection can connect From Side
            if (source.canConnectFromSide(direction)){
                BlockPosition position = source.getPosition().step(direction);
                TileEntity e = position.getTileEntity(world);
                //if there is any connection in that direction
                if (e instanceof IMatterNetworkConnectionProxy) {
                    IMatterNetworkConnection connection = ((IMatterNetworkConnectionProxy) e).getMatterNetworkConnection();
                    //check if the packet has passed trough the connection or if it can connect from opposite source side
                    if (!task.hasPassedTrough(connection) && connection.canConnectFromSide(direction.getOpposite()))
                    {
                        if (connection instanceof IMatterNetworkRouter) {
                            //if the connection is a Router
                            IMatterNetworkRouter router = ((IMatterNetworkRouter) connection);
                            router.queuePacket(task);
                        } else if (connection instanceof IMatterNetworkCable) {
                            //if the connection is a cable
                            IMatterNetworkCable cable = (IMatterNetworkCable) connection;
                            if (cable.isValid()) {
                                cable.broadcast(task);
                            }
                        } else if (connection instanceof IMatterNetworkClient) {
                            //if the connection is a client
                            IMatterNetworkClient c = (IMatterNetworkClient) connection;
                            if (c.canPreform(task)) {
                                c.queuePacket(task);
                            }
                        }
                    }
                }
            }
    }

    public static void broadcastTaskInDirection(World world, int taskID, IMatterNetworkDispatcher dispatcher, ForgeDirection direction)
    {
        if (taskID >= 0 && taskID < dispatcher.getQueue().size()) {
            broadcastTaskInDirection(world, new MatterNetworkTaskPacket(dispatcher, taskID), dispatcher, direction);
        }
    }
}
