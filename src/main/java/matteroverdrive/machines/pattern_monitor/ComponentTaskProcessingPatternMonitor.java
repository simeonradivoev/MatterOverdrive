package matteroverdrive.machines.pattern_monitor;

import matteroverdrive.matter_network.components.TaskQueueComponent;
import matteroverdrive.matter_network.events.MatterNetworkEventReplicate;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskReplicatePattern;
import matteroverdrive.util.TimeTracker;
import net.minecraft.util.ITickable;

/**
 * Created by Simeon on 2/6/2016.
 */
public class ComponentTaskProcessingPatternMonitor extends TaskQueueComponent<MatterNetworkTaskReplicatePattern, TileEntityMachinePatternMonitor> implements ITickable
{
	public static final int REPLICATION_SEARCH_TIME = 40;
	private final TimeTracker patternSendTimeTracker;

	public ComponentTaskProcessingPatternMonitor(String name, TileEntityMachinePatternMonitor machine, int taskQueueCapacity, int queueId)
	{
		super(name, machine, taskQueueCapacity, queueId);
		patternSendTimeTracker = new TimeTracker();
	}

	public void addReplicateTask(MatterNetworkTaskReplicatePattern task)
	{
		if (getTaskQueue().queue(task))
		{
			sendTaskQueueAddedToWatchers(task.getId());
		}
	}

	@Override
	public void update()
	{
		if (!getWorld().isRemote)
		{
			MatterNetworkTaskReplicatePattern replicatePattern = getTaskQueue().peek();
			if (replicatePattern != null)
			{
				if (patternSendTimeTracker.hasDelayPassed(getWorld(), REPLICATION_SEARCH_TIME))
				{
					MatterNetworkEventReplicate.Request requestPatternReplication = new MatterNetworkEventReplicate.Request(replicatePattern.getPattern(), replicatePattern.getAmount());
					machine.getNetwork().post(requestPatternReplication);
					if (requestPatternReplication.isAccepted())
					{
						getTaskQueue().dequeue();
					}
				}
			}
		}
	}
}
