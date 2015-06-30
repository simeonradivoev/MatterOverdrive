package matteroverdrive.tile;

import matteroverdrive.util.MatterNetworkHelper;
import cpw.mods.fml.relauncher.Side;

/**
 * Created by Simeon on 4/30/2015.
 */
public class TileEntityMachineNetworkSwitch extends TileEntitiyMachinePacketQueue
{

    public TileEntityMachineNetworkSwitch() {
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
