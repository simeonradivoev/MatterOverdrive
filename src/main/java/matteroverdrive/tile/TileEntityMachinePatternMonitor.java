package matteroverdrive.tile;

import cofh.lib.util.TimeTracker;
import cofh.lib.util.helpers.BlockHelper;
import cofh.lib.util.position.BlockPosition;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.matter.IMatterDatabase;
import matteroverdrive.api.network.IMatterNetworkClient;
import matteroverdrive.api.network.IMatterNetworkConnection;
import matteroverdrive.api.network.IMatterNetworkConnectionProxy;
import matteroverdrive.api.network.IMatterNetworkDispatcher;
import matteroverdrive.matter_network.MatterNetworkPacket;
import matteroverdrive.matter_network.MatterNetworkTaskQueue;
import matteroverdrive.matter_network.packets.MatterNetworkRequestPacket;
import matteroverdrive.matter_network.packets.MatterNetwrokResponcePacket;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskReplicatePattern;
import matteroverdrive.network.packet.client.PacketPatternMonitorSync;
import matteroverdrive.util.MatterNetworkHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashSet;

/**
 * Created by Simeon on 4/26/2015.
 */
public class TileEntityMachinePatternMonitor extends MOTileEntityMachineEnergy implements IMatterNetworkConnectionProxy, IMatterNetworkDispatcher, IMatterNetworkClient
{
    public static final int BROADCAST_WEATING_DELAY = 80;
    public static final int SEARCH_DELAY = 120;
    public static final int VALIDATE_DELAY = 120;
    public static final int TASK_QUEUE_SIZE = 16;
    HashSet<BlockPosition> databases;
    MatterNetworkTaskQueue<MatterNetworkTaskReplicatePattern> taskQueue;
    boolean needsSearchRefresh = true;
    TimeTracker searchDelayTracker;
    TimeTracker broadcastTracker;
    TimeTracker validateTracker;

    public TileEntityMachinePatternMonitor()
    {
        super(4);
        taskQueue = new MatterNetworkTaskQueue<MatterNetworkTaskReplicatePattern>(this,TASK_QUEUE_SIZE,MatterNetworkTaskReplicatePattern.class);
        databases = new HashSet<BlockPosition>();
        searchDelayTracker = new TimeTracker();
        broadcastTracker = new TimeTracker();
        validateTracker = new TimeTracker();
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
        return false;
    }

    @Override
    public float soundVolume() {
        return 0;
    }

    public boolean isAffectedByUpgrade(UpgradeTypes type)
    {
        return false;
    }

    //region Matter Network Functions
    @Override
    public IMatterNetworkConnection getMatterNetworkConnection()
    {
        return this;
    }

    @Override
    public int onNetworkTick(World world, TickEvent.Phase phase)
    {
        manageDatabaseValidation(world);
        manageSearch(world, phase);
        return manageBroadcast(world,phase);
    }

    public void manageSearch(World world,TickEvent.Phase phase)
    {
        if (phase.equals(TickEvent.Phase.END)) {
            if (needsSearchRefresh) {
                databases.clear();
                MatterOverdrive.packetPipeline.sendToAllAround(new PacketPatternMonitorSync(this), this, 64);

                for (int i = 0; i < 6; i++) {
                    MatterNetworkRequestPacket packet = new MatterNetworkRequestPacket(this, Reference.PACKET_REQUEST_CONNECTION,ForgeDirection.getOrientation(i), IMatterDatabase.class);
                    MatterNetworkHelper.broadcastTaskInDirection(world, packet, this, ForgeDirection.getOrientation(i));
                }
                needsSearchRefresh = false;
            }
        }
    }

    public void manageDatabaseValidation(World world)
    {
        if (validateTracker.hasDelayPassed(worldObj, VALIDATE_DELAY))
        {
            boolean hasChanged = false;

            for (BlockPosition blockPosition : databases)
            {
                if (blockPosition.getBlock(world) == null || blockPosition.getTileEntity(world) == null || !(blockPosition.getTileEntity(world) instanceof IMatterDatabase))
                {
                    forceSearch(true);
                    return;
                }
            }
        }
    }

