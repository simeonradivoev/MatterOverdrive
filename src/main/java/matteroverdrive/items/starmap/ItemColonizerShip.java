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
import matteroverdrive.api.starmap.ShipType;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.starmap.data.Planet;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Simeon on 7/2/2015.
 */
public class ItemColonizerShip extends ItemShipAbstract
{

	public ItemColonizerShip(String name)
	{
		super(name);
	}

	@Override
	public ShipType getType(ItemStack ship)
	{
		return ShipType.COLONIZER;
	}

	@Override
	public void onTravel(ItemStack shipStack, Planet to)
	{
		UUID owner = getOwnerID(shipStack);
		if (owner != null)
		{
			ItemStack base = new ItemStack(MatterOverdriveItems.buildingBase);
			MatterOverdriveItems.buildingBase.setOwner(base, owner);
			if (to.canBuild((IBuilding)base.getItem(), base, new ArrayList<>()))
			{
				shipStack.stackSize = 0;
				to.addBuilding(base);
				to.setOwnerUUID(owner);
			}
		}
	}

	@Override
	public boolean canBuild(ItemStack building, Planet planet, List<String> info)
	{
		return !planet.hasBuildingType(BuildingType.BASE);
	}

	@Override
	public int getBuildLengthUnscaled(ItemStack building, Planet planet)
	{
		return 20 * 250;
	}
}
