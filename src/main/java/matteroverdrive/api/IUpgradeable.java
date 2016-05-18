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

package matteroverdrive.api;

import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.machines.IUpgradeHandler;

/**
 * Created by Simeon on 6/12/2015.
 * Implemented by Machines that can be upgraded.
 */
public interface IUpgradeable
{
	/**
	 * Is the machine affected by curtain upgrade type.
	 *
	 * @param type the type of upgrade.
	 * @return can the type affect the machine.
	 */
	boolean isAffectedByUpgrade(UpgradeTypes type);

	/**
	 * Gets the upgrade handler for the machine.
	 * Used to control how upgrades are affected in machine.
	 *
	 * @return the upgrade handler.
	 */
	IUpgradeHandler getUpgradeHandler();
}
