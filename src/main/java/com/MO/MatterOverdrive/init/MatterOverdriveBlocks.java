package com.MO.MatterOverdrive.init;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.blocks.*;
import com.MO.MatterOverdrive.blocks.includes.MOBlock;
import com.MO.MatterOverdrive.blocks.world.DilithiumOre;
import com.MO.MatterOverdrive.guide.MatterOverdriveQuide;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.MO.MatterOverdrive.blocks.includes.MOBlockContainer;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.util.ResourceLocation;

public class MatterOverdriveBlocks 
{
	public static ReplicatorBlock replicator;
	public static DecomposerBlock decomposer;
	public static MOBlockContainer transporter;
	public static BlockMatterPipe matter_pipe;
    public static BlockNetworkPipe network_pipe;
    public static BlockNetworkController network_controller;
    public static BlockMatterAnalyzer matter_analyzer;
    public static DilithiumOre dilithiumOre;
    public static MOBlock tritaniumOre;
    public static MOBlock tritanium_block;
    public static MOBlock machine_hull;
    public static BlockPatternStorage pattern_storage;
    public static BlockSolarPanel solar_panel;
	
	public static void init()
	{
		replicator = new ReplicatorBlock(Material.glass,"replicator");
        replicator.setDetails("Replicates Items and Blocks from Matter Plasma.");
		decomposer = new DecomposerBlock(Material.iron,"decomposer");
        decomposer.setDetails("Decomposes Items into Matter Plasma.");
		transporter = new TransporterBlock(Material.iron,"transporter");
		matter_pipe = new BlockMatterPipe(Material.iron,"matter_pipe");
        matter_pipe.setDetails("Transports Matter Plasma.");
        network_pipe = new BlockNetworkPipe(Material.iron,"network_pipe");
        network_pipe.setDetails("Connects Devices to a Matter Network.");
        network_controller = new BlockNetworkController(Material.iron,"network_controller");
        network_controller.setDetails("Hearth of the Matter Network.");
        matter_analyzer = new BlockMatterAnalyzer(Material.iron,"matter_analyzer");
        matter_analyzer.setDetails("Analyzes Items Patterns.");
        dilithiumOre = new DilithiumOre(Material.rock,"dilithium_ore");
        tritaniumOre = new MOBlock(Material.rock,"tritanium_ore");
        tritaniumOre.setHardness(3.0F);
        tritaniumOre.setResistance(5.0F);
        tritaniumOre.setHarvestLevel("pickaxe", 2);
        tritaniumOre.setStepSound(Block.soundTypePiston);
        tritanium_block = new MOBlock(Material.iron,"tritanium_block");
        tritanium_block.setHardness(5.0F);
        tritanium_block.setResistance(10.0F);
        tritanium_block.setHarvestLevel("pickaxe", 2);
        machine_hull = new MOBlock(Material.iron,"machine_hull");
        machine_hull.setHardness(3.0F);
        machine_hull.setResistance(8.0F);
        machine_hull.setHarvestLevel("pickaxe", 2);
        machine_hull.setBlockTextureName(Reference.MOD_ID + ":" + "base");
        pattern_storage = new BlockPatternStorage(Material.iron,"pattern_storage");
        pattern_storage.setDetails("Stores item patterns, for replication.");
        solar_panel = new BlockSolarPanel(Material.iron,"solar_panel");
        solar_panel.setDetails("Produces RF from Sunlight.");
	}
	
	public static void register()
	{
		replicator.Register();
        MatterOverdriveQuide.Register(replicator);
		transporter.Register();
		decomposer.Register();
        MatterOverdriveQuide.Register(decomposer);
		matter_pipe.Register();
        network_pipe.Register();
        network_controller.Register();
        matter_analyzer.Register();
        dilithiumOre.Register();
        MatterOverdriveQuide.Register(dilithiumOre);
        tritaniumOre.Register();
        MatterOverdriveQuide.Register(tritaniumOre);
        tritanium_block.Register();
        machine_hull.Register();
        pattern_storage.Register();
        solar_panel.Register();

        GameRegistry.addRecipe(new ItemStack(decomposer), new Object[]{"TCT", "I I", "NTM", 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'M', MatterOverdriveItems.me_conversion_matrix, 'N', MatterOverdriveItems.integration_matrix, 'I', Items.iron_ingot,'T',MatterOverdriveItems.tritanium_plate});
		GameRegistry.addRecipe(new ItemStack(replicator), new Object[]{"TCT","IHI","NTM",'C',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,2),'M',MatterOverdriveItems.me_conversion_matrix,'H',MatterOverdriveItems.h_compensator,'I',Items.iron_ingot,'N',MatterOverdriveItems.integration_matrix,'T',MatterOverdriveItems.tritanium_plate});
        GameRegistry.addRecipe(new ItemStack(network_controller), new Object[]{"IGI","CDC","OMO",'M',MatterOverdriveItems.machine_casing,'C',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,0),'D',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,2),'I',Items.iron_ingot,'G',Blocks.glass});
        GameRegistry.addRecipe(new ItemStack(matter_pipe,8), new Object[]{"IGI","M M","IGI",'M',MatterOverdriveItems.s_magnet,'G',Blocks.glass,'I',Items.iron_ingot});
        GameRegistry.addRecipe(new ItemStack(network_pipe,4), new Object[]{"IGI","BCB","IGI",'M',MatterOverdriveItems.s_magnet,'G',Blocks.glass,'I',Items.iron_ingot,'B',Items.gold_ingot,'C',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,0)});
        GameRegistry.addRecipe(new ItemStack(matter_analyzer),new Object[]{" C ","IMI","ONO",'O',Blocks.iron_block,'C',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,2),'M',MatterOverdriveItems.me_conversion_matrix,'N',MatterOverdriveItems.integration_matrix,'I',Items.iron_ingot});
        GameRegistry.addRecipe(new ItemStack(tritanium_block),new Object[]{"TTT","TTT","TTT",'T',MatterOverdriveItems.tritanium_ingot});
        GameRegistry.addRecipe(new ItemStack(machine_hull),new Object[]{" T ","T T"," T ",'T',MatterOverdriveItems.tritanium_plate});
    }
}
