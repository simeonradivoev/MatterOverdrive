package com.MO.MatterOverdrive.tile;

import cofh.lib.util.TimeTracker;
import cofh.lib.util.position.BlockPosition;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.network.IMatterNetworkConnection;
import com.MO.MatterOverdrive.api.network.IMatterNetworkConnectionProxy;
import com.MO.MatterOverdrive.api.network.IMatterNetworkRouter;
import com.MO.MatterOverdrive.data.network.MatterNetworkTaskPacket;
import com.MO.MatterOverdrive.data.network.MatterNetworkTaskPacketQueue;
import com.MO.MatterOverdrive.util.MatterNetworkHelper;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 3/11/2015.
 */
public class TileEntityMachineNetworkRouter extends MOTileEntityMachine implements IMatterNetworkRouter, IMatterNetworkConnectionProxy
{
    public static final int BROADCAST_DELAY = 30;
    private TimeTracker broadcastTracker;
    private MatterNetworkTaskPacketQueue taskPacketQueue;

    public TileEntityMachineNetworkRouter()
    {
        super(4);
        taskPacketQueue = new MatterNetworkTaskPacketQueue(this,3);
        broadcastTracker = new TimeTracker();
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (!worldObj.isRemote) {
            manageBroadcast();
        }
    }

    private void manageBroadcast()
    {
        if (broadcastTracker.hasDelayPassed(worldObj, BROADCAST_DELAY))
        {
            MatterNetworkTaskPacket taskPacket = taskPacketQueue.dequeuePacket();

            if (taskPacket != null && taskPacket.isValid(worldObj))
            {
                for (int i = 0; i < 6; i++)
                {
                    MatterNetworkHelper.broadcastTaskInDirection(worldObj, taskPacket, this, ForgeDirection.getOrientation(i));
                }

                ForceSync();
            }
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt)
    {
        super.readCustomNBT(nbt);
        taskPacketQueue.readFromNBT(worldObj,nbt);
    }

    @Override
    public void  writeCustomNBT(NBTTagCompound nbt)
    {
        super.writeCustomNBT(nbt);
        taskPacketQueue.writeToNBT(worldObj,nbt);
    }

    @Override
    public String getSound() {
        return null;
    }

    @Override
    public boolean hasSound() {
        return false;
    }

    @Override
    public boolean isActive()
    {
        return taskPacketQueue.size() > 0;
    }

    @Override
    public float soundVolume() { return 0;}

    @Override
    public void onAdded()
    {

    }

    @Override
    public IMatterNetworkConnection getMatterNetworkConnection()
    {
        return this;
    }

    @Override
    public void queuePacket(MatterNetworkTaskPacket task)
    {
        if (task.isValid(worldObj) && task.getTask(worldObj).getState() <= Reference.TASK_STATE_WAITING)
        {
            if (taskPacketQueue.queuePacket(task))
            {
                broadcastTracker.markTime(worldObj);
                ForceSync();
            }
        }
    }

    @Override
    public BlockPosition getPosition()
    {
        return new BlockPosition(this);
    }

    @Override
    public boolean canConnectFromSide(ForgeDirection side)
    {
        return true;
    }
}