    public int manageBroadcast(World world,TickEvent.Phase phase)
    {
        if (phase.equals(TickEvent.Phase.START)) {
            int broadcastCount = 0;
            MatterNetworkTaskReplicatePattern task = taskQueue.peek();

            if (task != null) {
                if (task.getState() == Reference.TASK_STATE_FINISHED || task.getState() == Reference.TASK_STATE_PROCESSING || task.getState() == Reference.TASK_STATE_QUEUED) {
                    taskQueue.dequeueTask();
                    ForceSync();
                } else {
                    if (!task.isAlive() && broadcastTracker.hasDelayPassed(world, BROADCAST_WEATING_DELAY)) {
                        for (int i = 0; i < 6; i++) {
                            if (MatterNetworkHelper.broadcastTaskInDirection(worldObj, (byte) 0, task, this, ForgeDirection.getOrientation(i))) {
                                task.setState(Reference.TASK_STATE_WAITING);
                                broadcastCount++;
                            }

                        }
                    }
                }
            }

            taskQueue.tickAllAlive(world, false);
            return broadcastCount;
        }
        return 0;
    }

    @Override
    public MatterNetworkTaskQueue<MatterNetworkTaskReplicatePattern> getQueue(byte id)
    {
        return taskQueue;
    }

    public void SyncDatabasesWithClient()
    {
        MatterOverdrive.packetPipeline.sendToAllAround(new PacketPatternMonitorSync(this), this, 64);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt)
    {
        super.writeCustomNBT(nbt);
        taskQueue.writeToNBT(nbt);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt)
    {
        super.readCustomNBT(nbt);
        taskQueue.readFromNBT(nbt);
    }

    @Override
    protected void onAwake(Side side) {

    }

    @Override
    public BlockPosition getPosition() {
        return new BlockPosition(xCoord,yCoord,zCoord);
    }

    @Override
    public boolean canConnectFromSide(ForgeDirection side)
    {
        int meta = worldObj.getBlockMetadata(xCoord,yCoord,zCoord);
        return BlockHelper.getOppositeSide(meta) == side.ordinal();
    }

    //endregion

    public HashSet<BlockPosition> getDatabases()
    {
        return databases;
    }

    public void setDatabases(HashSet<BlockPosition> blockPositions)
    {
        databases = blockPositions;
    }

    public void forceSearch(boolean refresh)
    {
        needsSearchRefresh = refresh;
    }

    public void queueSearch()
    {
        if (searchDelayTracker.hasDelayPassed(worldObj,SEARCH_DELAY))
        {
            forceSearch(true);
        }
    }

    public boolean needsRefresh()
    {
        return needsSearchRefresh;
    }

    @Override
    public boolean canPreform(MatterNetworkPacket task)
    {
        if (task instanceof MatterNetwrokResponcePacket)
        {
            return true;
        }
        else if (task instanceof MatterNetworkRequestPacket)
        {
            return ((MatterNetworkRequestPacket) task).getRequestType() == Reference.PACKET_REQUEST_CONNECTION
                    || ((MatterNetworkRequestPacket) task).getRequestType() == Reference.PACKET_REQUEST_NEIGHBOR_CONNECTION;
        }
        return false;
    }

    @Override
    public void queuePacket(MatterNetworkPacket packet, ForgeDirection from)
    {
        if (packet instanceof MatterNetwrokResponcePacket)
        {
            MatterNetwrokResponcePacket responcePacket = (MatterNetwrokResponcePacket)packet;
            if (responcePacket.getResponceType() == Reference.PACKET_RESPONCE_VALID && responcePacket.getRequestType() == Reference.PACKET_REQUEST_CONNECTION)
            {
                if(!databases.contains(responcePacket.getSender(worldObj).getPosition()))
                {
                    databases.add(responcePacket.getSender(worldObj).getPosition());
                    SyncDatabasesWithClient();
                }
            }
        }else if (packet instanceof MatterNetworkRequestPacket)
        {
            manageRequestPackets((MatterNetworkRequestPacket)packet,from);
        }
    }

    public void manageRequestPackets(MatterNetworkRequestPacket packet,ForgeDirection direction)
    {
        MatterNetworkHelper.handleConnectionRequestPacket(worldObj,this,packet,direction);
    }

    public void queueRequest(int[] request)
    {
        for (int i = 0;i < request.length;i+=3)
        {
            MatterNetworkTaskReplicatePattern task = new MatterNetworkTaskReplicatePattern(this, request[i], request[i + 1], request[i + 2]);
            task.setState(Reference.TASK_STATE_WAITING);
            if (taskQueue.queueTask(task));
        }

        ForceSync();
    }

    @Override
    public boolean shouldRenderInPass(int pass)
    {
        return pass == 1;
    }

    @Override
    public void onAdded(World world, int x, int y, int z) {

    }

    @Override
    public void onPlaced(World world, EntityLivingBase entityLiving) {

    }

    @Override
    public void onDestroyed() {

    }
}
