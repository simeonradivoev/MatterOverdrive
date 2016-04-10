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

import matteroverdrive.data.matter_network.ItemPattern;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 3/27/2015.
 * Used by items that store Item Patterns such as the {@link matteroverdrive.items.PatternDrive}
 */
public interface IMatterPatternStorage
{
	ItemPattern getPatternAt(ItemStack storage, int slot);

	void setItemPatternAt(ItemStack storage, int slot, ItemPattern itemPattern);

	boolean increasePatternProgress(ItemStack itemStack, int slot, int amount);

	/**
	 * Gets the capacity of the storage item.
	 * How much pattern can it store.
	 * @param item The storage stack.
	 * @return the pattern capacity of the storage.
	 */
	int getCapacity(ItemStack item);
}
