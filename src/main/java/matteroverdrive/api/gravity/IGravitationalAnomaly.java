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

package matteroverdrive.api.gravity;

import net.minecraft.util.BlockPos;

/**
 * Created by Simeon on 10/14/2015.
 */
public interface IGravitationalAnomaly
{
    BlockPos getPos();

    /**
     * Gets the event horizon.
     * This is usually the point at which the anomaly consumes everything.
     * @return the event horizon radius.
     */
    double getEventHorizon();

    /**
     * Gets the distance at which the anomaly can brake blocks.
     * @return the max block brake distance/radius.
     */
    double getBlockBreakRange();

    /**
     * Gets the maximum affected rage by the anomaly.
     * This includes block destruction range and entity gravitational attraction distance.
     * @return the max affected range by the anomaly.
     */
    double getMaxRange();

    /**
     * Gets the acceleration applied to an object/entity at the given range/distance
     * @param distance this is the distance of the object from the anomaly.
     * @return the acceleration applied to the object at the given distance.
     */
    double getAcceleration(double distance);

    /**
     * Gets the Real mass in matter units.
     * Compared to {@link #getRealMassUnsuppressed()}, this method does not return the unsuppressed mass.
     * This will return the mass at which the anomaly operates at, including the suppression from stabilizers.
     * @return mass of the anomaly, at which it operates.
     */
    double getRealMass();

    /**
     * This method will return the mass of the anomaly without any suppression by stabilisers.
     * To get the suppressed mass use {@link #getRealMass()}.
     * @return the unsuppressed mass of the anomaly
     */
    double getRealMassUnsuppressed();

    /**
     * Gets the block break strength of the anomaly at the given distance and max range.
     * This will tell if the anomaly can brake a block based on it's hardness.
     * @param distance the distance of the block from the anomaly.
     * @param maxRange the maximum block brake range of the anomaly. Use {@link #getBlockBreakRange()} to get the max brake range.
     * @return the break strength of the anomaly.
     */
    float getBreakStrength(float distance,float maxRange);

    /**
     * Gets the overall block break strength of the anomaly.
     * @return the overall block break strength of the anomaly.
     * @deprecated use {@link #getBreakStrength(float, float)} to get the exact break strength at a given distance.
     */
    float getBreakStrength();

    /**
     * Get the Falloff based on the distance and max range of the anomaly
     * Used in conjunction with brake strength
     * @param distance the distance of the object
     * @param maxRange the range (max distance) of the anomaly
     * @return the falloff (1 - 0) in percent
     */
    float getDistanceFalloff(float distance,float maxRange);

    /**
     * Used to collapse the gravitational anomaly.
     * This will remove the anomaly from the world.
     */
    void collapse();

    /**
     * Used to suppress the gravitational anomaly by a given value.
     * @param suppressor Info on the suppressor, including the amount of suppression. Usually ranges from 0 - 1, but higher values can be used to increase the size of the anomaly.
     */
    void suppress(AnomalySuppressor suppressor);
}
