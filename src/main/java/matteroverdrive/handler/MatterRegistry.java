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

import com.google.gson.Gson;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.events.MOEventRegisterMatterEntry;
import matteroverdrive.api.exceptions.MORuntimeException;
import matteroverdrive.api.matter.IMatterEntry;
import matteroverdrive.api.matter.IMatterRegistry;
import matteroverdrive.data.matter.*;
import matteroverdrive.util.MOLog;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.*;

public class MatterRegistry implements IMatterRegistry
{
	private static final int MAX_DEPTH = 8;
	private final Set<String> modBlacklist = Collections.synchronizedSet(new HashSet<>());
	public boolean CALCULATION_DEBUG = false;
	public boolean AUTOMATIC_CALCULATION = true;
	public boolean CALCULATE_RECIPES = true;
	public boolean CALCULATE_FURNACE = true;
	public boolean hasComplitedRegistration = false;
	public int basicEntries = 0;
	private boolean REGISTRATION_DEBUG = false;
	private Map<Item, MatterEntryItem> itemEntires = new HashMap<>();
	private Map<Block, MatterEntryBlock> blockEntires = new HashMap<>();
	private Map<String, MatterEntryOre> oreEntries = new HashMap<>();

	public void preInit(final FMLPreInitializationEvent event, final ConfigurationHandler configurationHandler)
	{
		REGISTRATION_DEBUG = configurationHandler.getBool(ConfigurationHandler.KEY_MATTER_REGISTRATION_DEBUG, ConfigurationHandler.CATEGORY_DEBUG, false, "Enables Debug logging for Matter Registration");
		CALCULATION_DEBUG = configurationHandler.getBool(ConfigurationHandler.KEY_MATTER_CALCULATION_DEBUG, ConfigurationHandler.CATEGORY_DEBUG, false, "Enables Debug logging for Matter Calculation");
		CALCULATE_RECIPES = configurationHandler.getBool(ConfigurationHandler.KEY_AUTOMATIC_RECIPE_CALCULATION, ConfigurationHandler.CATEGORY_MATTER, true, "Enables Matter Calculation from recipes");
		CALCULATE_FURNACE = configurationHandler.getBool(ConfigurationHandler.KEY_AUTOMATIC_FURNACE_CALCULATION, ConfigurationHandler.CATEGORY_MATTER, true, "Enables Matter Calculation from furnace recipes");
		AUTOMATIC_CALCULATION = configurationHandler.getBool("automatic_calculation", ConfigurationHandler.CATEGORY_MATTER, true, "Should the matter registry calculation run on world start when recepie ");
	}

	public void saveToFile(final File file) throws Exception
	{
		file.getParentFile().mkdirs();
		file.createNewFile();
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		NBTTagCompound tagCompound = new NBTTagCompound();
		saveListToNBT("Items", tagCompound, itemEntires.values());
		saveListToNBT("Ores", tagCompound, oreEntries.values());
		tagCompound.setString("Version", Reference.VERSION);
		tagCompound.setInteger("RecipeCount", CraftingManager.getInstance().getRecipeList().size());
		tagCompound.setInteger("BasicEntriesCount", basicEntries);
		tagCompound.setInteger("ModBlacklistCount", modBlacklist.size());
		CompressedStreamTools.writeCompressed(tagCompound, fileOutputStream);
		fileOutputStream.close();
	}

	private void saveListToNBT(final String listName, final NBTTagCompound mainTag, final Collection<? extends MatterEntryAbstract> list)
	{
		NBTTagCompound itemEntryList = new NBTTagCompound();
		mainTag.setTag(listName, itemEntryList);
		for (MatterEntryAbstract entry : list)
		{
			if (entry.hasCached())
			{
				NBTTagCompound itemEntryTag = new NBTTagCompound();
				entry.writeTo(itemEntryTag);
				itemEntryList.setTag(entry.writeKey(), itemEntryTag);
			}
		}
	}

