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

package matteroverdrive.items.starmap;

import matteroverdrive.api.starmap.ShipType;
import matteroverdrive.starmap.data.Planet;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Simeon on 6/24/2015.
 */
public class ItemScoutShip extends ItemShipAbstract
{
    public ItemScoutShip(String name) {
        super(name);
    }

    @Override
    public boolean canBuild(ItemStack building, Planet planet,List<String> info) {
        return true;
    }

    @Override
    public int getBuildLengthUnscaled(ItemStack building, Planet planet) {
        return 20 * 180;
    }

    @Override
    public ShipType getType(ItemStack ship) {
        return ShipType.SCOUT;
    }

    @Override
    public void onTravel(ItemStack stack, Planet to)
    {

    }
}
