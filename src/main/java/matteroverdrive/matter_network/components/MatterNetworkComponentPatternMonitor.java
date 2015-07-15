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

import cofh.lib.util.TimeTracker;
import cofh.lib.util.position.BlockPosition;
import cpw.mods.fml.common.gameevent.TickEvent;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.matter.IMatterDatabase;
import matteroverdrive.matter_network.MatterNetworkPacket;
import matteroverdrive.matter_network.packets.MatterNetworkRequestPacket;
import matteroverdrive.matter_network.packets.MatterNetwrokResponcePacket;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskReplicatePattern;
import matteroverdrive.network.packet.client.PacketPatternMonitorSync;
import matteroverdrive.tile.TileEntityMachinePatternMonitor;
import matteroverdrive.util.MatterNetworkHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 7/13/2015.
 */
public class MatterNetworkComponentPatternMonitor extends MatterNetworkComponentClient
{
    private TileEntityMachinePatternMonitor patternMonitor;
    private TimeTracker broadcastTracker;
    TimeTracker validateTracker;
    private boolean needsSearchRefresh = true;

    public MatterNetworkComponentPatternMonitor(TileEntityMachinePatternMonitor patternMonitor)
    {
        this.patternMonitor = patternMonitor;
        broadcastTracker = new TimeTracker();
        validateTracker = new TimeTracker();
    }

    @Override
    public boolean canPreform(MatterNetworkPacket packet) {
        if (packet instanceof MatterNetwrokResponcePacket)
        {
            return true;
        }
        else if (packet instanceof MatterNetworkRequestPacket)
        {
            return ((MatterNetworkRequestPacket) packet).getRequestType() == Reference.PACKET_REQUEST_CONNECTION
                    || ((MatterNetworkRequestPacket) packet).getRequestType() == Reference.PACKET_REQUEST_NEIGHBOR_CONNECTION;
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
                if(!patternMonitor.getDatabases().contains(responcePacket.getSender(patternMonitor.getWorldObj()).getPosition()))
                {
                    patternMonitor.getDatabases().add(responcePacket.getSender(patternMonitor.getWorldObj()).getPosition());
                    patternMonitor.SyncDatabasesWithClient();
                }
            }
        }else if (packet instanceof MatterNetworkRequestPacket)
        {

            manageRequestPackets(patternMonitor,patternMonitor.getWorldObj(),(MatterNetworkRequestPacket)packet,from);
        }
    }

    @Override
    public BlockPosition getPosition() {
        return patternMonitor.getPosition();
    }

    @Override
    public boolean canConnectFromSide(ForgeDirection side) {
        return patternMonitor.canConnectFromSide(side);
    }

    @Override
    public int onNetworkTick(World world, TickEvent.Phase phase) {
        manageDatabaseValidation(world);
        manageSearch(world, phase);
        return manageBroadcast(world,phase);
    }

    public void manageSearch(World world,TickEvent.Phase phase)
    {
        if (phase.equals(TickEvent.Phase.END)) {
            if (patternMonitor.needsRefresh()) {
                patternMonitor.getDatabases().clear();
                MatterOverdrive.packetPipeline.sendToAllAround(new PacketPatternMonitorSync(patternMonitor), patternMonitor, 64);

                for (int i = 0; i < 6; i++) {
                    MatterNetworkRequestPacket packet = new MatterNetworkRequestPacket(patternMonitor, Reference.PACKET_REQUEST_CONNECTION,ForgeDirection.getOrientation(i), IMatterDatabase.class);
                    MatterNetworkHelper.broadcastTaskInDirection(world, packet, patternMonitor, ForgeDirection.getOrientation(i));
                }
                needsSearchRefresh = false;
            }
        }
    }

    public void manageDatabaseValidation(World world)
    {
        if (validateTracker.hasDelayPassed(world, patternMonitor.VALIDATE_DELAY))
        {
            for (BlockPosition blockPosition : patternMonitor.getDatabases())
            {
                if (blockPosition.getBlock(world) == null || blockPosition.getTileEntity(world) == null || !(blockPosition.getTileEntity(world) instanceof IMatterDatabase))
                {
                    needsSearchRefresh = true;
                    return;
                }
            }
        }
    }

    public int manageBroadcast(World world,TickEvent.Phase phase)
    {
        if (phase.equals(TickEvent.Phase.START)) {
            int broadcastCount = 0;
            MatterNetworkTaskReplicatePattern task = patternMonitor.getQueue(0).peek();

            if (task != null) {
                if (task.getState() == Reference.TASK_STATE_FINISHED || task.getState() == Reference.TASK_STATE_PROCESSING || task.getState() == Reference.TASK_STATE_QUEUED) {
                    patternMonitor.getQueue(0).dequeue();
                    patternMonitor.ForceSync();
                } else {
                    if (!task.isAlive() && broadcastTracker.hasDelayPassed(world, patternMonitor.BROADCAST_WEATING_DELAY)) {
                        for (int i = 0; i < 6; i++) {
                            if (MatterNetworkHelper.broadcastTaskInDirection(world, (byte) 0, task, patternMonitor, ForgeDirection.getOrientation(i))) {
                                task.setState(Reference.TASK_STATE_WAITING);
                                broadcastCount++;
                            }

                        }
                    }
                }
            }

            patternMonitor.getQueue(0).tickAllAlive(world, false);
            return broadcastCount;
        }
        return 0;
    }

    public void queueRequest(int[] request)
    {
        for (int i = 0;i < request.length;i+=3)
        {
            MatterNetworkTaskReplicatePattern task = new MatterNetworkTaskReplicatePattern(patternMonitor, request[i], request[i + 1], request[i + 2]);
            task.setState(Reference.TASK_STATE_WAITING);
            if (patternMonitor.getQueue(0).queue(task));
        }

        patternMonitor.ForceSync();
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