	public void loadFromFile(final File file) throws IOException, ClassNotFoundException
	{
		if (file.exists())
		{
			FileInputStream fileInputStream = new FileInputStream(file);
			NBTTagCompound tagCompound = CompressedStreamTools.readCompressed(fileInputStream);
			NBTTagCompound itemsList = tagCompound.getCompoundTag("Items");
			for (String key : itemsList.getKeySet())
			{
				MatterEntryItem item = new MatterEntryItem();
				item.readKey(key);
				MatterEntryItem existingEntry = this.itemEntires.get(item.getKey());
				if (existingEntry != null)
				{
					existingEntry.readFrom(itemsList.getCompoundTag(key));
				}
				else
				{
					item.readFrom(itemsList.getCompoundTag(key));
					this.itemEntires.put(item.getKey(), item);
				}
			}
			NBTTagCompound oresList = tagCompound.getCompoundTag("Ores");
			for (String key : oresList.getKeySet())
			{
				MatterEntryOre entry = new MatterEntryOre();
				entry.readKey(key);
				MatterEntryOre existingEntry = this.oreEntries.get(entry.getKey());
				if (existingEntry != null)
				{
					existingEntry.readFrom(oresList.getCompoundTag(key));
				}
				else
				{
					entry.readFrom(oresList.getCompoundTag(key));
					this.oreEntries.put(entry.getKey(), entry);
				}
			}
			String version = tagCompound.getString("Version");
			fileInputStream.close();
			MOLog.info("Registry Loaded %1$s cached itemEntires, from version %2$s from: %3$s", itemsList.getKeySet().size() + oresList.getKeySet().size(), version, file.getPath());
		}
	}

	public void loadCustomHandlers(final File file) throws FileNotFoundException
	{
		if (file.exists())
		{
			Gson gson = new Gson();
			FileReader fileReader = new FileReader(file);
			CusomHandlerListJSON list = gson.fromJson(fileReader, CusomHandlerListJSON.class);
			for (Map.Entry<String, CustomHandlerJSON> customHandlerJSON : list.items.entrySet())
			{
				Item item = Item.getByNameOrId(customHandlerJSON.getKey());

				if (item != null)
				{
					if (customHandlerJSON.getValue().hasMeta)
					{
						if (item != null)
						{
							for (int i = 0; i < customHandlerJSON.getValue().meta.length; i++)
							{
								ItemStackHandlerCachable cachable = new ItemStackHandlerCachable(customHandlerJSON.getValue().matter, customHandlerJSON.getValue().meta[i]);
								cachable.markCustom();
								register(item, cachable);
							}
						}
					}
					else
					{
						ItemStackHandlerCachable cachable = new ItemStackHandlerCachable(customHandlerJSON.getValue().matter);
						cachable.markCustom();
						if (item != null)
						{
							register(item, cachable);
						}
					}
				}
				else
				{
					MOLog.error("Trying to load a Matter Registry Custom Handler with invalid item ID: %s", customHandlerJSON.getKey());
					MatterOverdrive.proxy.getGoogleAnalytics().setExceptionHit("Trying to load Custom Matter Handler");
				}
			}
		}
	}

	public void unload()
	{
		itemEntires.values().forEach(MatterEntryItem::clearAllCashed);
		oreEntries.values().forEach(MatterEntryOre::clearAllCashed);
	}

	public boolean needsCalculation(final File file) throws IOException, ClassNotFoundException
	{
		String reason = "";
		if (file.exists())
		{
			FileInputStream fileInputStream = new FileInputStream(file);
			NBTTagCompound tagCompound = CompressedStreamTools.readCompressed(fileInputStream);
			String version = tagCompound.getString("Version");
			int recipeCount = tagCompound.getInteger("RecipeCount");
			int basicEntries = tagCompound.getInteger("BasicEntriesCount");
			fileInputStream.close();

			//checks if the saved versions differ from the current version of the mod
			//and alos checks if the recipe list count has changed
			if (version.equalsIgnoreCase(Reference.VERSION))
			{
				if (recipeCount == CraftingManager.getInstance().getRecipeList().size())
				{
					if (basicEntries != this.basicEntries)
					{
						reason = "Basic Entries size changed";
					}
					else
					{
						return false;
					}
				}
				else
				{
					reason = "Recipe List Changed";
				}
			}
		}
		else
		{
			reason = "Matter Registry File missing";
		}

		//if the registry file is missing then calculate
		MOLog.warn(reason + "! Recalculation required!");
		return true;
	}

	public boolean blacklistedFromMod(final ItemStack stack)
	{
		Item item = stack.getItem();
		if (item != null)
		{
			//todo find how to get mod for specific item
			return modBlacklist.contains(item.getRegistryName().getResourceDomain());
		}
		return false;
	}

