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

package cofh.api.inventory;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Implement this interface on TileEntities which should connect to item transportation blocks.
 */
public interface IInventoryConnection {

	/**
	 * @param from
	 *            Side to which a connector would connect
	 * @return DEFAULT if the connector should decide how to connect; FORCE if the connector should always connect; DENY if the connector should never connect.
	 */
	public ConnectionType canConnectInventory(ForgeDirection from);

	public static enum ConnectionType {
		DEFAULT, FORCE, DENY;

		public final boolean canConnect = ordinal() != 2;
		public final boolean forceConnect = ordinal() == 1;
	}

}
