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

package cofh.api.tileentity;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Implement this interface on Tile Entities which can have access restrictions.
 *
 * @author King Lemming
 *
 */
public interface ISecurable {

	/**
	 * Enum for Access Modes - Restricted is Friends Only, Private is Owner only.
	 *
	 * @author King Lemming
	 *
	 */
	public static enum AccessMode {
		PUBLIC, RESTRICTED, PRIVATE;

		public boolean isPublic() {

			return this == PUBLIC;
		}

		public boolean isRestricted() {

			return this == RESTRICTED;
		}

		public boolean isPrivate() {

			return this == PRIVATE;
		}

		public static AccessMode stepForward(AccessMode curAccess) {

			return curAccess == PUBLIC ? RESTRICTED : curAccess == PRIVATE ? PUBLIC : PRIVATE;
		}

		public static AccessMode stepBackward(AccessMode curAccess) {

			return curAccess == PUBLIC ? PRIVATE : curAccess == PRIVATE ? RESTRICTED : PUBLIC;
		}
	}

	boolean setAccess(AccessMode access);

	boolean setOwnerName(String name);

	boolean setOwner(GameProfile name);

	AccessMode getAccess();

	String getOwnerName();

	GameProfile getOwner();

	boolean canPlayerAccess(EntityPlayer player);

}
