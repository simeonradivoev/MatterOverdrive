package com.MO.MatterOverdrive.util;

import cofh.lib.util.position.BlockPosition;
import com.MO.MatterOverdrive.api.matter.IMatterNetworkConnection;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 3/11/2015.
 */
public class MatterNetworkHelper
{
    public static boolean tryConnectToNetwork(World world,IMatterNetworkConnection from,boolean notify)
    {
        for (int i =0;i < 6;i++)
        {
            ForgeDirection direction = ForgeDirection.values()[i];
            BlockPosition position = from.getPosition();
            position.x += direction.offsetX;
            position.y += direction.offsetY;
            position.z += direction.offsetZ;

            TileEntity entity = world.getTileEntity(position.x,position.y,position.z);
            if(entity instanceof IMatterNetworkConnection)
            {
                IMatterNetworkConnection connection = (IMatterNetworkConnection)entity;

                if(canEstablishConnection(connection,from,direction))
                {
                    if(connection.getNetwork() != null)
                    {
                        connection.getNetwork().Rebuild(from);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean canEstablishConnection(IMatterNetworkConnection from,IMatterNetworkConnection to,ForgeDirection direction)
    {
        return from.canConnectToNetwork(direction) && to.canConnectToNetwork(MatterHelper.opposite(direction));
    }

    public static void disconnectFromNetwork(World world,IMatterNetworkConnection from,boolean notify)
    {
        if(from.getNetwork() != null)
        {
            from.getNetwork().Dissconnect(from);
        }
    }
}
