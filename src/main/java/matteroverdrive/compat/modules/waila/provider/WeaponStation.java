package matteroverdrive.compat.modules.waila.provider;

import matteroverdrive.compat.modules.waila.IWailaBodyProvider;
import matteroverdrive.tile.TileEntityWeaponStation;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

/**
 * @author shadowfacts
 */
public class WeaponStation implements IWailaBodyProvider
{

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		TileEntity te = accessor.getTileEntity();

		if (te instanceof TileEntityWeaponStation) {
			TileEntityWeaponStation weaponStation = (TileEntityWeaponStation)te;

			if (weaponStation.getStackInSlot(weaponStation.INPUT_SLOT) != null) {
				String name = weaponStation.getStackInSlot(weaponStation.INPUT_SLOT).getDisplayName();
				currenttip.add(TextFormatting.YELLOW + "Current Weapon: " + TextFormatting.WHITE + name);
			}

		} else {
			throw new RuntimeException("Weapon Station WAILA provider is being used for something that is not a Weapon Station: " + te.getClass());
		}

		return currenttip;
	}

}
