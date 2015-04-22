package com.MO.MatterOverdrive.api.network;

import cofh.lib.util.position.BlockPosition;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 3/11/2015.
 */
public interface IMatterNetworkConnection
{
    BlockPosition getPosition();
    boolean canConnectFromSide(ForgeDirection side);
}
