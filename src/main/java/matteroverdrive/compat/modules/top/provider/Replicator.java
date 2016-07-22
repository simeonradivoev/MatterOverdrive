package matteroverdrive.compat.modules.top.provider;

import matteroverdrive.api.matter.IMatterHandler;
import matteroverdrive.api.network.MatterNetworkTask;
import matteroverdrive.machines.replicator.TileEntityMachineReplicator;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskReplicatePattern;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

/**
 * @author shadowfacts
 */
public class Replicator implements IProbeInfoProvider
{

	@Override
	public String getID()
	{
		return "mo:replicator";
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hit)
	{
		TileEntity te = world.getTileEntity(hit.getPos());
		if (te instanceof TileEntityMachineReplicator)
		{
			TileEntityMachineReplicator machine = (TileEntityMachineReplicator)te;

			MatterNetworkTask task = machine.getTaskQueue(0).peek();

			if (task != null && task instanceof MatterNetworkTaskReplicatePattern)
			{
				ItemStack pattern = ((MatterNetworkTaskReplicatePattern)task).getPattern().toItemStack(false);
				probeInfo.text(TextFormatting.YELLOW + "Replicating " + pattern.getDisplayName());
			}

		}
	}

}
