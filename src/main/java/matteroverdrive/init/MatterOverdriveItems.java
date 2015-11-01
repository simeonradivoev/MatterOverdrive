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
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.items.*;
import matteroverdrive.items.android.TritaniumSpine;
import matteroverdrive.items.armour.TritaniumArmour;
import matteroverdrive.items.food.AndroidPill;
import matteroverdrive.items.food.EarlGrayTea;
import matteroverdrive.items.food.RomulanAle;
import matteroverdrive.items.includes.MOBaseItem;
import matteroverdrive.items.starmap.ItemBuildingBase;
import matteroverdrive.items.starmap.ItemColonizerShip;
import matteroverdrive.items.starmap.ItemScoutShip;
import matteroverdrive.items.starmap.ShipFactory;
import matteroverdrive.items.tools.TritaniumAxe;
import matteroverdrive.items.tools.TritaniumPickaxe;
import matteroverdrive.items.weapon.EnergyPack;
import matteroverdrive.items.weapon.OmniTool;
import matteroverdrive.items.weapon.Phaser;
import matteroverdrive.items.weapon.PhaserRifle;
import matteroverdrive.items.weapon.module.WeaponModuleBarrel;
import matteroverdrive.items.weapon.module.WeaponModuleColor;
import net.minecraft.item.*;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;

public class MatterOverdriveItems
{
	public static MOBaseItem matter_scanner;
	public static ItemFood emergency_ration;
    public static EarlGrayTea earl_gray_tea;
    public static RomulanAle romulan_ale;
	public static MOBaseItem me_conversion_matrix;
	public static Phaser phaser;
	public static Battery battery;
    public static Battery hc_battery;
	public static MOBaseItem creative_battery;
	public static MatterDust matter_dust;
	public static MatterDust matter_dust_refined;
    public static IsolinearCircuit isolinear_circuit;
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
    public static WeaponModuleColor weapon_module_color;
    public static WeaponModuleBarrel weapon_module_barrel;
    public static SecurityProtocol security_protocol;
    public static SpacetimeEqualizer spacetime_equalizer;
    public static Wrench wrench;
    public static RougeAndroidParts androidParts;
    public static MOBaseItem forceFieldEmitter;
    public static ShipFactory shipFactory;
    public static ItemScoutShip scoutShip;
    public static ItemColonizerShip colonizerShip;
    public static ItemBuildingBase buildingBase;
    public static AndroidPill androidPill;
    public static NetworkFlashDrive networkFlashDrive;
    public static CreativePatternDrive creativePatternDrive;
    public static PhaserRifle phaserRifle;
    public static EnergyPack energyPack;
    public static TransportFlashDrive transportFlashDrive;
    public static MatterContainer matterContainerFull;
    public static MatterContainer matterContainer;
    public static DataPad dataPad;
    public static TritaniumSpine tritaniumSpine;
    public static MOBaseItem tritanium_nugget;
    public static OmniTool omniTool;
    public static TritaniumAxe tritaniumAxe;
    public static TritaniumPickaxe tritaniumPickaxe;
    public static ItemSword tritaniumSword;
    public static ItemHoe tritaniumHoe;
    public static TritaniumArmour tritaniumHelemet;
    public static TritaniumArmour tritaniumChestplate;
    public static TritaniumArmour tritaniumLeggings;
    public static TritaniumArmour tritaniumBoots;

    public static Item.ToolMaterial toolMaterialTritanium;
    public static ItemArmor.ArmorMaterial armorMaterialTritanium;

