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

package matteroverdrive.machines.replicator;

import matteroverdrive.api.network.MatterNetworkTaskState;
import matteroverdrive.data.matter_network.IMatterNetworkEvent;
import matteroverdrive.matter_network.components.MatterNetworkComponentClient;
import matteroverdrive.matter_network.events.MatterNetworkEventReplicate;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskReplicatePattern;

/**
 * Created by Simeon on 7/13/2015.
 */
public class ComponentMatterNetworkReplicator extends MatterNetworkComponentClient<TileEntityMachineReplicator>
{
    public ComponentMatterNetworkReplicator(TileEntityMachineReplicator replicator)
    {
        super(replicator);
    }

    @Override
    public void onNetworkEvent(IMatterNetworkEvent event)
    {
        if (event instanceof MatterNetworkEventReplicate.Request)
        {
            onReplicationRequest((MatterNetworkEventReplicate.Request)event);
        }
    }

    private void onReplicationRequest(MatterNetworkEventReplicate.Request request)
    {
        if (!request.isAccepted())
        {
            MatterNetworkTaskReplicatePattern replicatePattern = new MatterNetworkTaskReplicatePattern(request.pattern,request.amount);
            replicatePattern.setState(MatterNetworkTaskState.QUEUED);
            if(rootClient.getComponent(ComponentTaskProcessingReplicator.class).addReplicationTask(replicatePattern))
            {
                request.markAccepted();
            }
        }
    }
}
