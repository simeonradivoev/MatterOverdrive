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

import matteroverdrive.api.starmap.BuildingType;
import matteroverdrive.api.starmap.IBuilding;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Simeon on 7/2/2015.
 */
public class ItemBuildingBase extends ItemBuildingAbstract
{
    public ItemBuildingBase(String name) {
        super(name);
    }

    @Override
    public BuildingType getType(ItemStack building) {
        return BuildingType.BASE;
    }

    @Override
    public boolean canBuild(ItemStack building, Planet planet,List<String> info) {
        for (ItemStack buildingStack : planet.getBuildings())
        {
            if (buildingStack.getItem() instanceof IBuilding && ((IBuilding)buildingStack.getItem()).getType(buildingStack) == BuildingType.BASE)
            {
                info.add(MOStringHelper.translateToLocal("gui.tooltip.starmap.has_base"));
                return false;
            }
        }
        return true;
    }

    @Override
    public int getBuildLengthUnscaled(ItemStack building, Planet planet) {
        return 20 * 500;
    }
}
