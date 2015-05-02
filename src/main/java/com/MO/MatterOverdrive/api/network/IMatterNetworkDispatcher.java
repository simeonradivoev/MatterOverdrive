package com.MO.MatterOverdrive.api.network;

import com.MO.MatterOverdrive.matter_network.MatterNetworkTaskQueue;

/**
 * Created by Simeon on 4/20/2015.
 */
public interface IMatterNetworkDispatcher extends IMatterNetworkConnection
{
    MatterNetworkTaskQueue<? extends MatterNetworkTask> getQueue(byte queueID);
}
