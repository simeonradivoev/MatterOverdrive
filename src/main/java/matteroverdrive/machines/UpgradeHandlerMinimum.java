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

package matteroverdrive.machines;

import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.api.machines.IUpgradeHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Simeon on 8/23/2015.
 */
public class UpgradeHandlerMinimum implements IUpgradeHandler
{
    double totalMinimum;
    Map<UpgradeTypes,Double> mins;

    public UpgradeHandlerMinimum(double totalMinimum)
    {
        mins = new HashMap<>();
        this.totalMinimum = totalMinimum;
    }

    public UpgradeHandlerMinimum addUpgradeMinimum(UpgradeTypes type,double minimum)
    {
        mins.put(type,minimum);
        return this;
    }

    @Override
    public double affectUpgrade(UpgradeTypes type,double multiply)
    {
        if (mins.containsKey(type))
        {
            multiply = Math.max(multiply,mins.get(type));
        }
        return Math.max(multiply, totalMinimum);
    }
}
