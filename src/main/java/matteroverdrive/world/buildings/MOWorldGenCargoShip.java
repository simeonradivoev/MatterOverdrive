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

package matteroverdrive.world.buildings;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.blocks.BlockDecorative;
import matteroverdrive.data.quest.logic.QuestLogicBlockInteract;
import matteroverdrive.init.MatterOverdriveBlocks;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.tile.TileEntityTritaniumCrate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Simeon on 11/30/2015.
 */
public class MOWorldGenCargoShip extends MOWorldGenBuilding<MOWorldGenCargoShip.Worker>
{
	private static final int MIN_DISTANCE_APART = 4096;

	public MOWorldGenCargoShip(String name)
	{
		super(name, new ResourceLocation(Reference.PATH_WORLD_TEXTURES + "cargo_ship.png"), 58, 23);
		for (BlockDecorative blockDecorative : BlockDecorative.decorativeBlocks)
		{
			addMapping(blockDecorative.getBlockColor(0), blockDecorative);
		}
		addMapping(0xdb9c3a, MatterOverdriveBlocks.holoSign);
		addMapping(0x5fffbe, MatterOverdriveBlocks.transporter);
		addMapping(0xd2fb50, MatterOverdriveBlocks.forceGlass);
		addMapping(0xdc01d8, Blocks.wooden_pressure_plate);
		addMapping(0xfc6b34, new BlockMapping(true, Blocks.gold_ore, Blocks.iron_ore, Blocks.coal_ore, MatterOverdriveBlocks.tritaniumOre));
		addMapping(0xd1626, MatterOverdriveBlocks.fusionReactorIO);
		addMapping(0x1b2ff7, MatterOverdriveBlocks.network_pipe);
		addMapping(0x1f2312, MatterOverdriveBlocks.tritaniumCrate);
		addMapping(0xab4824, Blocks.oak_fence);
		addMapping(0x68d738, Blocks.carpet);
		addMapping(0xbdea8f, Blocks.ladder);
		addMapping(0xeff73d, MatterOverdriveBlocks.network_switch);
		addMapping(0xa8ed1c, MatterOverdriveBlocks.heavy_matter_pipe);
		addMapping(0x4b285d, Blocks.oak_stairs);
		addMapping(0xcfd752, MatterOverdriveBlocks.network_router);
		addMapping(0x4d8dd3, MatterOverdriveBlocks.pattern_monitor);
		addMapping(0x6b3534, Blocks.bed);
		addMapping(0xff00ff, Blocks.air);
		addMapping(0x69960c, MatterOverdriveBlocks.tritaniumCrate);
	}

	@Override
	protected void onGeneration(Random random, World world, BlockPos pos, Worker worker)
	{
		if (worker.contractDestination != null)
		{
			TileEntity tileEntity = world.getTileEntity(worker.contractDestination);
			if (tileEntity instanceof TileEntityTritaniumCrate)
			{
				ItemStack contract = worker.contractQuest.getContract();
				((TileEntityTritaniumCrate)tileEntity).getInventory().addItem(contract);
			}
		}
	}

	@Override
	public int getMetaFromColor(int color, Random random)
	{
		return 255 - getAlphaFromColor(color);
	}

	@Override
	public boolean isLocationValid(World world, BlockPos pos)
	{
		pos = new BlockPos(pos.getX(), Math.min(pos.getY() + 86, world.getHeight() - 18), pos.getZ());
		return world.getBlockState(pos).getBlock() == Blocks.air
				&& world.getBlockState(pos.add(layerWidth, 0, 0)) == Blocks.air
				&& world.getBlockState(pos.add(0, 0, layerHeight)) == Blocks.air
				&& world.getBlockState(pos.add(layerWidth, 0, layerHeight)) == Blocks.air
				&& world.getBlockState(pos.add(0, 16, 0)) == Blocks.air
				&& world.getBlockState(pos.add(layerWidth, 16, 0)) == Blocks.air
				&& world.getBlockState(pos.add(0, 16, layerHeight)) == Blocks.air
				&& world.getBlockState(pos.add(layerWidth, 16, layerHeight)) == Blocks.air;
	}

	@Override
	public boolean shouldGenerate(Random random, World world, BlockPos pos)
	{
		return (world.provider.getDimension() == 0 || world.provider.getDimension() == 1) && isFarEnoughFromOthers(world, pos.getX(), pos.getZ(), MIN_DISTANCE_APART) && random.nextDouble() < 0.1;
	}

	@Override
	public void onGenerationWorkerCreated(Worker worker)
	{
		super.onGenerationWorkerCreated(worker);
		setyOffset(86);
	}

	@Override
	public Worker getNewWorkerInstance()
	{
		return new Worker();
	}

	@Override
	public void onBlockPlace(World world, IBlockState blockState, BlockPos pos, Random random, int color, Worker worker)
	{
		if (colorsMatch(color, 0x69960c))
		{
			TileEntity tritaniumCrate = world.getTileEntity(pos);
			if (tritaniumCrate instanceof TileEntityTritaniumCrate)
			{
				worker.setQuestPos(pos);
				ItemStack itemStack = new ItemStack(MatterOverdriveItems.isolinear_circuit).setStackDisplayName("Trade Route Agreement");
				((TileEntityTritaniumCrate)tritaniumCrate).getInventory().addItem(itemStack);
			}
		}
		else if (colorsMatch(color, 0x1f2312))
		{
			TileEntity tritaniumCrate = world.getTileEntity(pos);
			if (tritaniumCrate instanceof TileEntityTritaniumCrate)
			{
				if (!worker.questAdded)
				{
					worker.markQuestAdded();
					worker.contractDestination = pos;
				}
			}
		}
	}

	static class Worker extends MOWorldGenBuilding.WorldGenBuildingWorker
	{
		private QuestStack contractQuest;
		private BlockPos contractDestination;
		private boolean questAdded;

		public Worker()
		{
			contractQuest = new QuestStack(MatterOverdrive.quests.getQuestByName("trade_route"));
		}

		private void setQuestPos(BlockPos pos)
		{
			QuestLogicBlockInteract.setBlockPosition(contractQuest, pos);
		}

		private void markQuestAdded()
		{
			this.questAdded = true;
		}
	}
}
