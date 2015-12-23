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
 * <b>Internal Only.</b><br>
 * Not to be implemented directly.
 */
public interface IEnderAttuned {

	/**
	 * Returns the channel this IEnderAttuned is operating on.<br>
	 * Typically, this is <code>_public_</code> or the player's profile (not display) name but can be anything.<br>
	 * It is used to separate frequency spectrums.
	 * <p>
	 * Before changing, the IEnderAttuned must be removed from all registries it has been added to.
	 *
	 * @return The channel this IEnderAttuned is operating on.
	 */
	public String getChannelString();

	/**
	 * Returns the frequency this IEnderAttuned is operating on.<br>
	 * Nominally, this value is positive and user-controlled.
	 * <p>
	 * Before changing, the IEnderAttuned must be removed from all registries it has been added to.
	 *
	 * @return The frequency this IEnderAttuned is operating on.
	 */
	public int getFrequency();

	/**
	 * Changes the value returned by <code>getFrequency()</code>
	 *
	 * @return True if the frequency was successfully modified.
	 */
	public boolean setFrequency(int frequency);

	/**
	 * Removes this IEnderAttuned from all registries.<br>
	 * It is recommended that this not be called from <code>setFrequency()</code>.
	 *
	 * @return True if the frequency was successfully modified.
	 */
	public boolean clearFrequency();

}
