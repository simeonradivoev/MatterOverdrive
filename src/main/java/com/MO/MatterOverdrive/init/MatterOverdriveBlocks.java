package com.MO.MatterOverdrive.init;

import cofh.lib.util.OreDictionaryProxy;
import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.blocks.*;
import com.MO.MatterOverdrive.blocks.includes.MOBlock;
import com.MO.MatterOverdrive.blocks.world.DilithiumOre;
import com.MO.MatterOverdrive.guide.MatterOverdriveQuide;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.MO.MatterOverdrive.blocks.includes.MOBlockContainer;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class MatterOverdriveBlocks 
{
	public static ReplicatorBlock replicator;
	public static DecomposerBlock decomposer;
	public static TransporterBlock transporter;
	public static BlockMatterPipe matter_pipe;
    public static BlockNetworkPipe network_pipe;
    public static BlockNetworkRouter network_router;
    public static BlockMatterAnalyzer matter_analyzer;
    public static DilithiumOre dilithiumOre;
    public static MOBlock tritaniumOre;
    public static MOBlock tritanium_block;
    public static MOBlock machine_hull;
    public static BlockPatternStorage pattern_storage;
    public static BlockSolarPanel solar_panel;
    public static BlockWeaponStation weapon_station;
    public static BlockMicrowave microwave;
    public static BlockPatternMonitor pattern_monitor;
    public static BlockNetworkSwitch network_switch;
	
	public static void init(FMLPreInitializationEvent event)
	{
		replicator = new ReplicatorBlock(Material.glass,"replicator");
		decomposer = new DecomposerBlock(Material.iron,"decomposer");
		transporter = new TransporterBlock(Material.iron,"transporter");
		matter_pipe = new BlockMatterPipe(Material.iron,"matter_pipe");
        network_pipe = new BlockNetworkPipe(Material.iron,"network_pipe");
        network_router = new BlockNetworkRouter(Material.iron,"network_router");
        matter_analyzer = new BlockMatterAnalyzer(Material.iron,"matter_analyzer");
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
        solar_panel = new BlockSolarPanel(Material.iron,"solar_panel");
        weapon_station = new BlockWeaponStation(Material.iron,"weapon_station");
        microwave = new BlockMicrowave(Material.iron,"microwave");
        pattern_monitor = new BlockPatternMonitor(Material.iron,"pattern_monitor");
        network_switch = new BlockNetworkSwitch(Material.iron,"network_switch");
	}
	
	public static void register(FMLPreInitializationEvent event)
	{
        MatterOverdrive.configHandler.load();
		replicator.Register();
        replicator.loadConfigs(MatterOverdrive.configHandler);
		transporter.Register();
        transporter.loadConfigs(MatterOverdrive.configHandler);
		decomposer.Register();
        decomposer.loadConfigs(MatterOverdrive.configHandler);
		matter_pipe.Register();
        network_pipe.Register();
        network_router.Register();
        matter_analyzer.Register();
        matter_analyzer.loadConfigs(MatterOverdrive.configHandler);
        dilithiumOre.Register();
        tritaniumOre.Register();
        tritanium_block.Register();
        machine_hull.Register();
        pattern_storage.Register();
        pattern_storage.loadConfigs(MatterOverdrive.configHandler);
        solar_panel.Register();
        solar_panel.loadConfigs(MatterOverdrive.configHandler);
        weapon_station.Register();
        microwave.Register();
        pattern_monitor.Register();
        pattern_monitor.loadConfigs(MatterOverdrive.configHandler);
        network_switch.Register();
        network_switch.loadConfigs(MatterOverdrive.configHandler);
        MatterOverdrive.configHandler.save();

        if (event.getSide() == Side.CLIENT)
        {
            MatterOverdriveQuide.Register(replicator);
            MatterOverdriveQuide.Register(decomposer);
            MatterOverdriveQuide.Register(dilithiumOre);
            MatterOverdriveQuide.Register(tritaniumOre);
        }

        GameRegistry.addRecipe(new ItemStack(decomposer), new Object[]{"TCT", "I I", "NTM", 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'M', MatterOverdriveItems.me_conversion_matrix, 'N', MatterOverdriveItems.integration_matrix, 'I', Items.iron_ingot, 'T', MatterOverdriveItems.tritanium_plate});
		GameRegistry.addRecipe(new ItemStack(replicator), new Object[]{"TCT", "IHI", "NTM", 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'M', MatterOverdriveItems.me_conversion_matrix, 'H', MatterOverdriveItems.h_compensator, 'I', Items.iron_ingot, 'N', MatterOverdriveItems.integration_matrix, 'T', MatterOverdriveItems.tritanium_plate});
        GameRegistry.addRecipe(new ItemStack(network_router), new Object[]{"IGI", "C C", "OMO", 'M', MatterOverdriveItems.machine_casing, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0), 'I', Items.iron_ingot, 'G', Blocks.glass});
        GameRegistry.addRecipe(new ItemStack(network_switch),new Object[]{" G ","CDC","OMO",'M',MatterOverdriveItems.machine_casing,'C',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,0),'D',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,1),'G',Blocks.glass});
        GameRegistry.addRecipe(new ItemStack(matter_pipe, 8), new Object[]{"IGI", "M M", "IGI", 'M', MatterOverdriveItems.s_magnet, 'G', Blocks.glass, 'I', Items.iron_ingot});
        GameRegistry.addRecipe(new ItemStack(network_pipe, 4), new Object[]{"IGI", "BCB", "IGI", 'M', MatterOverdriveItems.s_magnet, 'G', Blocks.glass, 'I', Items.iron_ingot, 'B', Items.gold_ingot, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0)});
        GameRegistry.addRecipe(new ItemStack(matter_analyzer), new Object[]{" C ", "IMI", "ONO", 'O', Blocks.iron_block, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'M', MatterOverdriveItems.me_conversion_matrix, 'N', MatterOverdriveItems.integration_matrix, 'I', Items.iron_ingot});
        GameRegistry.addRecipe(new ItemStack(tritanium_block), new Object[]{"TTT", "TTT", "TTT", 'T', MatterOverdriveItems.tritanium_ingot});
        GameRegistry.addRecipe(new ItemStack(machine_hull), new Object[]{" T ", "T T", " T ", 'T', MatterOverdriveItems.tritanium_plate});
        GameRegistry.addRecipe(new ItemStack(solar_panel), new Object[]{"CGC", "GQG", "KMK", 'C', Items.coal, 'Q', Items.quartz, 'K', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 'M', MatterOverdriveItems.machine_casing,'G', Blocks.glass});
        GameRegistry.addRecipe(new ItemStack(weapon_station), new Object[]{"   ","CBC","GMR",'C',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,2),'B',MatterOverdriveItems.battery,'G',Items.glowstone_dust,'R',Items.redstone,'M',MatterOverdriveItems.machine_casing});
        GameRegistry.addRecipe(new ItemStack(pattern_storage),new Object[]{"B4B","T3T","2M1",'B',new ItemStack(Blocks.wool,1,15),'1',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,0),'2',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,1),'3',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,2),'4',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,3),'M',MatterOverdriveItems.machine_casing,'T',MatterOverdriveItems.tritanium_ingot});
        GameRegistry.addRecipe(new ItemStack(pattern_monitor),new Object[]{"GGG","SCS"," M ",'M',MatterOverdriveItems.machine_casing,'G',Blocks.glass,'S',Items.glowstone_dust,'C',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,2)});
    }
}
