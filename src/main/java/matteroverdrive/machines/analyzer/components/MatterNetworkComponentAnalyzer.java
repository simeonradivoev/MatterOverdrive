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

package matteroverdrive.machines.analyzer.components;

import cofh.lib.util.TimeTracker;
import cpw.mods.fml.common.gameevent.TickEvent;
import matteroverdrive.Reference;
import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.api.network.MatterNetworkTaskState;
import matteroverdrive.machines.analyzer.TileEntityMachineMatterAnalyzer;
import matteroverdrive.matter_network.MatterNetworkPacket;
import matteroverdrive.matter_network.components.MatterNetworkComponentClientDispatcher;
import matteroverdrive.matter_network.packets.MatterNetworkRequestPacket;
import matteroverdrive.matter_network.packets.MatterNetworkResponsePacket;
import matteroverdrive.matter_network.packets.MatterNetworkTaskPacket;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskStorePattern;
import matteroverdrive.util.MatterNetworkHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 7/13/2015.
 */
public class MatterNetworkComponentAnalyzer extends MatterNetworkComponentClientDispatcher<MatterNetworkTaskStorePattern,TileEntityMachineMatterAnalyzer>
{
    private TimeTracker broadcastTracker,validDestinationTracker;

    public MatterNetworkComponentAnalyzer(TileEntityMachineMatterAnalyzer analyzer)
    {
        super(analyzer,TickEvent.Phase.START);
        broadcastTracker = new TimeTracker();
        validDestinationTracker = new TimeTracker();
    }

    @Override
    public boolean canPreform(MatterNetworkPacket packet)
    {
        if (super.canPreform(packet)) {
            if (packet instanceof MatterNetworkResponsePacket) {
                return true;
            } else if (packet instanceof MatterNetworkRequestPacket) {
                if (((MatterNetworkRequestPacket) packet).getRequestType() == Reference.PACKET_REQUEST_CONNECTION
                        || ((MatterNetworkRequestPacket) packet).getRequestType() == Reference.PACKET_REQUEST_NEIGHBOR_CONNECTION) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void queuePacket(MatterNetworkPacket packet, ForgeDirection from) {
        manageBasicPacketsQueuing(rootClient, rootClient.getWorldObj(), packet, from);
    }

    @Override
    public int manageTopQueue(World world, MatterNetworkTaskStorePattern task)
    {
        int broadcastCount = 0;
        if (task != null)
        {
            if (task.getState() == MatterNetworkTaskState.FINISHED)
            {
                onTaskComplete(rootClient.getTaskQueue(0).dequeue());
            }
            else
            {
                if (canBroadcastTask(world,task))
                {
                    for (int i = 0; i < 6; i++)
                    {
                        if (MatterNetworkHelper.broadcastTaskInDirection(world, (byte) 0, task, rootClient, ForgeDirection.getOrientation(i)))
                        {
                            onTaskBroadcast(world, task, ForgeDirection.getOrientation(i));
                            broadcastCount++;
                        }

                    }
                }
            }
        }
        else
        {
            return manageValidDestinationCheck(world);
        }

        rootClient.getTaskQueue(0).tickAllAlive(world, false);
        return broadcastCount;
    }

    @Override
    protected void manageTaskPacketQueuing(MatterNetworkTaskPacket packet, MatterNetworkTask task)
    {

    }

    @Override
    protected void manageRequestsQueuing(MatterNetworkRequestPacket packet)
    {

    }

    @Override
    protected void manageResponsesQueuing(MatterNetworkResponsePacket packet)
    {
        if (packet.fits(Reference.PACKET_RESPONCE_INVALID,Reference.PACKET_REQUEST_VALID_PATTERN_DESTINATION))
        {
            rootClient.setHasValidPatternDestination(false);
        }
    }

    private int manageValidDestinationCheck(World world)
    {
        int broadcastCount = 0;
        if (rootClient.getInventory().getStackInSlot(rootClient.input_slot) != null)
        {
            if (validDestinationTracker.hasDelayPassed(world, TileEntityMachineMatterAnalyzer.VALID_LOCATION_CHECK_DELAY))
            {
                for (int i = 0; i < 6; i++) {
                    NBTTagCompound itemTag = new NBTTagCompound();
                    rootClient.getInventory().getStackInSlot(rootClient.input_slot).writeToNBT(itemTag);
                    MatterNetworkRequestPacket packet = new MatterNetworkRequestPacket(rootClient, Reference.PACKET_REQUEST_VALID_PATTERN_DESTINATION,ForgeDirection.getOrientation(i), itemTag);
                    if (MatterNetworkHelper.broadcastTaskInDirection(world, packet, rootClient, ForgeDirection.getOrientation(i)))
                    {
                        onValidDestinationBroadcast(world, packet, ForgeDirection.getOrientation(i));
                        broadcastCount++;
                    }
                }
            }
        }
        return broadcastCount;
    }

    private boolean canBroadcastTask(World world,MatterNetworkTask task)
    {
        return !task.isAlive() && broadcastTracker.hasDelayPassed(world, task.getState() == MatterNetworkTaskState.WAITING ? TileEntityMachineMatterAnalyzer.BROADCAST_WEATING_DELAY : TileEntityMachineMatterAnalyzer.BROADCAST_DELAY);
    }

    //region Events
    private void onTaskComplete(MatterNetworkTask task)
    {
        rootClient.ForceSync();
    }

    private void onTaskBroadcast(World world,MatterNetworkTask task,ForgeDirection direction)
    {
        task.setState(MatterNetworkTaskState.WAITING);
    }

    private void onValidDestinationBroadcast(World world,MatterNetworkRequestPacket packet,ForgeDirection direction)
    {
        rootClient.setHasValidPatternDestination(true);
    }

    //endregion
}
