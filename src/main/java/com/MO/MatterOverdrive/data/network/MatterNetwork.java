package com.MO.MatterOverdrive.data.network;

import com.MO.MatterOverdrive.api.network.IMatterNetworkConnection;
import com.MO.MatterOverdrive.tile.TileEntityMachineNetworkRouter;

import java.util.*;

/**
 * Created by Simeon on 3/11/2015.
 */
public class MatterNetwork
{
    TileEntityMachineNetworkRouter controller;
    List<IMatterNetworkConnection> connections;

    public MatterNetwork(TileEntityMachineNetworkRouter controller)
    {
        connections = new ArrayList<IMatterNetworkConnection>();
        this.controller = controller;
    }


}
