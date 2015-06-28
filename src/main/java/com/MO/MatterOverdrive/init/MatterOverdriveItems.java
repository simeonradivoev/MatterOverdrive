package com.MO.MatterOverdrive.init;

import cofh.api.modhelpers.ThermalExpansionHelper;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.handler.MatterRegistry;
import com.MO.MatterOverdrive.items.*;
import com.MO.MatterOverdrive.items.starmap.ItemScoutShip;
import com.MO.MatterOverdrive.items.starmap.ShipFactory;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.items.includes.MOBaseItem;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

public class MatterOverdriveItems 
{
	public static MOBaseItem matter_scanner;
	public static ItemFood emergency_ration;
    public static EarlGrayTea earl_gray_tea;
	public static MOBaseItem me_conversion_matrix;
	public static Phaser phaser;
	public static MOBaseItem battery;
    public static MOBaseItem hc_battery;
	public static MOBaseItem creative_battery;
	public static MatterDust matter_dust;
	public static MatterDust matter_dust_refined;
    public static Isolinear_circuit isolinear_circuit;
    public static MOBaseItem h_compensator;
    public static MOBaseItem integration_matrix;
    public static MOBaseItem machine_casing;
    public static MOBaseItem s_magnet;
    public static MOBaseItem dilithium_ctystal;
    public static MOBaseItem tritanium_ingot;
    public static MOBaseItem tritanium_dust;
    public static MOBaseItem tritanium_plate;
    public static PatternDrive pattern_drive;
    public static ItemUpgrade item_upgrade;
    public static WeaponColorModule weapon_module_color;
    public static WeaponModuleBarrel weapon_module_barrel;
    public static SecurityProtocol security_protocol;
    public static SpacetimeEqualizer spacetime_equalizer;
    public static Wrench wrench;
    public static RougeAndroidParts androidParts;
    public static MOBaseItem forceFieldEmitter;
    public static ShipFactory shipFactory;
    public static ItemScoutShip scoutShip;
	
	public static void init(FMLPreInitializationEvent event)
	{
		matter_dust = new MatterDust("matter_dust",false);
		matter_dust_refined = new MatterDust("matter_dust_refined",true);
		matter_scanner = new MatterScanner("matter_scanner");
		battery = new Battery("battery", 1 << 19,Reference.COLOR_MATTER,400,800);
		creative_battery = new CreativeBattery("creative_battery",1 << 24,Reference.COLOR_HOLO_RED,8192,8192);
        hc_battery = new Battery("hc_battery",1 << 20,Reference.COLOR_YELLOW_STRIPES,4096,4096);
		phaser = new Phaser("phaser");
		emergency_ration = new ItemFood(8,0.8F,false);
		emergency_ration.setUnlocalizedName("emergency_ration").setCreativeTab(MatterOverdrive.tabMatterOverdrive).setTextureName(Reference.MOD_ID + ":" + "emergency_ration");
        earl_gray_tea = new EarlGrayTea("earl_gray_tea");
		me_conversion_matrix = new MOBaseItem("me_conversion_matrix");
        isolinear_circuit = new Isolinear_circuit("isolinear_circuit");
        item_upgrade = new ItemUpgrade("upgrade");
        h_compensator = new MOBaseItem("h_compensator");
        integration_matrix = new MOBaseItem("integration_matrix");
        machine_casing = new MOBaseItem("machine_casing");
        s_magnet = new MOBaseItem("s_magnet");
        dilithium_ctystal = new MOBaseItem("dilithium_crystal");
        tritanium_ingot = new MOBaseItem("tritanium_ingot");
        tritanium_dust = new MOBaseItem("tritanium_dust");
        tritanium_plate = new MOBaseItem("tritanium_plate");
        pattern_drive = new PatternDrive("pattern_drive",2);
        weapon_module_color = new WeaponColorModule("weapon_module_color");
        weapon_module_barrel = new WeaponModuleBarrel("weapon_module_barrel");
        security_protocol = new SecurityProtocol("security_protocol");
        spacetime_equalizer = new SpacetimeEqualizer("spacetime_equalizer");
        wrench = new Wrench("tritanium_wrench");
        androidParts = new RougeAndroidParts("rouge_android_part");
        forceFieldEmitter = new MOBaseItem("forcefield_emitter");
        shipFactory = new ShipFactory("ship_factory");
        scoutShip = new ItemScoutShip("scout_ship");
	}
	
