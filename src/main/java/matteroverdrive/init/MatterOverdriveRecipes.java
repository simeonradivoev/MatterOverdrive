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

import matteroverdrive.data.recipes.EnergyPackRecipe;
import matteroverdrive.data.recipes.InscriberRecipe;
import matteroverdrive.handler.recipes.InscriberRecipes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 8/29/2015.
 */
public class MatterOverdriveRecipes
{
	public static final List<IRecipe> recipes = new ArrayList<>();


	public static void registerBlockRecipes(FMLInitializationEvent event)
	{
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.decomposer), "TCT", "S S", "NTM", 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'M', MatterOverdriveItems.me_conversion_matrix, 'N', MatterOverdriveItems.integration_matrix, 'S', Blocks.STICKY_PISTON, 'T', MatterOverdriveItems.tritanium_plate);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.replicator), "PCF", "IHI", "NTM", 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'M', MatterOverdriveItems.me_conversion_matrix, 'H', MatterOverdriveItems.h_compensator, 'I', Items.IRON_INGOT, 'N', MatterOverdriveItems.integration_matrix, 'T', MatterOverdriveItems.tritanium_plate, 'F', MatterOverdriveItems.networkFlashDrive, 'P', MatterOverdriveItems.pattern_drive);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.network_router), "IGI", "DFC", "OMO", 'M', MatterOverdriveItems.machine_casing, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0), 'I', Items.IRON_INGOT, 'G', Blocks.GLASS, 'D', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 'F', MatterOverdriveItems.networkFlashDrive);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.network_switch), " G ", "CFC", "OMO", 'M', MatterOverdriveItems.machine_casing, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0), 'G', Blocks.GLASS, 'F', MatterOverdriveItems.networkFlashDrive);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.matter_pipe, 8), " G ", "IMI", " G ", 'M', MatterOverdriveItems.s_magnet, 'G', Blocks.GLASS, 'I', Items.IRON_INGOT);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.network_pipe, 16), "IGI", "BCB", "IGI", 'M', MatterOverdriveItems.s_magnet, 'G', Blocks.GLASS, 'I', Items.IRON_INGOT, 'B', Items.GOLD_INGOT, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0));
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.matter_analyzer), " C ", "PMF", "ONO", 'O', Blocks.IRON_BLOCK, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'M', MatterOverdriveItems.me_conversion_matrix, 'N', MatterOverdriveItems.integration_matrix, 'P', MatterOverdriveItems.pattern_drive, 'F', MatterOverdriveItems.networkFlashDrive);
		addRecipe(new ShapedOreRecipe(new ItemStack(MatterOverdriveBlocks.tritanium_block), "TTT", "TTT", "TTT", 'T', "tritaniumIngot"));
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.machine_hull), " T ", "T T", " T ", 'T', MatterOverdriveItems.tritanium_plate);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.solar_panel), "CGC", "GQG", "KMK", 'C', Items.COAL, 'Q', Blocks.QUARTZ_BLOCK, 'K', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 'M', MatterOverdriveItems.machine_casing, 'G', Blocks.GLASS);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.weapon_station), "   ", "GFR", "CMB", 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'B', MatterOverdriveItems.battery, 'G', Items.GLOWSTONE_DUST, 'R', Items.REDSTONE, 'M', MatterOverdriveItems.machine_casing, 'F', MatterOverdriveItems.forceFieldEmitter);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.pattern_storage), "B3B", "TCT", "2M1", 'B', new ItemStack(Blocks.WOOL, 1, 15), '1', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0), '2', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), '3', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'C', Blocks.CHEST, 'M', MatterOverdriveItems.machine_casing, 'T', MatterOverdriveItems.tritanium_ingot);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.pattern_monitor), " H ", "1N1", " F ", '1', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 'H', MatterOverdriveBlocks.holoSign, 'N', MatterOverdriveBlocks.network_switch, 'F', MatterOverdriveItems.networkFlashDrive);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.transporter), "TGT", "CMC", "NBH", 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 'M', MatterOverdriveItems.me_conversion_matrix, 'H', MatterOverdriveItems.h_compensator, 'E', Items.ENDER_PEARL, 'N', MatterOverdriveItems.integration_matrix, 'T', MatterOverdriveItems.tritanium_plate, 'G', Blocks.GLOWSTONE, 'B', MatterOverdriveItems.hc_battery);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.fusion_reactor_coil), "TMT", "M M", "CMC", 'M', MatterOverdriveItems.s_magnet, 'T', MatterOverdriveItems.tritanium_plate, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0));
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.recycler), "T T", "1P2", "NTM", '2', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), '1', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0), 'M', MatterOverdriveItems.me_conversion_matrix, 'N', MatterOverdriveItems.integration_matrix, 'T', MatterOverdriveItems.tritanium_plate, 'P', Blocks.PISTON);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.gravitational_stabilizer), " H ", "TST", "CMC", 'M', MatterOverdriveItems.machine_casing, 'S', MatterOverdriveItems.spacetime_equalizer, 'T', MatterOverdriveItems.tritanium_plate, 'C', MatterOverdriveItems.s_magnet, 'H', MatterOverdriveBlocks.holoSign);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.fusion_reactor_controller), "CHC", "2M3", "CTC", 'C', MatterOverdriveBlocks.fusion_reactor_coil, '2', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), '3', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'M', MatterOverdriveItems.machine_casing, 'T', MatterOverdriveItems.tritanium_plate, 'H', MatterOverdriveBlocks.holoSign);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.androidStation), "THA", "2F3", "GMR", '3', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), '2', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 'F', MatterOverdriveItems.forceFieldEmitter, 'G', Items.GLOWSTONE_DUST, 'R', Items.REDSTONE, 'M', MatterOverdriveItems.machine_casing, 'H', new ItemStack(MatterOverdriveItems.androidParts, 1, 0), 'T', new ItemStack(MatterOverdriveItems.androidParts, 1, 3), 'A', new ItemStack(MatterOverdriveItems.androidParts, 1, 1));
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.starMap), " S ", "CFC", "GMR", 'S', MatterOverdriveItems.security_protocol, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'M', MatterOverdriveItems.machine_casing, 'F', MatterOverdriveItems.forceFieldEmitter, 'G', Items.GLOWSTONE_DUST, 'R', Items.REDSTONE);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.chargingStation), " F ", "EDR", "BMB", 'M', MatterOverdriveItems.machine_casing, 'B', MatterOverdriveItems.hc_battery, 'E', Items.ENDER_EYE, 'R', Items.REPEATER, 'F', MatterOverdriveItems.forceFieldEmitter, 'D', MatterOverdriveItems.dilithium_ctystal);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.heavy_matter_pipe, 8), "RMR", "TMT", "RMR", 'M', MatterOverdriveItems.s_magnet, 'G', Blocks.GLASS, 'T', MatterOverdriveItems.tritanium_plate, 'R', Items.REDSTONE);
		addRecipe(new ShapedOreRecipe(new ItemStack(MatterOverdriveBlocks.holoSign), "GGG", "g0g", " T ", 'G', "blockGlass", 'g', Items.GLOWSTONE_DUST, '0', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0), 'T', MatterOverdriveItems.tritanium_plate));
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.forceGlass, 4), " G ", "GTG", " G ", 'G', Blocks.GLASS, 'T', MatterOverdriveItems.tritanium_plate);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.tritaniumCrate), "   ", "TCT", " T ", 'T', MatterOverdriveItems.tritanium_plate, 'C', Blocks.CHEST);
		addShapelessRecipe(new ItemStack(MatterOverdriveBlocks.tritaniumCrateYellow), new ItemStack(Items.DYE, 1, EnumDyeColor.YELLOW.getDyeDamage()), MatterOverdriveBlocks.tritaniumCrate);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.inscriber), "IDI", "TPT", "RMR", 'M', MatterOverdriveItems.machine_casing, 'D', MatterOverdriveItems.dilithium_ctystal, 'T', MatterOverdriveItems.tritanium_plate, 'P', Blocks.PISTON, 'R', Items.REDSTONE, 'I', Items.IRON_INGOT);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.fusionReactorIO), "TGT", "C C", "TGT", 'G', Items.GOLD_INGOT, 'T', MatterOverdriveItems.tritanium_plate, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0));
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.contractMarket), " T ", "GEG", " M ", 'T', MatterOverdriveItems.tritanium_ingot, 'G', Items.GOLD_INGOT, 'E', Items.EMERALD, 'M', MatterOverdriveItems.machine_casing);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.spacetimeAccelerator), "THT", "DDD", "THT", 'T', MatterOverdriveItems.tritanium_plate, 'H', MatterOverdriveItems.h_compensator, 'D', MatterOverdriveItems.dilithium_ctystal);

		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.decorative_tritanium_plate, 16), "SSS", "S#S", "SSS", '#', MatterOverdriveItems.tritanium_plate, 'S', Blocks.STONE);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.decorative_beams, 6), "#T#", "#T#", "#T#", '#', MatterOverdriveBlocks.decorative_tritanium_plate, 'T', MatterOverdriveItems.tritanium_nugget);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.decorative_tritanium_plate_stripe, 8), "###", "###", "#Y#", '#', MatterOverdriveBlocks.decorative_tritanium_plate, 'Y', new ItemStack(Items.DYE, 1, EnumDyeColor.YELLOW.getDyeDamage()), 'B', new ItemStack(Items.DYE, 1, EnumDyeColor.BLACK.getDyeDamage()));
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.decorative_holo_matrix, 8), "###", "#I#", "###", '#', MatterOverdriveBlocks.decorative_tritanium_plate, 'I', new ItemStack(MatterOverdriveItems.isolinear_circuit));
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.decorative_carbon_fiber_plate, 8), "###", "#C#", "###", '#', MatterOverdriveBlocks.decorative_tritanium_plate, 'C', new ItemStack(Items.COAL));
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.decorative_vent_bright, 6), " # ", "T T", " # ", '#', MatterOverdriveItems.tritanium_plate, 'T', MatterOverdriveItems.tritanium_ingot);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.decorative_vent_dark, 8), "###", "#B#", "###", '#', MatterOverdriveBlocks.decorative_vent_bright, 'B', new ItemStack(Items.DYE));
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.decorative_clean, 8), " S ", "STS", " S ", 'T', MatterOverdriveItems.tritanium_plate, 'S', Blocks.STONE);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.decorative_floor_tiles, 32), "###", "#Q#", "###", '#', Blocks.CLAY, 'Q', Items.QUARTZ);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.decorative_floor_tiles_green, 32), "#G#", "#Q#", "#G#", '#', Blocks.CLAY, 'Q', Items.QUARTZ, 'G', new ItemStack(Items.DYE, 1, EnumDyeColor.RED.getDyeDamage()));
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.decorative_floot_tile_white, 32), "#W#", "#Q#", "#W#", '#', Blocks.CLAY, 'Q', Items.QUARTZ, 'W', new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()));
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.decorative_separator, 8), "###", "#N#", "###", '#', MatterOverdriveBlocks.decorative_tritanium_plate, 'N', MatterOverdriveItems.tritanium_nugget);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.decorative_floor_noise, 32), "#G#", "#Q#", "#G#", '#', Blocks.CLAY, 'B', Items.BONE, 'G', Blocks.GRAVEL);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.decorative_white_plate, 8), "#W#", "###", "#W#", '#', MatterOverdriveBlocks.decorative_tritanium_plate, 'W', Blocks.WOOL);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.decorative_coils, 12), "###", "#C#", "###", '#', MatterOverdriveBlocks.decorative_tritanium_plate, 'C', MatterOverdriveItems.s_magnet);
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.decorative_stripes, 8), "#B#", "###", "#Y#", '#', MatterOverdriveBlocks.decorative_tritanium_plate, 'B', new ItemStack(Items.DYE), 'Y', new ItemStack(Items.DYE, 1, EnumDyeColor.YELLOW.getDyeDamage()));
		addShapedRecipe(new ItemStack(MatterOverdriveBlocks.decorative_tritanium_lamp, 2), "###", "#G#", "GGG", '#', MatterOverdriveBlocks.decorative_tritanium_plate, 'G', Items.GLOWSTONE_DUST);
		for (EnumDyeColor dyeColor : EnumDyeColor.values())
		{
			addShapedRecipe(new ItemStack(MatterOverdriveBlocks.decorative_tritanium_plate_colored, 8, dyeColor.getDyeDamage()), "###", "#D#", "###", '#', MatterOverdriveBlocks.decorative_tritanium_plate, 'D', new ItemStack(Items.DYE, 1, dyeColor.getDyeDamage()));
		}

	}

	public static void registerItemRecipes(FMLInitializationEvent event)
	{
		addShapedRecipe(new ItemStack(MatterOverdriveItems.battery), " R ", "TGT", "TDT", 'T', MatterOverdriveItems.tritanium_ingot, 'D', MatterOverdriveItems.dilithium_ctystal, 'R', Items.REDSTONE, 'G', Items.GOLD_INGOT);
		addShapedRecipe(new ItemStack(MatterOverdriveItems.hc_battery), " P ", "DBD", " P ", 'B', MatterOverdriveItems.battery, 'D', MatterOverdriveItems.dilithium_ctystal, 'P', MatterOverdriveItems.tritanium_plate);
		addShapedRecipe(new ItemStack(MatterOverdriveItems.matter_scanner), "III", "GDG", "IRI", 'I', Items.IRON_INGOT, 'D', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'R', Items.REDSTONE, 'G', Items.GOLD_INGOT);
		addShapedRecipe(new ItemStack(MatterOverdriveItems.h_compensator), " M ", "CPC", "DED", 'D', MatterOverdriveItems.dilithium_ctystal, 'M', MatterOverdriveItems.machine_casing, 'I', Items.IRON_INGOT, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0), 'P', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 'E', Items.ENDER_EYE);
		addShapedRecipe(new ItemStack(MatterOverdriveItems.integration_matrix), " M ", "GPG", "DED", 'G', Blocks.GLASS, 'M', MatterOverdriveItems.machine_casing, 'I', Items.IRON_INGOT, 'P', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 'E', Items.ENDER_PEARL, 'D', MatterOverdriveItems.dilithium_ctystal);
		addShapedRecipe(new ItemStack(MatterOverdriveItems.machine_casing), " T ", "I I", "GRG", 'G', Items.GOLD_INGOT, 'T', MatterOverdriveItems.tritanium_plate, 'I', MatterOverdriveItems.tritanium_ingot, 'R', Items.REDSTONE);
		addShapedRecipe(new ItemStack(MatterOverdriveItems.s_magnet, 4), "RRR", "TET", "RRR", 'E', Items.ENDER_PEARL, 'T', MatterOverdriveItems.tritanium_ingot, 'R', Items.REDSTONE);
		addShapedRecipe(new ItemStack(MatterOverdriveItems.me_conversion_matrix), "EIE", "CDC", "EIE", 'E', Items.ENDER_PEARL, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 'I', Items.IRON_INGOT, 'D', MatterOverdriveItems.dilithium_ctystal);
		addShapedRecipe(new ItemStack(MatterOverdriveItems.tritanium_plate), "TT", 'T', new ItemStack(MatterOverdriveItems.tritanium_ingot));
		addShapedRecipe(new ItemStack(MatterOverdriveItems.phaser), "IGI", "IPH", "WCW", 'I', Items.IRON_INGOT, 'G', Blocks.GLASS, 'P', MatterOverdriveItems.plasmaCore, 'W', Blocks.WOOL, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'H', MatterOverdriveItems.weaponHandle);
		addShapedRecipe(new ItemStack(MatterOverdriveItems.pattern_drive), " E ", "RMR", " C ", 'M', MatterOverdriveItems.machine_casing, 'E', Items.ENDER_PEARL, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 'R', Items.REDSTONE);
		addShapedRecipe(new ItemStack(MatterOverdriveItems.security_protocol), "PP", "CP", 'P', Items.PAPER, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0));
		addShapedRecipe(new ItemStack(MatterOverdriveItems.wrench), "T T", " Y ", " T ", 'T', MatterOverdriveItems.tritanium_ingot, 'Y', new ItemStack(Blocks.WOOL, 1, 4));
		addShapedRecipe(new ItemStack(MatterOverdriveItems.spacetime_equalizer), " M ", "EHE", " M ", 'M', MatterOverdriveItems.s_magnet, 'E', Items.ENDER_PEARL, 'H', MatterOverdriveItems.h_compensator);
		addShapedRecipe(new ItemStack(MatterOverdriveItems.forceFieldEmitter), "CGC", "CDC", "P1P", 'P', MatterOverdriveItems.tritanium_plate, 'E', Items.ENDER_PEARL, 'D', MatterOverdriveItems.dilithium_ctystal, '1', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1), 'C', MatterOverdriveItems.s_magnet, 'G', Blocks.GLASS);
		addShapedRecipe(new ItemStack(MatterOverdriveItems.networkFlashDrive), "RCR", 'R', Items.REDSTONE, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0));
		addShapedRecipe(new ItemStack(MatterOverdriveItems.transportFlashDrive), " I ", "ECR", " I", 'I', Items.IRON_INGOT, 'R', Items.REDSTONE, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0));
		addRecipe(new EnergyPackRecipe(new ItemStack(MatterOverdriveItems.tritanium_plate), new ItemStack(MatterOverdriveItems.battery), new ItemStack(Items.GUNPOWDER)));
		addRecipe(new EnergyPackRecipe(new ItemStack(MatterOverdriveItems.tritanium_plate), new ItemStack(MatterOverdriveItems.hc_battery), new ItemStack(Items.GUNPOWDER)));
		addShapedRecipe(new ItemStack(MatterOverdriveItems.phaserRifle), "III", "SPC", "WHB", 'I', Items.IRON_INGOT, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'W', Blocks.WOOL, 'S', MatterOverdriveItems.weaponReceiver, 'D', MatterOverdriveItems.dilithium_ctystal, 'P', MatterOverdriveItems.plasmaCore, 'B', MatterOverdriveItems.battery, 'H', MatterOverdriveItems.weaponHandle);
		addRecipe(new ShapedOreRecipe(new ItemStack(MatterOverdriveItems.matterContainer, 4), "TMT", " T ", 'T', MatterOverdriveItems.tritanium_ingot, 'M', MatterOverdriveItems.s_magnet));
		addRecipe(new ShapedOreRecipe(new ItemStack(MatterOverdriveItems.tritanium_ingot), "###", "###", "###", '#', "nuggetTritanium"));
		addRecipe(new ShapelessOreRecipe(new ItemStack(MatterOverdriveItems.tritanium_nugget, 9), "ingotTritanium"));
		addShapelessRecipe(new ItemStack(MatterOverdriveItems.dataPad), Items.BOOK, new ItemStack(MatterOverdriveItems.isolinear_circuit, 0));
		addShapedRecipe(new ItemStack(MatterOverdriveItems.omniTool), "IFC", "SPI", " BH", 'I', Items.IRON_INGOT, 'S', MatterOverdriveItems.weaponReceiver, 'P', MatterOverdriveItems.plasmaCore, 'B', MatterOverdriveItems.battery, 'F', MatterOverdriveItems.forceFieldEmitter, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'H', MatterOverdriveItems.weaponHandle);
		addShapedRecipe(new ItemStack(MatterOverdriveItems.tritaniumAxe), "XX ", "X# ", " # ", 'X', MatterOverdriveItems.tritanium_ingot, '#', Items.STICK);
		addShapedRecipe(new ItemStack(MatterOverdriveItems.tritaniumPickaxe), "XXX", " # ", " # ", 'X', MatterOverdriveItems.tritanium_ingot, '#', Items.STICK);
		addShapedRecipe(new ItemStack(MatterOverdriveItems.tritaniumSword), " X ", " X ", " # ", 'X', MatterOverdriveItems.tritanium_ingot, '#', Items.STICK);
		addShapedRecipe(new ItemStack(MatterOverdriveItems.tritaniumHoe), "XX ", " # ", " # ", 'X', MatterOverdriveItems.tritanium_ingot, '#', Items.STICK);
		addShapedRecipe(new ItemStack(MatterOverdriveItems.tritaniumShovel), " X ", " # ", " # ", 'X', MatterOverdriveItems.tritanium_ingot, '#', Items.STICK);
		addShapedRecipe(new ItemStack(MatterOverdriveItems.tritaniumHelemet), "XCX", "X X", "   ", 'X', MatterOverdriveItems.tritanium_ingot, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1));
		addShapedRecipe(new ItemStack(MatterOverdriveItems.tritaniumChestplate), "X X", "XCX", "XXX", 'X', MatterOverdriveItems.tritanium_ingot, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1));
		addShapedRecipe(new ItemStack(MatterOverdriveItems.tritaniumLeggings), "XCX", "X X", "X X", 'X', MatterOverdriveItems.tritanium_ingot, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1));
		addShapedRecipe(new ItemStack(MatterOverdriveItems.tritaniumBoots), "   ", "X X", "X X", 'X', MatterOverdriveItems.tritanium_ingot);
		addShapedRecipe(new ItemStack(MatterOverdriveItems.isolinear_circuit), "I", "R", "G", 'G', Item.getItemFromBlock(Blocks.GLASS), 'R', Items.REDSTONE, 'I', Items.IRON_INGOT);
		addShapedRecipe(new ItemStack(MatterOverdriveItems.sniperScope), "IIC", "GFG", "III", 'I', Items.IRON_INGOT, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 'G', new ItemStack(Blocks.STAINED_GLASS_PANE, 1, 5), 'F', MatterOverdriveItems.forceFieldEmitter);
		addShapedRecipe(new ItemStack(MatterOverdriveItems.weaponHandle), "TWT", "I I", "I I", 'I', Items.IRON_INGOT, 'W', new ItemStack(Blocks.WOOL, 1, 15), 'T', MatterOverdriveItems.tritanium_ingot);
		addShapedRecipe(new ItemStack(MatterOverdriveItems.weaponReceiver), "IRT", "   ", "IIT", 'I', Items.IRON_INGOT, 'R', Items.REDSTONE, 'T', MatterOverdriveItems.tritanium_ingot);
		addShapedRecipe(new ItemStack(MatterOverdriveItems.plasmaCore), "GI ", "MCM", " IG", 'G', Blocks.GLASS, 'I', Items.IRON_INGOT, 'M', MatterOverdriveItems.s_magnet, 'C', new ItemStack(MatterOverdriveItems.matterContainer));
		addShapedRecipe(new ItemStack(MatterOverdriveItems.plasmaShotgun), "SP ", "ICH", "SPB", 'S', MatterOverdriveItems.weaponReceiver, 'P', MatterOverdriveItems.plasmaCore, 'I', Items.IRON_INGOT, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'B', MatterOverdriveItems.battery, 'H', MatterOverdriveItems.weaponHandle);
		addShapedRecipe(new ItemStack(MatterOverdriveItems.ionSniper), "ICI", "SPP", " HB", 'I', Items.IRON_INGOT, 'S', MatterOverdriveItems.weaponReceiver, 'P', MatterOverdriveItems.plasmaCore, 'H', MatterOverdriveItems.weaponHandle, 'B', MatterOverdriveItems.battery, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 3));
		addShapedRecipe(new ItemStack(MatterOverdriveItems.portableDecomposer), " T ", "IPM", " T ", 'T', MatterOverdriveItems.tritanium_plate, 'I', MatterOverdriveItems.integration_matrix, 'M', MatterOverdriveItems.me_conversion_matrix, 'P', Blocks.STICKY_PISTON);
		addShapedRecipe(new ItemStack(MatterOverdriveItems.item_upgrade, 1, 0), " R ", " C ", " T ", 'G', Blocks.GLASS, 'R', Items.REDSTONE, 'T', MatterOverdriveItems.tritanium_plate, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0));
		//speed
		addShapedRecipe(new ItemStack(MatterOverdriveItems.item_upgrade, 1, 1), " R ", "GUG", " E ", 'U', MatterOverdriveItems.item_upgrade, 'G', Items.GLOWSTONE_DUST, 'R', Items.REDSTONE, 'E', Items.EMERALD);
		//power
		addShapedRecipe(new ItemStack(MatterOverdriveItems.item_upgrade, 1, 2), " B ", "RUR", " C ", 'U', MatterOverdriveItems.item_upgrade, 'B', MatterOverdriveItems.battery, 'R', Items.REDSTONE, 'C', Items.QUARTZ);
		//failsafe
		addShapedRecipe(new ItemStack(MatterOverdriveItems.item_upgrade, 1, 3), " D ", "RUR", " G ", 'U', MatterOverdriveItems.item_upgrade, 'D', Items.DIAMOND, 'R', Items.REDSTONE, 'G', Items.GOLD_INGOT);
		//range
		addShapedRecipe(new ItemStack(MatterOverdriveItems.item_upgrade, 1, 4), " E ", "RUR", " G ", 'U', MatterOverdriveItems.item_upgrade, 'E', Items.ENDER_PEARL, 'R', Items.REDSTONE, 'G', Items.GOLD_INGOT);
		//power storage
		addShapedRecipe(new ItemStack(MatterOverdriveItems.item_upgrade, 1, 5), "   ", "RUR", " B ", 'U', MatterOverdriveItems.item_upgrade, 'B', MatterOverdriveItems.hc_battery, 'R', Items.REDSTONE, 'G', Items.GOLD_INGOT);
		//hyper speed
		addShapelessRecipe(new ItemStack(MatterOverdriveItems.item_upgrade, 1, 6), MatterOverdriveItems.dilithium_ctystal, Items.NETHER_STAR, new ItemStack(MatterOverdriveItems.item_upgrade, 1, 1));
		//matter storage
		addShapedRecipe(new ItemStack(MatterOverdriveItems.item_upgrade, 1, 7), " R ", "MUM", " R ", 'U', MatterOverdriveItems.item_upgrade, 'M', MatterOverdriveItems.s_magnet, 'R', Items.REDSTONE);
		GameRegistry.addRecipe(new ItemStack(MatterOverdriveItems.weapon_module_barrel, 1, 0), " G ", "RDR", " T ", 'T', MatterOverdriveItems.tritanium_plate, 'D', MatterOverdriveItems.dilithium_ctystal, 'R', Items.REDSTONE, 'G', Blocks.GLASS);
		GameRegistry.addRecipe(new ItemStack(MatterOverdriveItems.weapon_module_barrel, 1, 1), " G ", "BFB", " T ", 'T', MatterOverdriveItems.tritanium_plate, 'F', Items.FIRE_CHARGE, 'B', Items.BLAZE_ROD, 'G', Blocks.GLASS);
		GameRegistry.addRecipe(new ItemStack(MatterOverdriveItems.weapon_module_barrel, 1, 2), " B ", "BRB", "DTD", 'T', MatterOverdriveItems.tritanium_plate, 'R', Items.BLAZE_ROD, 'B', Blocks.TNT, 'G', Blocks.GLASS, 'D', Items.DIAMOND);
		GameRegistry.addRecipe(new ItemStack(MatterOverdriveItems.weapon_module_barrel, 1, 3), " S ", "SAS", "ETE", 'T', MatterOverdriveItems.tritanium_plate, 'A', Items.GOLDEN_APPLE, 'S', Items.SUGAR, 'G', Blocks.GLASS, 'E', Items.EMERALD);
	}

	public static void registerInscriberRecipes(FMLInitializationEvent event)
	{
		InscriberRecipes.registerRecipe(new InscriberRecipe(new ItemStack(MatterOverdriveItems.isolinear_circuit), new ItemStack(Items.GOLD_INGOT), new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 64000, 300));
		InscriberRecipes.registerRecipe(new InscriberRecipe(new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), new ItemStack(Items.DIAMOND), new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 88000, 600));
		InscriberRecipes.registerRecipe(new InscriberRecipe(new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), new ItemStack(Items.EMERALD), new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 3), 114000, 1200));
	}

	public static void addShapedRecipe(ItemStack output, Object... params)
	{
		recipes.add(GameRegistry.addShapedRecipe(output, params));
	}

	public static void addShapelessRecipe(ItemStack output, Object... items)
	{
		GameRegistry.addShapelessRecipe(output, items);
	}

	public static void addRecipe(IRecipe recipe)
	{
		recipes.add(recipe);
		GameRegistry.addRecipe(recipe);
	}
}
