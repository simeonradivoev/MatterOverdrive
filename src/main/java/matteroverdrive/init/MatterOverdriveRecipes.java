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
import net.minecraft.block.Block;
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
		addShapedRecipe(MatterOverdrive.blocks.decomposer, "TCT", "S S", "NTM", 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), 'M', MatterOverdrive.items.me_conversion_matrix, 'N', MatterOverdrive.items.integration_matrix, 'S', Blocks.STICKY_PISTON, 'T', MatterOverdrive.items.tritanium_plate);
		addShapedOreRecipe(MatterOverdrive.blocks.replicator, "PCF", "IHI", "NTM", 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), 'M', MatterOverdrive.items.me_conversion_matrix, 'H', MatterOverdrive.items.h_compensator, 'I', "ingotIron", 'N', MatterOverdrive.items.integration_matrix, 'T', MatterOverdrive.items.tritanium_plate, 'F', MatterOverdrive.items.networkFlashDrive, 'P', MatterOverdrive.items.pattern_drive);
		addShapedOreRecipe(MatterOverdrive.blocks.network_router, "IGI", "DFC", "OMO", 'M', MatterOverdrive.items.machine_casing, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0), 'I', "ingotIron", 'G', "blockGlass", 'D', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), 'F', MatterOverdrive.items.networkFlashDrive);
		addShapedOreRecipe(MatterOverdrive.blocks.network_switch, " G ", "CFC", "OMO", 'M', MatterOverdrive.items.machine_casing, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0), 'G', "blockGlass", 'F', MatterOverdrive.items.networkFlashDrive);
		addShapedOreRecipe(new ItemStack(MatterOverdrive.blocks.matter_pipe, 8), " G ", "IMI", " G ", 'M', MatterOverdrive.items.s_magnet, 'G', "blockGlass", 'I', "ingotIron");
		addShapedOreRecipe(new ItemStack(MatterOverdrive.blocks.network_pipe, 16), "IGI", "BCB", "IGI", 'M', MatterOverdrive.items.s_magnet, 'G', "blockGlass", 'I', "ingotIron", 'B', "ingotGold", 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0));
		addShapedOreRecipe(MatterOverdrive.blocks.matter_analyzer, " C ", "PMF", "ONO", 'O', "blockIron", 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), 'M', MatterOverdrive.items.me_conversion_matrix, 'N', MatterOverdrive.items.integration_matrix, 'P', MatterOverdrive.items.pattern_drive, 'F', MatterOverdrive.items.networkFlashDrive);
		addShapedOreRecipe(MatterOverdrive.blocks.tritanium_block, "TTT", "TTT", "TTT", 'T', "ingotTritanium");
		addShapedRecipe(MatterOverdrive.blocks.machine_hull, " T ", "T T", " T ", 'T', MatterOverdrive.items.tritanium_plate);
		addShapedOreRecipe(MatterOverdrive.blocks.solar_panel, "CGC", "GQG", "KMK", 'C', Items.COAL, 'Q', "blockQuartz", 'K', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), 'M', MatterOverdrive.items.machine_casing, 'G', "blockGlass");
		addShapedOreRecipe(MatterOverdrive.blocks.weapon_station, "   ", "GFR", "CMB", 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), 'B', MatterOverdrive.items.battery, 'G', "dustGlowstone", 'R', "dustRedstone", 'M', MatterOverdrive.items.machine_casing, 'F', MatterOverdrive.items.forceFieldEmitter);
		addShapedOreRecipe(MatterOverdrive.blocks.pattern_storage, "B3B", "TCT", "2M1", 'B', new ItemStack(Blocks.WOOL, 1, 15), '1', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0), '2', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), '3', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), 'C', Blocks.CHEST, 'M', MatterOverdrive.items.machine_casing, 'T', "ingotTritanium");
		addShapedRecipe(MatterOverdrive.blocks.pattern_monitor, " H ", "1N1", " F ", '1', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), 'H', MatterOverdrive.blocks.holoSign, 'N', MatterOverdrive.blocks.network_switch, 'F', MatterOverdrive.items.networkFlashDrive);
		addShapedOreRecipe(MatterOverdrive.blocks.transporter, "TGT", "CMC", "NBH", 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), 'M', MatterOverdrive.items.me_conversion_matrix, 'H', MatterOverdrive.items.h_compensator, 'E', "enderpearl", 'N', MatterOverdrive.items.integration_matrix, 'T', MatterOverdrive.items.tritanium_plate, 'G', "glowstone", 'B', MatterOverdrive.items.hc_battery);
		addShapedRecipe(MatterOverdrive.blocks.fusion_reactor_coil, "TMT", "M M", "CMC", 'M', MatterOverdrive.items.s_magnet, 'T', MatterOverdrive.items.tritanium_plate, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0));
		addShapedRecipe(MatterOverdrive.blocks.recycler, "T T", "1P2", "NTM", '2', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), '1', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0), 'M', MatterOverdrive.items.me_conversion_matrix, 'N', MatterOverdrive.items.integration_matrix, 'T', MatterOverdrive.items.tritanium_plate, 'P', Blocks.PISTON);
		addShapedRecipe(MatterOverdrive.blocks.gravitational_stabilizer, " H ", "TST", "CMC", 'M', MatterOverdrive.items.machine_casing, 'S', MatterOverdrive.items.spacetime_equalizer, 'T', MatterOverdrive.items.tritanium_plate, 'C', MatterOverdrive.items.s_magnet, 'H', MatterOverdrive.blocks.holoSign);
		addShapedRecipe(MatterOverdrive.blocks.fusion_reactor_controller, "CHC", "2M3", "CTC", 'C', MatterOverdrive.blocks.fusion_reactor_coil, '2', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), '3', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), 'M', MatterOverdrive.items.machine_casing, 'T', MatterOverdrive.items.tritanium_plate, 'H', MatterOverdrive.blocks.holoSign);
		addShapedOreRecipe(MatterOverdrive.blocks.androidStation, "THA", "2F3", "GMR", '3', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), '2', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), 'F', MatterOverdrive.items.forceFieldEmitter, 'G', "dustGlowstone", 'R', "dustRedstone", 'M', MatterOverdrive.items.machine_casing, 'H', new ItemStack(MatterOverdrive.items.androidParts, 1, 0), 'T', new ItemStack(MatterOverdrive.items.androidParts, 1, 3), 'A', new ItemStack(MatterOverdrive.items.androidParts, 1, 1));
		addShapedOreRecipe(MatterOverdrive.blocks.starMap, " S ", "CFC", "GMR", 'S', MatterOverdrive.items.security_protocol, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), 'M', MatterOverdrive.items.machine_casing, 'F', MatterOverdrive.items.forceFieldEmitter, 'G', "dustGlowstone", 'R', "dustRedstone");
		addShapedOreRecipe(MatterOverdrive.blocks.chargingStation, " F ", "EDR", "BMB", 'M', MatterOverdrive.items.machine_casing, 'B', MatterOverdrive.items.hc_battery, 'E', Items.ENDER_EYE, 'R', Items.REPEATER, 'F', MatterOverdrive.items.forceFieldEmitter, 'D', "gemDilithium");
		addShapedOreRecipe(new ItemStack(MatterOverdrive.blocks.heavy_matter_pipe, 8), "RMR", "TMT", "RMR", 'M', MatterOverdrive.items.s_magnet, 'G', "blockGlass", 'T', MatterOverdrive.items.tritanium_plate, 'R', "dustRedstone");
		addShapedOreRecipe(MatterOverdrive.blocks.holoSign, "GGG", "g0g", " T ", 'G', "blockGlass", 'g', "dustGlowstone", '0', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0), 'T', MatterOverdrive.items.tritanium_plate);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.forceGlass, 4), " G ", "GTG", " G ", 'G', Blocks.GLASS, 'T', MatterOverdrive.items.tritanium_plate);
		addShapedRecipe(MatterOverdrive.blocks.tritaniumCrate, "   ", "TCT", " T ", 'T', MatterOverdrive.items.tritanium_plate, 'C', Blocks.CHEST);
		addShapelessRecipe(new ItemStack(MatterOverdrive.blocks.tritaniumCrateYellow), new ItemStack(Items.DYE, 1, EnumDyeColor.YELLOW.getDyeDamage()), MatterOverdrive.blocks.tritaniumCrate);
		addShapedOreRecipe(MatterOverdrive.blocks.inscriber, "IDI", "TPT", "RMR", 'M', MatterOverdrive.items.machine_casing, 'D', "gemDilithium", 'T', MatterOverdrive.items.tritanium_plate, 'P', Blocks.PISTON, 'R', "dustRedstone", 'I', "ingotIron");
		addShapedOreRecipe(MatterOverdrive.blocks.fusionReactorIO, "TGT", "C C", "TGT", 'G', "ingotGold", 'T', MatterOverdrive.items.tritanium_plate, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0));
		addShapedOreRecipe(MatterOverdrive.blocks.contractMarket, " T ", "GEG", " M ", 'T', "ingotTritanium", 'G', "ingotGold", 'E', "gemEmerald", 'M', MatterOverdrive.items.machine_casing);
		addShapedOreRecipe(MatterOverdrive.blocks.spacetimeAccelerator, "THT", "DDD", "THT", 'T', MatterOverdrive.items.tritanium_plate, 'H', MatterOverdrive.items.h_compensator, 'D', "gemDilithium");

		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_tritanium_plate, 16), "SSS", "S#S", "SSS", '#', MatterOverdrive.items.tritanium_plate, 'S', Blocks.STONE);
		addShapedOreRecipe(new ItemStack(MatterOverdrive.blocks.decorative_beams, 6), "#T#", "#T#", "#T#", '#', MatterOverdrive.blocks.decorative_tritanium_plate, 'T', "nuggetTritanium");
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_tritanium_plate_stripe, 8), "###", "###", "#Y#", '#', MatterOverdrive.blocks.decorative_tritanium_plate, 'Y', new ItemStack(Items.DYE, 1, EnumDyeColor.YELLOW.getDyeDamage()), 'B', new ItemStack(Items.DYE, 1, EnumDyeColor.BLACK.getDyeDamage()));
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_holo_matrix, 8), "###", "#I#", "###", '#', MatterOverdrive.blocks.decorative_tritanium_plate, 'I', MatterOverdrive.items.isolinear_circuit);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_carbon_fiber_plate, 8), "###", "#C#", "###", '#', MatterOverdrive.blocks.decorative_tritanium_plate, 'C', Items.COAL);
		addShapedOreRecipe(new ItemStack(MatterOverdrive.blocks.decorative_vent_bright, 6), " # ", "T T", " # ", '#', MatterOverdrive.items.tritanium_plate, 'T', "ingotTritanium");
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_vent_dark, 8), "###", "#B#", "###", '#', MatterOverdrive.blocks.decorative_vent_bright, 'B', Items.DYE);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_clean, 8), " S ", "STS", " S ", 'T', MatterOverdrive.items.tritanium_plate, 'S', Blocks.STONE);
		addShapedOreRecipe(new ItemStack(MatterOverdrive.blocks.decorative_floor_tiles, 32), "###", "#Q#", "###", '#', Blocks.CLAY, 'Q', "gemQuartz");
		addShapedOreRecipe(new ItemStack(MatterOverdrive.blocks.decorative_floor_tiles_green, 32), "#G#", "#Q#", "#G#", '#', Blocks.CLAY, 'Q', "gemQuartz", 'G', new ItemStack(Items.DYE, 1, EnumDyeColor.RED.getDyeDamage()));
		addShapedOreRecipe(new ItemStack(MatterOverdrive.blocks.decorative_floor_tile_white, 32), "#W#", "#Q#", "#W#", '#', Blocks.CLAY, 'Q', "gemQuartz", 'W', new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()));
		addShapedOreRecipe(new ItemStack(MatterOverdrive.blocks.decorative_separator, 8), "###", "#N#", "###", '#', MatterOverdrive.blocks.decorative_tritanium_plate, 'N', "nuggetTritanium");
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_floor_noise, 32), "#G#", "#Q#", "#G#", '#', Blocks.CLAY, 'B', Items.BONE, 'G', Blocks.GRAVEL);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_white_plate, 8), "#W#", "###", "#W#", '#', MatterOverdrive.blocks.decorative_tritanium_plate, 'W', Blocks.WOOL);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_coils, 12), "###", "#C#", "###", '#', MatterOverdrive.blocks.decorative_tritanium_plate, 'C', MatterOverdrive.items.s_magnet);
		addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_stripes, 8), "#B#", "###", "#Y#", '#', MatterOverdrive.blocks.decorative_tritanium_plate, 'B', new ItemStack(Items.DYE), 'Y', new ItemStack(Items.DYE, 1, EnumDyeColor.YELLOW.getDyeDamage()));
		addShapedOreRecipe(new ItemStack(MatterOverdrive.blocks.decorative_tritanium_lamp, 2), "###", "#G#", "GGG", '#', MatterOverdrive.blocks.decorative_tritanium_plate, 'G', "dustGlowstone");
		for (EnumDyeColor dyeColor : EnumDyeColor.values())
		{
			addShapedRecipe(new ItemStack(MatterOverdrive.blocks.decorative_tritanium_plate_colored, 8, dyeColor.getDyeDamage()), "###", "#D#", "###", '#', MatterOverdrive.blocks.decorative_tritanium_plate, 'D', new ItemStack(Items.DYE, 1, dyeColor.getDyeDamage()));
		}

	}

	public static void registerItemRecipes(FMLInitializationEvent event)
	{
		addShapedOreRecipe(MatterOverdrive.items.battery, " R ", "TGT", "TDT", 'T', "ingotTritanium", 'D', "gemDilithium", 'R', "dustRedstone", 'G', "ingotGold");
		addShapedOreRecipe(MatterOverdrive.items.hc_battery, " P ", "DBD", " P ", 'B', MatterOverdrive.items.battery, 'D', "gemDilithium", 'P', MatterOverdrive.items.tritanium_plate);
		addShapedOreRecipe(MatterOverdrive.items.matter_scanner, "III", "GDG", "IRI", 'I', "ingotIron", 'D', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), 'R', "dustRedstone", 'G', "ingotGold");
		addShapedOreRecipe(MatterOverdrive.items.h_compensator, " M ", "CPC", "DED", 'D', "gemDilithium", 'M', MatterOverdrive.items.machine_casing, 'I', "ingotIron", 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0), 'P', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), 'E', Items.ENDER_EYE);
		addShapedOreRecipe(MatterOverdrive.items.integration_matrix, " M ", "GPG", "DED", 'G', "blockGlass", 'M', MatterOverdrive.items.machine_casing, 'I', "ingotIron", 'P', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), 'E', "enderpearl", 'D', "gemDilithium");
		addShapedOreRecipe(MatterOverdrive.items.machine_casing, " T ", "I I", "GRG", 'G', "ingotGold", 'T', MatterOverdrive.items.tritanium_plate, 'I', "ingotTritanium", 'R', "dustRedstone");
		addShapedOreRecipe(new ItemStack(MatterOverdrive.items.s_magnet, 4), "RRR", "TET", "RRR", 'E', "enderpearl", 'T', "ingotTritanium", 'R', "dustRedstone");
		addShapedOreRecipe(MatterOverdrive.items.me_conversion_matrix, "EIE", "CDC", "EIE", 'E', "enderpearl", 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), 'I', "ingotIron", 'D', "gemDilithium");
		addShapedOreRecipe(MatterOverdrive.items.tritanium_plate, "TT", 'T', "ingotTritanium");
		addShapedOreRecipe(MatterOverdrive.items.phaser, "IGI", "IPH", "WCW", 'I', "ingotIron", 'G', "blockGlass", 'P', MatterOverdrive.items.plasmaCore, 'W', Blocks.WOOL, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), 'H', MatterOverdrive.items.weaponHandle);
		addShapedOreRecipe(MatterOverdrive.items.pattern_drive, " E ", "RMR", " C ", 'M', MatterOverdrive.items.machine_casing, 'E', "enderpearl", 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), 'R', "dustRedstone");
		addShapedOreRecipe(MatterOverdrive.items.security_protocol, "PP", "CP", 'P', "paper", 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0));
		addShapedOreRecipe(MatterOverdrive.items.wrench, "T T", " Y ", " T ", 'T', "ingotTritanium", 'Y', new ItemStack(Blocks.WOOL, 1, 4));
		addShapedRecipe(MatterOverdrive.items.spacetime_equalizer, " M ", "EHE", " M ", 'M', MatterOverdrive.items.s_magnet, 'E', Items.ENDER_PEARL, 'H', MatterOverdrive.items.h_compensator);
		addShapedOreRecipe(MatterOverdrive.items.forceFieldEmitter, "CGC", "CDC", "P1P", 'P', MatterOverdrive.items.tritanium_plate, 'E', "enderpearl", 'D', "gemDilithium", '1', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1), 'C', MatterOverdrive.items.s_magnet, 'G', "blockGlass");
		addShapedOreRecipe(MatterOverdrive.items.networkFlashDrive, "RCR", 'R', "dustRedstone", 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0));
		addShapedOreRecipe(MatterOverdrive.items.transportFlashDrive, " I ", "ECR", " I ", 'I', "ingotIron", 'R', "dustRedstone", 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0));
		addRecipe(new EnergyPackRecipe(new ItemStack(MatterOverdrive.items.tritanium_plate), new ItemStack(MatterOverdrive.items.battery), new ItemStack(Items.GUNPOWDER)));
		addRecipe(new EnergyPackRecipe(new ItemStack(MatterOverdrive.items.tritanium_plate), new ItemStack(MatterOverdrive.items.hc_battery), new ItemStack(Items.GUNPOWDER)));
		addShapedOreRecipe(MatterOverdrive.items.phaserRifle, "III", "SPC", "WHB", 'I', Items.IRON_INGOT, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), 'W', Blocks.WOOL, 'S', MatterOverdrive.items.weaponReceiver, 'D', "gemDilithium", 'P', MatterOverdrive.items.plasmaCore, 'B', MatterOverdrive.items.battery, 'H', MatterOverdrive.items.weaponHandle);
		addShapedOreRecipe(new ItemStack(MatterOverdrive.items.matterContainer, 4), "TMT", " T ", 'T', "ingotTritanium", 'M', MatterOverdrive.items.s_magnet);
		addShapedOreRecipe(MatterOverdrive.items.tritanium_ingot, "###", "###", "###", '#', "nuggetTritanium");
		addShapelessOreRecipe(new ItemStack(MatterOverdrive.items.tritanium_nugget, 9), "ingotTritanium");
		addShapelessRecipe(MatterOverdrive.items.dataPad, Items.BOOK, new ItemStack(MatterOverdrive.items.isolinear_circuit, 0));
		addShapedOreRecipe(MatterOverdrive.items.omniTool, "IFC", "SPI", " BH", 'I', "ingotIron", 'S', MatterOverdrive.items.weaponReceiver, 'P', MatterOverdrive.items.plasmaCore, 'B', MatterOverdrive.items.battery, 'F', MatterOverdrive.items.forceFieldEmitter, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), 'H', MatterOverdrive.items.weaponHandle);
		addShapedOreRecipe(MatterOverdrive.items.tritaniumAxe, "XX ", "X# ", " # ", 'X', "ingotTritanium", '#', "stickWood");
		addShapedOreRecipe(MatterOverdrive.items.tritaniumPickaxe, "XXX", " # ", " # ", 'X', "ingotTritanium", '#', "stickWood");
		addShapedOreRecipe(MatterOverdrive.items.tritaniumSword, " X ", " X ", " # ", 'X', "ingotTritanium", '#', "stickWood");
		addShapedOreRecipe(MatterOverdrive.items.tritaniumHoe, "XX ", " # ", " # ", 'X', "ingotTritanium", '#', "stickWood");
		addShapedOreRecipe(MatterOverdrive.items.tritaniumShovel, " X ", " # ", " # ", 'X', "ingotTritanium", '#', "stickWood");
		addShapedOreRecipe(MatterOverdrive.items.tritaniumHelmet, "XCX", "X X", "   ", 'X', "ingotTritanium", 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1));
		addShapedOreRecipe(MatterOverdrive.items.tritaniumChestplate, "X X", "XCX", "XXX", 'X', "ingotTritanium", 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1));
		addShapedOreRecipe(MatterOverdrive.items.tritaniumLeggings, "XCX", "X X", "X X", 'X', "ingotTritanium", 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1));
		addShapedOreRecipe(MatterOverdrive.items.tritaniumBoots, "   ", "X X", "X X", 'X', "ingotTritanium");
		addShapedOreRecipe(MatterOverdrive.items.isolinear_circuit, "I", "R", "G", 'G', "blockGlass", 'R', "dustRedstone", 'I', "ingotIron");
		addShapedOreRecipe(MatterOverdrive.items.sniperScope, "IIC", "GFG", "III", 'I', "ingotIron", 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 1), 'G', new ItemStack(Blocks.STAINED_GLASS_PANE, 1, 5), 'F', MatterOverdrive.items.forceFieldEmitter);
		addShapedOreRecipe(MatterOverdrive.items.weaponHandle, "TWT", "I I", "I I", 'I', "ingotIron", 'W', new ItemStack(Blocks.WOOL, 1, 15), 'T', "ingotTritanium");
		addShapedOreRecipe(MatterOverdrive.items.weaponReceiver, "IRT", "   ", "IIT", 'I', "ingotIron", 'R', "dustRedstone", 'T', "ingotTritanium");
		addShapedOreRecipe(MatterOverdrive.items.plasmaCore, "GI ", "MCM", " IG", 'G', "blockGlass", 'I', "ingotIron", 'M', MatterOverdrive.items.s_magnet, 'C', new ItemStack(MatterOverdrive.items.matterContainer));
		addShapedOreRecipe(MatterOverdrive.items.plasmaShotgun, "SP ", "ICH", "SPB", 'S', MatterOverdrive.items.weaponReceiver, 'P', MatterOverdrive.items.plasmaCore, 'I', "ingotIron", 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 2), 'B', MatterOverdrive.items.battery, 'H', MatterOverdrive.items.weaponHandle);
		addShapedOreRecipe(MatterOverdrive.items.ionSniper, "ICI", "SPP", " HB", 'I', "ingotIron", 'S', MatterOverdrive.items.weaponReceiver, 'P', MatterOverdrive.items.plasmaCore, 'H', MatterOverdrive.items.weaponHandle, 'B', MatterOverdrive.items.battery, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 3));
		addShapedRecipe(MatterOverdrive.items.portableDecomposer, " T ", "IPM", " T ", 'T', MatterOverdrive.items.tritanium_plate, 'I', MatterOverdrive.items.integration_matrix, 'M', MatterOverdrive.items.me_conversion_matrix, 'P', Blocks.STICKY_PISTON);
		addShapedOreRecipe(new ItemStack(MatterOverdrive.items.item_upgrade, 1, 0), " R ", " C ", " T ", 'G', "blockGlass", 'R', "dustRedstone", 'T', MatterOverdrive.items.tritanium_plate, 'C', new ItemStack(MatterOverdrive.items.isolinear_circuit, 1, 0));
		//speed
		addShapedOreRecipe(new ItemStack(MatterOverdrive.items.item_upgrade, 1, 1), " R ", "GUG", " E ", 'U', MatterOverdrive.items.item_upgrade, 'G', "dustGlowstone", 'R', "dustRedstone", 'E', "gemEmerald");
		//power
		addShapedOreRecipe(new ItemStack(MatterOverdrive.items.item_upgrade, 1, 2), " B ", "RUR", " C ", 'U', MatterOverdrive.items.item_upgrade, 'B', MatterOverdrive.items.battery, 'R', "dustRedstone", 'C', "gemQuartz");
		//failsafe
		addShapedOreRecipe(new ItemStack(MatterOverdrive.items.item_upgrade, 1, 3), " D ", "RUR", " G ", 'U', MatterOverdrive.items.item_upgrade, 'D', "gemDiamond", 'R', "dustRedstone", 'G', "ingotGold");
		//range
		addShapedOreRecipe(new ItemStack(MatterOverdrive.items.item_upgrade, 1, 4), " E ", "RUR", " G ", 'U', MatterOverdrive.items.item_upgrade, 'E', "enderpearl", 'R', "dustRedstone", 'G', "ingotGold");
		//power storage
		addShapedOreRecipe(new ItemStack(MatterOverdrive.items.item_upgrade, 1, 5), "   ", "RUR", " B ", 'U', MatterOverdrive.items.item_upgrade, 'B', MatterOverdrive.items.hc_battery, 'R', "dustRedstone", 'G', "ingotGold");
		//hyper speed
		addRecipe(new ShapelessOreRecipe(new ItemStack(MatterOverdrive.items.item_upgrade, 1, 6), "gemDilithium", Items.NETHER_STAR, new ItemStack(MatterOverdrive.items.item_upgrade, 1, 1)));
		//matter storage
		addShapedOreRecipe(new ItemStack(MatterOverdrive.items.item_upgrade, 1, 7), " R ", "MUM", " R ", 'U', MatterOverdrive.items.item_upgrade, 'M', MatterOverdrive.items.s_magnet, 'R', "dustRedstone");
		addShapedOreRecipe(new ItemStack(MatterOverdrive.items.weapon_module_barrel, 1, 0), " G ", "RDR", " T ", 'T', MatterOverdrive.items.tritanium_plate, 'D', "gemDilithium", 'R', "dustRedstone", 'G', "blockGlass");
		addShapedOreRecipe(new ItemStack(MatterOverdrive.items.weapon_module_barrel, 1, 1), " G ", "BFB", " T ", 'T', MatterOverdrive.items.tritanium_plate, 'F', Items.FIRE_CHARGE, 'B', Items.BLAZE_ROD, 'G', "blockGlass");
		addShapedOreRecipe(new ItemStack(MatterOverdrive.items.weapon_module_barrel, 1, 2), " B ", "BRB", "DTD", 'T', MatterOverdrive.items.tritanium_plate, 'R', Items.BLAZE_ROD, 'B', Blocks.TNT, 'G', "blockGlass", 'D', "gemDiamond");
		addShapedOreRecipe(new ItemStack(MatterOverdrive.items.weapon_module_barrel, 1, 3), " S ", "SAS", "ETE", 'T', MatterOverdrive.items.tritanium_plate, 'A', Items.GOLDEN_APPLE, 'S', Items.SUGAR, 'G', "blockGlass", 'E', "gemEmerald");

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

	private static void addShapedOreRecipe(ItemStack output, Object... params)
	{
		addRecipe(new ShapedOreRecipe(output, params));
	}

	private static void addShapedOreRecipe(Item output, Object... params)
	{
		addShapedOreRecipe(new ItemStack(output), params);
	}

	private static void addShapedOreRecipe(Block output, Object... params)
	{
		addShapedOreRecipe(new ItemStack(output), params);
	}

	private static void addShapelessOreRecipe(ItemStack output, Object... params)
	{
		addRecipe(new ShapelessOreRecipe(output, params));
	}

	private static void addShapelessOreRecipe(Item output, Object... params)
	{
		addShapelessOreRecipe(new ItemStack(output), params);
	}

	private static void addShapelessOreRecipe(Block output, Object... params)
	{
		addShapedOreRecipe(new ItemStack(output), params);
	}

	private static void addShapedRecipe(ItemStack output, Object... params)
	{
		recipes.add(GameRegistry.addShapedRecipe(output, params));
	}

	private static void addShapedRecipe(Item output, Object... params)
	{
		addShapedRecipe(new ItemStack(output), params);
	}

	private static void addShapedRecipe(Block output, Object... params)
	{
		addShapedRecipe(new ItemStack(output), params);
	}

	private static void addShapelessRecipe(ItemStack output, Object... items)
	{
		GameRegistry.addShapelessRecipe(output, items);
	}

	private static void addShapelessRecipe(Item output, Object... params)
	{
		addShapelessRecipe(new ItemStack(output), params);
	}

	private static void addShapelessRecipe(Block output, Object... params)
	{
		addShapelessRecipe(new ItemStack(output), params);
	}

	private static void addRecipe(IRecipe recipe)
	{
		recipes.add(recipe);
		GameRegistry.addRecipe(recipe);
	}
}
