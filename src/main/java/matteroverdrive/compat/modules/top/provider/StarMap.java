package matteroverdrive.compat.modules.top.provider;

import matteroverdrive.tile.TileEntityMachineStarMap;
import matteroverdrive.util.MOStringHelper;
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
public class StarMap implements IProbeInfoProvider
{

	private static final String[] LEVELS = new String[]{"gui.tooltip.page.galaxy", "gui.tooltip.page.quadrant", "gui.tooltip.page.star", "gui.tooltip.page.planet", "gui.tooltip.page.planet_stats"};

	@Override
	public String getID()
	{
		return "mo:starmap";
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hit)
	{
		TileEntity te = world.getTileEntity(hit.getPos());
		if (te instanceof TileEntityMachineStarMap)
		{
			TileEntityMachineStarMap machine = (TileEntityMachineStarMap)te;


			probeInfo.text(String.format("%sCurrent Mode: %s%s (%d)", TextFormatting.YELLOW, TextFormatting.WHITE, MOStringHelper.translateToLocal(LEVELS[machine.getZoomLevel()]), machine.getZoomLevel()));
		}
	}

}
