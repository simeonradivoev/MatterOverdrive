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

package matteroverdrive.init;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.blocks.*;
import matteroverdrive.blocks.alien.*;
import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.blocks.includes.MOBlockMachine;
import matteroverdrive.blocks.world.DilithiumOre;
import matteroverdrive.items.includes.MOMachineBlockItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MatterOverdriveBlocks
{
	public final static MaterialTritanium tritanium = new MaterialTritanium(MapColor.clayColor);
	public final static BlockReplicator replicator = new BlockReplicator(tritanium, "replicator");
	public final static BlockDecomposer decomposer = new BlockDecomposer(tritanium, "decomposer");
	public final static BlockTransporter transporter = new BlockTransporter(tritanium, "transporter");
	public final static BlockMatterPipe matter_pipe = new BlockMatterPipe(tritanium, "matter_pipe");
	public final static BlockNetworkPipe network_pipe = new BlockNetworkPipe(tritanium, "network_pipe");
	public final static BlockNetworkRouter network_router = new BlockNetworkRouter(tritanium, "network_router");
	public final static BlockMatterAnalyzer matter_analyzer = new BlockMatterAnalyzer(tritanium, "matter_analyzer");
	public final static DilithiumOre dilithium_ore = new DilithiumOre(Material.rock, "dilithium_ore");
	public final static MOBlock tritaniumOre = (MOBlock)new MOBlock(Material.rock, "tritanium_ore").setHardness(8f).setResistance(5.0F);
	public final static MOBlock tritanium_block = (MOBlock)new MOBlock(tritanium, "tritanium_block").setHardness(15.0F).setResistance(10.0F);
	public final static MOBlock machine_hull = (MOBlock)new MOBlock(tritanium, "machine_hull").setHardness(15.0F).setResistance(8.0F);
	public final static BlockPatternStorage pattern_storage = new BlockPatternStorage(tritanium, "pattern_storage");
	;
	public final static BlockSolarPanel solar_panel = new BlockSolarPanel(tritanium, "solar_panel");
	public final static BlockWeaponStation weapon_station = new BlockWeaponStation(tritanium, "weapon_station");
	public final static BlockPatternMonitor pattern_monitor = new BlockPatternMonitor(tritanium, "pattern_monitor");
	public final static BlockNetworkSwitch network_switch = new BlockNetworkSwitch(tritanium, "network_switch");
	public final static BlockGravitationalAnomaly gravitational_anomaly = new BlockGravitationalAnomaly(Material.portal, "gravitational_anomaly");
	public final static BlockGravitationalStabilizer gravitational_stabilizer = new BlockGravitationalStabilizer(tritanium, "gravitational_stabilizer");
	public final static BlockFusionReactorController fusion_reactor_controller = new BlockFusionReactorController(tritanium, "fusion_reactor_controller");
	public final static BlockFusionReactorCoil fusion_reactor_coil = new BlockFusionReactorCoil(tritanium, "fusion_reactor_coil");
	public final static BlockMatterRecycler recycler = new BlockMatterRecycler(tritanium, "matter_recycler");
	public final static BlockAndroidStation androidStation = new BlockAndroidStation(tritanium, "android_station");
	public final static BlockStarMap starMap = new BlockStarMap(tritanium, "star_map");
	public final static BlockChargingStation chargingStation = new BlockChargingStation(tritanium, "charging_station");
	public final static BlockMatterPipe heavy_matter_pipe = new BlockHeavyMatterPipe(tritanium, "heavy_matter_pipe");
	public final static BlockHoloSign holoSign = new BlockHoloSign(tritanium, "holo_sign");
	public final static ForceGlass forceGlass = new ForceGlass(Material.glass, "force_glass");
	public final static BlockFluidMatterPlasma blockMatterPlasma = (BlockFluidMatterPlasma)new BlockFluidMatterPlasma(MatterOverdriveFluids.matterPlasma, Material.water).setRegistryName(new ResourceLocation(Reference.MOD_ID, "matter_plasma"));
	public final static BlockFluidFinite blockMoltenTritanium = (BlockFluidFinite)new BlockFluidFinite(MatterOverdriveFluids.moltenTritanium, Material.lava).setRegistryName(new ResourceLocation(Reference.MOD_ID, "molten_tritanium"));
	public final static BlockBoundingBox boundingBox = new BlockBoundingBox(Material.air, "bounding_box");
	public final static BlockFusionReactorIO fusionReactorIO = new BlockFusionReactorIO(tritanium, "fusion_reactor_io");
	public final static BlockTritaniumCrate tritaniumCrate = new BlockTritaniumCrate(tritanium, "tritanium_crate");
	public final static BlockTritaniumCrate tritaniumCrateYellow = new BlockTritaniumCrate(tritanium, "tritanium_crate_yellow");
	public final static BlockInscriber inscriber = new BlockInscriber(tritanium, "inscriber");
	public final static BlockContractMarket contractMarket = new BlockContractMarket(tritanium, "contract_market");
	public final static BlockAndroidSpawner androidSpawner = new BlockAndroidSpawner(tritanium, "android_spawner");
	public final static BlockSpacetimeAccelerator spacetimeAccelerator = new BlockSpacetimeAccelerator(tritanium, "spacetime_accelerator");
	public final static BlockPylon pylon = new BlockPylon(tritanium, "pylon");

	public final static BlockDecorative decorative_stripes = new BlockDecorative(tritanium, "decorative.stripes", 5, 1, 8, 0xd4b108);
	public final static BlockDecorative decorative_coils = new BlockDecorative(tritanium, "decorative.coils", 5, 1, 8, 0xb6621e);
	public final static BlockDecorative decorative_clean = new BlockDecorative(tritanium, "decorative.clean", 5, 1, 8, 0x3b484b);
	public final static BlockDecorative decorative_vent_dark = new BlockDecorative(tritanium, "decorative.vent.dark", 5, 1, 8, 0x32393c);
	public final static BlockDecorative decorative_vent_bright = new BlockDecorative(tritanium, "decorative.vent.bright", 5, 1, 8, 0x3f4b4e);
	public final static BlockDecorative decorative_holo_matrix = new BlockDecorative(tritanium, "decorative.holo_matrix", 3, 1, 4, 0x323b3a);
	public final static BlockDecorative decorative_tritanium_plate = new BlockDecorative(tritanium, "decorative.tritanium_plate", 10, 1, 10, 0x475459);
	public final static BlockDecorative decorative_carbon_fiber_plate = new BlockDecorative(tritanium, "decorative.carbon_fiber_plate", 10, 1, 12, 0x1c1f20);
	public final static BlockDecorative decorative_matter_tube = new BlockDecorativeRotated(Material.glass, "decorative.matter_tube", 3, 1, 4, 0x5088a5);
	public final static BlockDecorative decorative_beams = new BlockDecorativeRotated(tritanium, "decorative.beams", 8, 1, 8, 0x1e2220);
	public final static BlockDecorative decorative_floor_tiles = new BlockDecorative(Material.clay, "decorative.floor_tiles", 4, 0, 4, 0x958d7c);
	public final static BlockDecorative decorative_floor_tiles_green = new BlockDecorative(Material.clay, "decorative.floor_tiles_green", 4, 0, 4, 0x53593f);
	public final static BlockDecorative decorative_floor_noise = new BlockDecorative(Material.clay, "decorative.floor_noise", 4, 0, 4, 0x7f7e7b);
	public final static BlockDecorative decorative_tritanium_plate_stripe = new BlockDecorative(tritanium, "decorative.tritanium_plate_stripe", 10, 1, 10, 0x576468);
	public final static BlockDecorative decorative_floot_tile_white = new BlockDecorative(Material.clay, "decorative.floor_tile_white", 4, 0, 4, 0xa3a49c);
	public final static BlockDecorative decorative_white_plate = new BlockDecorative(tritanium, "decorative.white_plate", 8, 1, 8, 0xe3e3e3);
	public final static BlockDecorative decorative_separator = new BlockDecorativeRotated(tritanium, "decorative.separator", 8, 1, 8, 0x303837);
	public final static BlockDecorative decorative_tritanium_lamp = (BlockDecorative)new BlockDecorativeRotated(tritanium, "decorative.tritanium_lamp", 2, 1, 4, 0xd4f8f5).setLightLevel(1);
	public final static BlockDecorative decorative_tritanium_plate_colored = new BlockDecorativeColored(tritanium, "decorative.tritanium_plate_colored", 10, 1, 10, 0x505050);
	public final static BlockDecorative decorative_engine_exhaust_plasma = (BlockDecorative)new BlockDecorative(Material.cactus, "decorative.engine_exhaust_plasma", 1, 1, 1, 0x387c9e).setLightLevel(1);

	// TODO: 3/26/2016 Find how to set block step sounds
	public final static BlockTallGrassAlien alienTallGrass = (BlockTallGrassAlien)new BlockTallGrassAlien().setHardness(0).setUnlocalizedName("alien_tall_grass").setRegistryName(new ResourceLocation(Reference.MOD_ID, "alien_tall_grass"));
	public final static BlockFlowerAlien alienFlower = (BlockFlowerAlien)new BlockFlowerAlien().setHardness(0).setUnlocalizedName("alien_flower").setRegistryName(new ResourceLocation(Reference.MOD_ID, "alien_flower"));
	public final static BlockLogAlien alienLog = (BlockLogAlien)new BlockLogAlien().setUnlocalizedName("alien_log").setRegistryName(new ResourceLocation(Reference.MOD_ID, "alien_log"));
	public final static BlockLeavesAlien alienLeaves = (BlockLeavesAlien)new BlockLeavesAlien().setUnlocalizedName("alien_leaves").setRegistryName(new ResourceLocation(Reference.MOD_ID, "alien_leaves"));
	public final static BlockFalling alienSand = (BlockFalling)new BlockFalling(Material.sand).setHardness(1).setUnlocalizedName("alien_sand").setCreativeTab(MatterOverdrive.tabMatterOverdrive_decorative).setRegistryName(new ResourceLocation(Reference.MOD_ID, "alien_sand"));
	public final static BlockFalling alienGravel = (BlockFalling)new BlockFalling(Material.sand).setHardness(1).setUnlocalizedName("alien_gravel").setCreativeTab(MatterOverdrive.tabMatterOverdrive_decorative).setRegistryName(new ResourceLocation(Reference.MOD_ID, "alien_gravel"));
	public final static BlockStoneAlien alienStone = (BlockStoneAlien)new BlockStoneAlien(Material.rock).setHardness(1.5f).setResistance(10.0f).setCreativeTab(MatterOverdrive.tabMatterOverdrive_decorative).setUnlocalizedName("alien_stone").setRegistryName(new ResourceLocation(Reference.MOD_ID, "alien_stone"));

	public static final List<IRecipe> recipes = new ArrayList<>();

	public static void init(FMLPreInitializationEvent event)
	{
		tritaniumOre.setHarvestLevel("pickaxe", 2);
		tritanium_block.setHarvestLevel("pickaxe", 2);
		machine_hull.setHarvestLevel("pickaxe", 2);
	}

	public static void register(FMLInitializationEvent event)
	{
		for (Field field : MatterOverdriveBlocks.class.getFields())
		{
			if (Block.class.isAssignableFrom(field.getType()))
			{
				try
				{
					Block block = (Block)field.get(null);
					GameRegistry.register(block);
					if (block instanceof MOBlockMachine)
					{
						GameRegistry.register(new MOMachineBlockItem(block), block.getRegistryName());
					}
					else
					{
						GameRegistry.register(new ItemBlock(block), block.getRegistryName());
					}

					if (field.get(null) instanceof ITileEntityProvider)
					{
						GameRegistry.registerTileEntity(((ITileEntityProvider)block).createNewTileEntity(null, 0).getClass(), block.getRegistryName().toString());
					}
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
		}

		MatterOverdrive.configHandler.subscribe(replicator);
		MatterOverdrive.configHandler.subscribe(transporter);
		MatterOverdrive.configHandler.subscribe(decomposer);
		MatterOverdrive.configHandler.subscribe(matter_analyzer);
		MatterOverdrive.configHandler.subscribe(pattern_storage);
		MatterOverdrive.configHandler.subscribe(solar_panel);
		MatterOverdrive.configHandler.subscribe(pattern_monitor);
		MatterOverdrive.configHandler.subscribe(network_switch);
		MatterOverdrive.configHandler.subscribe(gravitational_anomaly);
		MatterOverdrive.configHandler.subscribe(gravitational_stabilizer);
		MatterOverdrive.configHandler.subscribe(fusion_reactor_controller);
		MatterOverdrive.configHandler.subscribe(recycler);
		MatterOverdrive.configHandler.subscribe(androidStation);
		MatterOverdrive.configHandler.subscribe(starMap);
		MatterOverdrive.configHandler.subscribe(chargingStation);
		MatterOverdrive.configHandler.subscribe(spacetimeAccelerator);

		OreDictionary.registerOre("oreTritanium", tritaniumOre);
		OreDictionary.registerOre("oreDilithium", dilithium_ore);
		OreDictionary.registerOre("blockTritanium", tritanium_block);
	}
}
