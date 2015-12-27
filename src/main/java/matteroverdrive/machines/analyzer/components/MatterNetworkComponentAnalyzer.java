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

import cpw.mods.fml.common.gameevent.TickEvent;
import matteroverdrive.Reference;
import matteroverdrive.api.matter.IMatterDatabase;
import matteroverdrive.api.network.IMatterNetworkConnection;
import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.api.network.MatterNetworkTaskState;
import matteroverdrive.data.ItemPattern;
import matteroverdrive.machines.analyzer.TileEntityMachineMatterAnalyzer;
import matteroverdrive.matter_network.MatterNetworkPacket;
import matteroverdrive.matter_network.components.MatterNetworkComponentClientDispatcher;
import matteroverdrive.matter_network.packets.MatterNetworkRequestPacket;
import matteroverdrive.matter_network.packets.MatterNetworkResponsePacket;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskStorePattern;
import matteroverdrive.util.MatterNetworkHelper;
import matteroverdrive.util.TimeTracker;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 7/13/2015.
 */
public class MatterNetworkComponentAnalyzer extends MatterNetworkComponentClientDispatcher<MatterNetworkTaskStorePattern,TileEntityMachineMatterAnalyzer>
{
    private IMatterNetworkConnection connection;
    private boolean badLocation;
    private TimeTracker broadcastTracker,validDestinationTracker;

    public MatterNetworkComponentAnalyzer(TileEntityMachineMatterAnalyzer analyzer)
    {
        super(analyzer,TickEvent.Phase.START);
        broadcastTracker = new TimeTracker();
        validDestinationTracker = new TimeTracker();
        handlers.add(BASIC_CONNECTIONS_HANDLER);
    }

    @Override
    protected void executePacket(MatterNetworkPacket packet)
    {
        if (packet instanceof MatterNetworkResponsePacket)
        {
            executeResponses((MatterNetworkResponsePacket)packet);
        }
    }

    protected void executeResponses(MatterNetworkResponsePacket packet)
    {
        if (packet.fits(Reference.PACKET_RESPONCE_INVALID,Reference.PACKET_REQUEST_VALID_PATTERN_DESTINATION))
        {
            badLocation = true;
            connection = null;
        }else if (packet.fits(Reference.PACKET_RESPONCE_VALID,Reference.PACKET_REQUEST_VALID_PATTERN_DESTINATION) && !badLocation)
        {
            if (isConnectionBetter(connection,packet)) {
                connection = packet.getSender(rootClient.getWorldObj());
            }
        }
    }

    @Override
    public int manageTopQueue(World world,int queueID, MatterNetworkTaskStorePattern task) {
        int broadcastCount = 0;
        if (task.getState() == MatterNetworkTaskState.FINISHED)
        {
            onTaskComplete(rootClient.getTaskQueue(0).dequeue());
        }
        else
        {
            if (canBroadcastTask(world, task)) {
                for (int i = 0; i < 6; i++) {
                    if (MatterNetworkHelper.broadcastPacketInDirection(world, (byte) 0, task, rootClient, ForgeDirection.getOrientation(i), connection != null ? MatterNetworkHelper.getFilterFromPositions(connection.getPosition()) : null)) {
                        onTaskBroadcast(world, task, ForgeDirection.getOrientation(i));
                        broadcastCount++;
                    }

                }
            }
        }
        rootClient.getTaskQueue(queueID).tickAllAlive(world, false);
        return broadcastCount;
    }

    @Override
    public int onNetworkTick(World world, TickEvent.Phase phase)
    {
        int broadcasts = super.onNetworkTick(world,phase);
        if (phase.equals(TickEvent.Phase.START))
        {
            broadcasts += manageValidDestinationCheck(world);
        }
        return broadcasts;
    }

    private boolean isConnectionBetter(IMatterNetworkConnection one,MatterNetworkResponsePacket two)
    {
        if (one == null && two != null)
            return true;
        if (one == null && two == null)
            return false;
        if (!(two.getSender(rootClient.getWorldObj()) instanceof IMatterDatabase))
            return false;

        ItemPattern packetPattern = new ItemPattern(two.getResponse());
        ItemStack packetPatternStack = packetPattern.toItemStack(false);
        if (packetPattern != null && packetPatternStack != null)
        {
            if (one instanceof IMatterDatabase) {
                ItemPattern patternOne = ((IMatterDatabase) one).getPattern(packetPatternStack);
                int oneProgress = patternOne.getProgress();
                int twoProgress = packetPattern.getProgress();
                if (oneProgress < twoProgress)
                {
                    return true;
                }
            }else
            {
                return true;
            }
        }
        return false;
    }

    private int manageValidDestinationCheck(World world)
    {
        int broadcastCount = 0;
        if (rootClient.isActive() || (getTaskQueue(0).size() > 0 && connection == null))
        {
            if (validDestinationTracker.hasDelayPassed(world, TileEntityMachineMatterAnalyzer.VALID_LOCATION_CHECK_DELAY))
            {
                for (int i = 0; i < 6; i++) {
                    MatterNetworkRequestPacket packet = new MatterNetworkRequestPacket(rootClient, Reference.PACKET_REQUEST_VALID_PATTERN_DESTINATION,ForgeDirection.getOrientation(i),rootClient.getFilter(), new ItemPattern(rootClient.getInventory().getStackInSlot(rootClient.input_slot)));
                    if (MatterNetworkHelper.broadcastPacketInDirection(world, packet, rootClient, ForgeDirection.getOrientation(i)))
                    {
                        resetValidLocation();
                        broadcastCount++;
                    }
                }
            }
        }
        return broadcastCount;
    }

    public void resetValidLocation()
    {
        connection = null;
        badLocation = false;
    }

    private boolean canBroadcastTask(World world,MatterNetworkTask task)
    {
        return !task.isAlive() &&
                broadcastTracker.hasDelayPassed(world, task.getState() == MatterNetworkTaskState.WAITING ? TileEntityMachineMatterAnalyzer.BROADCAST_WEATING_DELAY : TileEntityMachineMatterAnalyzer.BROADCAST_DELAY);
    }

    //region Events
    private void onTaskComplete(MatterNetworkTask task)
    {
        rootClient.forceSync();
    }

    private void onTaskBroadcast(World world,MatterNetworkTask task,ForgeDirection direction)
    {
        task.setState(MatterNetworkTaskState.WAITING);
    }

    //endregion

    //region Getters and Setters
    public IMatterNetworkConnection getConnection()
    {
        return connection;
    }
    //endregion
}
