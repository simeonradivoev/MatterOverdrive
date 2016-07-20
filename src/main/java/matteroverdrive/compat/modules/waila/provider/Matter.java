package matteroverdrive.compat.modules.waila.provider;

import matteroverdrive.api.matter.IMatterHandler;
import matteroverdrive.compat.modules.waila.IWailaBodyProvider;
import matteroverdrive.init.MatterOverdriveCapabilities;
import matteroverdrive.tile.MOTileEntityMachineMatter;
import matteroverdrive.util.MatterHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

/**
 * @author shadowfacts
 */
public class Matter implements IWailaBodyProvider
{
	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		TileEntity te = accessor.getTileEntity();

		if (te instanceof MOTileEntityMachineMatter) {
			MOTileEntityMachineMatter machine = (MOTileEntityMachineMatter)te;
			IMatterHandler storage = machine.getCapability(MatterOverdriveCapabilities.MATTER_HANDLER, null);
			currenttip.add(TextFormatting.AQUA + String.format("%s / %s %s", storage.getMatterStored(), storage.getCapacity(), MatterHelper.MATTER_UNIT));

		} else {
			throw new RuntimeException("MOTileEntityMachineMatter WAILA provider is being used for something that is not a MOTileEntityMachineMatter: " + te.getClass());
		}

		return currenttip;
	}

}
