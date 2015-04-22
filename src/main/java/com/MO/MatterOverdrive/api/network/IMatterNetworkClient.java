package com.MO.MatterOverdrive.api.network;

import com.MO.MatterOverdrive.data.network.MatterNetworkTaskPacket;

/**
 * Created by Simeon on 4/19/2015.
 */
public interface IMatterNetworkClient extends IMatterNetworkConnection
{
    boolean canPreform(MatterNetworkTaskPacket task);
    void queuePacket(MatterNetworkTaskPacket task);
}
