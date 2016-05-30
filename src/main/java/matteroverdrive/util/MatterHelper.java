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

package matteroverdrive.util;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.inventory.IUpgrade;
import matteroverdrive.api.matter.IMatterItem;
import matteroverdrive.api.matter.IMatterPatternStorage;
import matteroverdrive.api.matter.IMatterProvider;
import matteroverdrive.init.MatterOverdriveFluids;
import matteroverdrive.items.MatterScanner;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.List;

public class MatterHelper
{
	public static final String MATTER_UNIT = " kM";

	public static boolean containsMatter(ItemStack item)
	{
		return getMatterAmountFromItem(item) > 0;
	}

	public static int getMatterAmountFromItem(ItemStack item)
	{
		if (item != null)
		{
			if (item.getItem() instanceof IMatterItem)
			{
				return ((IMatterItem)item.getItem()).getMatter(item);
			}
			else
			{
				return MatterOverdrive.matterRegistry.getMatter(item);
			}
		}
		return 0;
	}

	public static int getEnergyFromMatter(int multiply, ItemStack itemStack)
	{
		int matter = getMatterAmountFromItem(itemStack);
		return multiply * matter;
	}

	public static int getTotalEnergyFromMatter(int multiply, ItemStack itemStack, int time)
	{
		int matter = getMatterAmountFromItem(itemStack);
		return multiply * matter * time;
	}


	public static int Transfer(EnumFacing toDir, int amount, IMatterProvider from, IFluidHandler to)
	{
		EnumFacing oposite = toDir.getOpposite();
		int extract = from.extractMatter(toDir, amount, true);
		int recived = to.fill(oposite, new FluidStack(MatterOverdriveFluids.matterPlasma, extract), true);
		from.extractMatter(toDir, recived, false);
		return recived;
	}

	private static IRecipe GetRecipeOf(ItemStack item)
	{
		List recipes = CraftingManager.getInstance().getRecipeList();
		for (Object recipe1 : recipes)
		{
			IRecipe recipe = (IRecipe)recipe1;

			if (recipe != null && recipe.getRecipeOutput() != null && recipe.getRecipeOutput().getItem() == item.getItem())
			{
				return recipe;
			}
		}

		return null;
	}

	public static boolean isMatterScanner(ItemStack item)
	{
		return item != null && item.getItem() != null && item.getItem() instanceof MatterScanner;
	}

	public static boolean isMatterPatternStorage(ItemStack item)
	{
		return item != null && item.getItem() != null && item.getItem() instanceof IMatterPatternStorage;
	}

	public static boolean isUpgrade(ItemStack itemStack)
	{
		return itemStack != null && itemStack.getItem() instanceof IUpgrade;
	}

	public static boolean CanScan(ItemStack stack)
	{
		if (MatterHelper.getMatterAmountFromItem(stack) <= 0)
		{
			return false;
		}

		Item item = stack.getItem();

		if (item instanceof ItemBlock)
		{
			Block block = Block.getBlockFromItem(item);

			if (block == Blocks.BEDROCK || block == Blocks.AIR)
			{
				return false;
			}
		}

		return true;
	}

	public static String formatMatter(int matter)
	{
		return MOStringHelper.formatNumber(matter) + MATTER_UNIT;
	}

	public static String formatMatter(double matter)
	{
		return MOStringHelper.formatNumber(matter) + MATTER_UNIT;
	}

	public static String formatMatter(int matter, int capacity)
	{
		return MOStringHelper.formatNumber(matter) + " / " + MOStringHelper.formatNumber(capacity) + MATTER_UNIT;
	}

	public static boolean DropInventory(World world, IInventory inventory, BlockPos pos)
	{
		if (inventory != null)
		{
			for (int i1 = 0; i1 < inventory.getSizeInventory(); ++i1)
			{
				ItemStack itemstack = inventory.getStackInSlot(i1);

				if (itemstack != null)
				{
					float f = world.rand.nextFloat() * 0.8F + 0.1F;
					float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
					float f2 = world.rand.nextFloat() * 0.8F + 0.1F;

					while (itemstack.stackSize > 0)
					{
						int j1 = world.rand.nextInt(21) + 10;

						if (j1 > itemstack.stackSize)
						{
							j1 = itemstack.stackSize;
						}
						itemstack.stackSize -= j1;
						EntityItem entityitem = new EntityItem(world, (double)((float)pos.getX() + f), (double)((float)pos.getY() + f1), (double)((float)pos.getZ() + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

						if (itemstack.hasTagCompound())
						{
							entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
						}

						float f3 = 0.05F;
						entityitem.motionX = (double)((float)world.rand.nextGaussian() * f3);
						entityitem.motionY = (double)((float)world.rand.nextGaussian() * f3 + 0.2F);
						entityitem.motionZ = (double)((float)world.rand.nextGaussian() * f3);
						world.spawnEntityInWorld(entityitem);
					}
				}
			}
			return true;
		}

		return false;
	}

	public static void DrawMatterInfoTooltip(ItemStack itemStack, int speed, int energyPerTick, List tooltips)
	{
		int matter = MatterHelper.getMatterAmountFromItem(itemStack);
		if (matter > 0)
		{
			tooltips.add(TextFormatting.ITALIC.toString() + TextFormatting.BLUE.toString() + "Matter: " + MatterHelper.formatMatter(matter));
			tooltips.add(TextFormatting.ITALIC.toString() + TextFormatting.DARK_RED + "Power: " + MOEnergyHelper.formatEnergy(speed * matter * energyPerTick));
		}
	}
}
