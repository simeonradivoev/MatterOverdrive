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

import matteroverdrive.blocks.*;
import matteroverdrive.compat.Compat;
import matteroverdrive.compat.modules.waila.provider.*;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;

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

	public static void registerCallback(IWailaRegistrar registrar)
	{
		registrar.registerBodyProvider(new WeaponStation(), BlockWeaponStation.class);
		registrar.registerBodyProvider(new StarMap(), BlockStarMap.class);
		registrar.registerBodyProvider(new Transporter(), BlockTransporter.class);
		registrar.registerBodyProvider(new Matter(), BlockDecomposer.class);
		registrar.registerBodyProvider(new Replicator(), BlockReplicator.class);
		registrar.registerBodyProvider(new Matter(), BlockFusionReactorController.class);
	}

}
