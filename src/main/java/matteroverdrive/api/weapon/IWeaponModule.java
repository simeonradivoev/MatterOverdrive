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

package matteroverdrive.api.weapon;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 4/14/2015.
 * Used by weapon modules.
 */
public interface IWeaponModule
{
    /**
     * Shows the slot type the module can go into.
     * @param module the module stack.
     * @return the slot type the module can go into.
     */
    int getSlot(ItemStack module);

    /**
     * Gets the module's model path.
     * @return the path of the model resource file.
     */
    String getModelPath();

    /**
     * Gets the texture location for the module's model.
     * @param module the module.
     * @return the location of the model texture.
     */
    ResourceLocation getModelTexture(ItemStack module);

    /**
     * Gets the name of the object inside the model.
     * This is used to enable modules to share a base model file.
     * @param module the module.
     * @return the name of the object in the model.
     */
    String getModelName(ItemStack module);

    /**
     * Modifies the give weapon stats.
     * @param statID the Stat type ID;
     * @param module the module stack.
     * @param weapon the weapon stack.
     * @param originalStat the original stat value.
     * @return general value provided by the module.
     */
    float modifyWeaponStat(int statID,ItemStack module,ItemStack weapon,float originalStat);
}
