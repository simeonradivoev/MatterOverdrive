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

package matteroverdrive.handler.village;

import matteroverdrive.init.MatterOverdriveItems;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import java.util.Random;

/**
 * Created by Simeon on 5/30/2015.
 */
public class TradeHandlerMadScientist //implements VillagerRegistry.VillagerProfession
{
    //@Override
    public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random)
    {
        if (villager.getProfession() == 666)
        {
            addSellPart(recipeList, random, 0, 0.5f, 16 * 2, 8 * 2);
            addSellPart(recipeList, random, 1, 0.4f, 18 * 2, 10 * 2);
            addSellPart(recipeList, random, 2, 0.3f, 20 * 2, 12 * 2);
            addSellPart(recipeList, random, 3, 0.2f, 32 * 2, 8 * 2);

            addSellItemStack(recipeList,random,new ItemStack(MatterOverdriveItems.androidPill,1,1),1,16,16);
            addSellItemStack(recipeList,random,new ItemStack(MatterOverdriveItems.androidPill,1,2),1,16,16);
            addSellItemStack(recipeList, random, new ItemStack(MatterOverdriveItems.h_compensator), 0.5f, 32, 18);
            addBuyItemStack(recipeList, random, new ItemStack(MatterOverdriveItems.dilithium_ctystal), 1, 8, 8);
            addSellItemStack(recipeList, random, new ItemStack(MatterOverdriveItems.weapon_module_barrel, 1, 0), 1, 8, 4);
            addSellItemStack(recipeList, random, new ItemStack(MatterOverdriveItems.weapon_module_barrel, 1, 1), 1, 8, 4);
            addSellItemStack(recipeList, random, new ItemStack(MatterOverdriveItems.weapon_module_barrel, 1, 2), 1, 16, 16);
            addSellItemStack(recipeList, random, new ItemStack(MatterOverdriveItems.earl_gray_tea), 1, 3, 2);
        }
    }

    public void addBuyPart(MerchantRecipeList recipeList,Random random,int part,float chance,int price,int variation)
    {
         if (random.nextFloat() < chance)
         {
             MerchantRecipe recipe = new MerchantRecipe(new ItemStack(MatterOverdriveItems.androidParts,1,part),null,new ItemStack(Items.emerald,price + random.nextInt(variation)));
             recipeList.add(recipe);
         }
    }

    public void addSellPart(MerchantRecipeList recipeList,Random random,int part,float chance,int price,int variation)
    {
        if (random.nextFloat() < chance)
        {
            MerchantRecipe recipe = new MerchantRecipe(new ItemStack(Items.emerald,price + random.nextInt(variation)),null,new ItemStack(MatterOverdriveItems.androidParts,1,part));
            recipeList.add(recipe);
        }
    }

    public void addBuyItemStack(MerchantRecipeList recipeList,Random random,ItemStack stack,float chance,int price,int variation)
    {
        if (random.nextFloat() < chance)
        {
            MerchantRecipe recipe = new MerchantRecipe(stack,null,new ItemStack(Items.emerald,price + random.nextInt(variation)));
            recipeList.add(recipe);
        }
    }

    public void addSellItemStack(MerchantRecipeList recipeList,Random random,ItemStack stack,float chance,int price,int variation)
    {
        if (random.nextFloat() < chance)
        {
            MerchantRecipe recipe = new MerchantRecipe(new ItemStack(Items.emerald,price + random.nextInt(variation)),null,stack);
            recipeList.add(recipe);
        }
    }
}
