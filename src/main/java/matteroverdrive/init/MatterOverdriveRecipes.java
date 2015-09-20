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
import cpw.mods.fml.common.registry.GameRegistry;
import matteroverdrive.items.weapon.EnergyPackRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
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
        addShapedRecipe(new ItemStack(MatterOverdriveBlocks.decomposer), "TCT", "S S", "NTM", 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'M', MatterOverdriveItems.me_conversion_matrix, 'N', MatterOverdriveItems.integration_matrix, 'S', Blocks.sticky_piston, 'T', MatterOverdriveItems.tritanium_plate);
        addShapedRecipe(new ItemStack(MatterOverdriveBlocks.replicator), "PCF", "IHI", "NTM", 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'M', MatterOverdriveItems.me_conversion_matrix, 'H', MatterOverdriveItems.h_compensator, 'I', Items.iron_ingot, 'N', MatterOverdriveItems.integration_matrix, 'T', MatterOverdriveItems.tritanium_plate, 'F', MatterOverdriveItems.networkFlashDrive, 'P', MatterOverdriveItems.pattern_drive);
        addShapedRecipe(new ItemStack(MatterOverdriveBlocks.network_router), "IGI", "DFC", "OMO", 'M', MatterOverdriveItems.machine_casing, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0), 'I', Items.iron_ingot, 'G', Blocks.glass, 'D', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 'F', MatterOverdriveItems.networkFlashDrive);
        addShapedRecipe(new ItemStack(MatterOverdriveBlocks.network_switch), " G ", "CFC", "OMO", 'M', MatterOverdriveItems.machine_casing, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0), 'G', Blocks.glass, 'F', MatterOverdriveItems.networkFlashDrive);
        addShapedRecipe(new ItemStack(MatterOverdriveBlocks.matter_pipe, 8), " G ", "IMI", " G ", 'M', MatterOverdriveItems.s_magnet, 'G', Blocks.glass, 'I', Items.iron_ingot);
        addShapedRecipe(new ItemStack(MatterOverdriveBlocks.network_pipe, 16), "IGI", "BCB", "IGI", 'M', MatterOverdriveItems.s_magnet, 'G', Blocks.glass, 'I', Items.iron_ingot, 'B', Items.gold_ingot, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0));
        addShapedRecipe(new ItemStack(MatterOverdriveBlocks.matter_analyzer), " C ", "PMF", "ONO", 'O', Blocks.iron_block, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'M', MatterOverdriveItems.me_conversion_matrix, 'N', MatterOverdriveItems.integration_matrix, 'P', MatterOverdriveItems.pattern_drive, 'F', MatterOverdriveItems.networkFlashDrive);
        addRecipe(new ShapedOreRecipe(new ItemStack(MatterOverdriveBlocks.tritanium_block), "TTT", "TTT", "TTT", 'T', "tritaniumIngot"));
        addShapedRecipe(new ItemStack(MatterOverdriveBlocks.machine_hull), " T ", "T T", " T ", 'T', MatterOverdriveItems.tritanium_plate);
        addShapedRecipe(new ItemStack(MatterOverdriveBlocks.solar_panel), "CGC", "GQG", "KMK", 'C', Items.coal, 'Q', Items.quartz, 'K', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 'M', MatterOverdriveItems.machine_casing, 'G', Blocks.glass);
        addShapedRecipe(new ItemStack(MatterOverdriveBlocks.weapon_station), "   ", "GFR", "CMB", 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'B', MatterOverdriveItems.battery, 'G', Items.glowstone_dust, 'R', Items.redstone, 'M', MatterOverdriveItems.machine_casing, 'F', MatterOverdriveItems.forceFieldEmitter);
        addShapedRecipe(new ItemStack(MatterOverdriveBlocks.pattern_storage), "B3B", "TCT", "2M1", 'B', new ItemStack(Blocks.wool, 1, 15), '1', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0), '2', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), '3', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'C', Blocks.chest, 'M', MatterOverdriveItems.machine_casing, 'T', MatterOverdriveItems.tritanium_ingot);
        addShapedRecipe(new ItemStack(MatterOverdriveBlocks.pattern_monitor), " H ", "1N1", " F ", '1', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 'H', MatterOverdriveBlocks.holoSign, 'N', MatterOverdriveBlocks.network_switch, 'F', MatterOverdriveItems.networkFlashDrive);
        addShapedRecipe(new ItemStack(MatterOverdriveBlocks.transporter), "TGT", "CMC", "NBH", 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 'M', MatterOverdriveItems.me_conversion_matrix, 'H', MatterOverdriveItems.h_compensator, 'E', Items.ender_pearl, 'N', MatterOverdriveItems.integration_matrix, 'T', MatterOverdriveItems.tritanium_plate, 'G', Blocks.glowstone, 'B', MatterOverdriveItems.hc_battery);
        addShapedRecipe(new ItemStack(MatterOverdriveBlocks.fusion_reactor_coil), "TMT", "M M", "CMC", 'M', MatterOverdriveItems.s_magnet, 'T', MatterOverdriveItems.tritanium_plate, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0));
        addShapedRecipe(new ItemStack(MatterOverdriveBlocks.recycler), "T T", "1P2", "NTM", '2', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), '1', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0), 'M', MatterOverdriveItems.me_conversion_matrix, 'N', MatterOverdriveItems.integration_matrix, 'T', MatterOverdriveItems.tritanium_plate, 'P', Blocks.piston);
        addShapedRecipe(new ItemStack(MatterOverdriveBlocks.gravitational_stabilizer), " H ", "TST", "CMC", 'M', MatterOverdriveItems.machine_casing, 'S', MatterOverdriveItems.spacetime_equalizer, 'T', MatterOverdriveItems.tritanium_plate, 'C', MatterOverdriveItems.s_magnet, 'H', MatterOverdriveBlocks.holoSign);
        addShapedRecipe(new ItemStack(MatterOverdriveBlocks.fusion_reactor_controller), "CHC", "2M3", "CTC", 'C', MatterOverdriveBlocks.fusion_reactor_coil, '2', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), '3', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'M', MatterOverdriveItems.machine_casing, 'T', MatterOverdriveItems.tritanium_plate, 'H', MatterOverdriveBlocks.holoSign);
        addShapedRecipe(new ItemStack(MatterOverdriveBlocks.androidStation), "THA", "2F3", "GMR", '3', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), '2', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 'F', MatterOverdriveItems.forceFieldEmitter, 'G', Items.glowstone_dust, 'R', Items.redstone, 'M', MatterOverdriveItems.machine_casing, 'H', new ItemStack(MatterOverdriveItems.androidParts, 1, 0), 'T', new ItemStack(MatterOverdriveItems.androidParts, 1, 3), 'A', new ItemStack(MatterOverdriveItems.androidParts, 1, 1));
        addShapedRecipe(new ItemStack(MatterOverdriveBlocks.starMap), " S ", "CFC", "GMR", 'S', MatterOverdriveItems.security_protocol, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'M', MatterOverdriveItems.machine_casing, 'F', MatterOverdriveItems.forceFieldEmitter, 'G', Items.glowstone_dust, 'R', Items.redstone);
        addShapedRecipe(new ItemStack(MatterOverdriveBlocks.chargingStation), " F ", "EDR", "BMB", 'M', MatterOverdriveItems.machine_casing, 'B', MatterOverdriveItems.hc_battery, 'E', Items.ender_eye, 'R', Items.repeater, 'F', MatterOverdriveItems.forceFieldEmitter, 'D', MatterOverdriveItems.dilithium_ctystal);
        addShapedRecipe(new ItemStack(MatterOverdriveBlocks.heavy_matter_pipe, 8), "RMR", "TMT", "RMR", 'M', MatterOverdriveItems.s_magnet, 'G', Blocks.glass, 'T', MatterOverdriveItems.tritanium_plate, 'R', Items.redstone);
        addRecipe(new ShapedOreRecipe(new ItemStack(MatterOverdriveBlocks.holoSign), "GGG", "g0g", " T ", 'G', "glass", 'g', Items.glowstone_dust, '0', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0), 'T', MatterOverdriveItems.tritanium_plate));
        addShapedRecipe(new ItemStack(MatterOverdriveBlocks.forceGlass, 4), " G ", "GTG", " G ", 'G', Blocks.glass, 'T', MatterOverdriveItems.tritanium_plate);
    }

    public static void registerItemRecipes(FMLInitializationEvent event)
    {
        addShapedRecipe(new ItemStack(MatterOverdriveItems.battery), " R ", "TGT", "TDT", 'T', MatterOverdriveItems.tritanium_ingot, 'D', MatterOverdriveItems.dilithium_ctystal, 'R', Items.redstone, 'G', Items.gold_ingot);
        addShapedRecipe(new ItemStack(MatterOverdriveItems.hc_battery), " P ", "DBD", " P ", 'B', MatterOverdriveItems.battery, 'D', MatterOverdriveItems.dilithium_ctystal, 'P', MatterOverdriveItems.tritanium_plate);
        addShapedRecipe(new ItemStack(MatterOverdriveItems.matter_scanner), "III", "GDG", "IRI", 'I', Items.iron_ingot, 'D', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'R', Items.redstone, 'G', Items.gold_ingot);
        addShapedRecipe(new ItemStack(MatterOverdriveItems.h_compensator), " M ", "CPC", "DED", 'D', MatterOverdriveItems.dilithium_ctystal, 'M', MatterOverdriveItems.machine_casing, 'I', Items.iron_ingot, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0), 'P', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 'E', Items.ender_eye);
        addShapedRecipe(new ItemStack(MatterOverdriveItems.integration_matrix), " M ", "GPG", "DED", 'G', Blocks.glass, 'M', MatterOverdriveItems.machine_casing, 'I', Items.iron_ingot, 'P', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 'E', Items.ender_pearl, 'D', MatterOverdriveItems.dilithium_ctystal);
        addShapedRecipe(new ItemStack(MatterOverdriveItems.machine_casing), " T ", "I I", "GRG", 'G', Items.gold_ingot, 'T', MatterOverdriveItems.tritanium_plate, 'I', MatterOverdriveItems.tritanium_ingot, 'R', Items.redstone);
        addShapedRecipe(new ItemStack(MatterOverdriveItems.s_magnet), "RRR", "ETE", "RRR", 'E', Items.ender_pearl, 'T', MatterOverdriveItems.tritanium_ingot, 'R', Items.redstone);
        addShapedRecipe(new ItemStack(MatterOverdriveItems.me_conversion_matrix), "EIE", "CDC", "EIE", 'E', Items.ender_pearl, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 'I', Items.iron_ingot, 'D', MatterOverdriveItems.dilithium_ctystal);
        addShapedRecipe(new ItemStack(MatterOverdriveItems.tritanium_plate), "TTT", 'T', new ItemStack(MatterOverdriveItems.tritanium_ingot));
        addShapedRecipe(new ItemStack(MatterOverdriveItems.phaser), "IGI", "IDI", "WCW", 'I', Items.iron_ingot, 'G', Blocks.glass, 'D', MatterOverdriveItems.dilithium_ctystal, 'W', Blocks.wool, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 3));
        addShapedRecipe(new ItemStack(MatterOverdriveItems.pattern_drive), " E ", "RMR", " C ", 'M', MatterOverdriveItems.machine_casing, 'E', Items.ender_pearl, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 'R', Items.redstone);
        addShapedRecipe(new ItemStack(MatterOverdriveItems.security_protocol), "PP", "CP", 'P', Items.paper, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0));
        addShapedRecipe(new ItemStack(MatterOverdriveItems.wrench), "T T", " Y ", " T ", 'T', MatterOverdriveItems.tritanium_ingot, 'Y', new ItemStack(Blocks.wool, 1, 4));
        addShapedRecipe(new ItemStack(MatterOverdriveItems.spacetime_equalizer), " M ", "EHE", " M ", 'M', MatterOverdriveItems.s_magnet, 'E', Items.ender_pearl, 'H', MatterOverdriveItems.h_compensator);
        addShapedRecipe(new ItemStack(MatterOverdriveItems.forceFieldEmitter), "CDC", "CDC", "P1P", 'P', MatterOverdriveItems.tritanium_plate, 'E', Items.ender_pearl, 'D', MatterOverdriveItems.dilithium_ctystal, '1', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1), 'C', MatterOverdriveItems.s_magnet);
        addShapedRecipe(new ItemStack(MatterOverdriveItems.networkFlashDrive), "RCR", 'R', Items.redstone, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0));
        addShapedRecipe(new ItemStack(MatterOverdriveItems.transportFlashDrive), " I ", "ECR", " I", 'I', Items.iron_ingot, 'R', Items.redstone, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0));
        addRecipe(new EnergyPackRecipe(new ItemStack(MatterOverdriveItems.tritanium_plate), new ItemStack(MatterOverdriveItems.battery), new ItemStack(Items.gunpowder)));
        addRecipe(new EnergyPackRecipe(new ItemStack(MatterOverdriveItems.tritanium_plate), new ItemStack(MatterOverdriveItems.hc_battery), new ItemStack(Items.gunpowder)));
        addShapedRecipe(new ItemStack(MatterOverdriveItems.phaserRifle), "III", "GCF", " WB", 'I', Items.iron_ingot, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 3), 'W', Blocks.wool, 'G', Blocks.glass, 'D', MatterOverdriveItems.dilithium_ctystal, 'F', MatterOverdriveItems.forceFieldEmitter, 'B', MatterOverdriveItems.battery);
        addRecipe(new ShapedOreRecipe(new ItemStack(MatterOverdriveItems.matterContainer, 6), "G G", " T ", 'T', MatterOverdriveItems.tritanium_plate, 'G', "glass"));
        addRecipe(new ShapedOreRecipe(new ItemStack(MatterOverdriveItems.tritanium_ingot), "###", "###", "###", '#', "nuggetTritanium"));
        addRecipe(new ShapelessOreRecipe(new ItemStack(MatterOverdriveItems.tritanium_nugget,9),"ingotTritanium"));
        addShapelessRecipe(new ItemStack(MatterOverdriveItems.dataPad),Items.book,new ItemStack(MatterOverdriveItems.isolinear_circuit,0));
    }

    public static void addShapedRecipe(ItemStack output,Object... params)
    {
        recipes.add(GameRegistry.addShapedRecipe(output, params));
    }

    public static void addShapelessRecipe(ItemStack output,Object... items)
    {
        GameRegistry.addShapelessRecipe(output, items);
    }

    public static void addRecipe(IRecipe recipe)
    {
        recipes.add(recipe);
        GameRegistry.addRecipe(recipe);
    }
}
