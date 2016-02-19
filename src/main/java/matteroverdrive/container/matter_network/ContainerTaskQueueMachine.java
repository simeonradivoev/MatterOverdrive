package matteroverdrive.container.matter_network;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.network.IMatterNetworkDispatcher;
import matteroverdrive.container.ContainerMachine;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.network.packet.client.task_queue.PacketSyncTaskQueue;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;

/**
 * Created by Simeon on 2/6/2016.
 */
public class ContainerTaskQueueMachine<T extends MOTileEntityMachine & IMatterNetworkDispatcher> extends ContainerMachine<T> implements ITaskQueueWatcher
{
    public ContainerTaskQueueMachine(InventoryPlayer inventory, T machine)
    {
        super(inventory, machine);
    }

    @Override
    public void onWatcherAdded(MOTileEntityMachine machine)
    {
        super.onWatcherAdded(machine);
        if (machine instanceof IMatterNetworkDispatcher)
            sendAllTaskQueues((IMatterNetworkDispatcher)machine);
    }

    private void sendAllTaskQueues(IMatterNetworkDispatcher dispatcher)
    {
        for (int i = 0;i < dispatcher.getTaskQueueCount();i++)
        {
            sendTaskQueue(dispatcher,i);
        }
    }

    private void sendTaskQueue(IMatterNetworkDispatcher dispatcher,int queueId)
    {
        MatterOverdrive.packetPipeline.sendTo(new PacketSyncTaskQueue(dispatcher,queueId),(EntityPlayerMP) getPlayer());
    }

    @Override
    public void onTaskAdded(IMatterNetworkDispatcher dispatcher, long taskId, int queueId)
    {
        sendTaskQueue(dispatcher,queueId);
    }

    @Override
    public void onTaskRemoved(IMatterNetworkDispatcher dispatcher, long taskId, int queueId)
    {
        sendTaskQueue(dispatcher,queueId);
    }

    @Override
    public void onTaskChanged(IMatterNetworkDispatcher dispatcher, long taskId, int queueId)
    {
        sendTaskQueue(dispatcher,queueId);
    }
}
