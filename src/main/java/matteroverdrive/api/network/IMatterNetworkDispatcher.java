package matteroverdrive.api.network;

import matteroverdrive.matter_network.MatterNetworkTaskQueue;

/**
 * Created by Simeon on 4/20/2015.
 */
public interface IMatterNetworkDispatcher extends IMatterNetworkConnection , IMatterNetworkHandler
{
    MatterNetworkTaskQueue<? extends MatterNetworkTask> getQueue(byte queueID);
}
