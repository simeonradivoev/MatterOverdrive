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

import matteroverdrive.api.IMOTileEntity;
import matteroverdrive.util.MOLog;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author shadowfacts
 */
public class TileEntityBoundingBox extends TileEntity implements IMOTileEntity, ITickable
{

	private int tick = 0;
	private BlockPos ownerPos;
	private Block ownerBlock;

	@Override
	public void update() {
		tick++;
		if (tick == 80) { // update every 4 seconds (assuming 20 TPS)
			tick = 0;

			if (worldObj != null)
			{
				if (!ownerPresent())
				{
					worldObj.setBlockToAir(getPos());
				}
			}

		}
	}

	private boolean ownerPresent()
	{
		if (ownerPos != null)
		{
			return worldObj.getBlockState(ownerPos).getBlock() == ownerBlock;
		}
		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		ownerPos = BlockPos.fromLong(tag.getLong("owner"));

		String ownerModid = tag.getString("owner_block_modid");
		String ownerName = tag.getString("owner_block_name");
		Block block = GameRegistry.findBlock(ownerModid, ownerName);
		if (block == null) {
			MOLog.error("Missing owner block " + ownerModid + ":" + ownerName);
		} else {
			ownerBlock = block;
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		if (ownerPos != null) {
			tag.setLong("owner",ownerPos.toLong());
		}

		if (ownerBlock != null) {
			ResourceLocation id = ownerBlock.getRegistryName();
			tag.setString("owner_block_modid", id.getResourceDomain());
			tag.setString("owner_block_name", id.getResourcePath());
		}
	}

	public BlockPos getOwnerPos() {
		return ownerPos;
	}

	public void setOwnerPos(BlockPos ownerPos) {
		this.ownerPos = ownerPos;
	}

	public Block getOwnerBlock() {
		return ownerBlock;
	}

	public void setOwnerBlock(Block ownerBlock) {
		this.ownerBlock = ownerBlock;
	}

	@Override
	public void onPlaced(World world, EntityLivingBase entityLiving) {

	}

	@Override
	public void writeToDropItem(ItemStack itemStack) {

	}

	@Override
	public void readFromPlaceItem(ItemStack itemStack) {

	}

	@Override
	public void onAdded(World world, BlockPos pos, IBlockState state)
	{

	}

	@Override
	public void onDestroyed(World worldIn, BlockPos pos, IBlockState state)
	{

	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
	{

	}
}
