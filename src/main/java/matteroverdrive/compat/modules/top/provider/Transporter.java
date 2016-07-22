package matteroverdrive.compat.modules.top.provider;

import matteroverdrive.api.transport.TransportLocation;
import matteroverdrive.machines.transporter.TileEntityMachineTransporter;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

/**
 * @author shadowfacts
 */
public class Transporter implements IProbeInfoProvider
{

	@Override
	public String getID()
	{
		return "mo:transporter";
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hit)
	{
		TileEntity te = world.getTileEntity(hit.getPos());
		if (te instanceof TileEntityMachineTransporter)
		{
			TileEntityMachineTransporter machine = (TileEntityMachineTransporter)te;

			TransportLocation location = machine.getSelectedLocation();

			probeInfo.text(String.format("%sSelected Location: %s%s", TextFormatting.YELLOW, TextFormatting.WHITE, location.name));
			probeInfo.text(String.format("%sDestination Coords: %sX: %d Y: %d Z: %d", TextFormatting.YELLOW, TextFormatting.WHITE, location.pos.getX(), location.pos.getY(), location.pos.getZ()));
		}
	}

}
