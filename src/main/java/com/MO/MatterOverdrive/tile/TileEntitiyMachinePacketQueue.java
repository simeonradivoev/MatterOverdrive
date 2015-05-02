package com.MO.MatterOverdrive.tile;

import cofh.lib.util.TimeTracker;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.position.BlockPosition;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.api.inventory.UpgradeTypes;
import com.MO.MatterOverdrive.api.network.IMatterNetworkClient;
import com.MO.MatterOverdrive.api.network.IMatterNetworkConnection;
import com.MO.MatterOverdrive.api.network.IMatterNetworkConnectionProxy;
import com.MO.MatterOverdrive.api.network.MatterNetworkTask;
import com.MO.MatterOverdrive.util.MatterNetworkHelper;
import com.MO.MatterOverdrive.util.math.MOMathHelper;
import cpw.mods.fml.common.gameevent.TickEvent;
import com.MO.MatterOverdrive.matter_network.MatterNetworkPacket;
import com.MO.MatterOverdrive.matter_network.MatterNetworkPacketQueue;
import com.MO.MatterOverdrive.matter_network.packets.MatterNetworkBroadcastPacket;
import com.MO.MatterOverdrive.matter_network.packets.MatterNetworkRequestPacket;
import com.MO.MatterOverdrive.matter_network.packets.MatterNetworkTaskPacket;
import com.MO.MatterOverdrive.matter_network.packets.MatterNetwrokResponcePacket;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 4/30/2015.
 */
public class TileEntitiyMachinePacketQueue extends MOTileEntityMachine implements IMatterNetworkClient, IMatterNetworkConnectionProxy
{
    public static int[] directions = {0,1,2,3,4,5};
    protected int BROADCAST_DELAY = 2;
    protected int TASK_QUEUE_SIZE = 16;
    private TimeTracker broadcastTracker;
    private MatterNetworkPacketQueue packetQueue;

    private BlockPosition[] connections;

    public TileEntitiyMachinePacketQueue(int upgradeCount)
    {
        super(upgradeCount);
        packetQueue = new MatterNetworkPacketQueue(this,TASK_QUEUE_SIZE);
        broadcastTracker = new TimeTracker();
        connections = new BlockPosition[6];
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
        for (int i = 0;i < connections.length;i++)
        {
            if (nbt.hasKey("Connection" + i)) {
                BlockPosition position = new BlockPosition(nbt.getCompoundTag("Connection" + i));
                TileEntity tileEntity = position.getTileEntity(worldObj);
                if (tileEntity != null && tileEntity instanceof IMatterNetworkConnectionProxy)
                {
                    connections[i] = ((IMatterNetworkConnectionProxy) tileEntity).getMatterNetworkConnection().getPosition();
                }
            }
        }
    }

    @Override
    public void  writeCustomNBT(NBTTagCompound nbt)
    {
        super.writeCustomNBT(nbt);
        packetQueue.writeToNBT(worldObj, nbt);
        for (int i = 0;i < connections.length;i++)
        {
            if (getConnection(i) != null)
            {
                NBTTagCompound tagCompound = new NBTTagCompound();
                connections[i].writeToNBT(tagCompound);
                nbt.setTag("Connection" + i,tagCompound);
            }
        }
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

                        broadcastCount += handlePacketBroadcast(world,packet);
                    }

                    ForceSync();
                }
            }
        }
        return broadcastCount;
    }

    protected int handlePacketBroadcast(World world,MatterNetworkPacket packet)
    {
        boolean foundReceiver = false;
        int broadcastCount = 0;

        if (packet.isGuided()) {
            //check if it already has an established connection to reciver
            for (int i = 0; i < connections.length; i++) {
                if (connections[i].equals(packet.getReceiverPos()))
                {
                    if (MatterNetworkHelper.broadcastTaskInDirection(worldObj, packet, this, ForgeDirection.getOrientation(directions[i])))
                    {
                        foundReceiver = true;
                        broadcastCount++;
                    }
                }
            }
        }

        if (!foundReceiver) {
            //if there is no connection to receiver send to all around
            for (int i = 0; i < directions.length; i++) {
                if (packet instanceof MatterNetworkTaskPacket && !isInValidState(((MatterNetworkTaskPacket) packet).getTask(world)))
                    continue;

                if (MatterNetworkHelper.broadcastTaskInDirection(worldObj, packet, this, ForgeDirection.getOrientation(directions[i]))) {
                    broadcastCount++;
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
    public boolean canPreform(MatterNetworkPacket task) {
        return true;
    }

    @Override
    public void queuePacket(MatterNetworkPacket packet,ForgeDirection from)
    {
        if (packet.isValid(worldObj))
        {
            if (packet instanceof MatterNetworkTaskPacket && !isInValidState(((MatterNetworkTaskPacket) packet).getTask(worldObj))) {
                return;
            }
            if (packet instanceof MatterNetworkBroadcastPacket)
            {
                if (manageBroadcastPacket((MatterNetworkBroadcastPacket)packet,from))
                    return;
            }
            else if (packet instanceof MatterNetwrokResponcePacket)
            {
                if (manageResponcePackets((MatterNetwrokResponcePacket)packet,from))
                    return;
            }else if (packet instanceof MatterNetworkRequestPacket)
            {
                if (manageRequestPackets((MatterNetworkRequestPacket)packet,from))
                    return;
            }

            if (packetQueue.queuePacket(packet)) {
                packet.addToPath(this, from);
                broadcastTracker.markTime(worldObj);
                ForceSync();
            }
        }
    }

    boolean manageRequestPackets(MatterNetworkRequestPacket packet,ForgeDirection direction)
    {
        return MatterNetworkHelper.handleConnectionRequestPacket(worldObj,this,packet,direction);
    }

    boolean manageResponcePackets(MatterNetwrokResponcePacket packet,ForgeDirection direction)
    {
        if (packet.getResponceType() == Reference.PACKET_RESPONCE_VALID && packet.getRequestType() == Reference.PACKET_REQUEST_NEIGHBOR_CONNECTION)
        {
            connections[direction.ordinal()] = packet.getSender(worldObj).getPosition();
            ForceSync();
            return true;
        }
        return false;
    }

    boolean manageBroadcastPacket(MatterNetworkBroadcastPacket packet,ForgeDirection direction)
    {
        if ((packet.getBroadcastType() == Reference.PACKET_BROADCAST_CONNECTION))
        {
            connections[direction.ordinal()] = packet.getSender(worldObj).getPosition();
            ForceSync();
            return true;
        }
        return false;
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

    public BlockPosition getConnection(int id)
    {
        if (connections[id] != null)
        {
            if (connections[id].getTileEntity(worldObj) != null && connections[id].getTileEntity(worldObj) instanceof IMatterNetworkConnectionProxy)
                return connections[id];
            else {
                connections[id] = null;
                return null;
            }
        }
        return connections[id];
    }
}
