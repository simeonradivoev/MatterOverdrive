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

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.blocks.*;
import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.blocks.world.DilithiumOre;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class MatterOverdriveBlocks {
    public static BlockReplicator replicator;
    public static BlockDecomposer decomposer;
    public static BlockTransporter transporter;
    public static BlockMatterPipe matter_pipe;
    public static BlockNetworkPipe network_pipe;
    public static BlockNetworkRouter network_router;
    public static BlockMatterAnalyzer matter_analyzer;
    public static DilithiumOre dilithium_ore;
    public static MOBlock tritaniumOre;
    public static MOBlock tritanium_block;
    public static MOBlock machine_hull;
    public static BlockPatternStorage pattern_storage;
    public static BlockSolarPanel solar_panel;
    public static BlockWeaponStation weapon_station;
    public static BlockMicrowave microwave;
    public static BlockPatternMonitor pattern_monitor;
    public static BlockNetworkSwitch network_switch;
    public static BlockGravitationalAnomaly gravitational_anomaly;
    public static BlockGravitationalStabilizer gravitational_stabilizer;
    public static BlockFusionReactorController fusion_reactor_controller;
    public static BlockFusionReactorCoil fusion_reactor_coil;
    public static BlockMatterRecycler recycler;
    public static BlockAndroidStation androidStation;
    public static BlockStarMap starMap;
    public static BlockChargingStation chargingStation;
    public static BlockMatterPipe heavy_matter_pipe;
    public static BlockHoloSign holoSign;
    public static ForceGlass forceGlass;
    public static BlockFluidMatterPlasma blockMatterPlasma;
    public static BlockFluidFinite blockMoltenTritanium;
	public static BlockBoundingBox boundingBox;
    public static BlockFusionReactorIO fusionReactorIO;
    public static BlockTritaniumCrate[] tritaniumCrate;
    public static BlockInscriber inscriber;
    public static BlockContractMarket contractMarket;
    public static BlockAndroidSpawner androidSpawner;

    public static BlockDecorative decorative_stripes;
    public static BlockDecorative decorative_coils;
    public static BlockDecorative decorative_clean;
    public static BlockDecorative decorative_vent_dark;
    public static BlockDecorative decorative_vent_bright;
    public static BlockDecorative decorative_holo_matrix;
    public static BlockDecorative decorative_tritanium_plate;
    public static BlockDecorative decorative_carbon_fiber_plate;
    public static BlockDecorative decorative_matter_tube;
    public static BlockDecorative decorative_beams;
    public static BlockDecorative decorative_floor_tiles;
    public static BlockDecorative decorative_floor_tiles_green;
    public static BlockDecorative decorative_floor_noise;
    public static BlockDecorative decorative_tritanium_plate_stripe;
    public static BlockDecorative decorative_floot_tile_white;
    public static BlockDecorative decorative_white_plate;
    public static BlockDecorative decorative_separator;
    public static BlockDecorative decorative_tritanium_lamp;
    public static BlockDecorative getDecorative_tritanium_plate_colored;
    public static BlockDecorative decorative_engine_exhaust_plasma;

    public static final List<IRecipe> recipes = new ArrayList<>();

    public static void init(FMLPreInitializationEvent event) {
        replicator = new BlockReplicator(Material.iron, "replicator");
        decomposer = new BlockDecomposer(Material.iron, "decomposer");
        transporter = new BlockTransporter(Material.iron, "transporter");
        matter_pipe = new BlockMatterPipe(Material.iron, "matter_pipe");
        network_pipe = new BlockNetworkPipe(Material.iron, "network_pipe");
        network_router = new BlockNetworkRouter(Material.iron, "network_router");
        matter_analyzer = new BlockMatterAnalyzer(Material.iron, "matter_analyzer");
        dilithium_ore = new DilithiumOre(Material.rock, "dilithium_ore");
        tritaniumOre = new MOBlock(Material.rock, "tritanium_ore");
        tritaniumOre.setHardness(8f);
        tritaniumOre.setResistance(5.0F);
        tritaniumOre.setHarvestLevel("pickaxe", 2);
        tritaniumOre.setStepSound(Block.soundTypePiston);
        tritanium_block = new MOBlock(Material.iron, "tritanium_block");
        tritanium_block.setHardness(15.0F);
        tritanium_block.setResistance(10.0F);
        tritanium_block.setHarvestLevel("pickaxe", 2);
        machine_hull = new MOBlock(Material.iron, "machine_hull");
        machine_hull.setHardness(15.0F);
        machine_hull.setResistance(8.0F);
        machine_hull.setHarvestLevel("pickaxe", 2);
        machine_hull.setBlockTextureName(Reference.MOD_ID + ":" + "base");
        pattern_storage = new BlockPatternStorage(Material.iron, "pattern_storage");
        solar_panel = new BlockSolarPanel(Material.iron, "solar_panel");
        weapon_station = new BlockWeaponStation(Material.iron, "weapon_station");
        microwave = new BlockMicrowave(Material.iron, "microwave");
        pattern_monitor = new BlockPatternMonitor(Material.iron, "pattern_monitor");
        network_switch = new BlockNetworkSwitch(Material.iron, "network_switch");
        gravitational_anomaly = new BlockGravitationalAnomaly(Material.portal, "gravitational_anomaly");
        gravitational_stabilizer = new BlockGravitationalStabilizer(Material.iron, "gravitational_stabilizer");
        fusion_reactor_controller = new BlockFusionReactorController(Material.iron, "fusion_reactor_controller");
        fusion_reactor_coil = new BlockFusionReactorCoil(Material.iron, "fusion_reactor_coil");
        recycler = new BlockMatterRecycler(Material.iron, "matter_recycler");
        androidStation = new BlockAndroidStation(Material.iron, "android_station");
        starMap = new BlockStarMap(Material.iron, "star_map");
        chargingStation = new BlockChargingStation(Material.iron, "charging_station");
        heavy_matter_pipe = new BlockHeavyMatterPipe(Material.iron, "heavy_matter_pipe");
        holoSign = new BlockHoloSign(Material.iron,"holo_sign");
        forceGlass = new ForceGlass(Material.glass,"force_glass");
        blockMatterPlasma = new BlockFluidMatterPlasma(MatterOverdriveFluids.matterPlasma, Material.water);
        blockMoltenTritanium = (BlockFluidFinite)new BlockFluidFinite(MatterOverdriveFluids.moltenTritanium,Material.lava).setBlockName("molten_tritanium").setBlockTextureName(Reference.MOD_ID + ":" + "molten_tritanium_Still");
        blockMatterPlasma.setBlockName("matter_plasma");
		boundingBox = new BlockBoundingBox(Material.air, "bounding_box");
        fusionReactorIO = new BlockFusionReactorIO(Material.iron,"fusion_reactor_io");
        tritaniumCrate = BlockTritaniumCrate.createAllColors(Material.iron,"tritanium_crate");
        inscriber = new BlockInscriber(Material.iron,"inscriber");
        contractMarket = new BlockContractMarket(Material.iron,"contract_market");
        androidSpawner = new BlockAndroidSpawner(Material.iron,"android_spawner");

        decorative_stripes = new BlockDecorative(Material.iron,"decorative.stripes","base_stripes",5,1,8,0xd4b108);
        decorative_coils = new BlockDecorative(Material.iron,"decorative.coils","base_coil",5,1,8,0xb6621e);
        decorative_clean = new BlockDecorative(Material.iron,"decorative.clean","transporter_side",5,1,8,0x3b484b);
        decorative_vent_dark = new BlockDecorative(Material.iron,"decorative.vent.dark","vent",5,1,8,0x32393c);
        decorative_vent_bright = new BlockDecorative(Material.iron,"decorative.vent.bright","vent2",5,1,8,0x3f4b4e);
        decorative_holo_matrix = new BlockDecorative(Material.iron,"decorative.holo_matrix","weapon_station_top",3,1,4,0x323b3a);
        decorative_tritanium_plate = new BlockDecorative(Material.iron,"decorative.tritanium_plate","tritanium_plate",10,1,10,0x475459);
        decorative_carbon_fiber_plate = new BlockDecorative(Material.iron,"decorative.carbon_fiber_plate","carbon_fiber_plate",10,1,12,0x1c1f20);
        decorative_matter_tube = new BlockDecorative(Material.glass,"decorative.matter_tube","matter_tube",3,1,4,0x5088a5).setRotated(true);
        decorative_beams = new BlockDecorative(Material.iron,"decorative.beams","beams",8,1,8,0x1e2220).setRotated(true);
        decorative_floor_tiles = new BlockDecorative(Material.clay,"decorative.floor_tiles","floor_tiles",4,0,4,0x958d7c);
        decorative_floor_tiles_green = new BlockDecorative(Material.clay,"decorative.floor_tiles_green","floor_tiles_green",4,0,4,0x53593f);
        decorative_floor_noise = new BlockDecorative(Material.clay,"decorative.floor_noise","floor_noise",4,0,4,0x7f7e7b);
        decorative_tritanium_plate_stripe = new BlockDecorative(Material.iron,"decorative.tritanium_plate_stripe",10,1,10,0x576468,new String[]{"tritanium_plate","tritanium_plate","tritanium_plate_yellow_stripe","tritanium_plate_yellow_stripe","tritanium_plate_yellow_stripe","tritanium_plate_yellow_stripe"});
        decorative_floot_tile_white = new BlockDecorative(Material.clay,"decorative.floor_tile_white","floor_tile_white",4,0,4,0xa3a49c);
        decorative_white_plate = new BlockDecorative(Material.iron,"decorative.white_plate","white_plate",8,1,8,0xe3e3e3);
        decorative_separator = new BlockDecorative(Material.iron,"decorative.separator","separator",8,1,8,0x303837).setRotated(true);
        decorative_tritanium_lamp = (BlockDecorative)new BlockDecorative(Material.iron,"decorative.tritanium_lamp",2,1,4,0xd4f8f5,new String[]{"tritanium_lamp_bottom","tritanium_lamp_top","tritanium_plate_yellow_stripe","tritanium_plate_yellow_stripe","tritanium_lamp_sides","tritanium_lamp_sides"}).setLightLevel(1);
        getDecorative_tritanium_plate_colored = new BlockDecorative(Material.iron,"decorative.tritanium_plate_colored","tritanium_plate_colorless",10,1,10,0x505050).setColored(true);
        decorative_engine_exhaust_plasma = (BlockDecorative)new BlockDecorative(Material.cactus,"decorative.engine_exhaust_plasma","engine_exhaust_plasma",1,1,1,0x387c9e).setLightLevel(1);
    }

    public static void register(FMLInitializationEvent event) {
        replicator.register();
        MatterOverdrive.configHandler.subscribe(replicator);
        transporter.register();
        MatterOverdrive.configHandler.subscribe(transporter);
        decomposer.register();
        MatterOverdrive.configHandler.subscribe(decomposer);
        matter_pipe.register();
        network_pipe.register();
        network_router.register();
        matter_analyzer.register();
        MatterOverdrive.configHandler.subscribe(matter_analyzer);
        dilithium_ore.register();
        tritaniumOre.register();
        tritanium_block.register();
        machine_hull.register();
        pattern_storage.register();
        MatterOverdrive.configHandler.subscribe(pattern_storage);
        solar_panel.register();
        MatterOverdrive.configHandler.subscribe(solar_panel);
        weapon_station.register();
        microwave.register();
        pattern_monitor.register();
        MatterOverdrive.configHandler.subscribe(pattern_monitor);
        network_switch.register();
        MatterOverdrive.configHandler.subscribe(network_switch);
        gravitational_anomaly.register();
        MatterOverdrive.configHandler.subscribe(gravitational_anomaly);
        gravitational_stabilizer.register();
        MatterOverdrive.configHandler.subscribe(gravitational_stabilizer);
        fusion_reactor_controller.register();
        MatterOverdrive.configHandler.subscribe(fusion_reactor_controller);
        fusion_reactor_coil.register();
        recycler.register();
        MatterOverdrive.configHandler.subscribe(recycler);
        androidStation.register();
        MatterOverdrive.configHandler.subscribe(androidStation);
        starMap.register();
        MatterOverdrive.configHandler.subscribe(starMap);
        chargingStation.register();
        MatterOverdrive.configHandler.subscribe(chargingStation);
        heavy_matter_pipe.register();
        holoSign.register();
        forceGlass.register();
        GameRegistry.registerBlock(blockMatterPlasma, "matter_plasma");
        GameRegistry.registerBlock(blockMoltenTritanium,"molten_tritanium");
		boundingBox.register();
        fusionReactorIO.register();
        BlockTritaniumCrate.registerAll(tritaniumCrate,"tritanium_crate");
        inscriber.register();
        contractMarket.register();
        androidSpawner.register();

        decorative_stripes.register();
        decorative_coils.register();
        decorative_clean.register();
        decorative_vent_dark.register();
        decorative_vent_bright.register();
        decorative_holo_matrix.register();
        decorative_tritanium_plate.register();
        decorative_carbon_fiber_plate.register();
        decorative_matter_tube.register();
        decorative_beams.register();
        decorative_floor_tiles.register();
        decorative_floor_tiles_green.register();
        decorative_floor_noise.register();
        decorative_tritanium_plate_stripe.register();
        decorative_floot_tile_white.register();
        decorative_white_plate.register();
        decorative_separator.register();
        decorative_tritanium_lamp.register();
        getDecorative_tritanium_plate_colored.register();
        decorative_engine_exhaust_plasma.register();

        OreDictionary.registerOre("oreTritanium", tritaniumOre);
        OreDictionary.registerOre("oreDilithium",dilithium_ore);
        OreDictionary.registerOre("blockTritanium",tritanium_block);
    }
}
