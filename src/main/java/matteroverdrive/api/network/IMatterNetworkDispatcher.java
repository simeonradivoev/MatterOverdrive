package matteroverdrive.api.network;

import matteroverdrive.matter_network.MatterNetworkTaskQueue;

/**
 * Created by Simeon on 4/20/2015.
 */
public interface IMatterNetworkDispatcher<T extends MatterNetworkTask> extends IMatterNetworkConnection , IMatterNetworkHandler
{
    MatterNetworkTaskQueue<T> getTaskQueue(int queueID);
}
