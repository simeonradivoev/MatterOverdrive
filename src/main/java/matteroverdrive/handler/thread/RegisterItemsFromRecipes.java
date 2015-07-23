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

import matteroverdrive.MatterOverdrive;
import matteroverdrive.handler.MatterEntry;
import matteroverdrive.util.MOLog;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.util.List;

/**
 * Created by Simeon on 5/7/2015.
 */
public class RegisterItemsFromRecipes implements Runnable {

    String savePath;
    public static final boolean DEBUG = false;

    public RegisterItemsFromRecipes(String savePath)
    {
        this.savePath = savePath;
    }

    @Override
    public void run() {

        long startTime = System.currentTimeMillis();
        int count = 0;
        MOLog.log(Level.INFO, "Calculation Required! Starting Matter Recipe Calculation !");

        List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        for(IRecipe recipe : recipes) {
            if (recipe == null)
                continue;

            try {
                ItemStack itemStack = recipe.getRecipeOutput();
                if (itemStack != null) {
                    if (DEBUG) MOLog.log(Level.DEBUG, "Calculating Recipe for: %s", recipe.getRecipeOutput().getUnlocalizedName());
                    MatterEntry entry = MatterOverdrive.matterRegistry.getEntry(itemStack);
                    int matter = 0;

                    if (entry == null) {
                        matter += MatterOverdrive.matterRegistry.getMatterFromRecipe(itemStack,true,0,true);

                        if (matter > 0) {
                            MatterEntry e = MatterOverdrive.matterRegistry.register(itemStack, matter);
                            e.setCalculated(true);
                            count++;
                        } else {
                            if (DEBUG) MOLog.log(Level.DEBUG, "Could not calculate recipe for: %s", recipe.getRecipeOutput().getUnlocalizedName());
                        }
                    }else
                    {
                        if (DEBUG) MOLog.log(Level.DEBUG, "Entry for: %s is already present", recipe.getRecipeOutput().getUnlocalizedName());
                    }
                }
            } catch (Exception e) {
                if (recipe.getRecipeOutput() != null)
                {
                    MOLog.log(Level.ERROR, e, "There was a problem calculating matter from recipe for %s", recipe.getRecipeOutput().getUnlocalizedName());
                }else
                {
                    MOLog.log(Level.ERROR, e, "There was a problem calculating matter from recipe");
                }
            }
        }

        MOLog.log(Level.INFO, "Matter Recipe Calculation, Complete ! Took " + (System.currentTimeMillis() - startTime) + " Milliseconds. Registered total of " + count + " items");
        startTime = System.currentTimeMillis();
        MOLog.log(Level.INFO,"Saving Registry to Disk");
        try
        {
            MatterOverdrive.matterRegistry.saveToFile(savePath);
            MOLog.log(Level.INFO, "Registry saved at: " + savePath + ". Took " + (System.currentTimeMillis() - startTime) + " Milliseconds.");
        }
        catch (IOException e)
        {
            MOLog.log(Level.ERROR, e, "Could not save registry to: " + savePath);
        }
        MatterOverdrive.matterRegistry.hasComplitedRegistration = true;

    }
}
