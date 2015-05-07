package com.MO.MatterOverdrive.handler.thread;

import com.MO.MatterOverdrive.handler.IMatterEntry;
import com.MO.MatterOverdrive.handler.MatterRegistry;
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
        for(Object recipe : CraftingManager.getInstance().getRecipeList())
        {
            if (recipe instanceof IRecipe)
            {
                ItemStack itemStack = ((IRecipe) recipe).getRecipeOutput();
                if (itemStack != null) {
                    IMatterEntry entry = MatterRegistry.getEntry(itemStack);
                    int matter = 0;

                    if (entry == null)
                    {
                        if (recipe instanceof ShapedRecipes) {
                            matter += MatterRegistry.getMatterFromRecipe((ShapedRecipes) recipe, true, 0);
                        } else if (recipe instanceof ShapelessRecipes) {
                            matter += MatterRegistry.getMatterFromRecipe((ShapelessRecipes) recipe, true, 0);
                        } else if (recipe instanceof ShapedOreRecipe) {

                            ShapedOreRecipe r = (ShapedOreRecipe) recipe;
                            matter += MatterRegistry.getMatterFromList(r.getRecipeOutput(), r.getInput(), true, 0);
                        } else if (recipe instanceof ShapelessOreRecipe) {
                            ShapelessOreRecipe r = (ShapelessOreRecipe) recipe;
                            matter += MatterRegistry.getMatterFromList(r.getRecipeOutput(), r.getInput().toArray(), true, 0);
                        }

                        if (matter > 0)
                        {
                            MatterRegistry.register(itemStack, matter);
                        }
                    }
                }
            }
        }
    }
}
