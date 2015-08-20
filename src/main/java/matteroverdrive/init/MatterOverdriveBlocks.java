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
import cpw.mods.fml.relauncher.Side;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.blocks.*;
import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.blocks.world.DilithiumOre;
import matteroverdrive.guide.MatterOverdriveQuide;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

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

        if (event.getSide() == Side.CLIENT) {
            MatterOverdriveQuide.Register(replicator);
            MatterOverdriveQuide.Register(decomposer);
            MatterOverdriveQuide.Register(dilithium_ore);
            MatterOverdriveQuide.Register(tritaniumOre);
        }

        GameRegistry.addRecipe(new ItemStack(decomposer), "TCT", "S S", "NTM", 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'M', MatterOverdriveItems.me_conversion_matrix, 'N', MatterOverdriveItems.integration_matrix, 'S', Blocks.sticky_piston, 'T', MatterOverdriveItems.tritanium_plate);
        GameRegistry.addRecipe(new ItemStack(replicator), "PCF", "IHI", "NTM", 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'M', MatterOverdriveItems.me_conversion_matrix, 'H', MatterOverdriveItems.h_compensator, 'I', Items.iron_ingot, 'N', MatterOverdriveItems.integration_matrix, 'T', MatterOverdriveItems.tritanium_plate,'F',MatterOverdriveItems.networkFlashDrive,'P',MatterOverdriveItems.pattern_drive);
        GameRegistry.addRecipe(new ItemStack(network_router), "IGI", "DFC", "OMO", 'M', MatterOverdriveItems.machine_casing, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0), 'I', Items.iron_ingot, 'G', Blocks.glass, 'D', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1),'F',MatterOverdriveItems.networkFlashDrive);
        GameRegistry.addRecipe(new ItemStack(network_switch), " G ", "CFC", "OMO", 'M', MatterOverdriveItems.machine_casing, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0), 'G', Blocks.glass,'F',MatterOverdriveItems.networkFlashDrive);
        GameRegistry.addRecipe(new ItemStack(matter_pipe, 8), " G ", "IMI", " G ", 'M', MatterOverdriveItems.s_magnet, 'G', Blocks.glass, 'I', Items.iron_ingot);
        GameRegistry.addRecipe(new ItemStack(network_pipe, 16), "IGI", "BCB", "IGI", 'M', MatterOverdriveItems.s_magnet, 'G', Blocks.glass, 'I', Items.iron_ingot, 'B', Items.gold_ingot, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0));
        GameRegistry.addRecipe(new ItemStack(matter_analyzer), " C ", "PMF", "ONO", 'O', Blocks.iron_block, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'M', MatterOverdriveItems.me_conversion_matrix, 'N', MatterOverdriveItems.integration_matrix, 'P',MatterOverdriveItems.pattern_drive,'F',MatterOverdriveItems.networkFlashDrive);
        GameRegistry.addRecipe(new ItemStack(tritanium_block), "TTT", "TTT", "TTT", 'T', MatterOverdriveItems.tritanium_ingot);
        GameRegistry.addRecipe(new ItemStack(machine_hull), " T ", "T T", " T ", 'T', MatterOverdriveItems.tritanium_plate);
        GameRegistry.addRecipe(new ItemStack(solar_panel), "CGC", "GQG", "KMK", 'C', Items.coal, 'Q', Items.quartz, 'K', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 'M', MatterOverdriveItems.machine_casing, 'G', Blocks.glass);
        GameRegistry.addRecipe(new ItemStack(weapon_station), "   ", "GFR", "CMB", 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'B', MatterOverdriveItems.battery, 'G', Items.glowstone_dust, 'R', Items.redstone, 'M', MatterOverdriveItems.machine_casing,'F',MatterOverdriveItems.forceFieldEmitter);
        GameRegistry.addRecipe(new ItemStack(pattern_storage), "B3B", "TCT", "2M1", 'B', new ItemStack(Blocks.wool, 1, 15), '1', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0), '2', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), '3', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'C', Blocks.chest, 'M', MatterOverdriveItems.machine_casing, 'T', MatterOverdriveItems.tritanium_ingot);
        GameRegistry.addRecipe(new ItemStack(pattern_monitor), " H ", "1N1", " F ",'1', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1),'H',holoSign,'N',network_switch,'F',MatterOverdriveItems.networkFlashDrive);
        GameRegistry.addRecipe(new ItemStack(transporter), "TGT", "CMC", "NBH", 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 'M', MatterOverdriveItems.me_conversion_matrix, 'H', MatterOverdriveItems.h_compensator, 'E', Items.ender_pearl, 'N', MatterOverdriveItems.integration_matrix, 'T', MatterOverdriveItems.tritanium_plate, 'G', Blocks.glowstone,'B',MatterOverdriveItems.hc_battery);
        GameRegistry.addRecipe(new ItemStack(fusion_reactor_coil), "TMT", "M M", "CMC", 'M', MatterOverdriveItems.s_magnet, 'T', MatterOverdriveItems.tritanium_plate, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0));
        GameRegistry.addRecipe(new ItemStack(recycler), "T T", "1P2", "NTM", '2', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), '1', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0), 'M', MatterOverdriveItems.me_conversion_matrix, 'N', MatterOverdriveItems.integration_matrix, 'T', MatterOverdriveItems.tritanium_plate, 'P', Blocks.piston);
        GameRegistry.addRecipe(new ItemStack(gravitational_stabilizer), " H ", "TST", "CMC", 'M', MatterOverdriveItems.machine_casing, 'S', MatterOverdriveItems.spacetime_equalizer, 'T', MatterOverdriveItems.tritanium_plate, 'C', MatterOverdriveItems.s_magnet,'H',holoSign);
        GameRegistry.addRecipe(new ItemStack(fusion_reactor_controller), "CHC", "2M3", "CTC", 'C', fusion_reactor_coil, '2', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), '3', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'M', MatterOverdriveItems.machine_casing, 'T', MatterOverdriveItems.tritanium_plate,'H',holoSign);
        GameRegistry.addRecipe(new ItemStack(androidStation), "THA", "2F3", "GMR", '3', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), '2', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 'F', MatterOverdriveItems.forceFieldEmitter, 'G', Items.glowstone_dust, 'R', Items.redstone, 'M', MatterOverdriveItems.machine_casing, 'H', new ItemStack(MatterOverdriveItems.androidParts, 1, 0), 'T', new ItemStack(MatterOverdriveItems.androidParts, 1, 3), 'A', new ItemStack(MatterOverdriveItems.androidParts, 1, 1));
        GameRegistry.addRecipe(new ItemStack(starMap), " S ", "CFC", "GMR", 'S', MatterOverdriveItems.security_protocol, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'M', MatterOverdriveItems.machine_casing, 'F', MatterOverdriveItems.forceFieldEmitter, 'G', Items.glowstone_dust, 'R', Items.redstone);
        GameRegistry.addRecipe(new ItemStack(chargingStation), " F ", "EDR", "BMB", 'M', MatterOverdriveItems.machine_casing, 'B', MatterOverdriveItems.hc_battery, 'E', Items.ender_eye, 'R', Items.repeater, 'F', MatterOverdriveItems.forceFieldEmitter, 'D', MatterOverdriveItems.dilithium_ctystal);
        GameRegistry.addRecipe(new ItemStack(heavy_matter_pipe, 8), "RMR", "TMT", "RMR", 'M', MatterOverdriveItems.s_magnet, 'G', Blocks.glass, 'T', MatterOverdriveItems.tritanium_plate,'R',Items.redstone);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(holoSign), "GGG", "g0g", " T ", 'G', "glass", 'g', Items.glowstone_dust, '0', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0), 'T', MatterOverdriveItems.tritanium_plate));
        GameRegistry.addRecipe(new ItemStack(forceGlass,4)," G ","GTG"," G ",'G',Blocks.glass,'T',MatterOverdriveItems.tritanium_plate);

        OreDictionary.registerOre("oreTritanium", tritaniumOre);
        OreDictionary.registerOre("oreDilithium",dilithium_ore);
        OreDictionary.registerOre("blockTritanium",tritanium_block);
    }
}
