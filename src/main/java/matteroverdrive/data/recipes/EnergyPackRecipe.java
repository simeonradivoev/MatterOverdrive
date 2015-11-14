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

import cofh.api.energy.IEnergyContainerItem;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.items.Battery;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Simeon on 8/2/2015.
 */
public class EnergyPackRecipe extends ShapelessRecipes
{
    public EnergyPackRecipe(ItemStack... recipeitems)
    {
        super(new ItemStack(MatterOverdriveItems.energyPack), Arrays.asList(recipeitems));
        for (ItemStack stack : (List<ItemStack>)recipeItems)
        {
            if (stack != null && stack.getItem() instanceof Battery)
            {
                ((Battery) stack.getItem()).setEnergyStored(stack, ((Battery) stack.getItem()).getMaxEnergyStored(stack));
                getRecipeOutput().stackSize = ((Battery) stack.getItem()).getEnergyStored(stack) / MatterOverdriveItems.energyPack.getEnergyAmount(getRecipeOutput());
            }
        }
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting)
    {
        ItemStack stack = getRecipeOutput().copy();
        for (int i = 0;i < inventoryCrafting.getSizeInventory();i++)
        {
            if (inventoryCrafting.getStackInSlot(i) != null && inventoryCrafting.getStackInSlot(i).getItem() instanceof IEnergyContainerItem)
            {
                int energyStored = ((IEnergyContainerItem) inventoryCrafting.getStackInSlot(i).getItem()).getEnergyStored(inventoryCrafting.getStackInSlot(i));
                int packEnergy = MatterOverdriveItems.energyPack.getEnergyAmount(inventoryCrafting.getStackInSlot(i));
                if (energyStored > 0)
                {
                    stack.stackSize = energyStored / packEnergy;
                    return stack;
                }
            }
        }
        return null;
    }
}
