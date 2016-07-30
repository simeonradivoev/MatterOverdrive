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
import matteroverdrive.data.recipes.EnergyPackRecipe;
import matteroverdrive.data.recipes.InscriberRecipeManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.shadowfacts.shadowlib.util.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 8/29/2015.
 */
public class MatterOverdriveRecipes
{
	public static final List<IRecipe> recipes = new ArrayList<>();

	public static final InscriberRecipeManager INSCRIBER = new InscriberRecipeManager();

	public static void registerBlockRecipes(FMLInitializationEvent event)
	{
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decomposer), "TCT", "S S", "NTM", 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), 'M', MatterOverdrive.items.me_conversion_matrix, 'N', MatterOverdrive.items.integration_matrix, 'S', Blocks.STICKY_PISTON, 'T', MatterOverdrive.items.tritanium_plate);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.replicator), "PCF", "IHI", "NTM", 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), 'M', MatterOverdrive.items.me_conversion_matrix, 'H', MatterOverdrive.items.h_compensator, 'I', Items.IRON_INGOT, 'N', MatterOverdrive.items.integration_matrix, 'T', MatterOverdrive.items.tritanium_plate, 'F', MatterOverdrive.items.networkFlashDrive, 'P', MatterOverdrive.items.pattern_drive);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.network_router), "IGI", "DFC", "OMO", 'M', MatterOverdrive.items.machine_casing, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0), 'I', Items.IRON_INGOT, 'G', Blocks.GLASS, 'D', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), 'F', MatterOverdrive.items.networkFlashDrive);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.network_switch), " G ", "CFC", "OMO", 'M', MatterOverdrive.items.machine_casing, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0), 'G', Blocks.GLASS, 'F', MatterOverdrive.items.networkFlashDrive);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.matter_pipe, 8), " G ", "IMI", " G ", 'M', MatterOverdrive.items.s_magnet, 'G', Blocks.GLASS, 'I', Items.IRON_INGOT);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.network_pipe, 16), "IGI", "BCB", "IGI", 'M', MatterOverdrive.items.s_magnet, 'G', Blocks.GLASS, 'I', Items.IRON_INGOT, 'B', Items.GOLD_INGOT, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0));
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.matter_analyzer), " C ", "PMF", "ONO", 'O', Blocks.IRON_BLOCK, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), 'M', MatterOverdrive.items.me_conversion_matrix, 'N', MatterOverdrive.items.integration_matrix, 'P', MatterOverdrive.items.pattern_drive, 'F', MatterOverdrive.items.networkFlashDrive);
		addRecipe(new ShapedOreRecipe(new ItemStack(MatterOverdrive.blocks.tritanium_block), "TTT", "TTT", "TTT", 'T', "tritaniumIngot"));
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.machine_hull), " T ", "T T", " T ", 'T', MatterOverdrive.items.tritanium_plate);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.solar_panel), "CGC", "GQG", "KMK", 'C', Items.COAL, 'Q', Blocks.QUARTZ_BLOCK, 'K', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), 'M', MatterOverdrive.items.machine_casing, 'G', Blocks.GLASS);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.weapon_station), "   ", "GFR", "CMB", 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), 'B', MatterOverdrive.items.battery, 'G', Items.GLOWSTONE_DUST, 'R', Items.REDSTONE, 'M', MatterOverdrive.items.machine_casing, 'F', MatterOverdrive.items.forceFieldEmitter);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.pattern_storage), "B3B", "TCT", "2M1", 'B', new ItemStack(Blocks.WOOL, 1, 15), '1', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0), '2', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), '3', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), 'C', Blocks.CHEST, 'M', MatterOverdrive.items.machine_casing, 'T', MatterOverdrive.items.tritanium_ingot);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.pattern_monitor), " H ", "1N1", " F ", '1', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), 'H', MatterOverdrive.blocks.holoSign, 'N', MatterOverdrive.blocks.network_switch, 'F', MatterOverdrive.items.networkFlashDrive);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.transporter), "TGT", "CMC", "NBH", 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), 'M', MatterOverdrive.items.me_conversion_matrix, 'H', MatterOverdrive.items.h_compensator, 'E', Items.ENDER_PEARL, 'N', MatterOverdrive.items.integration_matrix, 'T', MatterOverdrive.items.tritanium_plate, 'G', Blocks.GLOWSTONE, 'B', MatterOverdrive.items.hc_battery);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.fusion_reactor_coil), "TMT", "M M", "CMC", 'M', MatterOverdrive.items.s_magnet, 'T', MatterOverdrive.items.tritanium_plate, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0));
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.recycler), "T T", "1P2", "NTM", '2', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), '1', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0), 'M', MatterOverdrive.items.me_conversion_matrix, 'N', MatterOverdrive.items.integration_matrix, 'T', MatterOverdrive.items.tritanium_plate, 'P', Blocks.PISTON);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.gravitational_stabilizer), " H ", "TST", "CMC", 'M', MatterOverdrive.items.machine_casing, 'S', MatterOverdrive.items.spacetime_equalizer, 'T', MatterOverdrive.items.tritanium_plate, 'C', MatterOverdrive.items.s_magnet, 'H', MatterOverdrive.blocks.holoSign);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.fusion_reactor_controller), "CHC", "2M3", "CTC", 'C', MatterOverdrive.blocks.fusion_reactor_coil, '2', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), '3', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), 'M', MatterOverdrive.items.machine_casing, 'T', MatterOverdrive.items.tritanium_plate, 'H', MatterOverdrive.blocks.holoSign);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.androidStation), "THA", "2F3", "GMR", '3', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), '2', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), 'F', MatterOverdrive.items.forceFieldEmitter, 'G', Items.GLOWSTONE_DUST, 'R', Items.REDSTONE, 'M', MatterOverdrive.items.machine_casing, 'H', new ItemStack(MatterOverdrive.items.androidParts, 1, 0), 'T', new ItemStack(MatterOverdrive.items.androidParts, 1, 3), 'A', new ItemStack(MatterOverdrive.items.androidParts, 1, 1));
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.starMap), " S ", "CFC", "GMR", 'S', MatterOverdrive.items.security_protocol, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), 'M', MatterOverdrive.items.machine_casing, 'F', MatterOverdrive.items.forceFieldEmitter, 'G', Items.GLOWSTONE_DUST, 'R', Items.REDSTONE);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.chargingStation), " F ", "EDR", "BMB", 'M', MatterOverdrive.items.machine_casing, 'B', MatterOverdrive.items.hc_battery, 'E', Items.ENDER_EYE, 'R', Items.REPEATER, 'F', MatterOverdrive.items.forceFieldEmitter, 'D', MatterOverdrive.items.dilithium_crystal);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.heavy_matter_pipe, 8), "RMR", "TMT", "RMR", 'M', MatterOverdrive.items.s_magnet, 'G', Blocks.GLASS, 'T', MatterOverdrive.items.tritanium_plate, 'R', Items.REDSTONE);
		addRecipe(new ShapedOreRecipe(new ItemStack(MatterOverdrive.blocks.holoSign), "GGG", "g0g", " T ", 'G', "blockGlass", 'g', Items.GLOWSTONE_DUST, '0', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0), 'T', MatterOverdrive.items.tritanium_plate));
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.forceGlass, 4), " G ", "GTG", " G ", 'G', Blocks.GLASS, 'T', MatterOverdrive.items.tritanium_plate);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.tritaniumCrate), "   ", "TCT", " T ", 'T', MatterOverdrive.items.tritanium_plate, 'C', Blocks.CHEST);
		addShapelessRecipe(new ItemStack(MatterOverdrive.blocks.tritaniumCrateYellow), new ItemStack(Items.DYE, 1, EnumDyeColor.YELLOW.getDyeDamage()), MatterOverdrive.blocks.tritaniumCrate);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.inscriber), "IDI", "TPT", "RMR", 'M', MatterOverdrive.items.machine_casing, 'D', MatterOverdrive.items.dilithium_crystal, 'T', MatterOverdrive.items.tritanium_plate, 'P', Blocks.PISTON, 'R', Items.REDSTONE, 'I', Items.IRON_INGOT);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.fusionReactorIO), "TGT", "C C", "TGT", 'G', Items.GOLD_INGOT, 'T', MatterOverdrive.items.tritanium_plate, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0));
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.contractMarket), " T ", "GEG", " M ", 'T', MatterOverdrive.items.tritanium_ingot, 'G', Items.GOLD_INGOT, 'E', Items.EMERALD, 'M', MatterOverdrive.items.machine_casing);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.spacetimeAccelerator), "THT", "DDD", "THT", 'T', MatterOverdrive.items.tritanium_plate, 'H', MatterOverdrive.items.h_compensator, 'D', MatterOverdrive.items.dilithium_crystal);

		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_tritanium_plate, 16), "SSS", "S#S", "SSS", '#', MatterOverdrive.items.tritanium_plate, 'S', Blocks.STONE);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_beams, 6), "#T#", "#T#", "#T#", '#', MatterOverdrive.blocks.decorative_tritanium_plate, 'T', MatterOverdrive.items.tritanium_nugget);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_tritanium_plate_stripe, 8), "###", "###", "#Y#", '#', MatterOverdrive.blocks.decorative_tritanium_plate, 'Y', new ItemStack(Items.DYE, 1, EnumDyeColor.YELLOW.getDyeDamage()), 'B', new ItemStack(Items.DYE, 1, EnumDyeColor.BLACK.getDyeDamage()));
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_holo_matrix, 8), "###", "#I#", "###", '#', MatterOverdrive.blocks.decorative_tritanium_plate, 'I', new ItemStack(MatterOverdrive.items.isolinear_circuit));
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_carbon_fiber_plate, 8), "###", "#C#", "###", '#', MatterOverdrive.blocks.decorative_tritanium_plate, 'C', new ItemStack(Items.COAL));
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_vent_bright, 6), " # ", "T T", " # ", '#', MatterOverdrive.items.tritanium_plate, 'T', MatterOverdrive.items.tritanium_ingot);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_vent_dark, 8), "###", "#B#", "###", '#', MatterOverdrive.blocks.decorative_vent_bright, 'B', new ItemStack(Items.DYE));
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_clean, 8), " S ", "STS", " S ", 'T', MatterOverdrive.items.tritanium_plate, 'S', Blocks.STONE);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_floor_tiles, 32), "###", "#Q#", "###", '#', Blocks.CLAY, 'Q', Items.QUARTZ);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_floor_tiles_green, 32), "#G#", "#Q#", "#G#", '#', Blocks.CLAY, 'Q', Items.QUARTZ, 'G', new ItemStack(Items.DYE, 1, EnumDyeColor.RED.getDyeDamage()));
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_floor_tile_white, 32), "#W#", "#Q#", "#W#", '#', Blocks.CLAY, 'Q', Items.QUARTZ, 'W', new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()));
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_separator, 8), "###", "#N#", "###", '#', MatterOverdrive.blocks.decorative_tritanium_plate, 'N', MatterOverdrive.items.tritanium_nugget);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_floor_noise, 32), "#G#", "#Q#", "#G#", '#', Blocks.CLAY, 'B', Items.BONE, 'G', Blocks.GRAVEL);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_white_plate, 8), "#W#", "###", "#W#", '#', MatterOverdrive.blocks.decorative_tritanium_plate, 'W', Blocks.WOOL);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_coils, 12), "###", "#C#", "###", '#', MatterOverdrive.blocks.decorative_tritanium_plate, 'C', MatterOverdrive.items.s_magnet);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_stripes, 8), "#B#", "###", "#Y#", '#', MatterOverdrive.blocks.decorative_tritanium_plate, 'B', new ItemStack(Items.DYE), 'Y', new ItemStack(Items.DYE, 1, EnumDyeColor.YELLOW.getDyeDamage()));
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_tritanium_lamp, 2), "###", "#G#", "GGG", '#', MatterOverdrive.blocks.decorative_tritanium_plate, 'G', Items.GLOWSTONE_DUST);
		for (EnumDyeColor dyeColor : EnumDyeColor.values())
		{
			addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_tritanium_plate_colored, 8, dyeColor.getDyeDamage()), "###", "#D#", "###", '#', MatterOverdrive.blocks.decorative_tritanium_plate, 'D', new ItemStack(Items.DYE, 1, dyeColor.getDyeDamage()));
		}

	}

	public static void registerItemRecipes(FMLInitializationEvent event)
	{
		addShapedRecipe(new ItemStack(MatterOverdrive.items.battery), " R ", "TGT", "TDT", 'T', MatterOverdrive.items.tritanium_ingot, 'D', MatterOverdrive.items.dilithium_crystal, 'R', Items.REDSTONE, 'G', Items.GOLD_INGOT);
		addShapedRecipe(new ItemStack(MatterOverdrive.items.hc_battery), " P ", "DBD", " P ", 'B', MatterOverdrive.items.battery, 'D', MatterOverdrive.items.dilithium_crystal, 'P', MatterOverdrive.items.tritanium_plate);
		addShapedRecipe(new ItemStack(MatterOverdrive.items.matter_scanner), "III", "GDG", "IRI", 'I', Items.IRON_INGOT, 'D', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), 'R', Items.REDSTONE, 'G', Items.GOLD_INGOT);
		addShapedRecipe(new ItemStack(MatterOverdrive.items.h_compensator), " M ", "CPC", "DED", 'D', MatterOverdrive.items.dilithium_crystal, 'M', MatterOverdrive.items.machine_casing, 'I', Items.IRON_INGOT, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0), 'P', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), 'E', Items.ENDER_EYE);
		addShapedRecipe(new ItemStack(MatterOverdrive.items.integration_matrix), " M ", "GPG", "DED", 'G', Blocks.GLASS, 'M', MatterOverdrive.items.machine_casing, 'I', Items.IRON_INGOT, 'P', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), 'E', Items.ENDER_PEARL, 'D', MatterOverdrive.items.dilithium_crystal);
		addShapedRecipe(new ItemStack(MatterOverdrive.items.machine_casing), " T ", "I I", "GRG", 'G', Items.GOLD_INGOT, 'T', MatterOverdrive.items.tritanium_plate, 'I', MatterOverdrive.items.tritanium_ingot, 'R', Items.REDSTONE);
		addShapedRecipe(new ItemStack(MatterOverdrive.items.s_magnet, 4), "RRR", "TET", "RRR", 'E', Items.ENDER_PEARL, 'T', MatterOverdrive.items.tritanium_ingot, 'R', Items.REDSTONE);
		addShapedRecipe(new ItemStack(MatterOverdrive.items.me_conversion_matrix), "EIE", "CDC", "EIE", 'E', Items.ENDER_PEARL, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), 'I', Items.IRON_INGOT, 'D', MatterOverdrive.items.dilithium_crystal);
		addShapedRecipe(new ItemStack(MatterOverdrive.items.tritanium_plate), "TT", 'T', new ItemStack(MatterOverdrive.items.tritanium_ingot));
		addShapedRecipe(new ItemStack(MatterOverdrive.items.phaser), "IGI", "IPH", "WCW", 'I', Items.IRON_INGOT, 'G', Blocks.GLASS, 'P', MatterOverdrive.items.plasmaCore, 'W', Blocks.WOOL, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), 'H', MatterOverdrive.items.weaponHandle);
		addShapedRecipe(new ItemStack(MatterOverdrive.items.pattern_drive), " E ", "RMR", " C ", 'M', MatterOverdrive.items.machine_casing, 'E', Items.ENDER_PEARL, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), 'R', Items.REDSTONE);
		addShapedRecipe(new ItemStack(MatterOverdrive.items.security_protocol), "PP", "CP", 'P', Items.PAPER, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0));
		addShapedRecipe(new ItemStack(MatterOverdrive.items.wrench), "T T", " Y ", " T ", 'T', MatterOverdrive.items.tritanium_ingot, 'Y', new ItemStack(Blocks.WOOL, 1, 4));
		addShapedRecipe(new ItemStack(MatterOverdrive.items.spacetime_equalizer), " M ", "EHE", " M ", 'M', MatterOverdrive.items.s_magnet, 'E', Items.ENDER_PEARL, 'H', MatterOverdrive.items.h_compensator);
		addShapedRecipe(new ItemStack(MatterOverdrive.items.forceFieldEmitter), "CGC", "CDC", "P1P", 'P', MatterOverdrive.items.tritanium_plate, 'E', Items.ENDER_PEARL, 'D', MatterOverdrive.items.dilithium_crystal, '1', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1), 'C', MatterOverdrive.items.s_magnet, 'G', Blocks.GLASS);
		addShapedRecipe(new ItemStack(MatterOverdrive.items.networkFlashDrive), "RCR", 'R', Items.REDSTONE, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0));
		addShapedRecipe(new ItemStack(MatterOverdrive.items.transportFlashDrive), " I ", "ECR", " I", 'I', Items.IRON_INGOT, 'R', Items.REDSTONE, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0));
		addRecipe(new EnergyPackRecipe(new ItemStack(MatterOverdrive.items.tritanium_plate), new ItemStack(MatterOverdrive.items.battery), new ItemStack(Items.GUNPOWDER)));
		addRecipe(new EnergyPackRecipe(new ItemStack(MatterOverdrive.items.tritanium_plate), new ItemStack(MatterOverdrive.items.hc_battery), new ItemStack(Items.GUNPOWDER)));
		addShapedRecipe(new ItemStack(MatterOverdrive.items.phaserRifle), "III", "SPC", "WHB", 'I', Items.IRON_INGOT, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), 'W', Blocks.WOOL, 'S', MatterOverdrive.items.weaponReceiver, 'D', MatterOverdrive.items.dilithium_crystal, 'P', MatterOverdrive.items.plasmaCore, 'B', MatterOverdrive.items.battery, 'H', MatterOverdrive.items.weaponHandle);
		addRecipe(new ShapedOreRecipe(new ItemStack(MatterOverdrive.items.matterContainer, 4), "TMT", " T ", 'T', MatterOverdrive.items.tritanium_ingot, 'M', MatterOverdrive.items.s_magnet));
		addRecipe(new ShapedOreRecipe(new ItemStack(MatterOverdrive.items.tritanium_ingot), "###", "###", "###", '#', "nuggetTritanium"));
		addRecipe(new ShapelessOreRecipe(new ItemStack(MatterOverdrive.items.tritanium_nugget, 9), "ingotTritanium"));
		addShapelessRecipe(new ItemStack(MatterOverdrive.items.dataPad), Items.BOOK, new ItemStack(MatterOverdrive.items.isolinear_circuit, 0));
		addShapedRecipe(new ItemStack(MatterOverdrive.items.omniTool), "IFC", "SPI", " BH", 'I', Items.IRON_INGOT, 'S', MatterOverdrive.items.weaponReceiver, 'P', MatterOverdrive.items.plasmaCore, 'B', MatterOverdrive.items.battery, 'F', MatterOverdrive.items.forceFieldEmitter, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), 'H', MatterOverdrive.items.weaponHandle);
		addShapedRecipe(new ItemStack(MatterOverdrive.items.tritaniumAxe), "XX ", "X# ", " # ", 'X', MatterOverdrive.items.tritanium_ingot, '#', Items.STICK);
		addShapedRecipe(new ItemStack(MatterOverdrive.items.tritaniumPickaxe), "XXX", " # ", " # ", 'X', MatterOverdrive.items.tritanium_ingot, '#', Items.STICK);
		addShapedRecipe(new ItemStack(MatterOverdrive.items.tritaniumSword), " X ", " X ", " # ", 'X', MatterOverdrive.items.tritanium_ingot, '#', Items.STICK);
		addShapedRecipe(new ItemStack(MatterOverdrive.items.tritaniumHoe), "XX ", " # ", " # ", 'X', MatterOverdrive.items.tritanium_ingot, '#', Items.STICK);
		addShapedRecipe(new ItemStack(MatterOverdrive.items.tritaniumShovel), " X ", " # ", " # ", 'X', MatterOverdrive.items.tritanium_ingot, '#', Items.STICK);
		addShapedRecipe(new ItemStack(MatterOverdrive.items.tritaniumHelmet), "XCX", "X X", "   ", 'X', MatterOverdrive.items.tritanium_ingot, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1));
		addShapedRecipe(new ItemStack(MatterOverdrive.items.tritaniumChestplate), "X X", "XCX", "XXX", 'X', MatterOverdrive.items.tritanium_ingot, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1));
		addShapedRecipe(new ItemStack(MatterOverdrive.items.tritaniumLeggings), "XCX", "X X", "X X", 'X', MatterOverdrive.items.tritanium_ingot, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1));
		addShapedRecipe(new ItemStack(MatterOverdrive.items.tritaniumBoots), "   ", "X X", "X X", 'X', MatterOverdrive.items.tritanium_ingot);
		addShapedRecipe(new ItemStack(MatterOverdrive.items.isolinear_circuit), "I", "R", "G", 'G', Item.getItemFromBlock(Blocks.GLASS), 'R', Items.REDSTONE, 'I', Items.IRON_INGOT);
		addShapedRecipe(new ItemStack(MatterOverdrive.items.sniperScope), "IIC", "GFG", "III", 'I', Items.IRON_INGOT, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), 'G', new ItemStack(Blocks.STAINED_GLASS_PANE, 1, 5), 'F', MatterOverdrive.items.forceFieldEmitter);
		addShapedRecipe(new ItemStack(MatterOverdrive.items.weaponHandle), "TWT", "I I", "I I", 'I', Items.IRON_INGOT, 'W', new ItemStack(Blocks.WOOL, 1, 15), 'T', MatterOverdrive.items.tritanium_ingot);
		addShapedRecipe(new ItemStack(MatterOverdrive.items.weaponReceiver), "IRT", "   ", "IIT", 'I', Items.IRON_INGOT, 'R', Items.REDSTONE, 'T', MatterOverdrive.items.tritanium_ingot);
		addShapedRecipe(new ItemStack(MatterOverdrive.items.plasmaCore), "GI ", "MCM", " IG", 'G', Blocks.GLASS, 'I', Items.IRON_INGOT, 'M', MatterOverdrive.items.s_magnet, 'C', new ItemStack(MatterOverdrive.items.matterContainer));
		addShapedRecipe(new ItemStack(MatterOverdrive.items.plasmaShotgun), "SP ", "ICH", "SPB", 'S', MatterOverdrive.items.weaponReceiver, 'P', MatterOverdrive.items.plasmaCore, 'I', Items.IRON_INGOT, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), 'B', MatterOverdrive.items.battery, 'H', MatterOverdrive.items.weaponHandle);
		addShapedRecipe(new ItemStack(MatterOverdrive.items.ionSniper), "ICI", "SPP", " HB", 'I', Items.IRON_INGOT, 'S', MatterOverdrive.items.weaponReceiver, 'P', MatterOverdrive.items.plasmaCore, 'H', MatterOverdrive.items.weaponHandle, 'B', MatterOverdrive.items.battery, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 3));
		addShapedRecipe(new ItemStack(MatterOverdrive.items.portableDecomposer), " T ", "IPM", " T ", 'T', MatterOverdrive.items.tritanium_plate, 'I', MatterOverdrive.items.integration_matrix, 'M', MatterOverdrive.items.me_conversion_matrix, 'P', Blocks.STICKY_PISTON);
		addShapedRecipe(new ItemStack(MatterOverdrive.items.item_upgrade, 1, 0), " R ", " C ", " T ", 'G', Blocks.GLASS, 'R', Items.REDSTONE, 'T', MatterOverdrive.items.tritanium_plate, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0));
		//speed
		addShapedRecipe(new ItemStack(MatterOverdrive.items.item_upgrade, 1, 1), " R ", "GUG", " E ", 'U', MatterOverdrive.items.item_upgrade, 'G', Items.GLOWSTONE_DUST, 'R', Items.REDSTONE, 'E', Items.EMERALD);
		//power
		addShapedRecipe(new ItemStack(MatterOverdrive.items.item_upgrade, 1, 2), " B ", "RUR", " C ", 'U', MatterOverdrive.items.item_upgrade, 'B', MatterOverdrive.items.battery, 'R', Items.REDSTONE, 'C', Items.QUARTZ);
		//failsafe
		addShapedRecipe(new ItemStack(MatterOverdrive.items.item_upgrade, 1, 3), " D ", "RUR", " G ", 'U', MatterOverdrive.items.item_upgrade, 'D', Items.DIAMOND, 'R', Items.REDSTONE, 'G', Items.GOLD_INGOT);
		//range
		addShapedRecipe(new ItemStack(MatterOverdrive.items.item_upgrade, 1, 4), " E ", "RUR", " G ", 'U', MatterOverdrive.items.item_upgrade, 'E', Items.ENDER_PEARL, 'R', Items.REDSTONE, 'G', Items.GOLD_INGOT);
		//power storage
		addShapedRecipe(new ItemStack(MatterOverdrive.items.item_upgrade, 1, 5), "   ", "RUR", " B ", 'U', MatterOverdrive.items.item_upgrade, 'B', MatterOverdrive.items.hc_battery, 'R', Items.REDSTONE, 'G', Items.GOLD_INGOT);
		//hyper speed
		addShapelessRecipe(new ItemStack(MatterOverdrive.items.item_upgrade, 1, 6), MatterOverdrive.items.dilithium_crystal, Items.NETHER_STAR, new ItemStack(MatterOverdrive.items.item_upgrade, 1, 1));
		//matter storage
		addShapedRecipe(new ItemStack(MatterOverdrive.items.item_upgrade, 1, 7), " R ", "MUM", " R ", 'U', MatterOverdrive.items.item_upgrade, 'M', MatterOverdrive.items.s_magnet, 'R', Items.REDSTONE);
		GameRegistry.addRecipe(new ItemStack(MatterOverdrive.items.weapon_module_barrel, 1, 0), " G ", "RDR", " T ", 'T', MatterOverdrive.items.tritanium_plate, 'D', MatterOverdrive.items.dilithium_crystal, 'R', Items.REDSTONE, 'G', Blocks.GLASS);
		GameRegistry.addRecipe(new ItemStack(MatterOverdrive.items.weapon_module_barrel, 1, 1), " G ", "BFB", " T ", 'T', MatterOverdrive.items.tritanium_plate, 'F', Items.FIRE_CHARGE, 'B', Items.BLAZE_ROD, 'G', Blocks.GLASS);
		GameRegistry.addRecipe(new ItemStack(MatterOverdrive.items.weapon_module_barrel, 1, 2), " B ", "BRB", "DTD", 'T', MatterOverdrive.items.tritanium_plate, 'R', Items.BLAZE_ROD, 'B', Blocks.TNT, 'G', Blocks.GLASS, 'D', Items.DIAMOND);
		GameRegistry.addRecipe(new ItemStack(MatterOverdrive.items.weapon_module_barrel, 1, 3), " S ", "SAS", "ETE", 'T', MatterOverdrive.items.tritanium_plate, 'A', Items.GOLDEN_APPLE, 'S', Items.SUGAR, 'G', Blocks.GLASS, 'E', Items.EMERALD);

		RecipeSorter.register("mo:energyPack", EnergyPackRecipe.class, RecipeSorter.Category.SHAPELESS, "");
	}

	public static void registerInscriberRecipes(FMLInitializationEvent event)
	{
		File file = new File(MatterOverdrive.configHandler.configDir, "MatterOverdrive/recipes/Inscriber.xml");
		if (!file.exists()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
				IOUtils.copy(MatterOverdriveRecipes.class.getResourceAsStream("/assets/mo/recipes/Inscriber.xml"), new FileOutputStream(file));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		INSCRIBER.load(file);
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
