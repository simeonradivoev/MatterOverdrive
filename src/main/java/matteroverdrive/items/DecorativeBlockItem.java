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

import matteroverdrive.blocks.BlockDecorativeColored;
import matteroverdrive.blocks.BlockDecorativeRotated;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

/**
 * Created by Simeon on 11/27/2015.
 */
public class DecorativeBlockItem extends ItemBlock
{
    public DecorativeBlockItem(Block block)
    {
        super(block);
        if (block instanceof BlockDecorativeRotated)
        {
            setHasSubtypes(true);
            setMaxDamage(0);
        }
        else if (block instanceof BlockDecorativeColored)
        {
            setHasSubtypes(true);
            setMaxDamage(0);
        }
    }

    @Override
    public int getMetadata(int damage)
    {
        if (block instanceof BlockDecorativeRotated)
        {
            return MathHelper.clamp_int(damage,0,1);
        }else if (block instanceof BlockDecorativeColored)
        {
            return MathHelper.clamp_int(damage,0,15);
        }
        return 0;
    }

    // TODO: 3/26/2016 Find how to get color for stack
    /*@SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass)
    {
        if (block instanceof BlockDecorativeColored)
        {
            return block.getRenderColor(block.getStateFromMeta(stack.getMetadata()));
        }
        return super.getColorFromItemStack(stack,renderPass);
    }*/

    @Override
    public String getItemStackDisplayName(ItemStack itemStack)
    {
        if (block instanceof BlockDecorativeRotated)
        {
            if (itemStack.getItemDamage() == 1)
            {
                return super.getItemStackDisplayName(itemStack) + " [Rotated]";
            }
        }else if (block instanceof BlockDecorativeColored)
        {
            return MOStringHelper.translateToLocal("color." + EnumDyeColor.byMetadata(MathHelper.clamp_int(itemStack.getItemDamage(),0,ItemDye.dyeColors.length-1)).getUnlocalizedName() + " " + super.getItemStackDisplayName(itemStack));
        }
        return super.getItemStackDisplayName(itemStack);
    }
}
