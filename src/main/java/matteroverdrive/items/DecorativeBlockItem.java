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

package matteroverdrive.items;

import matteroverdrive.blocks.BlockDecorative;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

/**
 * Created by Simeon on 11/27/2015.
 */
public class DecorativeBlockItem extends ItemBlock
{
    public DecorativeBlockItem(Block block)
    {
        super(block);
        if (block instanceof BlockDecorative)
        {
            setHasSubtypes(((BlockDecorative)block).canBeRotated());
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack)
    {
        if (((BlockDecorative)field_150939_a).canBeRotated() && itemStack.getItemDamage() > 0) {
            return super.getItemStackDisplayName(itemStack) + " [Rotated]";
        }else if (((BlockDecorative)field_150939_a).isColored())
        {
            return MOStringHelper.translateToLocal("color." + ItemDye.field_150923_a[MathHelper.clamp_int(itemStack.getItemDamage(),0,ItemDye.field_150923_a.length-1)]) + " " + super.getItemStackDisplayName(itemStack);
        }
        return super.getItemStackDisplayName(itemStack);
    }
}
