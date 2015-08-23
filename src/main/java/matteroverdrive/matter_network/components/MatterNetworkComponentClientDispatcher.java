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
import matteroverdrive.api.network.IMatterNetworkClient;
import matteroverdrive.api.network.IMatterNetworkDispatcher;
import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.matter_network.MatterNetworkTaskQueue;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

/**
 * Created by Simeon on 7/16/2015.
 */
public abstract class MatterNetworkComponentClientDispatcher <K extends MatterNetworkTask,T extends MOTileEntityMachine & IMatterNetworkClient & IMatterNetworkDispatcher> extends MatterNetworkComponentClient<T> implements IMatterNetworkDispatcher<K>
{
    private TickEvent.Phase dispatchPhase;

    public MatterNetworkComponentClientDispatcher(T rootClient,TickEvent.Phase dispatchPhase)
    {
        super(rootClient);
        this.dispatchPhase = dispatchPhase;
    }

    @Override
    public MatterNetworkTaskQueue<K> getTaskQueue(int queueID)
    {
        return rootClient.getTaskQueue(queueID);
    }

    @Override
    public int onNetworkTick(World world, TickEvent.Phase phase)
    {
        super.onNetworkTick(world,phase);
        if (phase.equals(dispatchPhase))
        {
            for (int i = 0;i < getTaskQueueCount();i++)
            {
                if (getTaskQueue(i).peek() != null) {
                    try {
                        return manageTopQueue(world,i, getTaskQueue(i).peek());
                    } catch (Exception e) {
                        MatterOverdrive.log.log(Level.ERROR, e, "Where was a problem while trying to get task from Queue from %s", getClass());
                    }
                }
            }
        }
        return 0;
    }

    public abstract int manageTopQueue(World world,int queueID,K element);
    public int getTaskQueueCount()
    {
        return  rootClient.getTaskQueueCount();
    }
}
