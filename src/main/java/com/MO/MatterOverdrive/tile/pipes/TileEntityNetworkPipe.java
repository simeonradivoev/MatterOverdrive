package com.MO.MatterOverdrive.tile.pipes;

import cofh.lib.util.position.BlockPosition;
import com.MO.MatterOverdrive.api.matter.IMatterNetworkConnection;
import com.MO.MatterOverdrive.data.MatterNetwork;
import com.MO.MatterOverdrive.util.MatterNetworkHelper;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 3/15/2015.
 */
public class TileEntityNetworkPipe extends TileEntityPipe<IMatterNetworkConnection> implements IMatterNetworkConnection
{
    MatterNetwork network;

    public TileEntityNetworkPipe() {
        super(IMatterNetworkConnection.class);
        network = null;
    }

    @Override
    public void onAdded()
    {
        super.onAdded();
        MatterNetworkHelper.tryConnectToNetwork(this.worldObj, this, true);
    }

    @Override
    public void onDestroyed()
    {
        super.onDestroyed();
        MatterNetworkHelper.disconnectFromNetwork(worldObj,this,true);
    }

    @Override
    public boolean canConnectToNetwork(ForgeDirection direction)
    {
        return true;
    }

    @Override
    public BlockPosition getPosition() {
        return new BlockPosition(xCoord,yCoord,zCoord);
    }

    @Override
    public MatterNetwork getNetwork() {
        return network;
    }

    @Override
    public boolean setNetwork(MatterNetwork network) {
        this.network = network;
        return true;
    }

    @Override
    public int getID()
    {
        return Block.getIdFromBlock(getBlockType());
    }
}
