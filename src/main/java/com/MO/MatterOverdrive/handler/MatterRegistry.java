package com.MO.MatterOverdrive.handler;

import java.io.*;
import java.util.*;

import cofh.lib.util.helpers.MathHelper;
import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLModIdMappingEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.server.FMLServerHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class MatterRegistry
{
    public static boolean hasComplitedRegistration = false;
    private static int MAX_DEPTH = 8;
    public static int basicEntires = 0;
	private static Map<String,MatterEntry> entries = new HashMap<String,MatterEntry>();
    private static Set<String> blacklist = Collections.synchronizedSet(new HashSet<String>());

	public static MatterEntry register(MatterEntry entry)
	{
        System.out.println("Registered: " + entry.getName());
		entries.put(entry.getName(), entry);
		return entry;
	}

    public static void saveToFile(String path) throws IOException {
        File file = new File(path);

        file.getParentFile().mkdirs();
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
        outputStream.writeObject(entries);
        outputStream.writeUTF(Reference.VERSION);
        outputStream.writeInt(CraftingManager.getInstance().getRecipeList().size());
        outputStream.writeInt(basicEntires);
        outputStream.writeInt(blacklist.size());
        outputStream.close();
        fileOutputStream.close();
        System.out.println("RegistrySaved to: " + path);
    }

    public static void loadFromFile(String path) throws IOException, ClassNotFoundException {
        File file = new File(path);
        if (file.exists()) {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
            entries = (HashMap<String,MatterEntry>) inputStream.readObject();
            String version = inputStream.readUTF();
            inputStream.close();
            fileInputStream.close();
            System.out.println("Registry Loaded with " + entries.size() + " entries, from version " + version + " from: " + path);
        }
    }
    public static boolean needsCalculation(String path) throws IOException, ClassNotFoundException
    {
        File file = new File(path);
        String reason = "";
        if (file.exists())
        {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
            HashMap<String, MatterEntry> entires = (HashMap<String, MatterEntry>) inputStream.readObject();
            String version = inputStream.readUTF();
            int recipeCount = inputStream.readInt();
            int basicEntries = inputStream.readInt();
            int blackListSize = inputStream.readInt();
            inputStream.close();
            fileInputStream.close();

            //checks if the saved versions differ from the current version of the mod
            //and alos checks if the recipe list count has changed
            if (version.equalsIgnoreCase(Reference.VERSION))
            {
                if (recipeCount == CraftingManager.getInstance().getRecipeList().size())
                {
                    if (basicEntries == MatterRegistry.basicEntires)
                    {
                        if (blackListSize == blacklist.size()) {

                            for (Map.Entry<String, MatterEntry> entry : entires.entrySet())
                            {
                                if (!entry.getValue().calculated) {
                                    if (MatterRegistry.entries.containsKey(entry.getKey())) {
                                        if (!MatterRegistry.entries.get(entry.getKey()).equals(entry.getValue())) {
                                            //if the entry is in the list but it's matter was changed
                                            System.out.println("Matter Registry has changed! " + entry.getKey() +  " changed from " + MatterRegistry.entries.get(entry.getKey()) + " to " + entry.getValue().getMatter() + ". Recalculation required!");
                                            return true;
                                        }
                                    }
                                }
                            }

                            return false;
                        }else
                        {
                            reason = "Blacklist changed";
                        }
                    }else
                    {
                        reason = "Basic Entries size changed";
                    }
                }else
                {
                    reason = "Recipe List Changed";
                }
            }
        }else
        {
            reason = "Recipe List File missing";
        }

        //if the registry file is missing then calculate
        System.out.println(reason + "! Recalculation required!");
        return true;
    }

    public static void addToBlacklist(ItemStack itemStack) {blacklist.add(getKey(itemStack));}
    public static void addToBlacklist(String key) {blacklist.add(key);}
    public static void addToBlacklist(Item item) {blacklist.add(getKey(item));}
    public static void addToBlacklist(Block block) {blacklist.add(getKey(block));}
    public static boolean blacklisted(Block block){return blacklist.contains(getKey(block));}
    public static boolean blacklisted(Item item){return blacklist.contains(getKey(item));}
    public static boolean blacklisted(ItemStack itemStack)
    {
        if (blacklisted(getKey(itemStack)))
        {
            return true;
        }else if (blacklisted(getKey(itemStack.getItem())))
        {
            return true;
        }else
        {
            return false;
        }
    }
    public static boolean blacklisted(String key)
    {
        return blacklist.contains(key);
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
    public static MatterEntry register(Block block,int matter) {
        if (!blacklisted(block)) {
            String key = getKey(block);
            int configMatter = checkInConfig(key);
            if (configMatter > 0)
                return register(new MatterEntry(key, configMatter, (byte) 1));
            else
                return register(new MatterEntry(key, matter, (byte) 1));
        }
        return null;
    }
    public static MatterEntry register(Item item,int matter)
    {
        if (!blacklisted(item)) {
            String key = getKey(item);
            int configMatter = checkInConfig(key);
            if (configMatter > 0)
                return register(new MatterEntry(key, configMatter, (byte) 1));
            else
                return register(new MatterEntry(key, matter, (byte) 1));
        }
        return null;
    }
    public static MatterEntry register(ItemStack itemStack,int matter)
    {
        if (!blacklisted(itemStack)) {
            String key = getKey(itemStack);
            int configMatter = checkInConfig(key);
            if (configMatter > 0)
                return register(new MatterEntry(key, configMatter, (byte) 1));
            else
                return register(new MatterEntry(key, matter, (byte) 2));
        }
        return null;
    }
    public static MatterEntry register(String key,int matter)
    {
        if (!blacklisted(key)) {
            int configMatter = checkInConfig(key);
            if (configMatter > 0)
                return register(new MatterEntry(key, configMatter, (byte) 1));
            else
                return register(new MatterEntry(key, matter, (byte) 0));
        }
        return null;
    }
    public static MatterEntry registerFromRecipe(Item item) {return registerFromRecipe(new ItemStack(item));}
    public static int checkInConfig(String key){
        if (MatterOverdrive.configHandler.config.hasKey(MOConfigurationHandler.CATEGORY_OVERRIDE_MATTER, key))
        {
            return MatterOverdrive.configHandler.getInt(key,MOConfigurationHandler.CATEGORY_OVERRIDE_MATTER,-1);
        }
        return -1;
    }
    public static MatterEntry registerFromRecipe(ItemStack item)
    {
        int matter = getMatterFromRecipe(item, false, 0,true);
        if(matter > 0)
            return register(new MatterEntry(getKey(item),matter,(byte)2));
        else {
            //System.out.println("Could not register "+ getKey(item)+" from recipe");
            return null;
        }
    }

    public static MatterEntry registerFromRecipe(Block block)
    {
        int matter = getMatterFromRecipe(block, false, 0,true);

        if(matter > 0)
            return register(new MatterEntry(getKey(block),matter,(byte)1));
        else {
            //System.out.println("Could not register "+Block.blockRegistry.getNameForObject(block)+" from recipe");
            return null;
        }
    }

	public static MatterEntry getEntry(Block block) {return getEntry(new ItemStack(block));}

	public static MatterEntry getEntry(Item item) {return getEntry(new ItemStack(item));}

    public static MatterEntry getEntry(ItemStack item)
    {
        try {
            if (!blacklist.contains(item.getUnlocalizedName()))
            {
                MatterEntry e = entries.get(item.getUnlocalizedName());
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

    public static MatterEntry getEntry(String name)
    {
        MatterEntry e = entries.get(name);

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

    static MatterEntry getOreDicionaryEntry(ItemStack stack)
    {
        MatterEntry e = null;

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

    public static int getMatterFromRecipe(Block block,boolean recursive,int depth,boolean calculated)
    {
        return getMatterFromRecipe(new ItemStack(block), recursive, depth,calculated);
    }

    public static int getMatterFromRecipe(ItemStack item,boolean recursive,int depth,boolean calculated)
    {
        int matter = 0;

        for(Object recipeObject : CraftingManager.getInstance().getRecipeList())
        {
            if (recipeObject instanceof IRecipe)
            {
                IRecipe recipe = (IRecipe)recipeObject;
                ItemStack recipeOutput = recipe.getRecipeOutput();

                if (recipeOutput != null && recipeOutput.isItemEqual(item)) {
                    int m = 0;

                    if (recipe instanceof ShapedRecipes) {
                        m = getMatterFromList(recipeOutput, ((ShapedRecipes) recipe).recipeItems, recursive, ++depth,calculated);
                    } else if (recipe instanceof ShapelessRecipes) {
                        m = getMatterFromList(recipeOutput, ((ShapelessRecipes) recipe).recipeItems.toArray(), recursive, ++depth,calculated);
                    } else if (recipe instanceof ShapedOreRecipe) {
                        m = getMatterFromList(recipeOutput, ((ShapedOreRecipe) recipe).getInput(), recursive, ++depth,calculated);
                    } else if (recipe instanceof ShapelessOreRecipe) {
                        m = getMatterFromList(recipeOutput, ((ShapelessOreRecipe) recipe).getInput().toArray(), recursive, ++depth,calculated);
                    }

                    matter += m;
                }
            }
        }

        return matter;
    }

    public static void loadNewItemsFromConfig(MOConfigurationHandler c)
    {
        List<Property> category = c.getCategory(MOConfigurationHandler.CATEGORY_NEW_ITEMS).getOrderedValues();
        for (int i = 0; i < category.size();i++)
        {
            int value = category.get(i).getInt(0);
            if (value > 0)
            {
                register(category.get(i).getName(),value);
                basicEntires++;
            }
        }
    }

    public static void loadBlacklistFromConfig(MOConfigurationHandler c)
    {
        String[] list = c.getStringList(MOConfigurationHandler.CATEGORY_MATTER,MOConfigurationHandler.KEY_MBLACKLIST);
        for (int i = 0; i < list.length;i++)
        {
            addToBlacklist(list[i]);
        }
    }

    public static int getMatterFromList(ItemStack item, Object[] list,boolean recursive,int depth,boolean calculated)
    {
        int totalMatter = 0;
        int tempMatter;
        MatterEntry tempEntry;

        if (depth < MAX_DEPTH)
        {
            for (Object s : list)
            {
                if (s == null)
                    continue;

                //reset temp vars
                tempMatter = 0;
                tempEntry = null;

                if (s instanceof ItemStack || s instanceof Item || s instanceof Block) {

                    //converting them all to a stack, for code simplification
                    ItemStack stack = null;
                    if (s instanceof ItemStack)
                    {
                        stack = (ItemStack)s;
                    }
                    else if (s instanceof Block)
                    {
                        stack = new ItemStack((Item)s);
                    }
                    else if (s instanceof Item)
                    {
                        stack = new ItemStack((Block)s);
                    }

                    if (stack == null || blacklisted(stack)) {
                        //System.out.println(item.getDisplayName() + " is blacklisted.");
                        return -1;
                    }

                    //check to see if the item in the recipe is the same as the output
                    //and if so then do not calculate to save unnecessary lopping
                    if (!ItemStack.areItemStacksEqual(stack,item)) {
                        tempEntry = getEntry(stack);
                        //if there is an entry use it's matter value
                        if (tempEntry != null) {
                            tempMatter = tempEntry.getMatter();
                        }
                        //if there is no entry for item and recursive is true, then continue searching to it's recipe list
                        else if (tempEntry == null && recursive) {
                            tempMatter = getMatterFromRecipe(stack, recursive, ++depth,calculated);

                            //if the matter is higher than 0 that means the recipe search was successful.
                            //registration now helps to remove it from future checks
                            if (tempMatter > 0) {
                                register(stack, tempMatter).setCalculated(calculated);
                            }
                            else if (tempMatter < 0) {
                                //that means the item had a recipe with a blacklisted item
                                //System.out.println(stack.getDisplayName() + " has a blacklisted item in it's recipe");
                                return -1;
                            }
                            else
                            {
                                //System.out.println(stack.getDisplayName() + " cannot be replicated. Contains 0 matter");
                                return 0;
                            }
                        }
                    }
                }
                //this is checking if it's a list of items stack
                //this is different from the recipe list, it's oreDictionary list with items with the same ore name
                //so recursion can't be used because of the devision of the stack size
                //and we want to use the lowest possible matter amount from the list
                else if (s instanceof List)
                {
                    List l = (List)s;
                    //flag for checking if the item in the list was the first to register
                    //for checking the lowest matter amount of the list
                    //not using a index check, because the first item can be empty, and nothing positive is smaller than zero
                    boolean first = true;

                    for (int i = 0;i < l.size();i++)
                    {
                        if (l.get(i) instanceof ItemStack)
                        {
                            ItemStack stack = null;
                            if (l.get(i) instanceof ItemStack)
                            {
                                stack = (ItemStack)l.get(i);
                            }
                            else if (l.get(i) instanceof Item)
                            {
                                stack = new ItemStack((Item)l.get(i));
                            }else if (l.get(i) instanceof Block)
                            {
                                stack = new ItemStack((Block)l.get(i));
                            }

                            tempEntry = getEntry(stack);
                            if (tempEntry != null)
                            {
                                //if the item has matter, has lower matter than the previous
                                //if the item was first there is no previous so store that amount
                                if ( tempEntry.getMatter() > 0 && (tempEntry.getMatter() < tempMatter || first))
                                {
                                    tempMatter = tempEntry.getMatter();
                                    first = false;
                                }

                            }
                            //here we use the recursion to calculate it's matter from any recipes it has
                            else if (tempEntry == null && recursive)
                            {
                                int m = getMatterFromRecipe(stack,recursive,++depth,calculated);
                                //if the item has matter, has lower matter than the previous
                                //if the item was first there is no previous so store that amount
                                if ( m > 0 && (m < tempMatter || first))
                                {
                                    tempMatter = m;
                                    first = false;
                                }
                            }
                        }
                    }

                    //if for same reason the item is invalid, that can't really happen
                    if (tempMatter < 0)
                        return -1;
                    //if all the items in the list have not matter return as empty
                    else if (tempMatter == 0)
                        return 0;
                }
                //if the recipe contains anything other than itemsStacks, Items, Blocks or lists
                //may be used if there are strings to OreDictionary items i don't really know
                else
                {
                    tempEntry = getEntry(s.toString());
                    if (tempEntry != null)
                    {
                        tempMatter = tempEntry.getMatter();
                    }
                }

                //increase the total matter amount
                totalMatter += tempMatter;
            }
        }

        //return the amount divided by the count of the items stack
        //sometimes after the division the result is 0
        return MathHelper.round((double) totalMatter / (double) item.stackSize);
    }

    static int handleReturns(Item item) {
        if (item == Items.lava_bucket || item == Items.water_bucket || item == Items.milk_bucket) {
            MatterEntry e = getEntry(Items.bucket);
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

    public static Map<String,MatterEntry> getEntries()
    {
        return entries;
    }

    public static void setEntries(Map<String,MatterEntry> entries)
    {
        MatterRegistry.entries = entries;
    }
	
}