	public IMatterEntry getEntry(final ItemStack item)
	{
		try
		{
			IMatterEntry e = itemEntires.get(item.getItem());
			if (e == null)
			{

				e = getOreDicionaryEntry(item);

			}
			return e;
		}
		catch (Exception e)
		{
			if (e == null)
			{
				debug("There was a problem getting a Matter Entry for %s.", item);
			}
			return null;
		}
	}

	@Override
	public int getMatter(final ItemStack itemStack)
	{
		IMatterEntry entry = getEntry(itemStack);
		int matter = 0;
		if (entry != null)
		{
			matter = entry.getMatter(itemStack);
		}
		else
		{
			debug("Could not find matter entry for: %s", itemStack);
		}

		if (matter <= 0)
		{
			entry = getOreDicionaryEntry(itemStack);
			if (entry != null)
			{
				matter = entry.getMatter(itemStack);
			}
			else
			{
				debug("Could not find ore dictionary entry for: %s", itemStack);
			}
		}
		return matter;
	}

	@Override
	public int getMatterOre(final String ore)
	{
		IMatterEntry entry = oreEntries.get(ore);
		if (entry != null)
		{
			return entry.getMatter(null);
		}
		return 0;
	}

	//region Registration


	@Override
	public IMatterEntry register(final @Nonnull Item item, final IMatterEntryHandler handler)
	{
		if (item != null)
		{
			IMatterEntry existingEntry = itemEntires.get(item);
			if (existingEntry != null)
			{
				existingEntry.addHandler(handler);
				return existingEntry;
			}
			else
			{
				MatterEntryItem entry = new MatterEntryItem(item);
				entry.addHandler(handler);

				if (!MinecraftForge.EVENT_BUS.post(new MOEventRegisterMatterEntry(entry)))
				{
					//debug("Registered: %1$s - %2$s kM", entry.getSpaceBodyName(), entry.getMatter());
					itemEntires.put(entry.getKey(), entry);
				}
				return entry;
			}
		}
		else
		{
			throw new MORuntimeException("Matter Registry Item Key cannot be NULL");
		}
	}

	@Override
	public IMatterEntry registerOre(final String ore, final IMatterEntryHandler handler)
	{
		IMatterEntry existingEntry = oreEntries.get(ore);
		if (existingEntry != null)
		{
			existingEntry.addHandler(handler);
		}
		else
		{
			MatterEntryOre matterEntryOre = new MatterEntryOre(ore);
			matterEntryOre.addHandler(handler);
			if (!MinecraftForge.EVENT_BUS.post(new MOEventRegisterMatterEntry(matterEntryOre)))
			{
				//debug("Registered: %1$s - %2$s kM", entry.getSpaceBodyName(), entry.getMatter());
				oreEntries.put(ore, matterEntryOre);
			}
			return matterEntryOre;
		}
		return null;
	}
	//endregion

	private IMatterEntry getOreDicionaryEntry(final ItemStack stack)
	{
		IMatterEntry e;
		int[] ids = OreDictionary.getOreIDs(stack);

		if (ids.length <= 0)
		{
			if (stack.getItemDamage() == Short.MAX_VALUE)
			{
				debug("Messed up damage for: %s.", stack);
			}
			else
			{
				debug("No OreDictionary support for: %s", stack);
			}
			return null;
		}
		for (int id : ids)
		{
			String entryName = OreDictionary.getOreName(id);
			debug("Searching for OreDictionary key with name: %s for item: %s", entryName, stack);
			e = oreEntries.get(entryName);

			if (e != null)
			{
				return e;
			}
		}

		return null;
	}

	public int getMatterFromRecipe(final ItemStack item)
	{
		int matter = 0;

		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		for (IRecipe recipe : recipes)
		{
			ItemStack recipeOutput = recipe.getRecipeOutput();

			if (recipeOutput != null && recipeOutput.isItemEqual(item))
			{
				int m = 0;

				if (recipe instanceof ShapedRecipes)
				{
					m = getMatterFromList(recipeOutput, ((ShapedRecipes)recipe).recipeItems);
				}
				else if (recipe instanceof ShapelessRecipes)
				{
					m = getMatterFromList(recipeOutput, ((ShapelessRecipes)recipe).recipeItems.toArray());
				}
				else if (recipe instanceof ShapedOreRecipe)
				{
					m = getMatterFromList(recipeOutput, ((ShapedOreRecipe)recipe).getInput());
				}
				else if (recipe instanceof ShapelessOreRecipe)
				{
					m = getMatterFromList(recipeOutput, ((ShapelessOreRecipe)recipe).getInput().toArray());
				}

				matter += m;
			}
		}

		return matter;
	}

