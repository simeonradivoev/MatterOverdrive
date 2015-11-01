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

package matteroverdrive.items.armour;

import matteroverdrive.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 11/1/2015.
 */
public class TritaniumArmour extends ItemArmor
{
    public TritaniumArmour(ArmorMaterial armorMaterial, int renderIndex, int renderType) {
        super(armorMaterial, renderIndex, renderType);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        return String.format(Reference.PATH_ARMOR + "tritanium_layer_%d%s.png",(slot == 2 ? 2 : 1),type == null ? "" : String.format("_%s", type));
    }
}