	public static void init(FMLPreInitializationEvent event)
	{
        toolMaterialTritanium = EnumHelper.addToolMaterial("tritanium",2,3122,6f,2f,14);
        armorMaterialTritanium = EnumHelper.addArmorMaterial("tritanium",66,new int[]{2, 6, 5, 2}, 9);

		matter_dust = new MatterDust("matter_dust",false);
		matter_dust_refined = new MatterDust("matter_dust_refined",true);
		matter_scanner = new MatterScanner("matter_scanner");
		battery = new Battery("battery", 1 << 19,Reference.COLOR_MATTER,400,800);
		creative_battery = new CreativeBattery("creative_battery",1 << 24,Reference.COLOR_HOLO_RED,8192,8192);
        hc_battery = new Battery("hc_battery",1 << 20,Reference.COLOR_YELLOW_STRIPES,4096,4096);
		phaser = new Phaser("phaser");
		emergency_ration = new ItemFood(8,0.8F,false);
		emergency_ration.setUnlocalizedName("emergency_ration").setCreativeTab(MatterOverdrive.tabMatterOverdrive_food).setTextureName(Reference.MOD_ID + ":" + "emergency_ration");
        earl_gray_tea = new EarlGrayTea("earl_gray_tea");
		romulan_ale = new RomulanAle("romulan_ale");
		me_conversion_matrix = new MOBaseItem("me_conversion_matrix");
        isolinear_circuit = new IsolinearCircuit("isolinear_circuit");
        item_upgrade = new ItemUpgrade("upgrade");
        h_compensator = new MOBaseItem("h_compensator");
        integration_matrix = new MOBaseItem("integration_matrix");
        machine_casing = new MOBaseItem("machine_casing");
        s_magnet = new MOBaseItem("s_magnet");
        dilithium_ctystal = new MOBaseItem("dilithium_crystal");
        tritanium_ingot = new MOBaseItem("tritanium_ingot");
        toolMaterialTritanium.setRepairItem(new ItemStack(tritanium_ingot));
        armorMaterialTritanium.customCraftingMaterial = tritanium_ingot;
        tritanium_dust = new MOBaseItem("tritanium_dust");
        tritanium_plate = new MOBaseItem("tritanium_plate");
        pattern_drive = new PatternDrive("pattern_drive",2);
        weapon_module_color = new WeaponModuleColor("weapon_module_color");
        weapon_module_barrel = new WeaponModuleBarrel("weapon_module_barrel");
        security_protocol = new SecurityProtocol("security_protocol");
        spacetime_equalizer = new SpacetimeEqualizer("spacetime_equalizer");
        wrench = new Wrench("tritanium_wrench");
        androidParts = new RougeAndroidParts("rouge_android_part");
        forceFieldEmitter = new MOBaseItem("forcefield_emitter");
        shipFactory = new ShipFactory("ship_factory");
        scoutShip = new ItemScoutShip("scout_ship");
        colonizerShip = new ItemColonizerShip("ship_colonizer");
        buildingBase = new ItemBuildingBase("building_base");
        androidPill = new AndroidPill("android_pill");
        networkFlashDrive = new NetworkFlashDrive("network_flash_drive",Reference.COLOR_YELLOW_STRIPES);
        creativePatternDrive = new CreativePatternDrive("creative_pattern_drive",0);
        phaserRifle = new PhaserRifle("phaser_rifle");
        energyPack = new EnergyPack("energy_pack");
        transportFlashDrive = new TransportFlashDrive("transport_flash_drive",Reference.COLOR_HOLO_GREEN);
        matterContainer = new MatterContainer("matter_container",false);
        matterContainerFull = new MatterContainer("matter_container_full",true);
        dataPad = new DataPad("data_pad");
        tritaniumSpine = new TritaniumSpine("tritainum_spine");
        tritanium_nugget = new MOBaseItem("tritanium_nugget");
        omniTool = new OmniTool("omni_tool");
        tritaniumAxe = new TritaniumAxe("tritanium_axe");
        tritaniumPickaxe = new TritaniumPickaxe("tritanium_pickaxe");
        tritaniumSword = (ItemSword)new ItemSword(toolMaterialTritanium).setUnlocalizedName("tritanium_sword").setTextureName(Reference.MOD_ID + ":" + "tritanium_sword");
        tritaniumHoe = (ItemHoe)new ItemHoe(toolMaterialTritanium).setUnlocalizedName("tritanium_hoe").setTextureName(Reference.MOD_ID + ":" + "tritanium_hoe");
        tritaniumHelemet = (TritaniumArmour)new TritaniumArmour(armorMaterialTritanium,2,0).setUnlocalizedName("tritanium_helmet").setTextureName(Reference.MOD_ID + ":" + "tritanium_helmet");
        tritaniumChestplate = (TritaniumArmour)new TritaniumArmour(armorMaterialTritanium,2,1).setUnlocalizedName("tritanium_chestplate").setTextureName(Reference.MOD_ID + ":" + "tritanium_chestplate");
        tritaniumLeggings = (TritaniumArmour)new TritaniumArmour(armorMaterialTritanium,2,2).setUnlocalizedName("tritanium_leggings").setTextureName(Reference.MOD_ID + ":" + "tritanium_leggings");
        tritaniumBoots = (TritaniumArmour)new TritaniumArmour(armorMaterialTritanium,2,3).setUnlocalizedName("tritanium_boots").setTextureName(Reference.MOD_ID + ":" + "tritanium_boots");
	}

