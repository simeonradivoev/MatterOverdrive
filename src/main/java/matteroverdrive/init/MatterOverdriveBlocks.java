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
	public static BlockBoundingBox boundingBox;

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
        tritaniumOre.setHardness(15.0F);
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
        blockMatterPlasma.setBlockName("matter_plasma");
		boundingBox = new BlockBoundingBox(Material.air, "bounding_box");
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
		boundingBox.register();

        OreDictionary.registerOre("oreTritanium", tritaniumOre);
        OreDictionary.registerOre("oreDilithium",dilithium_ore);
        OreDictionary.registerOre("blockTritanium",tritanium_block);
    }
}
