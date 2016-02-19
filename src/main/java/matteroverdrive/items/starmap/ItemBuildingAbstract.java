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

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.starmap.IBuilding;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 6/23/2015.
 */
public abstract class ItemBuildingAbstract extends ItemBuildableAbstract implements IBuilding {

    public ItemBuildingAbstract(String name)
    {
        super(name);
        setMaxStackSize(1);
        setCreativeTab(MatterOverdrive.tabMatterOverdrive_buildings);
    }

    public boolean hasDetails(ItemStack stack){return true;}
}
