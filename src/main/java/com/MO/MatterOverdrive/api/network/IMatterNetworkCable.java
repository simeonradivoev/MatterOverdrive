package com.MO.MatterOverdrive.api.network;

import matter_network.MatterNetworkPacket;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 4/19/2015.
 */
public interface IMatterNetworkCable extends IMatterNetworkConnection
{
    boolean isValid();
    void broadcast(MatterNetworkPacket task,ForgeDirection from);
}
