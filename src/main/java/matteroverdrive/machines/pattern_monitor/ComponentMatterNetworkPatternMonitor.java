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

package matteroverdrive.machines.pattern_monitor;

import matteroverdrive.api.matter.IMatterDatabase;
import matteroverdrive.container.matter_network.IMatterDatabaseWatcher;
import matteroverdrive.data.matter_network.MatterDatabaseEvent;
import matteroverdrive.data.matter_network.IMatterNetworkEvent;
import matteroverdrive.matter_network.components.MatterNetworkComponentClient;

/**
 * Created by Simeon on 7/13/2015.
 */
public class ComponentMatterNetworkPatternMonitor extends MatterNetworkComponentClient<TileEntityMachinePatternMonitor>
{
    public ComponentMatterNetworkPatternMonitor(TileEntityMachinePatternMonitor patternMonitor)
    {
        super(patternMonitor);
    }

    @Override
    public void onNetworkEvent(IMatterNetworkEvent event)
    {
        if (event instanceof IMatterNetworkEvent.ClientAdded)
        {
            onClientAdded((IMatterNetworkEvent.ClientAdded)event);
        }else if (event instanceof IMatterNetworkEvent.AddedToNetwork)
        {
            onAddedToNetwork((IMatterNetworkEvent.AddedToNetwork)event);
        }else if (event instanceof IMatterNetworkEvent.RemovedFromNetwork)
        {
            onRemovedFromNetwork((IMatterNetworkEvent.RemovedFromNetwork)event);
        }
        else if (event instanceof IMatterNetworkEvent.ClientRemoved)
        {
            onClientRemoved((IMatterNetworkEvent.ClientRemoved)event);
        }else if (event instanceof MatterDatabaseEvent)
        {
            onPatternChange((MatterDatabaseEvent)event);
        }
    }

    private void onRemovedFromNetwork(IMatterNetworkEvent.RemovedFromNetwork event)
    {
        rootClient.getWatchers().stream().filter(watcher -> watcher instanceof IMatterDatabaseWatcher).forEach(watcher -> ((IMatterDatabaseWatcher) watcher).onDisconnectFromNetwork(rootClient));
    }

    private void onAddedToNetwork(IMatterNetworkEvent.AddedToNetwork event)
    {
        rootClient.getWatchers().stream().filter(watcher -> watcher instanceof IMatterDatabaseWatcher).forEach(watcher -> ((IMatterDatabaseWatcher) watcher).onConnectToNetwork(rootClient));
    }

    private void onClientAdded(IMatterNetworkEvent.ClientAdded event)
    {
        if (event.client instanceof IMatterDatabase)
        {
            MatterDatabaseEvent databaseEvent = new MatterDatabaseEvent.Added((IMatterDatabase)event.client);
            rootClient.getWatchers().stream().filter(watcher -> watcher instanceof IMatterDatabaseWatcher).forEach(watcher -> ((IMatterDatabaseWatcher) watcher).onDatabaseEvent(databaseEvent));
        }
    }

    private void onClientRemoved(IMatterNetworkEvent.ClientRemoved event)
    {
        if (event.client instanceof IMatterDatabase)
        {
            MatterDatabaseEvent databaseEvent = new MatterDatabaseEvent.Removed((IMatterDatabase)event.client);
            rootClient.getWatchers().stream().filter(watcher -> watcher instanceof IMatterDatabaseWatcher).forEach(watcher -> ((IMatterDatabaseWatcher) watcher).onDatabaseEvent(databaseEvent));
        }
    }

    private void onPatternChange(MatterDatabaseEvent event)
    {
        rootClient.getWatchers().stream().filter(watcher -> watcher instanceof IMatterDatabaseWatcher).forEach(watcher -> ((IMatterDatabaseWatcher) watcher).onDatabaseEvent(event));
    }
}
