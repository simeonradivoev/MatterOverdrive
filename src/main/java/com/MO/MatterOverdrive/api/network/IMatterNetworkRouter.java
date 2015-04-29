package com.MO.MatterOverdrive.api.network;

import matter_network.MatterNetworkPacket;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 4/19/2015.
 */
public interface IMatterNetworkRouter extends IMatterNetworkConnection
{
    void queuePacket(MatterNetworkPacket task,ForgeDirection from);
}
