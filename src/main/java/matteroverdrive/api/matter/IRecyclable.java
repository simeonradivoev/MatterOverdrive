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

import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 5/15/2015.
 * Used by items that can be recycled at a Recycler.
 */
public interface IRecyclable
{
    /**
     * The Recycled output stack.
     * @param from the original stack being recycled.
     * @return the recycled output stack.
     */
    ItemStack getOutput(ItemStack from);

    /**
     * Get the matter the recycled output has.
     * This is manly used to calculated power and speed requirements.
     * @param stack the stack being recycled.
     * @return the amount of matter the recycled output has.
     */
    int getRecycleMatter(ItemStack stack);

    /**
     * Can the given item stack be recycled.
     * @param stack the recycled item stack.
     * @return can be recycled.
     */
    boolean canRecycle(ItemStack stack);
}
