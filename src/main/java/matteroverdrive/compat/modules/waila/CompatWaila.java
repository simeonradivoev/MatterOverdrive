package matteroverdrive.compat.modules.waila;

import matteroverdrive.blocks.*;
import matteroverdrive.compat.Compat;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import matteroverdrive.tile.*;
import mcp.mobius.waila.api.IWailaRegistrar;

/**
 * Compatibility for WAILA
 *
 * @author shadowfacts
 */
@Compat("Waila")
public class CompatWaila {

	@Compat.Init
	public static void init(FMLInitializationEvent event) {
		FMLInterModComms.sendMessage("Waila", "register", "matteroverdrive.compat.modules.waila.CompatWaila.registerCallback");
	}

	public static void registerCallback(IWailaRegistrar registrar) {
		registrar.registerBodyProvider(new TileEntityWeaponStation(), BlockWeaponStation.class);
		registrar.registerBodyProvider(new TileEntityMachineStarMap(), BlockStarMap.class);
		registrar.registerBodyProvider(new TileEntityMachineTransporter(), BlockTransporter.class);
		registrar.registerBodyProvider(new TileEntityMachineDecomposer(), BlockDecomposer.class);
		registrar.registerBodyProvider(new TileEntityMachineReplicator(), BlockReplicator.class);
		registrar.registerBodyProvider(new TileEntityMachineFusionReactorController(),BlockFusionReactorController.class);
	}
}
