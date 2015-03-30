package com.MO.MatterOverdrive.init;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.blocks.*;
import com.MO.MatterOverdrive.blocks.includes.MOBlock;
import com.MO.MatterOverdrive.blocks.world.DilithiumOre;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.MO.MatterOverdrive.blocks.includes.MOBlockContainer;

import cpw.mods.fml.common.registry.GameRegistry;

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
	
	public static void init()
	{
		replicator = new ReplicatorBlock(Material.glass,"replicator");
		decomposer = new DecomposerBlock(Material.iron,"decomposer");
		transporter = new TransporterBlock(Material.iron,"transporter");
		matter_pipe = new BlockMatterPipe(Material.iron,"matter_pipe");
        network_pipe = new BlockNetworkPipe(Material.iron,"network_pipe");
        network_controller = new BlockNetworkController(Material.iron,"network_controller");
        matter_analyzer = new BlockMatterAnalyzer(Material.iron,"matter_analyzer");
        dilithiumOre = new DilithiumOre(Material.rock,"dilithium_ore");
        tritaniumOre = new MOBlock(Material.rock,"tritanium_ore");
        tritaniumOre.setHardness(3.0F);
        tritaniumOre.setResistance(5.0F);
        tritaniumOre.setHarvestLevel("pickaxe", 2);
        tritaniumOre.setStepSound(Block.soundTypePiston);
        tritanium_block = new MOBlock(Material.iron,"tritanium_block");
        machine_hull = new MOBlock(Material.iron,"machine_hull");
        machine_hull.setBlockTextureName(Reference.MOD_ID + ":" + "base");
        pattern_storage = new BlockPatternStorage(Material.iron,"pattern_storage");
	}
	
	public static void register()
	{
		replicator.Register();
		transporter.Register();
		decomposer.Register();
		matter_pipe.Register();
        network_pipe.Register();
        network_controller.Register();
        matter_analyzer.Register();
        dilithiumOre.Register();
        tritaniumOre.Register();
        tritanium_block.Register();
        machine_hull.Register();
        pattern_storage.Register();

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