	@Override
	public void addModToBlacklist(final String modID)
	{
		modBlacklist.add(modID);
	}

	public void loadModBlacklistFromConfig(final ConfigurationHandler c)
	{
		String[] list = c.getStringList(ConfigurationHandler.CATEGORY_MATTER, ConfigurationHandler.KEY_BLACKLIST_MODS);
		for (String value : list)
		{
			addModToBlacklist(value);
		}
	}

	public int getMatterFromList(final ItemStack item, final Object[] list)
	{
		int totalMatter = 0;
		int tempMatter;
		IMatterEntry tempEntry;

		for (Object s : list)
		{
			if (s == null)
			{
				continue;
			}

			//reset temp vars
			tempMatter = 0;

			if (s instanceof ItemStack || s instanceof Item || s instanceof Block)
			{

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

				if (stack == null || blacklistedFromMod(stack))
				{
					debug("%s is blacklisted.", item);
					return -1;
				}

				//check to see if the item in the recipe is the same as the output
				//and if so then do not calculate to save unnecessary lopping
				if (!ItemStack.areItemStacksEqual(stack, item))
				{
					tempEntry = getEntry(stack);
					//if there is an entry use it's matter value
					if (tempEntry != null)
					{
						tempMatter = tempEntry.getMatter(stack);
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
						}
						else if (element instanceof Block)
						{
							stack = new ItemStack((Block)element);
						}

						tempEntry = getEntry(stack);
						if (tempEntry != null)
						{
							//if the item has matter, has lower matter than the previous
							//if the item was first there is no previous so store that amount
							if (tempEntry.getMatter(stack) > 0)
							{
								if ((tempEntry.getMatter(stack) < tempMatter || first))
								{
									tempMatter = tempEntry.getMatter(stack);
									first = false;
								}
							}
							else
							{
								debug("entry for %s, found in recipe for: %s was blacklisted or costs lower then previous", stack, item);
							}

						}
					}
					else
					{
						debug("Found another type of object in list ot type: %s", element.getClass().toString());
					}
				}
			}

			//if for same reason the item is invalid, that can't really happen
			if (tempMatter < 0)
			{
				debug("%s is invalid.", item);
				return -1;
			}
			//if there is an item that has 0 matter, aka NOT replicatable
			else if (tempMatter == 0)
			{
				debug("%s item in recipe has 0 matter.", item);
				return 0;
			}

			//increase the total matter amount
			totalMatter += tempMatter;
		}

		//return the amount divided by the count of the items stack
		//sometimes after the division the result is 0
		return (int)Math.round((double)totalMatter / (double)item.stackSize);
	}

	public Map<Item, MatterEntryItem> getItemEntires()
	{
		return itemEntires;
	}

	public Map<String, MatterEntryOre> getOreEntries()
	{
		return oreEntries;
	}

	public void debug(final String debug, final Object... params)
	{
		if (REGISTRATION_DEBUG)
		{
			for (int i = 0; i < params.length; i++)
			{
				if (params[i] instanceof ItemStack)
				{
					try
					{
						params[i] = ((ItemStack)params[i]).getItem();
					}
					catch (Exception e)
					{
						MOLog.warn("There was a problem getting ItemStack's name");
					}
				}
			}
			MOLog.debug(debug, params);
		}
	}

	private static class CusomHandlerListJSON
	{
		private final Map<String, CustomHandlerJSON> items;
		private final Map<String, CustomHandlerJSON> ores;

		public CusomHandlerListJSON(final Map<String, CustomHandlerJSON> items, final Map<String, CustomHandlerJSON> ores)
		{
			this.items = items;
			this.ores = ores;
		}
	}

	private static class CustomHandlerJSON
	{
		private final boolean isOre;
		private final boolean hasMeta;
		private final int[] meta;
		private final int matter;
		private final int priority;

		public CustomHandlerJSON(final boolean isOre, final boolean metadataSpecific, final int[] meta, final int matter, final int priority)
		{
			this.isOre = isOre;
			this.hasMeta = metadataSpecific;
			this.meta = meta;
			this.matter = matter;
			this.priority = priority;
		}
	}
}
