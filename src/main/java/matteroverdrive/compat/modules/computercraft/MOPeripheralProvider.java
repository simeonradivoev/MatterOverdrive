package matteroverdrive.compat.modules.computercraft;

import cpw.mods.fml.common.Optional;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import matteroverdrive.tile.TileEntityMachineTransporter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author shadowfacts
 */
@Optional.Interface(modid = "ComputerCraft", iface = "dan200.computercraft.api.peripheral.IPeripheralProvider")
public class MOPeripheralProvider implements IPeripheralProvider {

	@Override
	public IPeripheral getPeripheral(World world, int x, int y, int z, int side) {
		TileEntity te = world.getTileEntity(x, y, z);

		if (te instanceof TileEntityMachineTransporter) {
			return (TileEntityMachineTransporter)te;
		}

		return null;
	}

}
