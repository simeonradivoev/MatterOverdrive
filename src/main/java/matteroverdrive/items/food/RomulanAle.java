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

package matteroverdrive.items.food;

import cpw.mods.fml.common.registry.GameRegistry;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.entity.player.AndroidPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * @author shadowfacts
 */
public class RomulanAle extends ItemFood {

	public RomulanAle(String name) {
		super(4, 0.6f, false);
		setUnlocalizedName(name);
		setTextureName(Reference.MOD_ID + ":" + name);
		setAlwaysEdible();
	}

	public void register() {
		setCreativeTab(MatterOverdrive.tabMatterOverdrive_food);
		GameRegistry.registerItem(this, getUnlocalizedName().substring(5));
	}

	@Override
	public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player) {
		super.onEaten(itemStack, world, player);

		if (!player.capabilities.isCreativeMode && !world.isRemote) {
			--itemStack.stackSize;
		}


		if (!AndroidPlayer.get(player).isAndroid()) player.addPotionEffect(new PotionEffect(9, 160, 8));

		if (itemStack.stackSize > 0) {
			player.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
			return itemStack;
		} else {
			return new ItemStack(Items.glass_bottle);
		}
	}


	@Override
	public EnumAction getItemUseAction(ItemStack p_77661_1_) {
		return EnumAction.drink;
	}
}
