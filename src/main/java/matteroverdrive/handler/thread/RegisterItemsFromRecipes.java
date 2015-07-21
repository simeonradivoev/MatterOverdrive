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

package matteroverdrive.handler.thread;

import cpw.mods.fml.common.FMLLog;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.handler.MatterEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.io.IOException;

/**
 * Created by Simeon on 5/7/2015.
 */
public class RegisterItemsFromRecipes implements Runnable {

    String savePath;

    public RegisterItemsFromRecipes(String savePath)
    {
        this.savePath = savePath;
    }

    @Override
    public void run() {

        long startTime = System.currentTimeMillis();
        int count = 0;
        FMLLog.info("Calculation Required! Starting Matter Recipe Calculation !");


        for(Object recipe : CraftingManager.getInstance().getRecipeList())
        {
            try {
                if (recipe instanceof IRecipe) {
                    ItemStack itemStack = ((IRecipe) recipe).getRecipeOutput();

                    if (itemStack != null) {
                        MatterEntry entry = MatterOverdrive.matterRegistry.getEntry(itemStack);
                        int matter = 0;

                        if (entry == null) {
                            if (recipe instanceof ShapedRecipes) {
                                matter += MatterOverdrive.matterRegistry.getMatterFromList(itemStack, ((ShapedRecipes) recipe).recipeItems, true, 0,true);
                            } else if (recipe instanceof ShapelessRecipes) {
                                matter += MatterOverdrive.matterRegistry.getMatterFromList(itemStack, ((ShapelessRecipes) recipe).recipeItems.toArray(), true, 0,true);
                            } else if (recipe instanceof ShapedOreRecipe) {
                                matter += MatterOverdrive.matterRegistry.getMatterFromList(itemStack, ((ShapedOreRecipe) recipe).getInput(), true, 0,true);
                            } else if (recipe instanceof ShapelessOreRecipe) {
                                matter += MatterOverdrive.matterRegistry.getMatterFromList(itemStack,((ShapelessOreRecipe) recipe).getInput().toArray(), true, 0,true);
                            }

                            if (matter > 0) {
                                MatterEntry e = MatterOverdrive.matterRegistry.register(itemStack, matter);
                                e.setCalculated(true);
                                count++;
                            }
                        }
                    }
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        FMLLog.info("Matter Recipe Calculation, Complete ! Took " + (System.currentTimeMillis() - startTime) + " Milliseconds. Registered total of " + count + " items");
        startTime = System.currentTimeMillis();
        FMLLog.info("Saving Registry to Disk");
        try
        {
            MatterOverdrive.matterRegistry.saveToFile(savePath);
            FMLLog.info("Registry saved at: " + savePath + ". Took " + (System.currentTimeMillis() - startTime) + " Milliseconds.");
        }
        catch (IOException e)
        {
            FMLLog.severe("Could not save registry to: " + savePath);
            e.printStackTrace();
        }
        MatterOverdrive.matterRegistry.hasComplitedRegistration = true;

    }
}
