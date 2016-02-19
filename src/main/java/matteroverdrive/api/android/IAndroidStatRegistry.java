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

package matteroverdrive.api.android;

/**
 * Created by Simeon on 7/20/2015.
 */
public interface IAndroidStatRegistry
{
    /**
     * Used to register Bionic stats in the Matter overdrive Registry
     * @param stat the stat itself
     * @return if the stat was registered
     */
    boolean registerStat(IBioticStat stat);

    /**
     * Gets a specific stat by it's name from Matter Overdrive Bionic Stat Registry
     * @param name the name of the Bionic stat to search for
     * @return The Bionic stat. Null if Bionic stat was not found.
     */
    IBioticStat getStat(String name);

    /**
     * Check if a Bionic Stat with a given name exists in the Matter Overdrive Bionic Stat Registry
     * @param name the name of the Bionic Stat
     * @return True if stat exists. False if it does not.
     */
    boolean hasStat(String name);

    /**
     * Unregister (remove) a Bionic stat with a given name from the Matter Overdrive Bionic Stat Registry
     * Original Matter Overdrive Bionic Stats can also be removed
     * @param name the name of the stat
     * @return the stat that was removed. Null if there was no stat with the given name.
     */
    IBioticStat unregisterStat(String name);
}
