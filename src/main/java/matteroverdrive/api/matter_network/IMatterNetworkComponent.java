package matteroverdrive.api.matter_network;

import matteroverdrive.data.matter_network.IMatterNetworkEvent;

/**
 * Created by Simeon on 1/29/2016.
 */
public interface IMatterNetworkComponent
{
    void onNetworkEvent(IMatterNetworkEvent event);
}
