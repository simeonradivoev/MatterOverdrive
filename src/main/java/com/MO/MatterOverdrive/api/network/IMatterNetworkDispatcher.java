package com.MO.MatterOverdrive.api.network;

import com.MO.MatterOverdrive.data.network.MatterNetworkTaskQueue;

/**
 * Created by Simeon on 4/20/2015.
 */
public interface IMatterNetworkDispatcher extends IMatterNetworkConnection
{
    MatterNetworkTaskQueue<? extends MatterNetworkTask> getQueue();
    void onTaskChange(MatterNetworkTask task);
}
