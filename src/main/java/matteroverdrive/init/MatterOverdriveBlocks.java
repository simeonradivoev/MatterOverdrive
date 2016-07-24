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
import matteroverdrive.blocks.includes.MOBlockOre;
import matteroverdrive.blocks.world.DilithiumOre;
import matteroverdrive.items.includes.MOMachineBlockItem;
import matteroverdrive.util.IConfigSubscriber;
import matteroverdrive.util.MOLog;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemColored;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.shadowfacts.shadowmc.block.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public class MatterOverdriveBlocks extends ModBlocks
{
	public final MaterialTritanium TRITANIUM = new MaterialTritanium(MapColor.CLAY);

	private int registeredCount = 0;

//	Materials
	public DilithiumOre dilithium_ore;
	public MOBlockOre tritaniumOre;
	public MOBlockOre tritanium_block;

//	Crafting
	public MOBlock machine_hull;

//	Matter Network
	public BlockDecomposer decomposer;
	public BlockMatterRecycler recycler;
	public BlockReplicator replicator;
	public BlockMatterPipe matter_pipe;
	public BlockMatterPipe heavy_matter_pipe;
	public BlockNetworkPipe network_pipe;
	public BlockNetworkRouter network_router;
	public BlockMatterAnalyzer matter_analyzer;
	public BlockPatternMonitor pattern_monitor;
	public BlockPatternStorage pattern_storage;
	public BlockNetworkSwitch network_switch;

//	Energy Generation
	public BlockSolarPanel solar_panel;

//	Gravitational Anomaly / Fusion Reactor
	public BlockGravitationalAnomaly gravitational_anomaly;
	public BlockGravitationalStabilizer gravitational_stabilizer;
	public BlockFusionReactorController fusion_reactor_controller;
	public BlockFusionReactorCoil fusion_reactor_coil;
	public BlockFusionReactorIO fusionReactorIO;

//	Fluids
	public BlockFluidMatterPlasma blockMatterPlasma;
	public BlockFluidFinite blockMoltenTritanium;

//	Storage
	public BlockTritaniumCrate tritaniumCrate;
	public BlockTritaniumCrate tritaniumCrateYellow;

//	Machines
	public BlockInscriber inscriber;
	public BlockContractMarket contractMarket;
	public BlockAndroidSpawner androidSpawner;
	public BlockSpacetimeAccelerator spacetimeAccelerator;
	public BlockPylon pylon;

//	Misc
	public BlockTransporter transporter;
	public BlockStarMap starMap;
	public BlockHoloSign holoSign;
	public BlockWeaponStation weapon_station;
	public BlockAndroidStation androidStation;
	public BlockChargingStation chargingStation;

//	Decorative
	public ForceGlass forceGlass;
	public BlockDecorative decorative_stripes;
	public BlockDecorative decorative_coils;
	public BlockDecorative decorative_clean;
	public BlockDecorative decorative_vent_dark;
	public BlockDecorative decorative_vent_bright;
	public BlockDecorative decorative_holo_matrix;
	public BlockDecorative decorative_tritanium_plate;
	public BlockDecorative decorative_tritanium_plate_stripe;
	public BlockDecorative decorative_carbon_fiber_plate;
	public BlockDecorative decorative_matter_tube;
	public BlockDecorative decorative_beams;
	public BlockDecorative decorative_floor_tiles;
	public BlockDecorative decorative_floor_tile_white;
	public BlockDecorative decorative_floor_tiles_green;
	public BlockDecorative decorative_floor_noise;
	public BlockDecorative decorative_white_plate;
	public BlockDecorative decorative_separator;
	public BlockDecorative decorative_tritanium_lamp;
	public BlockDecorative decorative_tritanium_plate_colored;
	public BlockDecorative decorative_engine_exhaust_plasma;

//	Alien world
	public BlockTallGrassAlien alienTallGrass;
	public BlockFlowerAlien alienFlower;
	public BlockLogAlien alienLog;
	public BlockLeavesAlien alienLeaves;
	public BlockFalling alienSand;
	public BlockFalling alienGravel;
	public BlockStoneAlien alienStone;

//	Internal
	public BlockBoundingBox boundingBox;

	public static final List<IRecipe> recipes = new ArrayList<>();

	@Override
	public void init()
	{
		MOLog.info("Registering blocks");

//		Materials
		dilithium_ore = register(new DilithiumOre(Material.ROCK, "dilithium_ore", "oreDilithium"));
		tritaniumOre = register(new MOBlockOre(Material.ROCK, "tritanium_ore", "oreTritanium"));
		tritaniumOre.setHardness(8f).setResistance(5.0F).setHarvestLevel("pickaxe", 2);
		tritanium_block = register(new MOBlockOre(TRITANIUM, "tritanium_block", "blockTritanium"));
		tritanium_block.setHardness(15.0F).setResistance(10.0F).setHarvestLevel("pickaxe", 2);

//		Crafting
		machine_hull = register(new MOBlock(TRITANIUM, "machine_hull"));
		machine_hull.setHardness(15.0F).setResistance(8.0F).setHarvestLevel("pickaxe", 2);

//		Matter Network
		decomposer = register(new BlockDecomposer(TRITANIUM, "decomposer"));
		recycler = register(new BlockMatterRecycler(TRITANIUM, "matter_recycler"));
		replicator = register(new BlockReplicator(TRITANIUM, "replicator"));
		matter_pipe = register(new BlockMatterPipe(TRITANIUM, "matter_pipe"));
		heavy_matter_pipe = register(new BlockHeavyMatterPipe(TRITANIUM, "heavy_matter_pipe"));
		network_pipe = register(new BlockNetworkPipe(TRITANIUM, "network_pipe"));
		network_router = register(new BlockNetworkRouter(TRITANIUM, "network_router"));
		matter_analyzer = register(new BlockMatterAnalyzer(TRITANIUM, "matter_analyzer"));
		pattern_monitor = register(new BlockPatternMonitor(TRITANIUM, "pattern_monitor"));
		pattern_storage = register(new BlockPatternStorage(TRITANIUM, "pattern_storage"));
		network_switch = register(new BlockNetworkSwitch(TRITANIUM, "network_switch"));

//		Energy Generation
		solar_panel = register(new BlockSolarPanel(TRITANIUM, "solar_panel"));

//		Gravitational Anomaly / Fusion Reactor
		gravitational_anomaly = register(new BlockGravitationalAnomaly(Material.PORTAL, "gravitational_anomaly"));
		gravitational_stabilizer = register(new BlockGravitationalStabilizer(TRITANIUM, "gravitational_stabilizer"));
		fusion_reactor_controller = register(new BlockFusionReactorController(TRITANIUM, "fusion_reactor_controller"));
		fusion_reactor_coil = register(new BlockFusionReactorCoil(TRITANIUM, "fusion_reactor_coil"));
		fusionReactorIO = register(new BlockFusionReactorIO(TRITANIUM, "fusion_reactor_io"));

//		Fluids
		blockMatterPlasma = register(new BlockFluidMatterPlasma(MatterOverdriveFluids.matterPlasma, Material.WATER));
		blockMoltenTritanium = register((BlockFluidFinite)new BlockFluidFinite(MatterOverdriveFluids.moltenTritanium, Material.LAVA).setRegistryName(new ResourceLocation(Reference.MOD_ID, "molten_tritanium")));

//		Storage
		tritaniumCrate = register(new BlockTritaniumCrate(TRITANIUM, "tritanium_crate"));
		tritaniumCrateYellow = register(new BlockTritaniumCrate(TRITANIUM, "tritanium_crate_yellow"));

//		Machines
		inscriber = register(new BlockInscriber(TRITANIUM, "inscriber"));
		contractMarket = register(new BlockContractMarket(TRITANIUM, "contract_market"));
		androidSpawner = register(new BlockAndroidSpawner(TRITANIUM, "android_spawner"));
		spacetimeAccelerator = register(new BlockSpacetimeAccelerator(TRITANIUM, "spacetime_accelerator"));
		pylon = register(new BlockPylon(TRITANIUM, "pylon"));

//		Misc
		transporter = register(new BlockTransporter(TRITANIUM, "transporter"));
		starMap = register(new BlockStarMap(TRITANIUM, "star_map"));
		holoSign = register(new BlockHoloSign(TRITANIUM, "holo_sign"));
		weapon_station = register(new BlockWeaponStation(TRITANIUM, "weapon_station"));
		androidStation = register(new BlockAndroidStation(TRITANIUM, "android_station"));
		chargingStation = register(new BlockChargingStation(TRITANIUM, "charging_station"));

//		Decorative
		forceGlass = register(new ForceGlass(Material.GLASS, "force_glass"));
		decorative_stripes = register(new BlockDecorative(TRITANIUM, "decorative.stripes", 5, 1, 8, 0xd4b108));
		decorative_coils = register(new BlockDecorative(TRITANIUM, "decorative.coils", 5, 1, 8, 0xb6621e));
		decorative_clean = register(new BlockDecorative(TRITANIUM, "decorative.clean", 5, 1, 8, 0x3b484b));
		decorative_vent_dark = register(new BlockDecorative(TRITANIUM, "decorative.vent.dark", 5, 1, 8, 0x32393c));
		decorative_vent_bright = register(new BlockDecorative(TRITANIUM, "decorative.vent.bright", 5, 1, 8, 0x3f4b4e));
		decorative_holo_matrix = register(new BlockDecorative(TRITANIUM, "decorative.holo_matrix", 3, 1, 4, 0x323b3a));
		decorative_tritanium_plate = register(new BlockDecorative(TRITANIUM, "decorative.tritanium_plate", 10, 1, 10, 0x475459));
		decorative_tritanium_plate_stripe = register(new BlockDecorative(TRITANIUM, "decorative.tritanium_plate_stripe", 10, 1, 10, 0x576468));
		decorative_carbon_fiber_plate = register(new BlockDecorative(TRITANIUM, "decorative.carbon_fiber_plate", 10, 1, 12, 0x1c1f20));
		decorative_matter_tube = register(new BlockDecorativeRotated(Material.GLASS, "decorative.matter_tube", 3, 1, 4, 0x5088a5));
		decorative_beams = register(new BlockDecorativeRotated(TRITANIUM, "decorative.beams", 8, 1, 8, 0x1e2220));
		decorative_floor_tiles = register(new BlockDecorative(Material.CLAY, "decorative.floor_tiles", 4, 0, 4, 0x958d7c));
		decorative_floor_tile_white = register(new BlockDecorative(Material.CLAY, "decorative.floor_tile_white", 4, 0, 4, 0xa3a49c));
		decorative_floor_tiles_green = register(new BlockDecorative(Material.CLAY, "decorative.floor_tiles_green", 4, 0, 4, 0x53593f));
		decorative_floor_noise = register(new BlockDecorative(Material.CLAY, "decorative.floor_noise", 4, 0, 4, 0x7f7e7b));
		decorative_white_plate = register(new BlockDecorative(TRITANIUM, "decorative.white_plate", 8, 1, 8, 0xe3e3e3));
		decorative_separator = register(new BlockDecorativeRotated(TRITANIUM, "decorative.separator", 8, 1, 8, 0x303837));
		decorative_tritanium_lamp = register(new BlockDecorativeRotated(TRITANIUM, "decorative.tritanium_lamp", 2, 1, 4, 0xd4f8f5));
		decorative_tritanium_lamp.setLightLevel(1);
		decorative_tritanium_plate_colored = register(new BlockDecorativeColored(TRITANIUM, "decorative.tritanium_plate_colored", 10, 1, 10, 0x505050));
		decorative_engine_exhaust_plasma = register(new BlockDecorative(Material.CACTUS, "decorative.engine_exhaust_plasma", 1, 1, 1, 0x387c9e));
		decorative_engine_exhaust_plasma.setLightLevel(1);

//		Alien world
		alienTallGrass = register(new BlockTallGrassAlien("alien_tall_grass"));
		alienTallGrass.setHardness(0);
		alienFlower = register(new BlockFlowerAlien("alien_flower"));
		alienFlower.setHardness(0);
		alienLog = register(new BlockLogAlien("alien_log"));
		alienLeaves = register(new BlockLeavesAlien("alien_leaves"));
		alienSand = register((BlockFalling)new BlockFalling(Material.SAND).setRegistryName(new ResourceLocation(Reference.MOD_ID, "alien_sand")));
		alienSand.setHardness(1).setUnlocalizedName("alien_sand").setCreativeTab(MatterOverdrive.tabMatterOverdrive_decorative);
		alienGravel = register((BlockFalling)new BlockFalling(Material.SAND).setRegistryName(new ResourceLocation(Reference.MOD_ID, "alien_gravel")));
		alienGravel.setHardness(1).setUnlocalizedName("alien_gravel").setCreativeTab(MatterOverdrive.tabMatterOverdrive_decorative);
		alienStone = register(new BlockStoneAlien("alien_stone", Material.ROCK));
		alienStone.setHardness(1.5f).setResistance(10.0f).setCreativeTab(MatterOverdrive.tabMatterOverdrive_decorative);

//		Internal
		boundingBox = register(new BlockBoundingBox("bounding_box"));

		MOLog.info("Finished registering blocks");
		MOLog.info("Registered %d blocks", registeredCount);
	}

	@Override
	protected <T extends Block> T register(T block)
	{
		ItemBlock itemBlock;

		if (block instanceof MOBlockMachine)
		{
			itemBlock = new MOMachineBlockItem(block);
		}
		else if (block instanceof BlockDecorativeColored)
		{
			itemBlock = new ItemColored(block, false);
			itemBlock.setRegistryName(block.getRegistryName());
		}
		else
		{
			itemBlock = new ItemBlock(block);
			itemBlock.setRegistryName(block.getRegistryName());
		}
		return register(block, itemBlock);
	}

	@Override
	protected <T extends Block> T register(T block, ItemBlock itemBlock)
	{
		super.register(block, itemBlock);
		if (block instanceof IConfigSubscriber) {
			MatterOverdrive.configHandler.subscribe((IConfigSubscriber)block);
		}
		registeredCount++;
		return block;
	}
}