	public static void register(FMLPreInitializationEvent event)
	{
		GameRegistry.registerItem(emergency_ration, emergency_ration.getUnlocalizedName().substring(5));

		matter_dust_refined.Register();
		matter_dust.Register();
		creative_battery.Register();
        me_conversion_matrix.Register();
		matter_scanner.Register();
		phaser.Register();
		battery.Register();
        isolinear_circuit.Register();
        item_upgrade.Register();
        h_compensator.Register();
        integration_matrix.Register();
        machine_casing.Register();
        s_magnet.Register();
        dilithium_ctystal.Register();
        MatterRegistry.addToBlacklist(dilithium_ctystal);
        tritanium_ingot.Register();
        MatterRegistry.addToBlacklist(tritanium_ingot);
        tritanium_dust.Register();
        MatterRegistry.addToBlacklist(tritanium_dust);
        tritanium_plate.Register();
        MatterRegistry.addToBlacklist(tritanium_plate);
        pattern_drive.Register();
        weapon_module_color.Register();
        weapon_module_barrel.Register();
        earl_gray_tea.Register();
        security_protocol.Register();
        spacetime_equalizer.Register();
        wrench.Register();
        androidParts.Register();
        forceFieldEmitter.Register();
        hc_battery.Register();
        shipFactory.Register();
        scoutShip.Register();

        GameRegistry.addSmelting(new ItemStack(tritanium_dust),new ItemStack(tritanium_ingot),5);
        GameRegistry.addSmelting(new ItemStack(MatterOverdriveBlocks.tritaniumOre),new ItemStack(tritanium_ingot),10);

		GameRegistry.addRecipe(new ItemStack(battery), new Object[]{" R ", "TGT", "TDT", 'T', tritanium_ingot, 'D', MatterOverdriveItems.dilithium_ctystal, 'R', Items.redstone, 'G', Items.gold_ingot});
        GameRegistry.addRecipe(new ItemStack(hc_battery),new Object[]{" P ","DBD"," P ",'B',battery,'D',dilithium_ctystal,'P',tritanium_plate});
		GameRegistry.addRecipe(new ItemStack(matter_scanner), new Object[]{"III","GDG","IRI", 'I',Items.iron_ingot, 'D',new ItemStack(isolinear_circuit,1,2),'R',Items.redstone,'G',Items.gold_ingot});
        GameRegistry.addRecipe(new ItemStack(h_compensator),new Object[]{" M ","CPC","DED",'D', MatterOverdriveItems.dilithium_ctystal,'M',machine_casing,'I',Items.iron_ingot,'C',new ItemStack(isolinear_circuit,1,0),'P',new ItemStack(isolinear_circuit,1,1),'E',Items.ender_eye});
        GameRegistry.addRecipe(new ItemStack(integration_matrix),new Object[]{" M ","GPG","DED",'G', Blocks.glass,'M',machine_casing,'I',Items.iron_ingot,'P',new ItemStack(isolinear_circuit,1,1),'E',Items.ender_pearl,'D',MatterOverdriveItems.dilithium_ctystal});
        GameRegistry.addRecipe(new ItemStack(machine_casing),new Object[]{" T ","I I","GRG",'G', Items.gold_ingot,'T',tritanium_plate,'I',tritanium_ingot,'R',Items.redstone});
        GameRegistry.addRecipe(new ItemStack(s_magnet),new Object[]{"RRR","ETE","RRR",'E',Items.ender_pearl,'T',tritanium_ingot,'R',Items.redstone});
        GameRegistry.addRecipe(new ItemStack(me_conversion_matrix),new Object[]{"EIE","CDC","EIE",'E',Items.ender_pearl,'C',new ItemStack(isolinear_circuit,1,1),'I',Items.iron_ingot,'D',MatterOverdriveItems.dilithium_ctystal});
        GameRegistry.addRecipe(new ItemStack(tritanium_plate),new Object[]{"TTT",'T',new ItemStack(tritanium_ingot)});
        GameRegistry.addRecipe(new ItemStack(phaser),new Object[]{"IGI","IDI","WCW",'I',Items.iron_ingot,'G',Blocks.glass,'D',dilithium_ctystal,'W',Blocks.wool,'C',new ItemStack(isolinear_circuit,1,3)});
        GameRegistry.addRecipe(new ItemStack(pattern_drive),new Object[]{" M ", "RER"," C ",'M',machine_casing,'E',Items.ender_pearl,'C',new ItemStack(isolinear_circuit,1,1),'R',Items.redstone});
        GameRegistry.addRecipe(new ItemStack(security_protocol),new Object[]{"PP", "CP",'P',Items.paper,'C',new ItemStack(isolinear_circuit,1,0)});
        GameRegistry.addRecipe(new ItemStack(wrench),new Object[]{"T T"," Y "," T ",'T',tritanium_ingot,'Y',new ItemStack(Blocks.wool,1,4)});
        GameRegistry.addRecipe(new ItemStack(spacetime_equalizer),new Object[]{" M ","EHE", " M ",'M',s_magnet,'E',Items.ender_pearl,'H',h_compensator});
        GameRegistry.addRecipe(new ItemStack(forceFieldEmitter),new Object[]{"CDC","CDC","PCP",'P',tritanium_plate,'E',Items.ender_pearl,'D',dilithium_ctystal,'2',new ItemStack(isolinear_circuit,1,1),'C',s_magnet});

        MatterRegistry.register(emergency_ration,3);
        MatterRegistry.register(earl_gray_tea,2);
	}

    public static void addToDungons()
    {
        weapon_module_color.addToDunguns();
        addToDungons(emergency_ration, 1, 8, 6);
        addToDungons(earl_gray_tea,1,2,2);
    }

    private static void addToDungons(Item item,int min,int max,int chance)
    {
        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(item),min,max,chance));
        ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(item),min,max,chance));
        ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(item),min,max,chance));
        ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(item),min,max,chance));
        ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(item),min,max,chance));
    }
}
