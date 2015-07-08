package matteroverdrive.handler.thread;

import cpw.mods.fml.common.FMLLog;
import matteroverdrive.handler.MatterEntry;
import matteroverdrive.handler.MatterRegistry;
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
                        MatterEntry entry = MatterRegistry.getEntry(itemStack);
                        int matter = 0;

                        if (entry == null) {
                            if (recipe instanceof ShapedRecipes) {
                                matter += MatterRegistry.getMatterFromList(itemStack, ((ShapedRecipes) recipe).recipeItems, true, 0,true);
                            } else if (recipe instanceof ShapelessRecipes) {
                                matter += MatterRegistry.getMatterFromList(itemStack, ((ShapelessRecipes) recipe).recipeItems.toArray(), true, 0,true);
                            } else if (recipe instanceof ShapedOreRecipe) {
                                matter += MatterRegistry.getMatterFromList(itemStack, ((ShapedOreRecipe) recipe).getInput(), true, 0,true);
                            } else if (recipe instanceof ShapelessOreRecipe) {
                                matter += MatterRegistry.getMatterFromList(itemStack,((ShapelessOreRecipe) recipe).getInput().toArray(), true, 0,true);
                            }

                            if (matter > 0) {
                                MatterEntry e = MatterRegistry.register(itemStack, matter);
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
            MatterRegistry.saveToFile(savePath);
            FMLLog.info("Registry saved at: " + savePath + ". Took " + (System.currentTimeMillis() - startTime) + " Milliseconds.");
        }
        catch (IOException e)
        {
            FMLLog.severe("Could not save registry to: " + savePath);
            e.printStackTrace();
        }
        MatterRegistry.hasComplitedRegistration = true;

    }
}
