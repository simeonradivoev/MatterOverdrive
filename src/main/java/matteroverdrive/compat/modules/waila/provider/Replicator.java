package matteroverdrive.compat.modules.waila.provider;

import matteroverdrive.api.matter.IMatterHandler;
import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.compat.modules.waila.IWailaBodyProvider;
import matteroverdrive.init.MatterOverdriveCapabilities;
import matteroverdrive.machines.replicator.TileEntityMachineReplicator;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskReplicatePattern;
import matteroverdrive.util.MatterHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

/**
 * @author shadowfacts
 */
public class Replicator implements IWailaBodyProvider
{

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		if (accessor.getTileEntity() instanceof TileEntityMachineReplicator) {

			TileEntityMachineReplicator machine = (TileEntityMachineReplicator)accessor.getTileEntity();

			IMatterHandler storage = machine.getCapability(MatterOverdriveCapabilities.MATTER_HANDLER, null);
			currenttip.add(TextFormatting.AQUA + String.format("%s / %s %s", storage.getMatterStored(), storage.getCapacity(), MatterHelper.MATTER_UNIT));

			MatterNetworkTask task = machine.getTaskQueue(0).peek();

			if (task != null && task instanceof MatterNetworkTaskReplicatePattern) {
				ItemStack pattern = ((MatterNetworkTaskReplicatePattern)task).getPattern().toItemStack(false);
				currenttip.add(TextFormatting.YELLOW + String.format("Replicating %s", pattern.getDisplayName()));
			}
		}
		return currenttip;
	}

}
