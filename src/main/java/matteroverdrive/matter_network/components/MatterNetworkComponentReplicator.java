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
import matteroverdrive.Reference;
import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.api.network.MatterNetworkTaskState;
import matteroverdrive.data.ItemPattern;
import matteroverdrive.matter_network.MatterNetworkPacket;
import matteroverdrive.matter_network.packets.MatterNetworkRequestPacket;
import matteroverdrive.matter_network.packets.MatterNetworkResponsePacket;
import matteroverdrive.matter_network.packets.MatterNetworkTaskPacket;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskReplicatePattern;
import matteroverdrive.tile.TileEntityMachineReplicator;
import matteroverdrive.util.MatterNetworkHelper;
import matteroverdrive.util.TimeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 7/13/2015.
 */
public class MatterNetworkComponentReplicator extends MatterNetworkComponentClientDispatcher<MatterNetworkTaskReplicatePattern,TileEntityMachineReplicator>
{
    private TimeTracker patternSearchTracker;

    public MatterNetworkComponentReplicator(TileEntityMachineReplicator replicator)
    {
        super(replicator, TickEvent.Phase.END);
        patternSearchTracker = new TimeTracker();
        handlers.add(BASIC_CONNECTIONS_HANDLER);
    }

    @Override
    public boolean canPreform(MatterNetworkPacket packet)
    {
        if (super.canPreform(packet)) {
            if (packet instanceof MatterNetworkTaskPacket) {
                if (((MatterNetworkTaskPacket) packet).getTask(rootClient.getWorldObj()) instanceof MatterNetworkTaskReplicatePattern) {
                    return rootClient.getTaskQueue(0).remaintingCapacity() > 0;
                }
            }
            return true;
        }else
        {
            return false;
        }
    }

    @Override
    protected void executePacket(MatterNetworkPacket packet)
    {
        super.executePacket(packet);

        if (packet instanceof MatterNetworkTaskPacket)
        {
            executeTasks((MatterNetworkTaskPacket)packet,((MatterNetworkTaskPacket)packet).getTask(getWorldObj()));
        }else if (packet instanceof MatterNetworkResponsePacket)
        {
            executeResponses((MatterNetworkResponsePacket)packet);
        }
    }

    protected void executeTasks(MatterNetworkTaskPacket packet,MatterNetworkTask task)
    {
        if (task instanceof MatterNetworkTaskReplicatePattern)
        {
            if (rootClient.getTaskQueue(0).queue((MatterNetworkTaskReplicatePattern)task))
            {
                task.setSender(rootClient);
                task.setState(MatterNetworkTaskState.PROCESSING);
                task.setAlive(true);
                rootClient.forceSync();
            }
        }
    }

    protected void executeResponses(MatterNetworkResponsePacket packet)
    {
        //Request pattern search response
        if (packet.getRequestType() == Reference.PACKET_REQUEST_PATTERN_SEARCH && packet.getResponseType() == Reference.PACKET_RESPONCE_VALID)
        {
            NBTTagCompound responseTag = packet.getResponse();
            ItemPattern responsePattern = new ItemPattern(responseTag);
            MatterNetworkTaskReplicatePattern task = rootClient.getTaskQueue(0).peek();
            if (responseTag != null && responsePattern.equals(task.getPattern()))
            {
                if (rootClient.getInternalPatternStorage() != null)
                {
                    //if the previous tag is the same but has a higher progress, then continue
                    if (rootClient.getInternalPatternStorage().equals(responsePattern) && rootClient.getInternalPatternStorage().getProgress() > responsePattern.getProgress())
                    {
                        return;
                    }
                }

                //save the pattern in the machine
                rootClient.setInternalPatternStorage(responsePattern);
                rootClient.forceSync();
            }
        }
    }

    @Override
    public int manageTopQueue(World world,int queueID,MatterNetworkTaskReplicatePattern task)
    {
        int broadcasts = 0;

        if (rootClient.getRedstoneActive() && !rootClient.canCompleteTask(task) && patternSearchTracker.hasDelayPassed(world,rootClient.PATTERN_SEARCH_DELAY)) {
            if (task != null) {
                for (int i = 0; i < 6; i++) {
                    MatterNetworkRequestPacket requestPacket = new MatterNetworkRequestPacket(rootClient, Reference.PACKET_REQUEST_PATTERN_SEARCH,ForgeDirection.getOrientation(i),rootClient.getFilter(), task.getPattern());
                    if (MatterNetworkHelper.broadcastPacketInDirection(world, requestPacket, rootClient, ForgeDirection.getOrientation(i)))
                    {
                        broadcasts++;
                    }
                }
            }
        }
        return broadcasts;
    }
}
