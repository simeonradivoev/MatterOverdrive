package matteroverdrive.matter_network.components;

import matteroverdrive.api.container.IMachineWatcher;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.network.IMatterNetworkDispatcher;
import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.container.matter_network.ITaskQueueWatcher;
import matteroverdrive.data.Inventory;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.machines.MachineComponentAbstract;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.machines.events.MachineEvent;
import matteroverdrive.matter_network.MatterNetworkTaskQueue;
import net.minecraft.nbt.NBTTagCompound;

import java.util.EnumSet;

/**
 * Created by Simeon on 2/6/2016.
 */
public class TaskQueueComponent<T extends MatterNetworkTask,M extends MOTileEntityMachine & IMatterNetworkDispatcher> extends MachineComponentAbstract<M>
{
    private MatterNetworkTaskQueue<T> taskQueue;
    private final int queueId;

    public TaskQueueComponent(String name,M machine,int taskQueueCapacity,int queueId)
    {
        super(machine);
        taskQueue = new MatterNetworkTaskQueue<>(name,taskQueueCapacity);
        this.queueId = queueId;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
    {
        if (categories.contains(MachineNBTCategory.DATA))
        {
            if (nbt.hasKey("tasks"))
            {
                taskQueue.readFromNBT(nbt.getCompoundTag("tasks"));
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk)
    {
        if (categories.contains(MachineNBTCategory.DATA) && toDisk)
        {
            NBTTagCompound taskQueueTag = new NBTTagCompound();
            taskQueue.writeToNBT(taskQueueTag);
            nbt.setTag("tasks",taskQueueTag);
        }
    }

    @Override
    public void registerSlots(Inventory inventory)
    {

    }

    @Override
    public boolean isAffectedByUpgrade(UpgradeTypes type)
    {
        return false;
    }

    @Override
    public boolean isActive()
    {
        return true;
    }

    @Override
    public void onMachineEvent(MachineEvent event)
    {

    }

    public void sendTaskQueueAddedToWatchers(long taskId)
    {
        machine.getWatchers().stream().filter(watcher -> watcher instanceof ITaskQueueWatcher).forEach(watcher -> ((ITaskQueueWatcher) watcher).onTaskAdded(machine, taskId, queueId));
    }

    public void sendTaskQueueRemovedFromWatchers(long taskId)
    {
        machine.getWatchers().stream().filter(watcher -> watcher instanceof ITaskQueueWatcher).forEach(watcher -> ((ITaskQueueWatcher) watcher).onTaskRemoved(machine, taskId, queueId));
    }

    //region Getters and Setters
    public MatterNetworkTaskQueue<T> getTaskQueue()
    {
        return taskQueue;
    }
    public int getQueueId(){return queueId;}
    //endregion
}
