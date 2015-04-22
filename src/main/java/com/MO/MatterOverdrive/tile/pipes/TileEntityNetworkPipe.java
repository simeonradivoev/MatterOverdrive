package com.MO.MatterOverdrive.tile.pipes;

import cofh.lib.util.position.BlockPosition;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.network.*;
import com.MO.MatterOverdrive.data.network.MatterNetworkTaskPacket;
import com.MO.MatterOverdrive.util.MatterNetworkHelper;
import com.MO.MatterOverdrive.util.math.MOMathHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 3/15/2015.
 */
public class TileEntityNetworkPipe extends TileEntityPipe implements IMatterNetworkCable, IMatterNetworkConnectionProxy {

    public TileEntityNetworkPipe() {

    }

    @Override
    public boolean canConnectTo(TileEntity entity, ForgeDirection direction)
    {
        if (entity instanceof IMatterNetworkConnectionProxy)
        {
            if (entity instanceof TileEntityNetworkPipe)
            {
                TileEntityNetworkPipe networkPipe = (TileEntityNetworkPipe)entity;
                int pipeConnections = networkPipe.getConnections();
                if (MOMathHelper.getBoolean(pipeConnections,direction.ordinal())) {
                    return true;
                }
                else
                {
                    int pipeConnectionsCount = 0;
                    for (int i = 0; i < 6; i++) {
                        pipeConnectionsCount += ((pipeConnections >> i) & 1);
                    }
                    return pipeConnectionsCount < 2;
                }
            }
            else
            {
                return ((IMatterNetworkConnectionProxy) entity).getMatterNetworkConnection().canConnectFromSide(direction.getOpposite());
            }
        }
        return false;
    }

    @Override
    public void onAdded() {
        super.onAdded();

    }

    @Override
    public void onDestroyed() {
        super.onDestroyed();
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void broadcast(MatterNetworkTaskPacket task) {
        if (isValid() && task.getTask(worldObj).getState() <= Reference.TASK_STATE_WAITING) {
            for (int i = 0; i < 6; i++) {
                MatterNetworkHelper.broadcastTaskInDirection(worldObj, task.addToPath(getMatterNetworkConnection()), this, ForgeDirection.getOrientation(i));
            }
        }
    }

    @Override
    public BlockPosition getPosition() {
        return new BlockPosition(this);
    }

    @Override
    public boolean canConnectFromSide(ForgeDirection side)
    {
        return MOMathHelper.getBoolean(getConnections(),side.ordinal());
    }

    public void updateSides()
    {
        int connections = 0;
        int connectionCount = 0;

        for (int i = 0; i < 6; i++) {
            TileEntity t = this.worldObj.getTileEntity(ForgeDirection.values()[i].offsetX + this.xCoord, ForgeDirection.values()[i].offsetY + this.yCoord, ForgeDirection.values()[i].offsetZ + this.zCoord);

            if (connectionCount < 2 && canConnectTo(t, ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[i])))
            {
                connections |= ForgeDirection.values()[i].flag;
                connectionCount++;
            }
        }

        this.setConnections(connections, 2);
    }

    @Override
    public IMatterNetworkConnection getMatterNetworkConnection() {
        return this;
    }
}
