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
import matteroverdrive.blocks.BlockTritaniumCrate;
import matteroverdrive.blocks.BlockWeaponStation;
import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.init.MatterOverdriveBlocks;
import matteroverdrive.tile.TileEntityHoloSign;
import matteroverdrive.tile.TileEntityWeaponStation;
import matteroverdrive.util.MOInventoryHelper;
import matteroverdrive.util.WeaponFactory;
import matteroverdrive.world.MOImageGen;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Simeon on 11/26/2015.
 */
public class MOWorldGenCrashedSpaceShip extends MOWorldGenBuilding
{
	private static final int MIN_DISTANCE_APART = 256;
	private final String[] holoTexts;

	public MOWorldGenCrashedSpaceShip(String name)
	{
		super(name, new ResourceLocation(Reference.PATH_WORLD_TEXTURES + "crashed_space_ship.png"), 11, 35);
		holoTexts = new String[] {"Critical\nError", "Contacting\nSection 9", "System\nFailure", "Emergency\nPower\nOffline", "System\nReboot\nFailure", "Help Me", "I Need\nWater"};
		setyOffset(-1);
		addMapping(0x38c8df, MatterOverdriveBlocks.decorative_clean);
		addMapping(0x187b8b, MatterOverdriveBlocks.decorative_vent_bright);
		addMapping(0xaa38df, MatterOverdriveBlocks.forceGlass);
		addMapping(0x00ff78, Blocks.grass);
		addMapping(0xd8ff00, MatterOverdriveBlocks.holoSign);
		addMapping(0xaccb00, MatterOverdriveBlocks.holoSign);
		addMapping(0x3896df, MatterOverdriveBlocks.decorative_tritanium_plate);
		addMapping(0xdfd938, MatterOverdriveBlocks.decorative_tritanium_plate_stripe);
		addMapping(0x5d89ab, MatterOverdriveBlocks.decorative_holo_matrix);
		addMapping(0x77147d, MatterOverdriveBlocks.weapon_station);
		addMapping(0xb04a90, MatterOverdriveBlocks.tritaniumCrate);
		addMapping(0x94deea, MatterOverdriveBlocks.decorative_separator);
		addMapping(0xff9c00, MatterOverdriveBlocks.decorative_coils);
		addMapping(0xaca847, MatterOverdriveBlocks.decorative_matter_tube);
		addMapping(0x0c3b60, MatterOverdriveBlocks.decorative_carbon_fiber_plate);
		addMapping(0xc5ced0, Blocks.air);
	}

	@Override
	public void onBlockPlace(World world, IBlockState state, BlockPos pos, Random random, int color, MOImageGen.ImageGenWorker worker)
	{
		if (state.getBlock() == MatterOverdriveBlocks.holoSign)
		{
			if (colorsMatch(color, 0xd8ff00))
			{
				world.setBlockState(pos, state.withProperty(MOBlock.PROPERTY_DIRECTION, EnumFacing.EAST), 3);
			}
			else if (colorsMatch(color, 0xaccb00))
			{
				world.setBlockState(pos, state.withProperty(MOBlock.PROPERTY_DIRECTION, EnumFacing.WEST), 3);
			}
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity instanceof TileEntityHoloSign)
			{
				if (random.nextInt(100) < 30)
				{
					((TileEntityHoloSign)tileEntity).setText(holoTexts[random.nextInt(holoTexts.length)]);
				}
			}
		}
		else if (state.getBlock() instanceof BlockTritaniumCrate)
		{
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity instanceof IInventory)
			{
				// TODO: 3/26/2016 Find how to access Chest Gen Hooks
				//WeightedRandomChestContent.generateDispenserContents(random, ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).getItems(random), (IInventory) tileEntity, random.nextInt(10) + 10);
				QuestStack questStack = MatterOverdrive.questFactory.generateQuestStack(random, MatterOverdrive.quests.getQuestByName("crash_landing"));
				questStack.getTagCompound().setLong("pos", pos.toLong());
				MOInventoryHelper.insertItemStackIntoInventory((IInventory)tileEntity, questStack.getContract(), EnumFacing.DOWN);
			}

		}
		else if (state.getBlock() instanceof BlockWeaponStation)
		{
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity instanceof TileEntityWeaponStation)
			{
				if (random.nextInt(200) < 10)
				{
					((TileEntityWeaponStation)tileEntity).setInventorySlotContents(((TileEntityWeaponStation)tileEntity).INPUT_SLOT, MatterOverdrive.weaponFactory.getRandomDecoratedEnergyWeapon(new WeaponFactory.WeaponGenerationContext(3, null, true)));
				}
			}
		}
	}

	@Override
	public WorldGenBuildingWorker getNewWorkerInstance()
	{
		return new WorldGenBuildingWorker();
	}

	@Override
	protected void onGeneration(Random random, World world, BlockPos pos, WorldGenBuildingWorker worker)
	{

	}

	@Override
	public boolean shouldGenerate(Random random, World world, BlockPos pos)
	{
		return world.provider.getDimension() == 0 && isFarEnoughFromOthers(world, pos.getX(), pos.getZ(), MIN_DISTANCE_APART);
	}
}
