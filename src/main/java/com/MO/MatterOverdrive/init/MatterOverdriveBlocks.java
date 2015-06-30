package com.MO.MatterOverdrive.init;

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

import cpw.mods.fml.common.registry.GameRegistry;

public class MatterOverdriveBlocks 
{
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
	
	public static void init(FMLPreInitializationEvent event)
	{
		replicator = new BlockReplicator(Material.iron,"replicator");
		decomposer = new BlockDecomposer(Material.iron,"decomposer");
		transporter = new BlockTransporter(Material.iron,"transporter");
		matter_pipe = new BlockMatterPipe(Material.iron,"matter_pipe");
        network_pipe = new BlockNetworkPipe(Material.iron,"network_pipe");
        network_router = new BlockNetworkRouter(Material.iron,"network_router");
        matter_analyzer = new BlockMatterAnalyzer(Material.iron,"matter_analyzer");
        dilithium_ore = new DilithiumOre(Material.rock,"dilithium_ore");
        tritaniumOre = new MOBlock(Material.rock,"tritanium_ore");
        tritaniumOre.setHardness(15.0F);
        tritaniumOre.setResistance(5.0F);
        tritaniumOre.setHarvestLevel("pickaxe", 2);
        tritaniumOre.setStepSound(Block.soundTypePiston);
        tritanium_block = new MOBlock(Material.iron,"tritanium_block");
        tritanium_block.setHardness(15.0F);
        tritanium_block.setResistance(10.0F);
        tritanium_block.setHarvestLevel("pickaxe", 2);
        machine_hull = new MOBlock(Material.iron,"machine_hull");
        machine_hull.setHardness(15.0F);
        machine_hull.setResistance(8.0F);
        machine_hull.setHarvestLevel("pickaxe", 2);
        machine_hull.setBlockTextureName(Reference.MOD_ID + ":" + "base");
        pattern_storage = new BlockPatternStorage(Material.iron,"pattern_storage");
        solar_panel = new BlockSolarPanel(Material.iron,"solar_panel");
        weapon_station = new BlockWeaponStation(Material.iron,"weapon_station");
        microwave = new BlockMicrowave(Material.iron,"microwave");
        pattern_monitor = new BlockPatternMonitor(Material.iron,"pattern_monitor");
        network_switch = new BlockNetworkSwitch(Material.iron,"network_switch");
        gravitational_anomaly = new BlockGravitationalAnomaly(Material.portal,"gravitational_anomaly");
        gravitational_stabilizer = new BlockGravitationalStabilizer(Material.iron,"gravitational_stabilizer");
        fusion_reactor_controller = new BlockFusionReactorController(Material.iron,"fusion_reactor_controller");
        fusion_reactor_coil = new BlockFusionReactorCoil(Material.iron,"fusion_reactor_coil");
        recycler = new BlockMatterRecycler(Material.iron,"matter_recycler");
        androidStation = new BlockAndroidStation(Material.iron,"android_station");
        starMap = new BlockStarMap(Material.iron,"star_map");
	}
	
