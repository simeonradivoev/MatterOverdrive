package matteroverdrive.compat.modules.top.provider;

import matteroverdrive.compat.modules.top.StackTitleElement;
import matteroverdrive.tile.TileEntityWeaponStation;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author shadowfacts
 */
public class WeaponStation implements IProbeInfoProvider
{

	@Override
	public String getID()
	{
		return "mo:weaponstation";
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData hit)
	{
		TileEntity te = world.getTileEntity(hit.getPos());
		if (te instanceof TileEntityWeaponStation)
		{
			TileEntityWeaponStation machine = (TileEntityWeaponStation)te;

			ItemStack stack = machine.getStackInSlot(machine.INPUT_SLOT);
			if (stack != null)
			{
				probeInfo.element(new StackTitleElement(stack));
			}
		}
	}

}
