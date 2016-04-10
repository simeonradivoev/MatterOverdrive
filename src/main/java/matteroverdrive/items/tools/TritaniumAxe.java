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

package matteroverdrive.items.tools;

import com.google.common.collect.Sets;
import matteroverdrive.init.MatterOverdriveItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

import java.util.Set;

/**
 * Created by Simeon on 11/1/2015.
 */
public class TritaniumAxe extends ItemTool
{
	private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[] {Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2, Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin, Blocks.melon_block, Blocks.ladder, Blocks.wooden_button, Blocks.wooden_pressure_plate});

	public TritaniumAxe(String name)
	{
		super(MatterOverdriveItems.toolMaterialTritanium, EFFECTIVE_ON);
		this.setUnlocalizedName(name);
	}

	public float getStrVsBlock(ItemStack stack, IBlockState state)
	{
		Material material = state.getMaterial();
		return material != Material.wood && material != Material.plants && material != Material.vine ? super.getStrVsBlock(stack, state) : this.efficiencyOnProperMaterial;
	}
}
