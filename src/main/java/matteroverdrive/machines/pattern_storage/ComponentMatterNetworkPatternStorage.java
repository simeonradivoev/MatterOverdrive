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

package matteroverdrive.machines.pattern_storage;

import matteroverdrive.api.network.MatterNetworkTaskState;
import matteroverdrive.data.matter_network.IMatterNetworkEvent;
import matteroverdrive.matter_network.components.MatterNetworkComponentClient;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskStorePattern;

/**
 * Created by Simeon on 7/15/2015.
 */
public class ComponentMatterNetworkPatternStorage extends MatterNetworkComponentClient<TileEntityMachinePatternStorage>
{
	public ComponentMatterNetworkPatternStorage(TileEntityMachinePatternStorage patternStorage)
	{
		super(patternStorage);
	}

	@Override
	public void onNetworkEvent(IMatterNetworkEvent event)
	{
		if (event instanceof IMatterNetworkEvent.Task && ((IMatterNetworkEvent.Task)event).task instanceof MatterNetworkTaskStorePattern)
		{
			onTask((MatterNetworkTaskStorePattern)((IMatterNetworkEvent.Task)event).task);
		}
	}

	private void onTask(MatterNetworkTaskStorePattern task)
	{
		if (task.getState().belowOrEqual(MatterNetworkTaskState.WAITING) && rootClient.addItem(task.getItemStack(), task.getProgress(), false, null))
		{
			task.setState(MatterNetworkTaskState.FINISHED);
		}
	}
}