	public static void register(FMLInitializationEvent event)
	{
		GameRegistry.registerItem(emergency_ration, emergency_ration.getUnlocalizedName().substring(5));

		matter_dust_refined.register();
		matter_dust.register();
		creative_battery.register();
        me_conversion_matrix.register();
		matter_scanner.register();
		phaser.register();
		battery.register();
        isolinear_circuit.register();
        item_upgrade.register();
        h_compensator.register();
        integration_matrix.register();
        machine_casing.register();
        s_magnet.register();
        dilithium_ctystal.register();
        MatterOverdrive.matterRegistry.addToBlacklist(dilithium_ctystal);
        tritanium_ingot.register();
        MatterOverdrive.matterRegistry.addToBlacklist(tritanium_ingot);
        tritanium_dust.register();
        MatterOverdrive.matterRegistry.addToBlacklist(tritanium_dust);
        tritanium_plate.register();
        MatterOverdrive.matterRegistry.addToBlacklist(tritanium_plate);
        pattern_drive.register();
        weapon_module_color.register();
        weapon_module_barrel.register();
        earl_gray_tea.Register();
		romulan_ale.register();
        security_protocol.register();
        spacetime_equalizer.Register();
        wrench.register();
        androidParts.register();
        forceFieldEmitter.register();
        hc_battery.register();
        shipFactory.register();
        scoutShip.register();
        colonizerShip.register();
        buildingBase.register();
        androidPill.register();
        networkFlashDrive.register();
        creativePatternDrive.register();
        phaserRifle.register();
        energyPack.register();
        transportFlashDrive.register();
        matterContainer.register();
        matterContainerFull.register();
        dataPad.register();
        tritaniumSpine.register();
        tritanium_nugget.register();
        omniTool.register();

        GameRegistry.addSmelting(new ItemStack(tritanium_dust), new ItemStack(tritanium_ingot), 5);
        GameRegistry.addSmelting(new ItemStack(MatterOverdriveBlocks.tritaniumOre), new ItemStack(tritanium_ingot), 10);

        GameRegistry.registerItem(tritaniumAxe,"tritanium_axe");
        GameRegistry.registerItem(tritaniumPickaxe,"tritanium_pickaxe");
        GameRegistry.registerItem(tritaniumSword,"tritanium_sword");
        GameRegistry.registerItem(tritaniumHoe,"tritanium_hoe");
        GameRegistry.registerItem(tritaniumHelemet,"tritanium_helmet");
        GameRegistry.registerItem(tritaniumChestplate,"tritanium_chestplate");
        GameRegistry.registerItem(tritaniumLeggings,"tritanium_leggings");
        GameRegistry.registerItem(tritaniumBoots,"tritanium_boots");

        OreDictionary.registerOre("dustTritanium", tritanium_dust);
        OreDictionary.registerOre("ingotTritanium",tritanium_ingot);
        OreDictionary.registerOre("gemDilithium",dilithium_ctystal);
        OreDictionary.registerOre("matterDust",matter_dust);
        OreDictionary.registerOre("matterDustRefined",matter_dust_refined);
        OreDictionary.registerOre("nuggetTritanium",tritanium_nugget);
	}

    public static void addToDungons()
    {
        weapon_module_color.addToDunguns();
        androidPill.addToDunguns();
        addToDungons(emergency_ration, 1, 8, 6);
        addToDungons(earl_gray_tea, 1, 2, 2);
        addToDungons(romulan_ale, 1, 2, 2);
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
