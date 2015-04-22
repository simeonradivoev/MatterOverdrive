package com.MO.MatterOverdrive.api.network;

import com.MO.MatterOverdrive.data.network.MatterNetworkTaskPacket;

/**
 * Created by Simeon on 4/19/2015.
 */
public interface IMatterNetworkRouter extends IMatterNetworkConnection
{
    void queuePacket(MatterNetworkTaskPacket task);
}
