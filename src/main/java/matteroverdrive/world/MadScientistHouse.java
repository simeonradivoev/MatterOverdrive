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

package matteroverdrive.world;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.entity.EntityVillagerMadScientist;
import matteroverdrive.init.MatterOverdriveBlocks;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.tile.TileEntityTritaniumCrate;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 5/30/2015.
 */
public class MadScientistHouse extends StructureVillagePieces.Village
{
	int villagersSpawned;
	private boolean hasMadeChest;

	public MadScientistHouse()
	{
	}

	public MadScientistHouse(StructureVillagePieces.Start start, int p_i45571_2_, Random rand, StructureBoundingBox p_i45571_4_, EnumFacing facing)
	{
		super(start, p_i45571_2_);
		this.setCoordBaseMode(facing);
		this.boundingBox = p_i45571_4_;
	}

	public static StructureVillagePieces.House1 func_175850_a(StructureVillagePieces.Start start, List<StructureComponent> p_175850_1_, Random rand, int p_175850_3_, int p_175850_4_, int p_175850_5_, EnumFacing facing, int p_175850_7_)
	{
		StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p_175850_3_, p_175850_4_, p_175850_5_, 0, 0, 0, 9, 9, 6, facing);
		return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(p_175850_1_, structureboundingbox) == null ? new StructureVillagePieces.House1(start, p_175850_7_, rand, structureboundingbox, facing) : null;
	}

	@Override
	protected void writeStructureToNBT(NBTTagCompound p_143012_1_)
	{
		super.writeStructureToNBT(p_143012_1_);
		p_143012_1_.setBoolean("Chest", this.hasMadeChest);
	}

	@Override
	protected void readStructureFromNBT(NBTTagCompound p_143011_1_)
	{
		super.readStructureFromNBT(p_143011_1_);
		this.hasMadeChest = p_143011_1_.getBoolean("Chest");
	}

	/**
	 * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
	 * Mineshafts at the end, it adds Fences...
	 */
	public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
	{
		if (this.averageGroundLvl < 0)
		{
			this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

			if (this.averageGroundLvl < 0)
			{
				return true;
			}

			this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 9 - 1, 0);
		}

		this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 1, 7, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
		this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 8, 0, 5, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
		this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 5, 0, 8, 5, 5, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
		this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 6, 1, 8, 6, 4, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
		this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 7, 2, 8, 7, 3, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);

		for (int i = -1; i <= 2; ++i)
		{
			for (int j = 0; j <= 8; ++j)
			{
				this.setBlockState(worldIn, Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH), j, 6 + i, i, structureBoundingBoxIn);
				this.setBlockState(worldIn, Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH), j, 6 + i, 5 - i, structureBoundingBoxIn);
			}
		}

		this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 1, 0, 0, 1, 5, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
		this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 5, 8, 1, 5, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
		this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 1, 0, 8, 1, 4, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
		this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 1, 0, 7, 1, 0, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
		this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 0, 0, 4, 0, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
		this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 5, 0, 4, 5, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
		this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 2, 5, 8, 4, 5, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
		this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 2, 0, 8, 4, 0, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
		this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, 1, 0, 4, 4, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
		this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 5, 7, 4, 5, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
		this.fillWithBlocks(worldIn, structureBoundingBoxIn, 8, 2, 1, 8, 4, 4, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
		this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 2, 0, 7, 4, 0, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
		this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 4, 2, 0, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 5, 2, 0, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 6, 2, 0, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 4, 3, 0, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 5, 3, 0, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 6, 3, 0, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 0, 2, 3, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 0, 3, 2, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 0, 3, 3, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 2, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 8, 2, 3, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 8, 3, 2, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 8, 3, 3, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 2, 2, 5, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 3, 2, 5, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 5, 2, 5, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.GLASS_PANE.getDefaultState(), 6, 2, 5, structureBoundingBoxIn);
		this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 1, 7, 4, 1, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
		this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 4, 7, 4, 4, Blocks.PLANKS.getDefaultState(), Blocks.PLANKS.getDefaultState(), false);
		this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 3, 4, 7, 3, 4, Blocks.BOOKSHELF.getDefaultState(), Blocks.BOOKSHELF.getDefaultState(), false);
		this.setBlockState(worldIn, Blocks.PLANKS.getDefaultState(), 7, 1, 4, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST), 7, 1, 3, structureBoundingBoxIn);
		IBlockState iblockstate = Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH);
		this.setBlockState(worldIn, iblockstate, 6, 1, 4, structureBoundingBoxIn);
		this.setBlockState(worldIn, iblockstate, 5, 1, 4, structureBoundingBoxIn);
		this.setBlockState(worldIn, iblockstate, 4, 1, 4, structureBoundingBoxIn);
		this.setBlockState(worldIn, iblockstate, 3, 1, 4, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.OAK_FENCE.getDefaultState(), 6, 1, 3, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), 6, 2, 3, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.OAK_FENCE.getDefaultState(), 4, 1, 3, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), 4, 2, 3, structureBoundingBoxIn);
		this.setBlockState(worldIn, MatterOverdriveBlocks.inscriber.getDefaultState(), 7, 1, 1, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 1, 1, 0, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.AIR.getDefaultState(), 1, 2, 0, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.OAK_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.NORTH), 1, 1, 0, structureBoundingBoxIn);
		this.setBlockState(worldIn, Blocks.OAK_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.NORTH).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), 1, 2, 0, structureBoundingBoxIn);


		if (this.getBlockStateFromPos(worldIn, 1, 0, -1, structureBoundingBoxIn).getMaterial() == Material.AIR && this.getBlockStateFromPos(worldIn, 1, -1, -1, structureBoundingBoxIn).getMaterial() != Material.AIR)
		{
			this.setBlockState(worldIn, Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH), 1, 0, -1, structureBoundingBoxIn);
		}

		if (!this.hasMadeChest)
		{
			this.hasMadeChest = true;
			BlockPos pos = new BlockPos(this.getXWithOffset(1, 4), this.getYWithOffset(1), this.getZWithOffset(1, 4));

			if (boundingBox.isVecInside(pos))
			{
				worldIn.setBlockState(pos, MatterOverdriveBlocks.tritaniumCrate.getDefaultState(), 2);
				TileEntityTritaniumCrate tileentitycrate = (TileEntityTritaniumCrate)worldIn.getTileEntity(pos);
				tileentitycrate.getInventory().addItem(MatterOverdrive.questFactory.generateQuestStack(randomIn, MatterOverdrive.quests.getQuestByName("gmo")).getContract());
				ItemStack scanner = new ItemStack(MatterOverdriveItems.dataPad);
				scanner.setStackDisplayName("Mad Scientist's Data Pad");
				MatterOverdriveItems.dataPad.addToScanWhitelist(scanner, Blocks.CARROTS);
				MatterOverdriveItems.dataPad.addToScanWhitelist(scanner, Blocks.POTATOES);
				MatterOverdriveItems.dataPad.addToScanWhitelist(scanner, Blocks.WHEAT);
				scanner.getTagCompound().setBoolean("Destroys", true);
				scanner.getTagCompound().setBoolean("nogui", true);
				tileentitycrate.getInventory().addItem(scanner);
				return true;
			}
			else
			{
				return false;
			}
		}

		for (int l = 0; l < 6; ++l)
		{
			for (int k = 0; k < 9; ++k)
			{
				this.clearCurrentPositionBlocksUpwards(worldIn, k, 9, l, structureBoundingBoxIn);
				this.replaceAirAndLiquidDownwards(worldIn, Blocks.COBBLESTONE.getDefaultState(), k, -1, l, structureBoundingBoxIn);
			}
		}

		//spawnVillagers(world, boundingBox, 2, 3, 2, 1);
		spawnVillagers(worldIn, structureBoundingBoxIn, 2, 1, 2, 1);
		return true;
	}

	/**
	 * Returns the villager type to spawn in this component, based on the number of villagers already spawned.
	 */
//	TODO: switch to Forge profession
	@Override
	protected int chooseProfession(int p_180779_1_, int p_180779_2_)
	{
		return 666;
	}

	@Override
	protected void spawnVillagers(@Nonnull World world, @Nonnull StructureBoundingBox structureBoundingBox, int x, int y, int z, int count)
	{
		if (this.villagersSpawned < count)
		{
			for (int i1 = this.villagersSpawned; i1 < count; ++i1)
			{
				int j1 = this.getXWithOffset(x + i1, z);
				int k1 = this.getYWithOffset(y);
				int l1 = this.getZWithOffset(x + i1, z);

				if (!structureBoundingBox.isVecInside(new Vec3i(j1, k1, l1)))
				{
					break;
				}

				++this.villagersSpawned;
				EntityVillagerMadScientist madScientist = new EntityVillagerMadScientist(world);
				madScientist.setLocationAndAngles((double)j1 + 0.5D, (double)k1, (double)l1 + 0.5D, 0.0F, 0.0F);
				world.spawnEntityInWorld(madScientist);
			}
		}
	}
}
