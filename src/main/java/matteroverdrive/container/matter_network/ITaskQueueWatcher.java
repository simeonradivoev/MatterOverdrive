package matteroverdrive.container.matter_network;

import matteroverdrive.api.container.IMachineWatcher;
import matteroverdrive.api.network.IMatterNetworkDispatcher;

/**
 * Created by Simeon on 2/6/2016.
 */
public interface ITaskQueueWatcher extends IMachineWatcher
{
    void onTaskAdded(IMatterNetworkDispatcher dispatcher,long taskId,int queueId);
    void onTaskRemoved(IMatterNetworkDispatcher dispatcher,long taskId,int queueId);
    void onTaskChanged(IMatterNetworkDispatcher dispatcher,long taskId,int queueId);
}
