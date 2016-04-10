package matteroverdrive.data.matter_network;

import matteroverdrive.api.matter_network.IMatterNetworkClient;
import matteroverdrive.api.network.MatterNetworkTask;

/**
 * Created by Simeon on 1/29/2016.
 */
public interface IMatterNetworkEvent
{
	class ClientAdded implements IMatterNetworkEvent
	{
		public final IMatterNetworkClient client;

		public ClientAdded(IMatterNetworkClient gridNode)
		{
			this.client = gridNode;
		}
	}

	class ClientRemoved implements IMatterNetworkEvent
	{
		public final IMatterNetworkClient client;

		public ClientRemoved(IMatterNetworkClient gridNode)
		{
			this.client = gridNode;
		}
	}

	class AddedToNetwork implements IMatterNetworkEvent
	{
	}

	class RemovedFromNetwork implements IMatterNetworkEvent
	{
	}

	class Task implements IMatterNetworkEvent
	{
		public final MatterNetworkTask task;

		public Task(MatterNetworkTask task)
		{
			this.task = task;
		}
	}
}
