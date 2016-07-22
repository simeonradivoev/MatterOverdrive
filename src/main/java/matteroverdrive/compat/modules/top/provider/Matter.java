package matteroverdrive.compat.modules.top.provider;

import matteroverdrive.api.matter.IMatterHandler;
import matteroverdrive.init.MatterOverdriveCapabilities;
import matteroverdrive.util.MatterHelper;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author shadowfacts
 */
public class Matter implements IProbeInfoProvider
{

	@Override
	public String getID()
	{
		return "mo:matter";
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState state, IProbeHitData hit)
	{
		TileEntity te = world.getTileEntity(hit.getPos());
		if (te.hasCapability(MatterOverdriveCapabilities.MATTER_HANDLER, hit.getSideHit()))
		{
			IMatterHandler handler = te.getCapability(MatterOverdriveCapabilities.MATTER_HANDLER, hit.getSideHit());
			probeInfo.progress(handler.getMatterStored(), handler.getCapacity(), probeInfo.defaultProgressStyle().suffix(MatterHelper.MATTER_UNIT).filledColor(0xFF5AACBC).alternateFilledColor(0xFF334E78));
		}
	}

}
