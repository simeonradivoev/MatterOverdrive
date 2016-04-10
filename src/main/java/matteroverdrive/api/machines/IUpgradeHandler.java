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

package matteroverdrive.api.machines;

import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.machines.UpgradeHandlerGeneric;

/**
 * Created by Simeon on 8/23/2015.
 * Used by machines to affect the upgrade amount from Machine Upgrades.
 * Used mainly to check if speed or some other Machine Stat doesn't reach 0 as in the implementation {@link UpgradeHandlerGeneric}.
 * @see UpgradeHandlerGeneric
 */
public interface IUpgradeHandler
{
	/**
	 * Called when a specified Upgrade Type is changed.
	 * @param type The Upgrade Type being changed.
	 * @param multiply The multiply amount of all the upgrades that change the given Upgrade Type
	 * @return The affected and handled multiply of the original Multiply parameter.
	 */
	double affectUpgrade(UpgradeTypes type, double multiply);
}
