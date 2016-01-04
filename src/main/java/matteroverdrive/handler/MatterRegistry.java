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

package matteroverdrive.handler;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.events.MOEventRegisterMatterEntry;
import matteroverdrive.api.matter.IMatterRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.io.*;
import java.util.*;

public class MatterRegistry implements IMatterRegistry
{
    private boolean REGISTRATION_DEBUG = false;
    public boolean CALCULATION_DEBUG = false;
    public boolean AUTOMATIC_CALCULATION = true;
    public boolean CALCULATE_RECIPES = true;
    public boolean CALCULATE_FURNACE = true;
    public boolean hasComplitedRegistration = false;
    private static final int MAX_DEPTH = 8;
    public int basicEntries = 0;
	private Map<String,MatterEntry> entries = new HashMap<>();
    private Set<String> blacklist = Collections.synchronizedSet(new HashSet<>());
    private Set<String> modBlacklist = Collections.synchronizedSet(new HashSet<String>());

    public void preInit(FMLPreInitializationEvent event,ConfigurationHandler configurationHandler)
    {
        REGISTRATION_DEBUG = configurationHandler.getBool(ConfigurationHandler.KEY_MATTER_REGISTRATION_DEBUG,ConfigurationHandler.CATEGORY_DEBUG,false,"Enables Debug logging for Matter Registration");
        CALCULATION_DEBUG = configurationHandler.getBool(ConfigurationHandler.KEY_MATTER_CALCULATION_DEBUG,ConfigurationHandler.CATEGORY_DEBUG,false,"Enables Debug logging for Matter Calculation");
        CALCULATE_RECIPES = configurationHandler.getBool(ConfigurationHandler.KEY_AUTOMATIC_RECIPE_CALCULATION,ConfigurationHandler.CATEGORY_MATTER,true,"Enables Matter Calculation from recipes");
        CALCULATE_FURNACE = configurationHandler.getBool(ConfigurationHandler.KEY_AUTOMATIC_FURNACE_CALCULATION,configurationHandler.CATEGORY_MATTER,true,"Enables Matter Calculation from furnace recipes");
        AUTOMATIC_CALCULATION = configurationHandler.getBool("automatic_calculation",configurationHandler.CATEGORY_MATTER,true,"Should the matter registry calculation run on world start when recipes, mods or blocks change");
    }

	public MatterEntry register(MatterEntry entry)
	{
        if (!MinecraftForge.EVENT_BUS.post(new MOEventRegisterMatterEntry(entry)))
        {
			debug("Registered: %1$s - %2$s kM", entry.getName(), entry.getMatter());
            entries.put(entry.getName(), entry);
        }
		return entry;
	}

    public void saveToFile(String path) throws IOException {
        File file = new File(path);

        file.getParentFile().mkdirs();
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
        outputStream.writeObject(entries);
        outputStream.writeUTF(Reference.VERSION);
        outputStream.writeInt(CraftingManager.getInstance().getRecipeList().size());
        outputStream.writeInt(basicEntries);
        outputStream.writeInt(blacklist.size());
        outputStream.writeInt(modBlacklist.size());
        outputStream.close();
        fileOutputStream.close();
    }

