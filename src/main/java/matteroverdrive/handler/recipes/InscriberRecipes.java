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

package matteroverdrive.handler.recipes;

import matteroverdrive.data.recipes.InscriberRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 11/12/2015.
 */
public class InscriberRecipes
{
    private static List<InscriberRecipe> recipes = new ArrayList<>();

    public static void registerRecipe(InscriberRecipe recipe)
    {
        recipes.add(recipe);
    }

    public static InscriberRecipe getRecipe(ItemStack main,ItemStack sec)
    {
        for (InscriberRecipe recipe : recipes)
        {
            if (recipe.matches(main,sec))
            {
                return recipe;
            }
        }
        return null;
    }

    public static boolean containedInRecipe(ItemStack itemStack,boolean main)
    {
        if (itemStack != null) {
            ItemStack other;
            for (InscriberRecipe recipe : recipes) {
                if (main) {
                    other = recipe.getMain();
                } else {
                    other = recipe.getSec();
                }
                if (other != null)
                {
                    if (itemStack.getItem() == other.getItem() && itemStack.getItemDamage() == other.getItemDamage())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static List<InscriberRecipe> getRecipes()
    {
        return recipes;
    }
}
