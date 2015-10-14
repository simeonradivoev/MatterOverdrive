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

package matteroverdrive.api.matter;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 7/20/2015.
 * This is the Matter Registry, where all matter values for Items are stored.
 * The main implementation of this is {@link matteroverdrive.handler.MatterRegistry}.
 */
public interface IMatterRegistry
{
    /**
     * Adds an Item Stack to the matter blacklist.
     * When in the blacklist, all matter calculation from recipe containing that item stack in it, will be skipped.
     * @param itemStack The item stack being blacklisted.
     */
    void addToBlacklist(ItemStack itemStack);
    /**
     * Adds a key to the matter blacklist.
     * Mainly used for blacklist Ore Dictionary entries.
     * When in the blacklist, all matter calculation from recipe containing that key in it, will be skipped.
     * @param key The key being blacklisted.
     */
    void addToBlacklist(String key);
    /**
     * Adds an Item to the matter blacklist.
     * When in the blacklist, all matter calculation from recipe containing that item in it, will be skipped.
     * @param item The item being blacklisted.
     */
    void addToBlacklist(Item item);
    /**
     * Adds an block to the matter blacklist.
     * When in the blacklist, all matter calculation from recipe containing that block in it, will be skipped.
     * @param block The block being blacklisted.
     */
    void addToBlacklist(Block block);

    /**
     * Is the specified block blacklisted.
     * @param block the block being checked.
     * @return is the given block blacklisted.
     */
    boolean blacklisted(Block block);
    /**
     * Is the specified item blacklisted.
     * @param item the item being checked.
     * @return is the given item blacklisted.
     */
    boolean blacklisted(Item item);
    /**
     * Is the specified itemStack blacklisted.
     * @param itemStack the itemStack being checked.
     * @return is the given itemStack blacklisted.
     */
    boolean blacklisted(ItemStack itemStack);
    /**
     * Is the specified key blacklisted.
     * Mainly used to check if Ore Dictionary entices are blacklisted.
     * @param key the key being checked.
     * @return is the given key blacklisted.
     */
    boolean blacklisted(String key);

    /**
     * Used to register a block in the Matter Registry.
     * @param block the block being registered.
     * @param matter the amount of matter the block will contain.
     * @return the constructed Matter Entry for the given block.
     */
    IMatterEntry register(Block block,int matter);
    /**
     * Used to register an item in the Matter Registry.
     * @param item the item being registered.
     * @param matter the amount of matter the item will contain.
     * @return the constructed Matter Entry for the given item.
     */
    IMatterEntry register(Item item,int matter);
    /**
     * Used to register an Item Stack in the Matter Registry.
     * @param itemStack the Item Stack being registered.
     * @param matter the amount of matter the Item Stack will contain.
     * @return the constructed Matter Entry for the given Item Stack.
     */
    IMatterEntry register(ItemStack itemStack,int matter);
    /**
     * Used to register an key in the Matter Registry.
     * Mainly used to register Ore Dictionary entries.
     * @param key the key being registered.
     * @param matter the amount of matter the key will contain.
     * @return the constructed Matter Entry for the given key.
     */
    IMatterEntry register(String key,int matter);

    /**
     * Calculates the matter contained in an item based on it's recipe.
     * This is where the blacklist comes.
     * When an item in the recipe is blacklisted or does not contain any matter.
     * The calculation will stop and return 0.
     * @param item the item being calculated
     * @param recursive is the recipe calculation recursive.
     *                  This means that if an item contains 0 matter.
     *                  Then that item will be calculated based on it's recipe as well and so on.
     * @param depth the depth at which the recursive calculation is at. This is called by the recursive algorithm.
     *              At the beginning of the recursive calculation this value is usually set ot 0.
     *              This is mainly used to limit the depth at which the recursion can occur.
     * @param calculated will the matter entry be marked as calculated.
     *                   This will affect the {@link IMatterEntry#getCalculated()} method.
     *                   This is usually set to true by the automatic matter recipe calculation.
     * @return the matter contained in the calculated item based on its recipe.
     */
    int getMatterFromRecipe(ItemStack item,boolean recursive,int depth,boolean calculated);

    /**
     * This add a whole mod the the blacklist.
     * All items from that mod are treated as blacklisted.
     * @param modID the ID of the blacklisted mod.
     */
    void addModToBlacklist(String modID);
}
