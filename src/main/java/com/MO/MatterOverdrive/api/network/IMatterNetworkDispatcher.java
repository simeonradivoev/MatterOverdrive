package com.MO.MatterOverdrive.api.network;

import matter_network.MatterNetworkTaskQueue;

/**
 * Created by Simeon on 4/20/2015.
 */
public interface IMatterNetworkDispatcher extends IMatterNetworkConnection
{
    MatterNetworkTaskQueue<? extends MatterNetworkTask> getQueue(byte queueID);
    void onResponce(IMatterNetworkConnection from,int requestType,int responceType,Object responce);
}
