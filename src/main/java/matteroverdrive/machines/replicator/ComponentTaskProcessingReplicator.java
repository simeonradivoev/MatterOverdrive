package matteroverdrive.machines.replicator;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.matter.IMatterHandler;
import matteroverdrive.api.network.MatterNetworkTaskState;
import matteroverdrive.blocks.BlockReplicator;
import matteroverdrive.data.matter_network.ItemPattern;
import matteroverdrive.handler.GoogleAnalyticsCommon;
import matteroverdrive.handler.SoundHandler;
import matteroverdrive.init.MatterOverdriveCapabilities;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.matter_network.components.TaskQueueComponent;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskReplicatePattern;
import matteroverdrive.network.packet.client.PacketReplicationComplete;
import matteroverdrive.util.MatterHelper;
import matteroverdrive.util.TimeTracker;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;

import java.util.EnumSet;
import java.util.Random;

/**
 * Created by Simeon on 2/6/2016.
 */
public class ComponentTaskProcessingReplicator extends TaskQueueComponent<MatterNetworkTaskReplicatePattern, TileEntityMachineReplicator> implements ITickable
{
	public static final double FAIL_CHANCE = 0.005;
	public static int REPLICATE_SPEED_PER_MATTER = 120;
	public static int REPLICATE_ENERGY_PER_MATTER = 16000;
	private final TimeTracker radiationTimeTracker;
	private final Random random;
	public int replicateTime;
	private float replicateProgress;

	public ComponentTaskProcessingReplicator(String name, TileEntityMachineReplicator machine, int taskQueueCapacity)
	{
		super(name, machine, taskQueueCapacity, taskQueueCapacity);
		radiationTimeTracker = new TimeTracker();
		random = new Random();
	}

	public boolean addReplicationTask(MatterNetworkTaskReplicatePattern task)
	{
		if (getTaskQueue().queue(task))
		{
			return true;
		}
		return false;
	}

	@Override
	public void update()
	{
		manageReplicate();
	}

	protected void manageReplicate()
	{

		if (this.isReplicating())
		{

			if (!getWorld().isRemote)
			{

				MatterNetworkTaskReplicatePattern replicatePattern = getTaskQueue().peek();
				ItemStack patternStack = replicatePattern.getPattern().toItemStack(false);

				if (replicatePattern.isValid(getWorld()))
				{
					if (machine.getEnergyStorage().getEnergyStored() >= getEnergyDrainPerTick())
					{


						replicatePattern.setState(MatterNetworkTaskState.PROCESSING);
						this.replicateTime++;
						machine.extractEnergy(EnumFacing.DOWN, getEnergyDrainPerTick(patternStack), false);
						int time = getSpeed(patternStack);

						if (this.replicateTime >= time)
						{
							this.replicateTime = 0;
							this.replicateItem(replicatePattern.getPattern(), patternStack);
							MatterOverdrive.packetPipeline.sendToDimention(new PacketReplicationComplete(machine), getWorld());
							SoundHandler.PlaySoundAt(getWorld(), MatterOverdriveSounds.replicateSuccess, SoundCategory.BLOCKS, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 0.25F * machine.getBlockType(BlockReplicator.class).replication_volume, 1.0F, 0.2F, 0.8F);
						}
						if (radiationTimeTracker.hasDelayPassed(getWorld(), TileEntityMachineReplicator.RADIATION_DAMAGE_DELAY))
						{
							machine.manageRadiation();
						}

						replicateProgress = (float)replicateTime / (float)time;
					}
				}
				else
				{
					getTaskQueue().dequeue();
				}
			}
		}
		else
		{
			this.replicateTime = 0;
			replicateProgress = 0;
		}

	}

