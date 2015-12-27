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

import matteroverdrive.data.ItemPattern;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 3/27/2015.
 * Used by items that store Item Patterns such as the {@link matteroverdrive.items.PatternDrive}
 */
public interface IMatterPatternStorage
{
    /**
     * @param storage the storage stack
     * @return a list of all the patterns
     */
    ItemPattern[] getPatterns(ItemStack storage);

    /**
     * Adds an item as a pattern in the given Item Stack storage.
     * @param storage The storage stack.
     * @param itemStack The item stack being stored as a patten.
     * @param initialAmount The initial progress amount of the pattern.
     *                      Ranges from 0 - 100%.
     * @param simulate if the call is a simulation, no pattern is stored in the storage item.
     *                 Used to check if a pattern can be stored without actually storing the pattern.
     * @return was the pattern stored.
     */
    boolean addItem(ItemStack storage, ItemStack itemStack, int initialAmount, boolean simulate);

    /**
     * Gets a pattern for a given Item Stack.
     * @param storage The Item Stack storage.
     * @param item The item stack being searched for.
     * @return The pattern for a given Item Stack in storage.
     * Returns null if pattern is not present in the storage.
     */
    ItemPattern getPattern(ItemStack storage, ItemStack item);

    /**
     * Gets the capacity of the storage item.
     * How much pattern can it store.
     * @param item The storage stack.
     * @return the pattern capacity of the storage.
     */
    int getCapacity(ItemStack item);
}
