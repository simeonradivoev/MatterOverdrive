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

import matteroverdrive.init.MatterOverdriveFluids;
import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

import java.util.List;

import static net.minecraftforge.fluids.capability.CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;

/**
 * Created by Simeon on 8/20/2015.
 */
public class MatterContainer extends MOBaseItem
{

	public MatterContainer(String name)
	{
		super(name);
		setMaxStackSize(8);
	}

	public ItemStack getFullStack() {
		ItemStack full = new ItemStack(this);
		full.getCapability(FLUID_HANDLER_CAPABILITY, null).fill(new FluidStack(MatterOverdriveFluids.matterPlasma, 1000), true);
		return full;
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		TileEntity tile = world.getTileEntity(pos);
		if (tile != null && tile.hasCapability(FLUID_HANDLER_CAPABILITY, facing)) {
			IFluidHandler handler = tile.getCapability(FLUID_HANDLER_CAPABILITY, facing);
			return FluidUtil.interactWithFluidHandler(stack, handler, player) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
		}
		return EnumActionResult.PASS;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		FluidStack result = stack.getCapability(FLUID_HANDLER_CAPABILITY, null).drain(1000, false);
		return super.getUnlocalizedName(stack) + (result == null ? "_empty" : "_full");
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
	{
		FluidStack result = stack.getCapability(FLUID_HANDLER_CAPABILITY, null).drain(1000, false);
		int amount = result == null ? 0 : result.amount;
		tooltip.add("Amount: " + amount);
	}

	@Override
	public void initItemModel()
	{
		ModelLoader.setCustomMeshDefinition(this, stack -> {
			FluidStack result = stack.getCapability(FLUID_HANDLER_CAPABILITY, null).drain(1000, false);
			int amount = result == null ? 0 : result.amount;
			float percent = amount / 1000f;
			return new ModelResourceLocation(getRegistryName(), percent == 1 ? "level=full" : percent > 0 ? "level=partial" : "level=empty");
		});
		ModelBakery.registerItemVariants(this, new ModelResourceLocation(getRegistryName(), "level=full"),
													new ModelResourceLocation(getRegistryName(), "level=partial"),
													new ModelResourceLocation(getRegistryName(), "level=empty"));
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems)
	{
		subItems.add(new ItemStack(this));
		subItems.add(getFullStack());
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		return new MatterContainerCapabilityProvider();
	}

	public static class MatterContainerCapabilityProvider implements ICapabilitySerializable<NBTTagCompound>
	{

		private FluidTank tank = new FluidTank(1000)
		{
			@Override
			public boolean canFillFluidType(FluidStack fluid)
			{
				return fluid.getFluid() == MatterOverdriveFluids.matterPlasma;
			}
		};

		@Override
		public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
		{
			return capability == FLUID_HANDLER_CAPABILITY;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
		{
			return capability == FLUID_HANDLER_CAPABILITY ? (T)tank : null;
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			return tank.writeToNBT(new NBTTagCompound());
		}

		@Override
		public void deserializeNBT(NBTTagCompound tag)
		{
			tank.readFromNBT(tag);
		}

	}

}
