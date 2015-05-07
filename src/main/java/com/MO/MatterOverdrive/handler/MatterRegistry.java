package com.MO.MatterOverdrive.handler;

import java.util.*;

import cofh.lib.util.helpers.MathHelper;
import com.MO.MatterOverdrive.MatterOverdrive;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class MatterRegistry
{


    private static int MAX_DEPTH = 8;
	private static Map<String,IMatterEntry> entries = Collections.synchronizedMap(new HashMap<String,IMatterEntry>());
    private static Set<String> blacklist = Collections.synchronizedSet(new HashSet<String>());

	public static IMatterEntry register(IMatterEntry entry)
	{
		entries.put(entry.getName(), entry);
		return entry;
	}

    public static void addToBlacklist(ItemStack itemStack) {blacklist.add(getKey(itemStack));}
    public static void addToBlacklist(Item item) {blacklist.add(getKey(item));}
    public static void addToBlacklist(Block block) {blacklist.add(getKey(block));}
    public static boolean blacklisted(Block block){return blacklist.contains(getKey(block));}
    public static boolean blacklisted(Item item){return blacklist.contains(getKey(item));}
    public static boolean blacklisted(ItemStack itemStack)
    {
        if (blacklist.contains(getKey(itemStack)))
        {
            return true;
        }else if (blacklist.contains(getKey(itemStack.getItem())))
        {
            return true;
        }else
        {
            return false;
        }
    }
    public static String getKey(Block block) {return getKey(new ItemStack(block));}
    public static String getKey(Item item) {return getKey(new ItemStack(item));}
    public static String getKey(ItemStack itemStack) {
        try {
            return itemStack.getUnlocalizedName();
        } catch (Exception e)
        {
            if (itemStack.getItem() != null)
            {
                int damage = itemStack.getItemDamage();
                damage = MathHelper.clampI(damage, 0, itemStack.getMaxDamage());
                return itemStack.getItem().getUnlocalizedNameInefficiently(new ItemStack(itemStack.getItem(), 1, damage));
            }
            return null;
        }
    }
    public static IMatterEntry register(Block block,int matter) {
        String key = getKey(block);
        int configMatter = checkInConfig(key);
        if (configMatter > 0)
            return register(new MatterEntry(key, configMatter,(byte)1));
        else
            return register(new MatterEntry(key, matter,(byte)1));

    }
    public static IMatterEntry register(Item item,int matter)
    {
        String key = getKey(item);
        int configMatter = checkInConfig(key);
        if (configMatter > 0)
            return register(new MatterEntry(key, configMatter,(byte)1));
        else
        return register(new MatterEntry(key, matter, (byte) 1));
    }
    public static IMatterEntry register(ItemStack itemStack,int matter)
    {
        String key = getKey(itemStack);
        int configMatter = checkInConfig(key);
        if (configMatter > 0)
            return register(new MatterEntry(key, configMatter,(byte)1));
        else
            return register(new MatterEntry(key, matter, (byte) 2));
    }
    public static IMatterEntry register(String key,int matter)
    {
        int configMatter = checkInConfig(key);
        if (configMatter > 0)
            return register(new MatterEntry(key, configMatter,(byte)1));
        else
            return register(new MatterEntry(key, matter, (byte) 0));
    }
    public static IMatterEntry registerFromRecipe(Item item) {return registerFromRecipe(new ItemStack(item));}
    public static int checkInConfig(String key){
        if (MatterOverdrive.configHandler.config.hasKey(MOConfigurationHandler.OVERRIDE_MATTER_CATEGORY,key))
        {
            return MatterOverdrive.configHandler.config.get(MOConfigurationHandler.OVERRIDE_MATTER_CATEGORY,key,-1).getInt(-1);
        }
        return -1;
    }
    public static IMatterEntry registerFromRecipe(ItemStack item)
    {
        int matter = getMatterFromRecipe(item, false, 0);
        if(matter > 0)
            return register(new MatterEntry(getKey(item),matter,(byte)2));
        else {
            System.out.println("Could not register "+ getKey(item)+" from recipe");
            return null;
        }
    }

    public static IMatterEntry registerFromRecipe(Block block)
    {
        int matter = getMatterFromRecipe(block, false, 0);

        if(matter > 0)
            return register(new MatterEntry(getKey(block),matter,(byte)1));
        else {
            System.out.println("Could not register "+Block.blockRegistry.getNameForObject(block)+" from recipe");
            return null;
        }
    }

	public static IMatterEntry getEntry(Block block) {return getEntry(new ItemStack(block));}

	public static IMatterEntry getEntry(Item item) {return getEntry(new ItemStack(item));}

    public static IMatterEntry getEntry(ItemStack item)
    {
        try {
            if (!blacklist.contains(item.getUnlocalizedName()))
            {
                IMatterEntry e = entries.get(item.getUnlocalizedName());
                if (e == null) {
                    e = getOreDicionaryEntry(item);
                }
                return e;
            }else
            {
                return null;
            }
        }catch (Exception e)
        {
            return null;
        }
    }

    public static IMatterEntry getEntry(String name)
    {
        IMatterEntry e = entries.get(name);

        if(e == null)
        {
            for (ItemStack itemStack : OreDictionary.getOres(name))
            {
                e = entries.get(Item.itemRegistry.getNameForObject(itemStack.getItem()));

                if(e != null)
                    return e;
            }
        }
        return e;
    }

    static IMatterEntry getOreDicionaryEntry(ItemStack stack)
    {
        IMatterEntry e = null;

        int[] ids = OreDictionary.getOreIDs(stack);
        for (int i = 0;i < ids.length;i++)
        {
            String entryName = OreDictionary.getOreName(ids[i]);
            e = entries.get(entryName);

            if(e != null)
                return e;
        }

        return e;
    }

    public static int getMatterFromRecipe(Block block,boolean recursive,int depth)
    {
        return getMatterFromRecipe(new ItemStack(block), recursive, depth);
    }

    public static int getMatterFromRecipe(ShapedRecipes recipe,boolean recursive,int depth)
    {
        return getMatterFromList(recipe.getRecipeOutput(), recipe.recipeItems, recursive, depth);
    }

    public static int getMatterFromRecipe(ShapelessRecipes recipe,boolean recursive,int depth)
    {
        return getMatterFromList(recipe.getRecipeOutput(), recipe.recipeItems.toArray(), recursive,depth);
    }

    public static int getMatterFromRecipe(ItemStack item,boolean recursive,int depth)
    {
        int matter = 0;

        for(Object recipe : CraftingManager.getInstance().getRecipeList())
        {
            if (recipe instanceof IRecipe)
            {
                if (((IRecipe) recipe).getRecipeOutput() != null && ((IRecipe) recipe).getRecipeOutput().isItemEqual(item))
                {
                    if (recipe instanceof ShapedRecipes) {
                        int m = getMatterFromRecipe((ShapedRecipes) recipe,recursive,++depth);
                        if (m <= 0)
                            System.out.println("No matter from shaped recipe for: " + item.getUnlocalizedName());
                        matter += m;
                    } else if (recipe instanceof ShapelessRecipes) {
                        matter += getMatterFromRecipe((ShapelessRecipes) recipe,recursive,++depth);
                    } else if (recipe instanceof ShapedOreRecipe) {

                        ShapedOreRecipe r = (ShapedOreRecipe) recipe;
                        int m = getMatterFromList(r.getRecipeOutput(), r.getInput(),recursive,++depth);
                        matter += m;
                    }else if(recipe instanceof ShapelessOreRecipe)
                    {
                        ShapelessOreRecipe r = (ShapelessOreRecipe) recipe;
                        int m = getMatterFromList(r.getRecipeOutput(), r.getInput().toArray(),recursive,++depth);
                        matter += m;
                    }
                }
            }
        }

        return matter;
    }

    public static void loadNewItemsFromConfig(MOConfigurationHandler c)
    {
        c.load();
        List<Property> category = c.config.getCategory(c.NEW_MATTER_CATEGORY).getOrderedValues();
        for (int i = 0; i < category.size();i++)
        {
            if(category.get(i).isIntValue())
            {
                if (category.get(i).getInt(0) > 0)
                {
                    register(category.get(i).getName(),category.get(i).getInt(0));
                }
            }
        }
    }

    public static void loadBlacklistFromConfig(MOConfigurationHandler c)
    {
        c.load();
        List<Property> category = c.config.getCategory(c.MATTER_BLACKLIST_CATEGORY).getOrderedValues();
        for (int i = 0; i < category.size();i++)
        {
            register(category.get(i).getName(),category.get(i).getInt(0));
        }
    }

    public static int getMatterFromList(ItemStack item, Object[] list,boolean recursive,int depth)
    {
        int matter = 0;

        if (depth < MAX_DEPTH)
        {
            for (Object stack : list) {
                if (stack instanceof Block) {
                    if (blacklisted((Block) stack))
                        return -1;

                    IMatterEntry entry = getEntry((Block) stack);
                    if (entry != null) {
                        matter += entry.getMatter();
                    } else if (recursive) {
                        int m = getMatterFromRecipe((Block) stack, recursive, ++depth);
                        if (m > 0) {
                            register((Item) stack, m);
                            matter += m;
                        }else if (m < 0)
                        {
                            return -1;
                        }else return 0;
                    }
                } else if (stack instanceof Item) {
                    if (blacklisted((Item) stack))
                        return -1;

                    IMatterEntry entry = getEntry((Item) stack);
                    if (entry != null) {
                        matter += entry.getMatter();
                        matter += handleReturns((Item) stack);
                    } else if (recursive) {
                        int m = getMatterFromRecipe(new ItemStack((Item) stack), recursive, ++depth);
                        if (m > 0) {
                            register((Item) stack, m);
                            matter += m;
                        }else if (m < 0)
                        {
                            return -1;
                        }else return 0;
                    }
                } else if (stack instanceof ItemStack) {

                    if (blacklisted((ItemStack) stack))
                        return -1;

                    IMatterEntry entry = getEntry((ItemStack) stack);
                    if (entry != null) {
                        matter += entry.getMatter();
                        matter += handleReturns(((ItemStack) stack).getItem());
                    } else if (recursive) {
                        int m = getMatterFromRecipe((ItemStack) stack, recursive, ++depth);
                        if (m > 0) {
                            register((ItemStack) stack, m);
                            matter += m;
                        }else if (m <= 0)
                        {
                            return -1;
                        }else return 0;
                    }
                } else if (stack instanceof List)
                {
                    int m = getMatterFromList(item, ((List) stack).toArray(), recursive, ++depth);
                    if (m < 0)
                        return -1;
                    matter += m;
                }
            }
        }

        return MathHelper.round((double) matter / (double) item.stackSize);
    }

    static int handleReturns(Item item) {
        if (item == Items.lava_bucket || item == Items.water_bucket || item == Items.milk_bucket) {
            IMatterEntry e = getEntry(Items.bucket);
            if (e != null)
                return -e.getMatter();
            else {
                e = getEntry(item);
                if (e != null)
                    return -e.getMatter();
            }
        }

        return 0;
    }
	
}
