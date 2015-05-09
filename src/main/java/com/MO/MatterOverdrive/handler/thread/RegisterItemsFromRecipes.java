package com.MO.MatterOverdrive.handler.thread;

import com.MO.MatterOverdrive.handler.MatterEntry;
import com.MO.MatterOverdrive.handler.MatterRegistry;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 * Created by Simeon on 5/7/2015.
 */
public class RegisterItemsFromRecipes implements Runnable {

    @Override
    public void run() {

        long startTime = System.currentTimeMillis();
        int count = 0;
        FMLLog.info("Starting Matter Recipe Calculation !");


        for(Object recipe : CraftingManager.getInstance().getRecipeList())
        {
            try {
                if (recipe instanceof IRecipe) {
                    ItemStack itemStack = ((IRecipe) recipe).getRecipeOutput();

                    if (itemStack != null) {
                        MatterEntry entry = MatterRegistry.getEntry(itemStack);
                        int matter = 0;

                        if (entry == null) {
                            if (recipe instanceof ShapedRecipes) {
                                matter += MatterRegistry.getMatterFromList(itemStack, ((ShapedRecipes) recipe).recipeItems, true, 0);
                            } else if (recipe instanceof ShapelessRecipes) {
                                matter += MatterRegistry.getMatterFromList(itemStack, ((ShapelessRecipes) recipe).recipeItems.toArray(), true, 0);
                            } else if (recipe instanceof ShapedOreRecipe) {
                                matter += MatterRegistry.getMatterFromList(itemStack, ((ShapedOreRecipe) recipe).getInput(), true, 0);
                            } else if (recipe instanceof ShapelessOreRecipe) {
                                matter += MatterRegistry.getMatterFromList(itemStack,((ShapelessOreRecipe) recipe).getInput().toArray(), true, 0);
                            }

                            if (matter > 0) {
                                MatterRegistry.register(itemStack, matter);
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

        MatterRegistry.hasComplitedRegistration = true;
    }
}
