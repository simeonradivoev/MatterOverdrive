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
import cpw.mods.fml.common.gameevent.TickEvent;
import matteroverdrive.Reference;
import matteroverdrive.api.network.IMatterNetworkHandler;
import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.api.network.MatterNetworkTaskState;
import matteroverdrive.tile.TileEntityMachineMatterAnalyzer;
import matteroverdrive.util.MatterNetworkHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 7/13/2015.
 */
public class MatterNetworkComponentAnalyzer implements IMatterNetworkHandler
{
    private TileEntityMachineMatterAnalyzer analyzer;
    private TimeTracker broadcastTracker;

    public MatterNetworkComponentAnalyzer(TileEntityMachineMatterAnalyzer analyzer)
    {
        this.analyzer = analyzer;
        broadcastTracker = new TimeTracker();
    }

    @Override
    public int onNetworkTick(World world, TickEvent.Phase phase) {
        if(phase.equals(TickEvent.Phase.START))
        {
            return manageBroadcast(world, analyzer.getQueue(0).peek());
        }
        return 0;
    }

    private int manageBroadcast(World world,MatterNetworkTask task)
    {
        int broadcastCount = 0;
        if (task != null)
        {
            if (task.getState() == MatterNetworkTaskState.PROCESSING) {

            } else if (task.getState() == MatterNetworkTaskState.FINISHED)
            {
                onTaskComplete(analyzer.getQueue(0).dequeue());
            }
            else
            {
                if (canBroadcastTask(world,task))
                {
                    for (int i = 0; i < 6; i++)
                    {
                        if (MatterNetworkHelper.broadcastTaskInDirection(world, (byte) 0, task, analyzer, ForgeDirection.getOrientation(i)))
                        {
                            onTaskBroadcast(world, task, ForgeDirection.getOrientation(i));
                            broadcastCount++;
                        }

                    }
                }
            }
        }

        analyzer.getQueue(0).tickAllAlive(world,false);
        return broadcastCount;
    }

    private void onTaskComplete(MatterNetworkTask task)
    {
        analyzer.ForceSync();
    }

    private boolean canBroadcastTask(World world,MatterNetworkTask task)
    {
        return !task.isAlive() && broadcastTracker.hasDelayPassed(world, task.getState() == MatterNetworkTaskState.WAITING ? analyzer.BROADCAST_WEATING_DELAY : analyzer.BROADCAST_DELAY);
    }

    private void onTaskBroadcast(World world,MatterNetworkTask task,ForgeDirection direction)
    {
        task.setState(MatterNetworkTaskState.WAITING);
    }
}
