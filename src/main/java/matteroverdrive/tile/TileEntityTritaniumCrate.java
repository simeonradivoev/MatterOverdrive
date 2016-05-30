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

package matteroverdrive.tile;

import matteroverdrive.data.TileEntityInventory;
import matteroverdrive.data.inventory.CrateSlot;
import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumSet;

/**
 * Created by Simeon on 11/5/2015.
 */
public class TileEntityTritaniumCrate extends MOTileEntity implements IInventory, IInteractionObject
{
	final TileEntityInventory inventory;

	public TileEntityTritaniumCrate()
	{
		inventory = new TileEntityInventory(this, MOStringHelper.translateToLocal("container.tritanium_crate"));
		for (int i = 0; i < 54; i++)
		{
			CrateSlot slot = new CrateSlot(false);
			inventory.AddSlot(slot);
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk)
	{
		if (categories.contains(MachineNBTCategory.INVENTORY) && toDisk)
		{
			inventory.writeToNBT(nbt, true);
		}
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories)
	{
		if (categories.contains(MachineNBTCategory.INVENTORY))
		{
			inventory.readFromNBT(nbt);
		}
	}

	@Override
	protected void onAwake(Side side)
	{

	}

	@Override
	public void onAdded(World world, BlockPos pos, IBlockState state)
	{

	}

	@Override
	public void onPlaced(World world, EntityLivingBase entityLiving)
	{

	}

	@Override
	public void onDestroyed(World worldIn, BlockPos pos, IBlockState state)
	{

	}

	@Override
	public void onNeighborBlockChange(IBlockAccess world, BlockPos pos, IBlockState state, Block neighborBlock)
	{

	}

	@Override
	public void writeToDropItem(ItemStack itemStack)
	{
		if (!itemStack.hasTagCompound())
		{
			itemStack.setTagCompound(new NBTTagCompound());
		}

		inventory.writeToNBT(itemStack.getTagCompound(), true);
	}

	@Override
	public void readFromPlaceItem(ItemStack itemStack)
	{
		if (itemStack.hasTagCompound())
		{
			inventory.readFromNBT(itemStack.getTagCompound());
		}
	}

	public TileEntityInventory getInventory()
	{
		return inventory;
	}

	@Override
	public int getSizeInventory()
	{
		return inventory.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return inventory.getStackInSlot(slot);
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount)
	{
		return inventory.decrStackSize(slot, amount);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		inventory.setInventorySlotContents(slot, stack);
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return inventory.removeStackFromSlot(index);
	}

	@Override
	public int getField(int id)
	{
		return inventory.getField(id);
	}

	@Override
	public void setField(int id, int value)
	{
		inventory.setField(id, value);
	}

	@Override
	public int getFieldCount()
	{
		return inventory.getFieldCount();
	}

	@Override
	public void clear()
	{
		inventory.clear();
	}

	@Override
	public String getName()
	{
		return inventory.getName();
	}

	@Override
	public boolean hasCustomName()
	{
		return inventory.hasCustomName();
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return inventory.getDisplayName();
	}

	@Override
	public int getInventoryStackLimit()
	{
		return inventory.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_)
	{
		return true;
	}

	@Override
	public void openInventory(EntityPlayer entityPlayer)
	{
		inventory.openInventory(entityPlayer);
	}

	@Override
	public void closeInventory(EntityPlayer entityPlayer)
	{
		inventory.closeInventory(entityPlayer);
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return inventory.isItemValidForSlot(slot, stack);
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerChest(playerInventory, getInventory(), playerIn);
	}

	@Override
	public String getGuiID()
	{
		return "minecraft:chest";
	}
}
