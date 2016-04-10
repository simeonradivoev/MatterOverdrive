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

package matteroverdrive.api.starmap;

/**
 * Created by Simeon on 12/19/2015.
 */
public enum PlanetStatType
{
	FLEET_SIZE("fleet_size"),
	BUILDINGS_SIZE("building_size"),
	SHIP_BUILD_SPEED("ship_build_speed"),
	BUILDING_BUILD_SPEED("building_build_speed"),
	MATTER_STORAGE("matter_storage"),
	ENERGY_PRODUCTION("energy_production"),
	POPULATION_COUNT("population_count"),
	HAPPINESS("happiness"),
	MATTER_PRODUCTION("matter_production");

	private final String unlocalizedName;

	PlanetStatType(String unlocalizedName)
	{
		this.unlocalizedName = unlocalizedName;
	}

	public String getUnlocalizedName()
	{
		return unlocalizedName;
	}
}
