package com.MO.MatterOverdrive.compat.modules.waila;

import com.MO.MatterOverdrive.blocks.*;
import com.MO.MatterOverdrive.compat.Compat;
import com.MO.MatterOverdrive.tile.*;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
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
		FMLInterModComms.sendMessage("Waila", "register", "com.MO.MatterOverdrive.compat.modules.waila.CompatWaila.registerCallback");
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
