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

package matteroverdrive.enchantment;

import matteroverdrive.api.weapon.IWeapon;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 8/7/2015.
 */
public class EnchantmentOverclock extends Enchantment
{
	public EnchantmentOverclock(Enchantment.Rarity rarity)
	{
		super(rarity, EnumEnchantmentType.ALL, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND});
		setName("matteroverdrive.weapon.damage");
	}

	public boolean canApply(ItemStack itemStack)
	{
		return itemStack.getItem() instanceof IWeapon;
	}

	@Override
	public int getMinLevel()
	{
		return 1;
	}

	@Override
	public int getMaxLevel()
	{
		return 4;
	}
}
