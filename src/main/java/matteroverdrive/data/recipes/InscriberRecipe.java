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

package matteroverdrive.data.recipes;

import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 11/12/2015.
 */
public class InscriberRecipe
{
	private final ItemStack main, sec;
	private final ItemStack recipeOutput;
	private final int energy;
	private final int time;

	public InscriberRecipe(ItemStack main, ItemStack sec, ItemStack recipeOutput, int energy, int time)
	{
		this.main = main;
		this.sec = sec;
		this.recipeOutput = recipeOutput;
		this.energy = energy;
		this.time = time;
	}

	public boolean matches(ItemStack main, ItemStack sec)
	{
		return this.main.isItemEqual(main) && main.stackSize > 0 && this.sec.isItemEqual(sec) && sec.stackSize > 0;
	}

	public ItemStack getCraftingResult(ItemStack main, ItemStack sec)
	{
		return recipeOutput.copy();
	}

	public ItemStack getRecipeOutput()
	{
		return recipeOutput;
	}

	public ItemStack getMain()
	{
		return main;
	}

	public ItemStack getSec()
	{
		return sec;
	}

	public int getEnergy()
	{
		return energy;
	}

	public int getTime()
	{
		return this.time;
	}
}
