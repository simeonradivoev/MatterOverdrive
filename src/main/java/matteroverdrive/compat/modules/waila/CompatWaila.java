/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.compat.modules.waila;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import matteroverdrive.compat.Compat;

/**
 * Compatibility for WAILA
 *
 * @author shadowfacts
 */
@Compat("Waila")
public class CompatWaila
{

	@Compat.Init
	public static void init(FMLInitializationEvent event)
	{
		FMLInterModComms.sendMessage("Waila", "register", "matteroverdrive.compat.modules.waila.CompatWaila.registerCallback");
	}

	/*public static void registerCallback(IWailaRegistrar registrar)
	{
		registrar.registerBodyProvider(new TileEntityWeaponStation(), BlockWeaponStation.class);
		registrar.registerBodyProvider(new TileEntityMachineStarMap(), BlockStarMap.class);
		registrar.registerBodyProvider(new TileEntityMachineTransporter(), BlockTransporter.class);
		registrar.registerBodyProvider(new TileEntityMachineDecomposer(), BlockDecomposer.class);
		registrar.registerBodyProvider(new TileEntityMachineReplicator(), BlockReplicator.class);
		registrar.registerBodyProvider(new TileEntityMachineFusionReactorController(),BlockFusionReactorController.class);
	}*/
}
