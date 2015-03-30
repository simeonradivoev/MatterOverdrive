package com.MO.MatterOverdrive.init;

import com.MO.MatterOverdrive.handler.MatterRegistry;
import com.MO.MatterOverdrive.items.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.items.includes.MOBaseItem;

import cpw.mods.fml.common.registry.GameRegistry;

public class MatterOverdriveItems 
{
	public static MOBaseItem matter_scanner;
	public static ItemFood emergency_ration;
	public static MOBaseItem me_conversion_matrix;
	public static Phaser phaser;
	public static MOBaseItem battery;
	public static MOBaseItem creative_battery;
	public static MatterDust matter_dust;
	public static MatterDust matter_dust_refined;
    public static Isolinear_circuit isolinear_circuit;
    public static MOBaseItem base_upgrade;
    public static MOBaseItem h_compensator;
    public static MOBaseItem integration_matrix;
    public static MOBaseItem machine_casing;
    public static MOBaseItem s_magnet;
    public static MOBaseItem dilithium_ctystal;
    public static MOBaseItem tritanium_ingot;
    public static MOBaseItem tritanium_dust;
    public static MOBaseItem tritanium_plate;
    public static PatternDrive pattern_drive;
	
	public static void init()
	{
		matter_dust = new MatterDust("matter_dust");
		matter_dust_refined = new MatterDust("matter_dust_refined");
		matter_scanner = new MatterScanner("matter_scanner");
		battery = new Battery("battery",131072);
		creative_battery = new CreativeBattery("creative_battery",1048576);
		phaser = new Phaser("phaser");
		emergency_ration = new ItemFood(8,0.8F,false);
		emergency_ration.setUnlocalizedName("emergency_ration").setCreativeTab(MatterOverdrive.tabMatterOverdrive).setTextureName("mo:emergency_ration");
		me_conversion_matrix = new MOBaseItem("me_conversion_matrix");
        isolinear_circuit = new Isolinear_circuit("isolinear_circuit");
        base_upgrade = new MOBaseItem("base_upgrade");
        h_compensator = new MOBaseItem("h_compensator");
        integration_matrix = new MOBaseItem("integration_matrix");
        machine_casing = new MOBaseItem("machine_casing");
        s_magnet = new MOBaseItem("s_magnet");
        dilithium_ctystal = new MOBaseItem("dilithium_crystal");
        tritanium_ingot = new MOBaseItem("tritanium_ingot");
        tritanium_dust = new MOBaseItem("tritanium_dust");
        tritanium_plate = new MOBaseItem("tritanium_plate");
        pattern_drive = new PatternDrive("pattern_drive",2);
	}
	
	public static void register()
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
        base_upgrade.Register();
        h_compensator.Register();
        integration_matrix.Register();
        machine_casing.Register();
        s_magnet.Register();
        dilithium_ctystal.Register();
        tritanium_ingot.Register();
        tritanium_dust.Register();
        tritanium_plate.Register();
        pattern_drive.Register();

        GameRegistry.addSmelting(new ItemStack(matter_dust), new ItemStack(matter_dust_refined), 0);
        GameRegistry.addSmelting(new ItemStack(tritanium_dust),new ItemStack(tritanium_ingot),5);
        GameRegistry.addSmelting(new ItemStack(MatterOverdriveBlocks.tritaniumOre),new ItemStack(tritanium_ingot),10);

		GameRegistry.addRecipe(new ItemStack(battery), new Object[]{" R ", "TGT", "TDT", 'T', tritanium_ingot, 'D', MatterOverdriveItems.dilithium_ctystal, 'R', Items.redstone, 'G', Items.gold_ingot});
		GameRegistry.addRecipe(new ItemStack(matter_scanner), new Object[]{"III","GDG","IRI", 'I',Items.iron_ingot, 'D',new ItemStack(isolinear_circuit,1,2),'R',Items.redstone,'G',Items.gold_ingot});
        GameRegistry.addRecipe(new ItemStack(base_upgrade),new Object[]{" R "," C "," T ",'G', Blocks.glass,'R',Items.redstone,'T',tritanium_plate,'C',new ItemStack(isolinear_circuit,1,0)});
        GameRegistry.addRecipe(new ItemStack(h_compensator),new Object[]{" M ","CPC","DED",'D', MatterOverdriveItems.dilithium_ctystal,'M',machine_casing,'I',Items.iron_ingot,'C',new ItemStack(isolinear_circuit,1,0),'P',new ItemStack(isolinear_circuit,1,1),'E',Items.ender_pearl});
        GameRegistry.addRecipe(new ItemStack(integration_matrix),new Object[]{" M ","GPG","DED",'G', Blocks.glass,'M',machine_casing,'I',Items.iron_ingot,'P',new ItemStack(isolinear_circuit,1,1),'E',Items.ender_pearl,'D',MatterOverdriveItems.dilithium_ctystal});
        GameRegistry.addRecipe(new ItemStack(machine_casing),new Object[]{" T ","I I","GRG",'G', Items.gold_ingot,'T',tritanium_plate,'I',tritanium_ingot,'R',Items.redstone});
        GameRegistry.addRecipe(new ItemStack(s_magnet),new Object[]{"RRR","ETE","RRR",'E',Items.ender_pearl,'T',tritanium_ingot,'R',Items.redstone});
        GameRegistry.addRecipe(new ItemStack(me_conversion_matrix),new Object[]{"EIE","CDC","EIE",'E',Items.ender_pearl,'C',new ItemStack(isolinear_circuit,1,1),'I',Items.iron_ingot,'D',MatterOverdriveItems.dilithium_ctystal});
        GameRegistry.addRecipe(new ItemStack(tritanium_plate),new Object[]{"TTT",'T',new ItemStack(tritanium_ingot)});
        GameRegistry.addRecipe(new ItemStack(phaser),new Object[]{"IGI","IDI","WCW",'I',Items.iron_ingot,'G',Blocks.glass,'D',dilithium_ctystal,'W',Blocks.wool,'C',new ItemStack(isolinear_circuit,1,1)});
        GameRegistry.addRecipe(new ItemStack(pattern_drive),new Object[]{" M ", "RER"," C ",'M',machine_casing,'E',Items.ender_pearl,'C',new ItemStack(isolinear_circuit,1,1),'R',Items.redstone});
        MatterRegistry.register(matter_dust_refined, 1);
	}
}
