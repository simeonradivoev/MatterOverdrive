package matteroverdrive.tile;

import cpw.mods.fml.relauncher.Side;
import matteroverdrive.util.MatterNetworkHelper;

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

    @Override
    protected void onActiveChange() {

    }
}
