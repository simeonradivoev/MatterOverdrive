/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.matter_network.components;

import cpw.mods.fml.common.gameevent.TickEvent;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.matter.IMatterDatabase;
import matteroverdrive.api.network.MatterNetworkTaskState;
import matteroverdrive.data.BlockPos;
import matteroverdrive.data.ItemPattern;
import matteroverdrive.matter_network.MatterNetworkPacket;
import matteroverdrive.matter_network.packets.MatterNetworkRequestPacket;
import matteroverdrive.matter_network.packets.MatterNetworkResponsePacket;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskReplicatePattern;
import matteroverdrive.network.packet.client.PacketPatternMonitorSync;
import matteroverdrive.tile.TileEntityMachinePatternMonitor;
import matteroverdrive.util.MatterNetworkHelper;
import matteroverdrive.util.TimeTracker;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Created by Simeon on 7/13/2015.
 */
public class MatterNetworkComponentPatternMonitor extends MatterNetworkComponentClient<TileEntityMachinePatternMonitor>
{
    private TimeTracker broadcastTracker;
    TimeTracker validateTracker;
    private boolean needsSearchRefresh = true;

    public MatterNetworkComponentPatternMonitor(TileEntityMachinePatternMonitor patternMonitor)
    {
        super(patternMonitor);
        broadcastTracker = new TimeTracker();
        validateTracker = new TimeTracker();
        handlers.add(BASIC_CONNECTIONS_HANDLER);
    }

    @Override
    protected void executePacket(MatterNetworkPacket packet)
    {
        super.executePacket(packet);

        if (packet instanceof MatterNetworkResponsePacket)
        {
            executeResponses((MatterNetworkResponsePacket)packet);
        }
    }

    protected void executeResponses(MatterNetworkResponsePacket packet)
    {
        if (packet.fits(Reference.PACKET_RESPONCE_VALID,Reference.PACKET_REQUEST_CONNECTION))
        {
            if(!rootClient.getDatabases().contains(packet.getSender(getWorldObj()).getPosition()))
            {
                rootClient.getDatabases().add(packet.getSender(rootClient.getWorldObj()).getPosition());
                rootClient.SyncDatabasesWithClient();
            }
        }
    }

    @Override
    public int onNetworkTick(World world, TickEvent.Phase phase)
    {
        int broadcasts = super.onNetworkTick(world,phase);
        manageDatabaseValidation(world);
        manageSearch(world, phase);
        return broadcasts + manageTaskBroadcast(world, phase);
    }

    private void manageSearch(World world,TickEvent.Phase phase)
    {
        if (phase.equals(TickEvent.Phase.END)) {
            if (needsSearchRefresh)
            {
                rootClient.getDatabases().clear();
                MatterOverdrive.packetPipeline.sendToAllAround(new PacketPatternMonitorSync(rootClient), rootClient, 64);

                for (int i = 0; i < 6; i++) {
                    MatterNetworkRequestPacket packet = new MatterNetworkRequestPacket(rootClient, Reference.PACKET_REQUEST_CONNECTION,ForgeDirection.getOrientation(i),rootClient.getFilter(), IMatterDatabase.class);
                    MatterNetworkHelper.broadcastPacketInDirection(world, packet, rootClient, ForgeDirection.getOrientation(i));
                }
                needsSearchRefresh = false;
            }
        }
    }

    /**
     * Gets called to validate all connected Databases, if they exist.
     * @param world
     */
    private void manageDatabaseValidation(World world)
    {
        if (validateTracker.hasDelayPassed(world, TileEntityMachinePatternMonitor.VALIDATE_DELAY))
        {
            for (BlockPos blockPosition : rootClient.getDatabases())
            {
                if (blockPosition.getBlock(world) == null || blockPosition.getTileEntity(world) == null || !(blockPosition.getTileEntity(world) instanceof IMatterDatabase))
                {
                    needsSearchRefresh = true;
                    return;
                }
            }
        }
    }

    private int manageTaskBroadcast(World world, TickEvent.Phase phase)
    {
        if (phase.equals(TickEvent.Phase.START)) {
            int broadcastCount = 0;
            MatterNetworkTaskReplicatePattern task = rootClient.getTaskQueue(0).peek();

            if (task != null) {
                if (task.getState() == MatterNetworkTaskState.FINISHED || task.getState() == MatterNetworkTaskState.PROCESSING) {
                    rootClient.getTaskQueue(0).dequeue();
                    rootClient.forceSync();
                } else{
                    if (!task.isAlive() && broadcastTracker.hasDelayPassed(world, TileEntityMachinePatternMonitor.BROADCAST_WEATING_DELAY)) {
                        for (int i = 0; i < 6; i++) {
                            if (MatterNetworkHelper.broadcastPacketInDirection(world, (byte) 0, task, rootClient, ForgeDirection.getOrientation(i), rootClient.getFilter())) {
                                task.setState(MatterNetworkTaskState.WAITING);
                                broadcastCount++;
                            }

                        }
                    }
                }
            }

            rootClient.getTaskQueue(0).tickAllAlive(world, false);
            return broadcastCount;
        }
        return 0;
    }

    public void queuePatternRequest(List<ItemPattern> patternRequests)
    {
        for (int i = 0;i < patternRequests.size();i++)
        {
            MatterNetworkTaskReplicatePattern task = new MatterNetworkTaskReplicatePattern(rootClient, patternRequests.get(i));
            task.setState(MatterNetworkTaskState.WAITING);
            if (rootClient.getTaskQueue(0).queue(task));
        }

        rootClient.forceSync();
    }

    public boolean getNeedsSearchRefresh()
    {
        return needsSearchRefresh;
    }

    public void setNeedsSearchRefresh(boolean needsSearchRefresh)
    {
        this.needsSearchRefresh = needsSearchRefresh;
    }
}
