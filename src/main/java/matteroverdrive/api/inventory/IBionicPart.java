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

package matteroverdrive.api.inventory;

import com.google.common.collect.Multimap;
import matteroverdrive.entity.AndroidPlayer;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 5/26/2015.
 * This class represents parts that can be worn by Android players.
 * By equipping them in the Android Station.
 */
public interface IBionicPart
{
    /**
     * The type of part. At witch part for the body can the Bionic part be worn.
     * <ol>
     *     <li>Head</li>
     *     <li>Arms</li>
     *     <li>Legs</li>
     *     <li>Chest</li>
     *     <li>Other</li>
     *     <li>Battery</li>
     * </ol>
     * @param itemStack The bionic Items Stack.
     * @return The type of bionic part.
     */
    int getType(ItemStack itemStack);

    /**
     * @param player The android player.
     * @param itemStack The bionic item stack.
     * @return Does the bionic part affect the android player.
     */
    boolean affectAndroid(AndroidPlayer player, ItemStack itemStack);

    /**
     * A Multimap of modifiers similar to vanilla armor modifiers.
     * @param player The android player.
     * @param itemStack The Bionic part item stack.
     * @return A multimap of modifiers.
     */
    Multimap<String, AttributeModifier> getModifiers(AndroidPlayer player, ItemStack itemStack);
}
