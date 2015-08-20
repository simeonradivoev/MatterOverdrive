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

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.handler.MatterEntry;
import matteroverdrive.util.MatterHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

        int passesCount = 8;
        long startTime = System.nanoTime();
        int startEntriesCount = MatterOverdrive.matterRegistry.getEntries().size();
		MatterOverdrive.log.info("Calculation Required! Starting Matter Recipe Calculation !");

        for (int pass = 0;pass < passesCount;pass++)
        {
            long passStartTime = System.nanoTime();
            int passStartRecipeCount = MatterOverdrive.matterRegistry.getEntries().size();

            List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
            MatterOverdrive.log.info("Matter Recipe Calculation Started for %s recipes at pass %s, with %s matter entries",recipes.size(),pass+1,passStartRecipeCount);
            for (IRecipe recipe : recipes) {
                if (recipe == null)
                    continue;

                try {
                    ItemStack itemStack = recipe.getRecipeOutput();
                    if (itemStack != null && !MatterOverdrive.matterRegistry.blacklisted(itemStack) && !MatterOverdrive.matterRegistry.blacklistedFromMod(itemStack)) {
                        if (DEBUG) MatterOverdrive.log.debug("Calculating Recipe for: %s", recipe.getRecipeOutput().getUnlocalizedName());
                        MatterEntry entry = MatterOverdrive.matterRegistry.getEntry(itemStack);
                        int matter = 0;

                        if (entry == null) {
                            matter += MatterOverdrive.matterRegistry.getMatterFromRecipe(itemStack, false, 0, true);

                            if (matter > 0) {
                                MatterEntry e = MatterOverdrive.matterRegistry.register(itemStack, matter);
                                e.setCalculated(true);
                            } else {
                                if (DEBUG)
                                    MatterOverdrive.log.debug("Could not calculate recipe for: %s. Matter from recipe is 0.", recipe.getRecipeOutput().getUnlocalizedName());
                            }
                        } else {
                            if (DEBUG)
                                MatterOverdrive.log.debug("Entry for: %s is already present", recipe.getRecipeOutput().getUnlocalizedName());
                        }
                    }else
                    {
                        if (DEBUG)
                            MatterOverdrive.log.debug("% was blacklisted. Skipping matter calculation", recipe.getRecipeOutput().getUnlocalizedName());
                    }
                } catch (Exception e) {
                    if (recipe.getRecipeOutput() != null) {
                        MatterOverdrive.log.error(String.format("There was a problem calculating matter from recipe for %s", recipe.getRecipeOutput().getUnlocalizedName()), e);
                    } else {
                        MatterOverdrive.log.error("There was a problem calculating matter from recipe", e);
                    }
                }
            }

            MatterOverdrive.log.info("Matter Recipe Calculation for pass %s complete. Took %s milliseconds. Registered %s recipes",pass+1,TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - passStartTime),MatterOverdrive.matterRegistry.getEntries().size() - passStartRecipeCount);
            if (MatterOverdrive.matterRegistry.getEntries().size() - passStartRecipeCount <= 0)
            {
                break;
            }
        }

		MatterOverdrive.log.info("Matter Recipe Calculation, Complete ! Took %s Milliseconds. Registered total of %s items",TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime),MatterOverdrive.matterRegistry.getEntries().size() - startEntriesCount);
        startTime = System.nanoTime();
        startEntriesCount = MatterOverdrive.matterRegistry.getEntries().size();

        MatterOverdrive.log.info("Matter Furnace Calculation Started");
        registerFromFurnace();
        MatterOverdrive.log.info("Matter Furnace Calculation Complete. Took %s Milliseconds. Registered %s entries", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime), MatterOverdrive.matterRegistry.getEntries().size() - startEntriesCount);

        ItemLoop();

        startTime = System.nanoTime();

		MatterOverdrive.log.info("Saving Registry to Disk");
        try
        {
            MatterOverdrive.matterRegistry.saveToFile(savePath);
			MatterOverdrive.log.info("Registry saved at: %s. Took %s Milliseconds.",savePath,TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime));
        }
        catch (IOException e)
        {
			MatterOverdrive.log.log(Level.ERROR,e, "Could not save registry to: %s", savePath);
        }
        MatterOverdrive.matterRegistry.hasComplitedRegistration = true;

    }

    private void ItemLoop()
    {
        int startEntriesCount = MatterOverdrive.matterRegistry.getEntries().size();
        MatterOverdrive.log.info("Matter Fuel Calculation Started");

        Iterator<Item> itemIterator = GameData.getItemRegistry().iterator();
        while (itemIterator.hasNext())
        {
            Item item = itemIterator.next();
            if (item.getHasSubtypes())
            {
                for (int i = 0;i < item.getMaxDamage();i++)
                {
                    ItemStack stack = new ItemStack(item,1,i);
                    onItemLoop(stack);

                }
            }else
            {
                ItemStack stack = new ItemStack(item);
                onItemLoop(stack);
            }
        }

        MatterOverdrive.log.info("Matter Fuel Calculation Complete. Registered %s entries",MatterOverdrive.matterRegistry.getEntries().size() - startEntriesCount);
    }

    private void onItemLoop(ItemStack itemStack)
    {
        float matterPerFuel = (float)MatterOverdrive.matterRegistry.getEntry(Items.coal).getMatter() / (float)GameRegistry.getFuelValue(new ItemStack(Items.coal));
        tryRegisterFuel(itemStack,matterPerFuel);
    }

    private void registerFromFurnace()
    {
        Map<ItemStack,ItemStack> smeltingMap = (Map<ItemStack,ItemStack>)FurnaceRecipes.smelting().getSmeltingList();
        for (Map.Entry<ItemStack,ItemStack> entry : smeltingMap.entrySet())
        {
            int keyMatter = (MatterHelper.getMatterAmountFromItem(entry.getKey()) * entry.getKey().stackSize) / entry.getValue().stackSize;
            int valueMatter = MatterHelper.getMatterAmountFromItem(entry.getValue());
            if (keyMatter > 0 && valueMatter <= 0)
            {
                MatterOverdrive.matterRegistry.register(entry.getValue(),keyMatter);
            }
        }
    }

    private boolean tryRegisterFuel(ItemStack stack,float matterPerFuel)
    {
        int stackMatter = MatterHelper.getMatterAmountFromItem(stack);
        int fuelMatter = Math.round(GameRegistry.getFuelValue(stack) * matterPerFuel);
        if (stackMatter <= 0 && fuelMatter > 0)
        {
            MatterOverdrive.matterRegistry.register(stack,stackMatter);
            return true;
        }
        return false;
    }
}
