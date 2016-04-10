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

package matteroverdrive.init;

import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.world.DimensionalRifts;
import matteroverdrive.world.MOWorldGen;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Simeon on 3/23/2015.
 */
public class MatterOverdriveWorld
{
	public final MOWorldGen worldGen;
	private final DimensionalRifts dimensionalRifts;

	public MatterOverdriveWorld(ConfigurationHandler configurationHandler)
	{
		worldGen = new MOWorldGen(configurationHandler);
		dimensionalRifts = new DimensionalRifts(0.04);
		configurationHandler.subscribe(worldGen);
	}

	public void onWorldTick(TickEvent.WorldTickEvent event)
	{
		if (event.side.equals(Side.SERVER))
		{
			worldGen.manageBuildingGeneration(event);
		}
	}

	public void register()
	{
		GameRegistry.registerWorldGenerator(worldGen, 0);
	}

	public DimensionalRifts getDimensionalRifts()
	{
		return dimensionalRifts;
	}
}
