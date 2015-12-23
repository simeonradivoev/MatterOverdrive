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

package cofh.api.transport;

/**
 * This interface is implemented on Ender Attuned objects which can receive Energy (Redstone Flux).
 *
 * @author King Lemming
 *
 */
public interface IEnderEnergyHandler extends IEnderAttuned {

	/**
	 * Return whether or not the Ender Attuned object can currently send energy (Redstone Flux).
	 */
	boolean canSendEnergy();

	/**
	 * This should be checked to see if the Ender Attuned object can currently receive energy (Redstone Flux).
	 *
	 * Note: In practice, this can (and should) be used to ensure that something does not send to itself.
	 */
	boolean canReceiveEnergy();

	/**
	 * This tells the Ender Attuned object to receive energy. This returns the amount remaining, *not* the amount received - a return of 0 means that all energy
	 * was received!
	 *
	 * @param energy
	 *            Amount of energy to be received.
	 * @param simulate
	 *            If TRUE, the result will only be simulated.
	 * @return Amount of energy that is remaining (or would be remaining, if simulated).
	 */
	int receiveEnergy(int energy, boolean simulate);

}