	private void replicateItem(ItemPattern itemPattern, ItemStack newItem)
	{
		if (isActive())
		{
			int matterAmount = MatterHelper.getMatterAmountFromItem(newItem);

			float chance = random.nextFloat();

			if (chance < getFailChance(itemPattern))
			{
				if (machine.failReplicate(MatterHelper.getMatterAmountFromItem(newItem)))
				{
					MatterOverdrive.proxy.getGoogleAnalytics().sendEventHit(GoogleAnalyticsCommon.EVENT_CATEGORY_MACHINES, GoogleAnalyticsCommon.EVENT_ACTION_REPLICATION_FAIL, newItem.getUnlocalizedName(), null);
					IMatterHandler storage = machine.getCapability(MatterOverdriveCapabilities.MATTER_HANDLER, null);
					int matter = storage.getMatterStored();
					storage.setMatterStored(matter - matterAmount);
				}
			}
			else
			{
				if (machine.putInOutput(newItem))
				{
					MatterOverdrive.proxy.getGoogleAnalytics().sendEventHit(GoogleAnalyticsCommon.EVENT_CATEGORY_MACHINES, GoogleAnalyticsCommon.EVENT_ACTION_REPLICATE, newItem.getUnlocalizedName(), null);
					IMatterHandler storage = machine.getCapability(MatterOverdriveCapabilities.MATTER_HANDLER, null);
					int matter = storage.getMatterStored();
					storage.setMatterStored(matter - matterAmount);
					MatterNetworkTaskReplicatePattern task = getTaskQueue().peek();
					task.setAmount(task.getAmount() - 1);
					if (task.getAmount() <= 0)
					{
						task.setState(MatterNetworkTaskState.FINISHED);
						getTaskQueue().dequeue();
					}
				}
			}
		}
	}

	public int getEnergyDrainPerTick(ItemStack itemStack)
	{
		int maxEnergy = getEnergyDrainMax();
		return maxEnergy / getSpeed(itemStack);
	}

	public int getEnergyDrainPerTick()
	{
		if (getTaskQueue().peek() != null && getTaskQueue().peek().isValid(getWorld()))
		{
			return getEnergyDrainPerTick(getTaskQueue().peek().getPattern().toItemStack(false));
		}
		return 0;
	}

	public int getEnergyDrainMax()
	{
		if (getTaskQueue().peek() != null && getTaskQueue().peek().isValid(getWorld()))
		{
			int matter = MatterHelper.getMatterAmountFromItem(getTaskQueue().peek().getPattern().toItemStack(false));
			double upgradeMultiply = machine.getUpgradeMultiply(UpgradeTypes.PowerUsage);
			return (int)Math.round((Math.log1p(matter * 0.05) * 4 * REPLICATE_ENERGY_PER_MATTER) * upgradeMultiply);
		}
		return 0;
	}

	public int getSpeed(ItemStack itemStack)
	{
		double matter = Math.log1p(MatterHelper.getMatterAmountFromItem(itemStack));
		matter *= matter;
		return (int)Math.round(((REPLICATE_SPEED_PER_MATTER * Math.log1p(matter * 0.05) * 10) - 60) * machine.getUpgradeMultiply(UpgradeTypes.Speed)) + 60;
	}

	public double getFailChance(ItemPattern itemPattern)
	{
		double progressChance = 1f - itemPattern.getProgressF();
		double upgradeMultiply = machine.getUpgradeMultiply(UpgradeTypes.Fail);
		//this does not negate all fail chance if item is not fully scanned
		return FAIL_CHANCE * upgradeMultiply + progressChance * 0.5 + (progressChance * 0.5) * upgradeMultiply;
	}

	public boolean isReplicating()
	{
		if (machine.getRedstoneActive() && getTaskQueue().size() > 0 && getTaskQueue().peek().isValid(getWorld()))
		{
			ItemStack item = getTaskQueue().peek().getPattern().toItemStack(false);
			int matter = MatterHelper.getMatterAmountFromItem(item);
			return machine.getCapability(MatterOverdriveCapabilities.MATTER_HANDLER, null).getMatterStored() >= matter && machine.canReplicateIntoOutput(item) && machine.canReplicateIntoSecoundOutput(matter);
		}
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
	{
		super.readFromNBT(nbt, categories);
		if (categories.contains(MachineNBTCategory.DATA))
		{
			this.replicateTime = nbt.getShort("ReplicateTime");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk)
	{
		super.writeToNBT(nbt, categories, toDisk);
		if (categories.contains(MachineNBTCategory.DATA) && toDisk)
		{
			nbt.setShort("ReplicateTime", (short)this.replicateTime);
		}
	}

	public float getReplicateProgress()
	{
		return replicateProgress;
	}
}
