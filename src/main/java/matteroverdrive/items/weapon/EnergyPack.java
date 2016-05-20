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

package matteroverdrive.items.weapon;

import matteroverdrive.api.inventory.IEnergyPack;
import matteroverdrive.items.includes.MOBaseItem;
import matteroverdrive.util.MOEnergyHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

/**
 * Created by Simeon on 8/2/2015.
 */
public class EnergyPack extends MOBaseItem implements IEnergyPack
{

	public EnergyPack(String name)
	{
		super(name);
	}

	public boolean hasDetails(ItemStack stack)
	{
		return true;
	}

	public void addDetails(ItemStack itemstack, EntityPlayer player, List infos)
	{
		super.addDetails(itemstack, player, infos);
		infos.add(TextFormatting.YELLOW + MOEnergyHelper.formatEnergy(null, getEnergyAmount(itemstack)));
	}

	@Override
	public int getEnergyAmount(ItemStack pack)
	{
		return 32000;
	}
}
