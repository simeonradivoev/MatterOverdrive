package com.MO.MatterOverdrive.tile;

import com.MO.MatterOverdrive.util.MatterNetworkHelper;
import cpw.mods.fml.relauncher.Side;

/**
 * Created by Simeon on 3/11/2015.
 */
public class TileEntityMachineNetworkRouter extends TileEntitiyMachinePacketQueue
{

    public TileEntityMachineNetworkRouter() {
        super(4);
    }

    @Override
    protected void onAwake(Side side)
    {
        if (side.isServer()) {
            MatterNetworkHelper.broadcastConnection(worldObj, this);
            MatterNetworkHelper.requestNeighborConnections(worldObj,this);
        }
    }
}
