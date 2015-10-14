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
 * Created by Simeon on 5/16/2015.
 * This is used by items that have specific amount of matter based on their items stack.
 * This is a dynamic way of getting matter based on each Item Stack.
 * For example the item matter can be based in it's damage or NBT tag.
 */
public interface IMatterItem
{
    /**
     * The amount of matter the item stack has.
     * @param itemStack the item stack.
     * @return the matter of the Item stack.
     */
    int getMatter(ItemStack itemStack);
}
