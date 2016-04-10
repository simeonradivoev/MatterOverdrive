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

import com.mojang.realmsclient.gui.ChatFormatting;
import matteroverdrive.api.starmap.BuildingType;
import matteroverdrive.api.starmap.IPlanetStatChange;
import matteroverdrive.api.starmap.PlanetStatType;
import matteroverdrive.starmap.data.Planet;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by Simeon on 12/20/2015.
 */
public class ItemBuildingShipHangar extends ItemBuildingAbstract implements IPlanetStatChange
{
	private static final int SHIP_SPACES = 2;

	public ItemBuildingShipHangar(String name)
	{
		super(name);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addDetails(ItemStack itemstack, EntityPlayer player, List infos)
	{
		super.addDetails(itemstack, player, infos);
		infos.add(ChatFormatting.GREEN + MOStringHelper.translateToLocal(PlanetStatType.FLEET_SIZE) + ": +" + SHIP_SPACES);
	}

	@Override
	public BuildingType getType(ItemStack building)
	{
		return BuildingType.OTHER;
	}

	@Override
	protected int getBuildLengthUnscaled(ItemStack buildableStack, Planet planet)
	{
		return 20 * 60 * 4;
	}

	@Override
	public boolean canBuild(ItemStack building, Planet planet, List<String> info)
	{
		return true;
	}

	@Override
	public float changeStat(ItemStack stack, Planet planet, PlanetStatType statType, float original)
	{
		switch (statType)
		{
			case FLEET_SIZE:
				return original + SHIP_SPACES;
			default:
				return original;
		}
	}
}
