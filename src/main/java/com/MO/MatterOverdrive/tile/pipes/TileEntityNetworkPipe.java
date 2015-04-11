package com.MO.MatterOverdrive.tile.pipes;

import cofh.lib.util.position.BlockPosition;
import com.MO.MatterOverdrive.api.matter.IMatterNetworkConnection;
import com.MO.MatterOverdrive.data.MatterNetwork;
import com.MO.MatterOverdrive.util.MatterNetworkHelper;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 3/15/2015.
 */
public class TileEntityNetworkPipe extends TileEntityPipe implements IMatterNetworkConnection
{
    MatterNetwork network;

    public TileEntityNetworkPipe() {
        network = null;
    }

    @Override
    public boolean canConnectTo(TileEntity entity, ForgeDirection direction)
    {
        if (entity instanceof IMatterNetworkConnection)
        {
            return ((IMatterNetworkConnection) entity).canConnectToNetwork(direction);
        }
        return false;
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