    public void loadFromFile(String path) throws IOException, ClassNotFoundException {
        File file = new File(path);
        if (file.exists()) {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
            entries = (HashMap<String,MatterEntry>) inputStream.readObject();
            String version = inputStream.readUTF();
            inputStream.close();
            fileInputStream.close();
			MatterOverdrive.log.info("Registry Loaded with %1$s entries, from version %2$s from: %3$s", entries.size(), version, file.getPath());
        }
    }
    public boolean needsCalculation(String path) throws IOException, ClassNotFoundException
    {
        File file = new File(path);
        String reason = "";
        if (file.exists())
        {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
            HashMap<String, MatterEntry> entires = (HashMap<String, MatterEntry>)inputStream.readObject();
            String version = inputStream.readUTF();
            int recipeCount = inputStream.readInt();
            int basicEntries = inputStream.readInt();
            int blackListSize = inputStream.readInt();
            int modBlacklistSize = inputStream.readInt();
            inputStream.close();
            fileInputStream.close();

            //checks if the saved versions differ from the current version of the mod
            //and alos checks if the recipe list count has changed
            if (version.equalsIgnoreCase(Reference.VERSION))
            {
                if (recipeCount == CraftingManager.getInstance().getRecipeList().size())
                {
                    if (basicEntries == this.basicEntries)
                    {
                        if (blackListSize == blacklist.size() && modBlacklistSize == modBlacklist.size()) {

                            for (Map.Entry<String, MatterEntry> entry : entires.entrySet())
                            {
                                if (!entry.getValue().getCalculated()) {
                                    if (entries.containsKey(entry.getKey())) {
                                        if (!entries.get(entry.getKey()).equals(entry.getValue())) {
                                            //if the entry is in the list but it's matter was changed
											MatterOverdrive.log.warn("Matter Registry has changed! %1$s changed from %2$s to %3$s. Recalculation required!", entry.getKey(), entries.get(entry.getKey()), entry.getValue().getMatter());
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
		MatterOverdrive.log.warn(reason + "! Recalculation required!");
        return true;
    }

    @Override
    public void addToBlacklist(ItemStack itemStack) {blacklist.add(getKey(itemStack));}
    @Override
    public void addToBlacklist(String key) {blacklist.add(key);}
    @Override
    public void addToBlacklist(Item item) {blacklist.add(getKey(item));}
    @Override
    public void addToBlacklist(Block block) {blacklist.add(getKey(block));}
    @Override
    public boolean blacklisted(Block block){return blacklist.contains(getKey(block));}
    @Override
    public boolean blacklisted(Item item){return blacklist.contains(getKey(item));}
    @Override
    public boolean blacklisted(ItemStack itemStack)
    {
        return blacklisted(getKey(itemStack.getItem())) || blacklisted(getKey(itemStack));
    }
    @Override
    public boolean blacklisted(String key)
    {
        return blacklist.contains(key);
    }
    public boolean blacklistedFromMod(ItemStack stack)
    {
        Item item = stack.getItem();
        if (item != null)
        {
            return modBlacklist.contains(GameRegistry.findUniqueIdentifierFor(item).modId);
        }
        return false;
    }
    public String getKey(Block block) {return getKey(new ItemStack(block));}
    public String getKey(Item item) {return getKey(new ItemStack(item));}
    public String getKey(ItemStack itemStack) {
        try {
            if (itemStack.getHasSubtypes())
            {
                if (itemStack.getItemDamage() > 0 && itemStack.getItemDamage() < Short.MAX_VALUE)
                {
                    return GameData.getItemRegistry().getNameForObject(itemStack.getItem()) + itemStack.getItemDamage();
                }

                return GameData.getItemRegistry().getNameForObject(itemStack.getItem());
            }else
            {
                return GameData.getItemRegistry().getNameForObject(itemStack.getItem());
            }
        } catch (Exception e)
        {
            if (itemStack.getItem() != null)
            {
                int damage = itemStack.getItemDamage();
                damage = MathHelper.clamp_int(damage, 0, itemStack.getMaxDamage());
                return itemStack.getItem().getUnlocalizedNameInefficiently(new ItemStack(itemStack.getItem(), 1, damage));
            }
            return null;
        }
    }
    public MatterEntry register(Block block,int matter) {
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
    public MatterEntry register(Item item,int matter)
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
    public MatterEntry register(ItemStack itemStack,int matter)
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
    public MatterEntry register(String key,int matter)
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
    public int checkInConfig(String key){
        if (MatterOverdrive.configHandler.config.hasKey(ConfigurationHandler.CATEGORY_OVERRIDE_MATTER, key))
        {
            return MatterOverdrive.configHandler.getInt(key, ConfigurationHandler.CATEGORY_OVERRIDE_MATTER,-1);
        }
        return -1;
    }
    public MatterEntry registerFromRecipe(ItemStack item)
    {
        int matter = getMatterFromRecipe(item, false, 0,true);
        if(matter > 0)
            return register(new MatterEntry(getKey(item),matter,(byte)2));
        else {
            //System.out.println("Could not register "+ getKey(item)+" from recipe");
            return null;
        }
    }

	public MatterEntry getEntry(Block block) {return getEntry(new ItemStack(block));}

	public MatterEntry getEntry(Item item) {return getEntry(new ItemStack(item));}

    public MatterEntry getEntry(ItemStack item)
    {
        try {
            if (!blacklist.contains(getKey(item)))
            {
                MatterEntry e = entries.get(getKey(item));
                if (e == null)
                {
                    if (e == null) debug("Could not find matter entry for: %s", item);
                    e = getOreDicionaryEntry(item);
                    if (e == null) debug("Could not find ore dictionary entry for: %s", item);
                }
                return e;
            }else
            {
                return null;
            }
        }catch (Exception e)
        {
            if (e == null) debug("There was a problem getting a Matter Entry for %s.", item);
            return null;
        }
    }

    public MatterEntry getEntry(String name)
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

    private MatterEntry getOreDicionaryEntry(ItemStack stack)
    {
        MatterEntry e;
        int[] ids = OreDictionary.getOreIDs(stack);

        if (ids.length <= 0)
        {
            if (stack.getItemDamage() == Short.MAX_VALUE)
            {
                debug("Messed up damage for: %s.", stack);
            }else
            {
                debug("No OreDictionary support for: %s", stack);
            }
            return null;
        }
        for (int id : ids)
        {
            String entryName = OreDictionary.getOreName(id);
            debug("Searching for OreDictionary key with name: %s for item: %s", entryName, stack);
            e = entries.get(entryName);

            if(e != null)
                return e;
        }

        return null;
    }

    public int getMatterFromRecipe(Block block,boolean recursive,int depth,boolean calculated)
    {
        return getMatterFromRecipe(new ItemStack(block), recursive, depth,calculated);
    }

    public int getMatterFromRecipe(ItemStack item,boolean recursive,int depth,boolean calculated)
    {
        int matter = 0;

        List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        for(IRecipe recipe : recipes) {
            ItemStack recipeOutput = recipe.getRecipeOutput();

            if (recipeOutput != null && recipeOutput.isItemEqual(item)) {
                int m = 0;

                if (recipe instanceof ShapedRecipes) {
                    m = getMatterFromList(recipeOutput, ((ShapedRecipes) recipe).recipeItems, recursive, ++depth, calculated);
                } else if (recipe instanceof ShapelessRecipes) {
                    m = getMatterFromList(recipeOutput, ((ShapelessRecipes) recipe).recipeItems.toArray(), recursive, ++depth, calculated);
                } else if (recipe instanceof ShapedOreRecipe) {
                    m = getMatterFromList(recipeOutput, ((ShapedOreRecipe) recipe).getInput(), recursive, ++depth, calculated);
                } else if (recipe instanceof ShapelessOreRecipe) {
                    m = getMatterFromList(recipeOutput, ((ShapelessOreRecipe) recipe).getInput().toArray(), recursive, ++depth, calculated);
                }

                matter += m;
            }
        }

        return matter;
    }

    @Override
    public void addModToBlacklist(String modID)
    {
        modBlacklist.add(modID);
    }

    public void loadNewItemsFromConfig(ConfigurationHandler c)
    {
        List<Property> category = c.getCategory(ConfigurationHandler.CATEGORY_NEW_ITEMS).getOrderedValues();
        for (Property key : category)
        {
            int value = key.getInt(0);
            if (value > 0)
            {
                register(key.getName(),value);
                basicEntries++;
            }
        }
    }

    public void loadBlacklistFromConfig(ConfigurationHandler c)
    {
        String[] list = c.getStringList(ConfigurationHandler.CATEGORY_MATTER, ConfigurationHandler.KEY_MBLACKLIST);
        for (String value : list)
        {
            addToBlacklist(value);
        }
    }

    public void loadModBlacklistFromConfig(ConfigurationHandler c)
    {
        String[] list = c.getStringList(ConfigurationHandler.CATEGORY_MATTER, ConfigurationHandler.KEY_BLACKLIST_MODS);
        for (String value : list)
        {
            addModToBlacklist(value);
        }
    }

    public int getMatterFromList(ItemStack item, Object[] list,boolean recursive,int depth,boolean calculated)
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

                if (s instanceof ItemStack || s instanceof Item || s instanceof Block) {

                    //converting them all to a stack, for code simplification
                    ItemStack stack = null;
                    if (s instanceof ItemStack)
                    {
                        stack = (ItemStack)s;
                    }
                    else if (s instanceof Block)
                    {
                        stack = new ItemStack((Block)s);
                    }
                    else if (s instanceof Item)
                    {
                        stack = new ItemStack((Item)s);
                    }

                    if (stack == null || blacklisted(stack) || blacklistedFromMod(stack)) {
                        debug("%s is blacklisted.", item);
                        return -1;
                    }

                    //check to see if the item in the recipe is the same as the output
                    //and if so then do not calculate to save unnecessary lopping
                    if (!ItemStack.areItemStacksEqual(stack,item)) {
                        tempEntry = getEntry(stack);
                        //if there is an entry use it's matter value
                        if (tempEntry != null)
                        {
                            tempMatter = tempEntry.getMatter();
                        }
                        //if there is no entry for item and recursive is true, then continue searching to it's recipe list
                        else if (recursive) {
                            tempMatter = getMatterFromRecipe(stack, true, ++depth,calculated);
                            debug("searching %s in depth: %s", stack, depth - 1);

                            //if the matter is higher than 0 that means the recipe search was successful.
                            //registration now helps to remove it from future checks
                            if (tempMatter > 0) {
                                register(stack, tempMatter).setCalculated(calculated);
                            }
                            else if (tempMatter < 0) {
                                //that means the item had a recipe with a blacklisted item
                                debug("%s has a blacklisted item in it's recipe", stack);
                                return -1;
                            }
                            else
                            {
                                debug("%s cannot be replicated. Contains 0 matter", stack);
                                return 0;
                            }
                        }
                        //if it's not recursive then check if any item in recipe is not replicatable
                        else
                        {
                            debug("%s cannot be replicated. Contains 0 matter", stack);
                            return 0;
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

                    for (Object element : l)
                    {
                        if (element instanceof ItemStack || element instanceof Item || element instanceof Block)
                        {
                            ItemStack stack = null;
                            if (element instanceof ItemStack)
                            {
                                stack = (ItemStack)element;
                            }
                            else if (element instanceof Item)
                            {
                                stack = new ItemStack((Item)element);
                            }else if (element instanceof Block)
                            {
                                stack = new ItemStack((Block)element);
                            }

                            tempEntry = getEntry(stack);
                            if (tempEntry != null)
                            {
                                //if the item has matter, has lower matter than the previous
                                //if the item was first there is no previous so store that amount
                                if ( tempEntry.getMatter() > 0)
                                {
                                    if ((tempEntry.getMatter() < tempMatter || first)) {
                                        tempMatter = tempEntry.getMatter();
                                        first = false;
                                    }
                                }else
                                {
                                    debug("entry for %s, found in recipe for: %s was blacklisted or costs lower then previous", stack, item);
                                }

                            }
                            //here we use the recursion to calculate it's matter from any recipes it has
                            else if (recursive)
                            {
								debug("Could not find Matter entry for %s, found in recipe for: %s", stack, item);
                                int m = getMatterFromRecipe(stack,true,++depth,calculated);
                                //if the item has matter, has lower matter than the previous
                                //if the item was first there is no previous so store that amount
                                if ( m > 0 && (m < tempMatter || first))
                                {
                                    tempMatter = m;
                                    first = false;
                                }else
                                {
                                    debug("entry for %s, found in recipe for: %s was blacklisted or costs lower then previous", stack, item);
                                }
                            }
                        }else
                        {
                            debug("Found another type of object in list ot type: %s", element.getClass().toString());
                        }
                    }

                    //if for same reason the item is invalid, that can't really happen
                    if (tempMatter < 0)
                    {
                        debug("%s is invalid.", item);
                        return -1;
                    }
                    //if all the items in the list have not matter return as empty
                    else if (tempMatter == 0) {
                        debug("%s has no items with matter in recipe.", item);
                        return 0;
                    }
                }
                //if the recipe contains anything other than itemsStacks, Items, Blocks or lists
                //may be used if there are strings to OreDictionary items i don't really know
                else
                {
                    debug("Element in list is unknown type: %s", s);
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
        return (int) Math.round((double) totalMatter / (double) item.stackSize);
    }

    private int handleReturns(Item item) {
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

    public void clearCaluclatedEntries()
    {
        Iterator<Map.Entry<String,MatterEntry>> iter = entries.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String,MatterEntry> entry = iter.next();
            if(entry.getValue().getCalculated()){
                iter.remove();
            }
        }
    }

    public Map<String,MatterEntry> getEntries()
    {
        return entries;
    }

    public void setEntries(Map<String,MatterEntry> entries)
    {
        this.entries = entries;
    }

    public void debug(String debug,Object... params)
    {
        if (REGISTRATION_DEBUG)
        {
            for (int i = 0;i < params.length;i++)
            {
                if (params[i] instanceof ItemStack)
                {
                    try
                    {
                        params[i] = getKey(((ItemStack)params[i]));
                    }
                    catch (Exception e)
                    {
                        MatterOverdrive.log.warn("There was a problem getting ItemStack's name");
                    }
                }
            }
            MatterOverdrive.log.debug(debug,params);
        }
    }
}
