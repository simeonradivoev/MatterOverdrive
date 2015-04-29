package com.MO.MatterOverdrive.tile;

import cofh.lib.util.TimeTracker;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.position.BlockPosition;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.inventory.UpgradeTypes;
import com.MO.MatterOverdrive.api.network.IMatterNetworkConnection;
import com.MO.MatterOverdrive.api.network.IMatterNetworkConnectionProxy;
import com.MO.MatterOverdrive.api.network.IMatterNetworkRouter;
import com.MO.MatterOverdrive.api.network.MatterNetworkTask;
import matter_network.MatterNetworkPacket;
import matter_network.packets.MatterNetworkTaskPacket;
import matter_network.MatterNetworkPacketQueue;
import com.MO.MatterOverdrive.util.MatterNetworkHelper;
import com.MO.MatterOverdrive.util.math.MOMathHelper;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 3/11/2015.
 */
public class TileEntityMachineNetworkRouter extends MOTileEntityMachine implements IMatterNetworkRouter, IMatterNetworkConnectionProxy
{
    public static int[] directions = {0,1,2,3,4,5};
    public static final int BROADCAST_DELAY = 2;
    public static final int TASK_QUEUE_SIZE = 16;
    private TimeTracker broadcastTracker;
    private MatterNetworkPacketQueue packetQueue;

    public TileEntityMachineNetworkRouter()
    {
        super(4);
        packetQueue = new MatterNetworkPacketQueue(this,TASK_QUEUE_SIZE);
        broadcastTracker = new TimeTracker();
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt)
    {
        super.readCustomNBT(nbt);
        packetQueue.readFromNBT(worldObj, nbt);
    }

    @Override
    public void  writeCustomNBT(NBTTagCompound nbt)
    {
        super.writeCustomNBT(nbt);
        packetQueue.writeToNBT(worldObj, nbt);
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
        return packetQueue.size() > 0;
    }

    @Override
    public float soundVolume() { return 0;}

    @Override
    public void onContainerOpen() {

    }

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
    public int onNetworkTick(World world,TickEvent.Phase phase)
    {
        int broadcastCount = 0;
        if (phase.equals(TickEvent.Phase.END))
        {
            packetQueue.tickAllAlive(world, true);

            if (broadcastTracker.hasDelayPassed(worldObj, getBroadcastDelay()))
            {
                MatterNetworkPacket packet = packetQueue.dequeuePacket();

                if (packet != null)
                {
                    if (packet.isValid(worldObj)) {
                        MOMathHelper.shuffleArray(random, directions);

                        for (int i = 0; i < directions.length; i++) {
                            if (packet instanceof MatterNetworkTaskPacket && !isInValidState(((MatterNetworkTaskPacket) packet).getTask(world)))
                                continue;

                            if (MatterNetworkHelper.broadcastTaskInDirection(worldObj, packet, this, ForgeDirection.getOrientation(directions[i]))) {
                                broadcastCount++;
                            }
                        }
                    }

                    ForceSync();
                }
            }
        }
        return broadcastCount;
    }

    private boolean isInValidState(MatterNetworkTask task)
    {
        if (task != null) {
            return task.getState() == Reference.TASK_STATE_WAITING;
        }
        return false;
    }

    private int getBroadcastDelay()
    {
        return MathHelper.round(BROADCAST_DELAY * getUpgradeMultiply(UpgradeTypes.Speed));
    }

    @Override
    public void queuePacket(MatterNetworkPacket packet,ForgeDirection from)
    {
        if (packet.isValid(worldObj)) {
            if (packet instanceof MatterNetworkTaskPacket && !isInValidState(((MatterNetworkTaskPacket) packet).getTask(worldObj))) {
                return;
            }
            if (packetQueue.queuePacket(packet)) {
                packet.addToPath(this, from);
                broadcastTracker.markTime(worldObj);
                ForceSync();
            }
        }
    }

    @Override
    public BlockPosition getPosition()
    {
        return new BlockPosition(xCoord,yCoord,zCoord);
    }

    @Override
    public boolean canConnectFromSide(ForgeDirection side)
    {
        return true;
    }

    @Override
    public boolean isAffectedBy(UpgradeTypes type)
    {
        return type.equals(UpgradeTypes.Speed);
    }
}
