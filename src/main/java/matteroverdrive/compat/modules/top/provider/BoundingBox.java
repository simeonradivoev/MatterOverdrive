package matteroverdrive.compat.modules.top.provider;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.compat.modules.top.StackTitleElement;
import matteroverdrive.tile.TileEntityBoundingBox;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author shadowfacts
 */
public class BoundingBox implements IProbeInfoProvider
{

	@Override
	public String getID()
	{
		return "mo:boundingbox";
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState state, IProbeHitData hit)
	{
		if (state.getBlock() == MatterOverdrive.blocks.boundingBox)
		{
			TileEntityBoundingBox boundingBox = (TileEntityBoundingBox)world.getTileEntity(hit.getPos());
			probeInfo.element(new StackTitleElement(new ItemStack(boundingBox.getOwnerBlock())));
		}
	}

}
