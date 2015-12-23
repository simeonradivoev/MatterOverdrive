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

/**
 * Implement this interface on Tile Entities which have Redstone Control functionality. This means that a tile can be set to ignore redstone entirely, or
 * respond to a low or high redstone state.
 *
 * @author King Lemming
 *
 */
public interface IRedstoneControl extends IRedstoneCache {

	public static enum ControlMode {
		DISABLED(true), LOW(false), HIGH(true);

		private final boolean state;

		private ControlMode(boolean state) {

			this.state = state;
		}

		public boolean isDisabled() {

			return this == DISABLED;
		}

		public boolean isLow() {

			return this == LOW;
		}

		public boolean isHigh() {

			return this == HIGH;
		}

		public boolean getState() {

			return state;
		}

		public static ControlMode stepForward(ControlMode curControl) {

			return curControl == DISABLED ? LOW : curControl == HIGH ? DISABLED : HIGH;
		}

		public static ControlMode stepBackward(ControlMode curControl) {

			return curControl == DISABLED ? HIGH : curControl == HIGH ? LOW : DISABLED;
		}
	}

	void setControl(ControlMode control);

	ControlMode getControl();

}
