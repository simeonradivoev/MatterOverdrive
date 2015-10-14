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

package matteroverdrive.api.matter;

/**
 * Created by Simeon on 7/21/2015.
 * All matter values for items stored in instances of this class.
 * Used in the {@link IMatterRegistry} to store matter values on items.
 */
public interface IMatterEntry
{
    /**
     * The amount of matter the entry is composed of.
     * @return The matter amount of the entry.
     */
    int getMatter();

    /**
     * Sets the matter amount of the entry.
     * @param matter
     */
    void setMatter(int matter);

    /**
     * Gets the name (key) of the matter entry.
     * @return
     */
    String getName();

    /**
     * Is the matter entry calculated or added by configs or by Matter overdrive.
     * Used to show if the matter entry was created by the automatic recipe calculation process.
     * @return
     */
    boolean getCalculated();
}