	public static void register(FMLPreInitializationEvent event)
	{
		replicator.Register();
        MatterOverdrive.configHandler.subscribe(replicator);
		transporter.Register();
        MatterOverdrive.configHandler.subscribe(transporter);
		decomposer.Register();
        MatterOverdrive.configHandler.subscribe(decomposer);
		matter_pipe.Register();
        network_pipe.Register();
        network_router.Register();
        matter_analyzer.Register();
        MatterOverdrive.configHandler.subscribe(matter_analyzer);
        dilithium_ore.Register();
        tritaniumOre.Register();
        tritanium_block.Register();
        machine_hull.Register();
        pattern_storage.Register();
        MatterOverdrive.configHandler.subscribe(pattern_storage);
        solar_panel.Register();
        MatterOverdrive.configHandler.subscribe(solar_panel);
        weapon_station.Register();
        microwave.Register();
        pattern_monitor.Register();
        MatterOverdrive.configHandler.subscribe(pattern_monitor);
        network_switch.Register();
        MatterOverdrive.configHandler.subscribe(network_switch);
        gravitational_anomaly.Register();
        MatterOverdrive.configHandler.subscribe(gravitational_anomaly);
        gravitational_stabilizer.Register();
        MatterOverdrive.configHandler.subscribe(gravitational_stabilizer);
        fusion_reactor_controller.Register();
        MatterOverdrive.configHandler.subscribe(fusion_reactor_controller);
        fusion_reactor_coil.Register();
        recycler.Register();
        MatterOverdrive.configHandler.subscribe(recycler);
        androidStation.Register();
        MatterOverdrive.configHandler.subscribe(androidStation);
        starMap.Register();
        MatterOverdrive.configHandler.subscribe(starMap);

        if (event.getSide() == Side.CLIENT)
        {
            MatterOverdriveQuide.Register(replicator);
            MatterOverdriveQuide.Register(decomposer);
            MatterOverdriveQuide.Register(dilithium_ore);
            MatterOverdriveQuide.Register(tritaniumOre);
        }

        GameRegistry.addRecipe(new ItemStack(decomposer), new Object[]{"TCT", "S S", "NTM", 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'M', MatterOverdriveItems.me_conversion_matrix, 'N', MatterOverdriveItems.integration_matrix, 'S', Blocks.sticky_piston, 'T', MatterOverdriveItems.tritanium_plate});
		GameRegistry.addRecipe(new ItemStack(replicator), new Object[]{"TCT", "IHI", "NTM", 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'M', MatterOverdriveItems.me_conversion_matrix, 'H', MatterOverdriveItems.h_compensator, 'I', Items.iron_ingot, 'N', MatterOverdriveItems.integration_matrix, 'T', MatterOverdriveItems.tritanium_plate});
        GameRegistry.addRecipe(new ItemStack(network_router), new Object[]{"IGI", "CDC", "OMO", 'M', MatterOverdriveItems.machine_casing, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0), 'I', Items.iron_ingot, 'G', Blocks.glass,'D',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,1)});
        GameRegistry.addRecipe(new ItemStack(network_switch),new Object[]{" G ","C C","OMO",'M',MatterOverdriveItems.machine_casing,'C',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,0),'G',Blocks.glass});
        GameRegistry.addRecipe(new ItemStack(matter_pipe, 8), new Object[]{"IGI", "M M", "IGI", 'M', MatterOverdriveItems.s_magnet, 'G', Blocks.glass, 'I', Items.iron_ingot});
        GameRegistry.addRecipe(new ItemStack(network_pipe, 4), new Object[]{"IGI", "BCB", "IGI", 'M', MatterOverdriveItems.s_magnet, 'G', Blocks.glass, 'I', Items.iron_ingot, 'B', Items.gold_ingot, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0)});
        GameRegistry.addRecipe(new ItemStack(matter_analyzer), new Object[]{" C ", "IMI", "ONO", 'O', Blocks.iron_block, 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 2), 'M', MatterOverdriveItems.me_conversion_matrix, 'N', MatterOverdriveItems.integration_matrix, 'I', Items.iron_ingot});
        GameRegistry.addRecipe(new ItemStack(tritanium_block), new Object[]{"TTT", "TTT", "TTT", 'T', MatterOverdriveItems.tritanium_ingot});
        GameRegistry.addRecipe(new ItemStack(machine_hull), new Object[]{" T ", "T T", " T ", 'T', MatterOverdriveItems.tritanium_plate});
        GameRegistry.addRecipe(new ItemStack(solar_panel), new Object[]{"CGC", "GQG", "KMK", 'C', Items.coal, 'Q', Items.quartz, 'K', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 'M', MatterOverdriveItems.machine_casing,'G', Blocks.glass});
        GameRegistry.addRecipe(new ItemStack(weapon_station), new Object[]{"   ","CBC","GMR",'C',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,2),'B',MatterOverdriveItems.battery,'G',Items.glowstone_dust,'R',Items.redstone,'M',MatterOverdriveItems.machine_casing});
        GameRegistry.addRecipe(new ItemStack(pattern_storage),new Object[]{"B3B","TCT","2M1",'B',new ItemStack(Blocks.wool,1,15),'1',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,0),'2',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,1),'3',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,2),'C',Blocks.chest,'M',MatterOverdriveItems.machine_casing,'T',MatterOverdriveItems.tritanium_ingot});
        GameRegistry.addRecipe(new ItemStack(pattern_monitor),new Object[]{"GGG","SCS"," M ",'M',MatterOverdriveItems.machine_casing,'G',Blocks.glass,'S',Items.glowstone_dust,'C',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,2)});
        GameRegistry.addRecipe(new ItemStack(transporter),new Object[]{"TGT", "CHC", "NTM", 'C', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1), 'M', MatterOverdriveItems.me_conversion_matrix, 'H', MatterOverdriveItems.h_compensator, 'E', Items.ender_pearl, 'N', MatterOverdriveItems.integration_matrix, 'T', MatterOverdriveItems.tritanium_plate,'G',Blocks.glowstone});
        GameRegistry.addRecipe(new ItemStack(fusion_reactor_coil),new Object[]{"TMT","M M","CMC",'M',MatterOverdriveItems.s_magnet,'T',MatterOverdriveItems.tritanium_plate,'C',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,0)});
        GameRegistry.addRecipe(new ItemStack(recycler),new Object[]{"T T", "1P2", "NTM", '2', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 1),'1', new ItemStack(MatterOverdriveItems.isolinear_circuit, 1, 0), 'M', MatterOverdriveItems.me_conversion_matrix, 'N', MatterOverdriveItems.integration_matrix, 'T', MatterOverdriveItems.tritanium_plate,'P',Blocks.piston});
        GameRegistry.addRecipe(new ItemStack(gravitational_stabilizer),new Object[]{" T ","TST","CMC",'M',MatterOverdriveItems.machine_casing,'S',MatterOverdriveItems.spacetime_equalizer,'T',MatterOverdriveItems.tritanium_plate,'C',MatterOverdriveItems.s_magnet});
        GameRegistry.addRecipe(new ItemStack(fusion_reactor_controller),new Object[]{"CTC","2M3","CTC",'C',fusion_reactor_coil,'2',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,1),'3',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,2),'M',MatterOverdriveItems.machine_casing,'T',MatterOverdriveItems.tritanium_plate});
        GameRegistry.addRecipe(new ItemStack(androidStation),new Object[]{"THA","2F3","GMR",'3',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,2),'2',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,1),'F',MatterOverdriveItems.forceFieldEmitter,'G',Items.glowstone_dust,'R',Items.redstone,'M',MatterOverdriveItems.machine_casing,'H',new ItemStack(MatterOverdriveItems.androidParts, 1,0),'T',new ItemStack(MatterOverdriveItems.androidParts, 1,3),'A',new ItemStack(MatterOverdriveItems.androidParts, 1,1)});
        GameRegistry.addRecipe(new ItemStack(starMap),new Object[]{" S ","CFC","GMR",'S',MatterOverdriveItems.security_protocol,'C',new ItemStack(MatterOverdriveItems.isolinear_circuit,1,2),'M',MatterOverdriveItems.machine_casing,'F',MatterOverdriveItems.forceFieldEmitter,'G',Items.glowstone_dust,'R',Items.redstone});
    }
}
