package com.MO.MatterOverdrive.handler;

import java.util.HashMap;
import java.util.List;

import cofh.lib.util.helpers.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class MatterRegistry
{
	private static HashMap<String,IMatterEntry> entries = new HashMap<String,IMatterEntry>();

	public static IMatterEntry register(IMatterEntry entry)
	{
		entries.put(entry.getName(), entry);
		return entry;
	}

	public static IMatterEntry register(Item item,int matter)
	{
		return register(new MatterEntry(item,matter));
	}

    public static IMatterEntry register(String entry,int matter)
    {
        return register(new MatterEntry(entry,matter));
    }

    public static IMatterEntry register(Block block,int matter)
    {
        return register(new MatterEntry(block,matter));
    }

    public static IMatterEntry registerFromRecipe(Item item)
    {
        int matter = getMatterFromRecipe(item);
        if(matter > 0)
            return register(new MatterEntry(item,matter));
        else {
            System.out.println("Could not register "+Item.itemRegistry.getNameForObject(item)+" from recipe");
            return null;
        }
    }

    public static IMatterEntry registerFromRecipe(Block block)
    {
        int matter = getMatterFromRecipe(block);

        if(matter > 0)
            return register(new MatterEntry(block,getMatterFromRecipe(block)));
        else {
            System.out.println("Could not register "+Block.blockRegistry.getNameForObject(block)+" from recipe");
            return null;
        }
    }

	public static boolean hasEntry(Block block)
	{
		return entries.containsKey(Block.blockRegistry.getNameForObject(block));
	}

	public static boolean hasEntry(Item item)
	{
		return entries.containsKey(Item.itemRegistry.getNameForObject(item));
	}

	public static boolean hasEntry(ItemStack item)
	{
		return hasEntry(item.getItem());
	}

	public static boolean hasEntry(String name)
	{
		return entries.containsKey(name);
	}

	public static IMatterEntry getEntry(Block block)
	{
        IMatterEntry e = entries.getOrDefault(Block.blockRegistry.getNameForObject(block), null);
        if(e == null)
        {
            e = getOreDicionaryEntry(new ItemStack(block));
        }
        return e;
	}

	public static IMatterEntry getEntry(Item item)
	{
        IMatterEntry e = entries.getOrDefault(Item.itemRegistry.getNameForObject(item), null);
        if(e == null)
        {
            e = getOreDicionaryEntry(new ItemStack(item));
        }
        return e;
	}

    static IMatterEntry getOreDicionaryEntry(ItemStack stack)
    {
        IMatterEntry e = null;

        int[] ids = OreDictionary.getOreIDs(stack);
        for (int i = 0;i < ids.length;i++)
        {
            String entryName = OreDictionary.getOreName(ids[0]);
            e = entries.getOrDefault(entryName, null);

            if(e != null)
                return e;
        }

        return e;
    }

	public static IMatterEntry getEntry(ItemStack item)
	{
        if(item != null)
		    return getEntry(item.getItem());
        return null;
	}

	public static IMatterEntry getEntry(String name)
	{
		IMatterEntry e = entries.getOrDefault(name, null);

        if(e == null)
        {
            for (ItemStack itemStack : OreDictionary.getOres(name))
            {
                e = entries.getOrDefault(Item.itemRegistry.getNameForObject(itemStack.getItem()), null);

                if(e != null)
                    return e;
            }
        }
        return e;
	}

    public static int getMatterFromRecipe(Block block)
    {
        return getMatterFromRecipe(Item.getItemFromBlock(block));
    }

    public static int getMatterFromRecipe(Item item)
    {
        int matter = 0;

        for(Object recipe : CraftingManager.getInstance().getRecipeList())
        {
            if (recipe instanceof IRecipe)
            {
                if (((IRecipe) recipe).getRecipeOutput() != null && ((IRecipe) recipe).getRecipeOutput().getItem() == item)
                {
                    if (recipe instanceof ShapedRecipes) {
                        int m = getMatterFromRecipe((ShapedRecipes) recipe);
                        if (m <= 0)
                            System.out.println("No matter from shaped recipe for: " + Item.itemRegistry.getNameForObject(item));
                        matter += m;
                    } else if (recipe instanceof ShapelessRecipes) {
                        matter += getMatterFromRecipe((ShapelessRecipes) recipe);
                    } else if (recipe instanceof ShapedOreRecipe) {

                        ShapedOreRecipe r = (ShapedOreRecipe) recipe;
                        int m = getMatterFromList(r.getRecipeOutput(), r.getInput());
                        matter += m;
                    }else if(recipe instanceof ShapelessOreRecipe)
                    {
                        ShapelessOreRecipe r = (ShapelessOreRecipe) recipe;
                        int m = getMatterFromList(r.getRecipeOutput(), r.getInput().toArray());
                        matter += m;
                    }
                }
            }
        }

        return matter;
    }

    public static int getMatterFromRecipe(ShapedRecipes recipe)
    {
        return getMatterFromList(recipe.getRecipeOutput(),recipe.recipeItems);
    }

    public static int getMatterFromRecipe(ShapelessRecipes recipe)
    {
        return getMatterFromList(recipe.getRecipeOutput(),recipe.recipeItems.toArray());
    }

    public static int getMatterFromList(ItemStack item, Object[] list)
    {
        int matter = 0;

        for (Object stack : list)
        {
            if(stack instanceof Block)
            {
                IMatterEntry entry = getEntry((Block)stack);
                if (entry != null) {
                    matter += entry.getMatter();
                }
            }else if(stack instanceof Item || stack instanceof ItemStack)
            {
                Item i;
                if(stack instanceof ItemStack)
                    i = ((ItemStack) stack).getItem();
                else
                    i = (Item)stack;


                IMatterEntry entry = getEntry(i);
                if (entry != null)
                {
                    matter += entry.getMatter();
                    matter += handleReturns(i);
                }
            }else if(stack instanceof List)
            {
                matter += getMatterFromList(item,((List) stack).toArray());
            }
        }

        return MathHelper.round((double) matter / (double) item.stackSize);
    }

    static int handleReturns(Item item)
    {
        if(item == Items.lava_bucket || item == Items.water_bucket || item == Items.milk_bucket)
        {
            IMatterEntry e = getEntry(Items.bucket);
            if(e != null)
                return -e.getMatter();
            else
            {
                e = getEntry(item);
                if(e != null)
                    return -e.getMatter();
            }
        }

        return 0;
    }
	
}
