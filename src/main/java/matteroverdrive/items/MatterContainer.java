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

import matteroverdrive.init.MatterOverdriveBlocks;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Created by Simeon on 8/20/2015.
 */
public class MatterContainer extends MOBaseItem
{
	final boolean isFull;

	public MatterContainer(String name, boolean isFull)
	{
		super(name);
		this.isFull = isFull;
		setMaxStackSize(8);
	}

    /*@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon(Reference.MOD_ID + ":" + "container");
        centerFill = iconRegister.registerIcon(Reference.MOD_ID + ":" + "container_center_fill");
        bottomFill = iconRegister.registerIcon(Reference.MOD_ID + ":" + "container_bottom_fill");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return isFull;
    }

    @Override
    public int getRenderPasses(int metadata)
    {
        return isFull ? 3 : 1;
    }*/

	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		RayTraceResult movingobjectposition = this.rayTrace(world, entityPlayer, !isFull);

		if (movingobjectposition == null)
		{
			return itemStack;
		}
		else
		{
			if (movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK)
			{
				BlockPos pos = movingobjectposition.getBlockPos();

				if (!world.getBlockState(pos).getBlock().canHarvestBlock(world, pos, entityPlayer))
				{
					return itemStack;
				}

				if (!isFull)
				{
					if (!entityPlayer.canPlayerEdit(pos, movingobjectposition.sideHit, itemStack))
					{
						return itemStack;
					}

					IBlockState block = world.getBlockState(pos);

					if (block.getBlock() == MatterOverdriveBlocks.blockMatterPlasma)
					{
						world.setBlockToAir(pos);
						return this.darinFluid(itemStack, entityPlayer, MatterOverdriveItems.matterContainerFull);
					}
				}
				else
				{
					if (!this.isFull)
					{
						return new ItemStack(MatterOverdriveItems.matterContainer);
					}

					pos = pos.offset(movingobjectposition.sideHit);

					if (!entityPlayer.canPlayerEdit(pos, movingobjectposition.sideHit, itemStack))
					{
						return itemStack;
					}

					if (this.tryPlaceContainedLiquid(world, pos) && !entityPlayer.capabilities.isCreativeMode)
					{
						itemStack.stackSize--;
						if (itemStack.stackSize > 1)
						{
							if (!entityPlayer.inventory.addItemStackToInventory(new ItemStack(MatterOverdriveItems.matterContainer)))
							{
								entityPlayer.dropItem(new ItemStack(MatterOverdriveItems.matterContainer), false);
							}
						}
						else if (itemStack.stackSize <= 0)
						{
							return new ItemStack(MatterOverdriveItems.matterContainer);
						}
					}
				}
			}
		}
		return itemStack;
	}

	private ItemStack darinFluid(ItemStack itemStack, EntityPlayer entityPlayer, Item item)
	{
		if (entityPlayer.capabilities.isCreativeMode)
		{
			return itemStack;
		}
		else if (--itemStack.stackSize <= 0)
		{
			return new ItemStack(item);
		}
		else
		{
			if (!entityPlayer.inventory.addItemStackToInventory(new ItemStack(item)))
			{
				entityPlayer.dropItem(new ItemStack(item, 1, 0), false);
			}

			return itemStack;
		}
	}

	public boolean tryPlaceContainedLiquid(World world, BlockPos pos)
	{
		if (!isFull)
		{
			return false;
		}
		else
		{
			Material material = world.getBlockState(pos).getMaterial();

			if (!world.isAirBlock(pos))
			{
				return false;
			}
			else
			{
				if (!world.isRemote && !material.isLiquid())
				{
					world.destroyBlock(pos, true);
				}

				world.setBlockState(pos, MatterOverdriveBlocks.blockMatterPlasma.getDefaultState(), 3);

				return true;
			}
		}
	}

	// TODO: 3/26/2016 Find how to get color from stack
	/*@Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemStack, int pass)
    {
        if (pass == 1 && isFull)
        {
            return Reference.COLOR_MATTER.getColor();
        }else if (pass == 2 && isFull)
        {
            return Reference.COLOR_YELLOW_STRIPES.getColor();
        }
        return super.getColorFromItemStack(itemStack,pass);
    }*/
}
