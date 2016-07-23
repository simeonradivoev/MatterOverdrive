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
import matteroverdrive.data.matter.ItemHandler;
import matteroverdrive.items.*;
import matteroverdrive.items.android.RougeAndroidParts;
import matteroverdrive.items.android.TritaniumSpine;
import matteroverdrive.items.armour.TritaniumArmor;
import matteroverdrive.items.food.AndroidPill;
import matteroverdrive.items.food.EarlGrayTea;
import matteroverdrive.items.food.MOItemFood;
import matteroverdrive.items.food.RomulanAle;
import matteroverdrive.items.includes.MOBaseItem;
import matteroverdrive.items.includes.MOItemOre;
import matteroverdrive.items.starmap.*;
import matteroverdrive.items.tools.*;
import matteroverdrive.items.weapon.*;
import matteroverdrive.items.weapon.module.*;
import matteroverdrive.util.MOLog;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.shadowfacts.shadowmc.item.ModItems;

public class MatterOverdriveItems extends ModItems
{
	public final static Item.ToolMaterial TOOL_MATERIAL_TRITANIUM = EnumHelper.addToolMaterial("TRITANIUM", 2, 3122, 6f, 2f, 14);
	public final static ItemArmor.ArmorMaterial ARMOR_MATERIAL_TRITANIUM = EnumHelper.addArmorMaterial("TRITANIUM", "tritanium", 66, new int[] {4, 9, 7, 4}, 20, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2f);

	private int registeredCount = 0;

//	Materials
	public MOItemOre dilithium_crystal;
	public MOItemOre tritanium_ingot;
	public MOItemOre tritanium_nugget;
	public MOItemOre tritanium_dust;
	public MOBaseItem tritanium_plate;
	public MatterDust matter_dust;
	public MatterDust matter_dust_refined;

//	Food(ish)
	public AndroidPill androidPill;
	public MOItemFood emergency_ration;
	public EarlGrayTea earl_gray_tea;
	public RomulanAle romulan_ale;

//	Storage
	public Battery battery;
	public Battery hc_battery;
	public Battery creative_battery;
	public MatterContainer matterContainer;
	public MatterContainer matterContainerFull;

//	Crafting
	public MOBaseItem me_conversion_matrix;
	public MOBaseItem h_compensator;
	public MOBaseItem integration_matrix;
	public MOBaseItem machine_casing;
	public MOBaseItem s_magnet;
	public IsolinearCircuit isolinear_circuit;
	public MOBaseItem forceFieldEmitter;
	public MOBaseItem weaponHandle;
	public MOBaseItem weaponReceiver;
	public MOBaseItem plasmaCore;

//	Weapons
	public Phaser phaser;
	public PhaserRifle phaserRifle;
	public PlasmaShotgun plasmaShotgun;
	public IonSniper ionSniper;
	public OmniTool omniTool;

//	Weapon Modules
	public WeaponModuleColor weapon_module_color;
	public WeaponModuleBarrel weapon_module_barrel;
	public WeaponModuleSniperScope sniperScope;
	public WeaponModuleRicochet weaponModuleRicochet;
	public WeaponModuleHoloSights weaponModuleHoloSights;

//	Tools
	public Wrench wrench;
	public TritaniumAxe tritaniumAxe;
	public TritaniumPickaxe tritaniumPickaxe;
	public TritaniumSword tritaniumSword;
	public TritaniumHoe tritaniumHoe;
	public TritaniumShovel tritaniumShovel;
	public TritaniumArmor tritaniumHelmet;
	public TritaniumArmor tritaniumChestplate;
	public TritaniumArmor tritaniumLeggings;
	public TritaniumArmor tritaniumBoots;

//	Ships & Buildings
	public ShipFactory shipFactory;
	public ItemScoutShip scoutShip;
	public ItemColonizerShip colonizerShip;
	public ItemBuildingBase buildingBase;
	public ItemBuildingResidential buildingResidential;
	public ItemBuildingMatterExtractor buildingMatterExtractor;
	public ItemBuildingShipHangar buildingShipHangar;
	public ItemBuildingPowerGenerator buildingPowerGenerator;

//	Android
	public RougeAndroidParts androidParts;
	public TritaniumSpine tritaniumSpine;

//	Misc
	public MatterScanner matter_scanner;
	public PatternDrive pattern_drive;
	public NetworkFlashDrive networkFlashDrive;
	public ItemUpgrade item_upgrade;
	public TransportFlashDrive transportFlashDrive;
	public EnergyPack energyPack;
	public DataPad dataPad;
	public Contract contract;
	public PortableDecomposer portableDecomposer;
	public SecurityProtocol security_protocol;
	public SpacetimeEqualizer spacetime_equalizer;



	@Override
	public void init()
	{

		MOLog.info("Registering items");

//		Materials
		dilithium_crystal = register(new MOItemOre("dilithium_crystal", "gemDilithium"));
		tritanium_ingot = register(new MOItemOre("tritanium_ingot", "ingotTritanium"));
		tritanium_nugget = register(new MOItemOre("tritanium_nugget", "nuggetTritanium"));
		tritanium_dust = register(new MOItemOre("tritanium_dust", "dustTritanium"));
		tritanium_plate = register(new MOBaseItem("tritanium_plate"));
		matter_dust = register(new MatterDust("matter_dust", "matterDust", false));
		matter_dust_refined = register(new MatterDust("matter_dust_refined", "matterDustRefined", true));

//		Food(ish)
		androidPill = register(new AndroidPill("android_pill"));
		emergency_ration = register(new MOItemFood("emergency_ration", 8, 0.8f, false));
		earl_gray_tea = register(new EarlGrayTea("earl_gray_tea"));
		romulan_ale = register(new RomulanAle("romulan_ale"));

//		Storage
		battery = register(new Battery("battery", 1 << 19, 400, 800));
		hc_battery = register(new Battery("hc_battery", 1 << 20, 4096, 4096));
		creative_battery = register(new CreativeBattery("creative_battery", 1 << 24, 8192, 8192));
		matterContainer = register(new MatterContainer("matter_container", false));
		matterContainerFull = register(new MatterContainer("matter_container_full", true));

//		Crafting
		me_conversion_matrix = register(new MOBaseItem("me_conversion_matrix"));
		h_compensator = register(new MOBaseItem("h_compensator"));
		integration_matrix = register(new MOBaseItem("integration_matrix"));
		machine_casing = register(new MOBaseItem("machine_casing"));
		s_magnet = register(new MOBaseItem("s_magnet"));
		isolinear_circuit = register(new IsolinearCircuit("isolinear_circuit"));
		forceFieldEmitter = register(new MOBaseItem("forcefield_emitter"));
		weaponHandle = register(new MOBaseItem("weapon_handle"));
		weaponReceiver = register(new MOBaseItem("weapon_receiver"));
		plasmaCore = register(new MOBaseItem("plasma_core"));

//		Weapons
		phaser = register(new Phaser("phaser"));
		phaserRifle = register(new PhaserRifle("phaser_rifle"));
		plasmaShotgun = register(new PlasmaShotgun("plasma_shotgun"));
		ionSniper = register(new IonSniper("ion_sniper"));
		omniTool = register(new OmniTool("omni_tool"));

//		Weapon Modules
		weapon_module_color = register(new WeaponModuleColor("weapon_module_color"));
		weapon_module_barrel = register(new WeaponModuleBarrel("weapon_module_barrel"));
		sniperScope = register(new WeaponModuleSniperScope("sniper_scope"));
		weaponModuleRicochet = register(new WeaponModuleRicochet("weapon_module_ricochet"));
		weaponModuleHoloSights = register(new WeaponModuleHoloSights("weapon_module_holo_sights"));

//		Tools
		wrench = register(new Wrench("tritanium_wrench"));
		tritaniumAxe = register(new TritaniumAxe("tritanium_axe"));
		tritaniumPickaxe = register(new TritaniumPickaxe("tritanium_pickaxe"));
		tritaniumSword = register(new TritaniumSword("tritanium_sword"));
		tritaniumHoe = register(new TritaniumHoe("tritanium_hoe"));
		tritaniumShovel = register(new TritaniumShovel("tritanium_shovel"));
		tritaniumHelmet = register(new TritaniumArmor("tritanium_helmet", ARMOR_MATERIAL_TRITANIUM, 2, EntityEquipmentSlot.HEAD));
		tritaniumChestplate = register(new TritaniumArmor("tritanium_chestplate", ARMOR_MATERIAL_TRITANIUM, 2, EntityEquipmentSlot.CHEST));
		tritaniumLeggings = register(new TritaniumArmor("tritanium_leggings", ARMOR_MATERIAL_TRITANIUM, 2, EntityEquipmentSlot.LEGS));
		tritaniumBoots = register(new TritaniumArmor("tritanium_boots", ARMOR_MATERIAL_TRITANIUM, 2, EntityEquipmentSlot.FEET));

//		Ships & Buildings
		shipFactory = register(new ShipFactory("ship_factory"));
		scoutShip = register(new ItemScoutShip("scout_ship"));
		colonizerShip = register(new ItemColonizerShip("ship_colonizer"));
		buildingBase = register(new ItemBuildingBase("building_base"));
		buildingResidential = register(new ItemBuildingResidential("building_residential"));
		buildingMatterExtractor = register(new ItemBuildingMatterExtractor("building_matter_extractor"));
		buildingShipHangar = register(new ItemBuildingShipHangar("building_ship_hangar"));
		buildingPowerGenerator = register(new ItemBuildingPowerGenerator("building_power_generator"));

//		Android
		androidParts = register(new RougeAndroidParts("rogue_android_part"));
		tritaniumSpine = register(new TritaniumSpine("tritainum_spine"));

//		Misc
		matter_scanner = register(new MatterScanner("matter_scanner"));
		pattern_drive = register(new PatternDrive("pattern_drive", 2));
		networkFlashDrive = register(new NetworkFlashDrive("network_flash_drive"));
		item_upgrade = register(new ItemUpgrade("upgrade"));
		transportFlashDrive = register(new TransportFlashDrive("transport_flash_drive"));
		energyPack = register(new EnergyPack("energy_pack"));
		dataPad = register(new DataPad("data_pad"));
		contract = register(new Contract("contract"));
		portableDecomposer = register(new PortableDecomposer("portable_decomposer", 128000, 256, 512, 0.1f));
		security_protocol = register(new SecurityProtocol("security_protocol"));
		spacetime_equalizer = register(new SpacetimeEqualizer("spacetime_equalizer"));



		MOLog.info("Finished registering items");
		MOLog.info("Registered %d items", registeredCount);


		TOOL_MATERIAL_TRITANIUM.setRepairItem(new ItemStack(tritanium_ingot));
		ARMOR_MATERIAL_TRITANIUM.customCraftingMaterial = tritanium_ingot;

		MatterOverdrive.matterRegistry.register(dilithium_crystal, new ItemHandler(0, true));
		MatterOverdrive.matterRegistry.register(tritanium_ingot, new ItemHandler(0, true));
		MatterOverdrive.matterRegistry.register(tritanium_dust, new ItemHandler(0, true));
		MatterOverdrive.matterRegistry.register(tritanium_plate, new ItemHandler(0, true));

		GameRegistry.addSmelting(new ItemStack(tritanium_dust), new ItemStack(tritanium_ingot), 5);
		GameRegistry.addSmelting(new ItemStack(MatterOverdrive.blocks.tritaniumOre), new ItemStack(tritanium_ingot), 10);
	}

	@Override
	protected <T extends Item> T register(T item)
	{
		registeredCount++;
		return super.register(item);
	}

	public void addToDungons()
	{
		weapon_module_color.addToDunguns();
		androidPill.addToDunguns();
		addToDungons(emergency_ration, 1, 8, 6);
		addToDungons(earl_gray_tea, 1, 2, 2);
		addToDungons(romulan_ale, 1, 2, 2);

		addToMODungons();
	}

	public void addToMODungons()
	{
		// TODO: 3/25/2016 Find how to add to dungon chests
		/*ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new WeightedRandomChestContent(new ItemStack(emergency_ration),8,20,100));
		ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new WeightedRandomChestContent(new ItemStack(earl_gray_tea),4,10,50));
        ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new WeightedRandomChestContent(new ItemStack(romulan_ale),4,10,50));

        ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new WeightedRandomChestContent(isolinear_circuit,0,1,5,50));
        ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new WeightedRandomChestContent(isolinear_circuit,1,1,4,40));
        ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new WeightedRandomChestContent(isolinear_circuit,2,1,3,30));
        ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new WeightedRandomChestContent(isolinear_circuit,3,1,2,20));

        ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new WeightedRandomChestContent(androidPill,1,1,2,10));
        ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new WeightedRandomChestContent(androidPill,0,1,1,5));
        ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new WeightedRandomChestContent(weapon_module_barrel,WeaponModuleBarrel.DAMAGE_BARREL_ID,1,1,10));
        ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new WeightedRandomChestContent(weapon_module_barrel,WeaponModuleBarrel.FIRE_BARREL_ID,1,1,8));
        ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new WeightedRandomChestContent(weapon_module_barrel,WeaponModuleBarrel.HEAL_BARREL_ID,1,1,10));
        ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new WeightedRandomChestContent(weapon_module_barrel,WeaponModuleBarrel.EXPLOSION_BARREL_ID,1,1,5));
        ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new WeightedRandomChestContent(new ItemStack(weaponModuleRicochet),1,1,5));
        ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new WeightedRandomChestContent(new ItemStack(tritaniumSpine),1,1,10));
        ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new WeightedRandomChestContent(androidParts,0,1,2,15));
        ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new WeightedRandomChestContent(androidParts,1,1,2,15));
        ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new WeightedRandomChestContent(androidParts,2,1,2,15));
        ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new WeightedRandomChestContent(androidParts,3,1,2,15));
        ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new WeightedRandomChestContent(new ItemStack(hc_battery),1,1,10));
        ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new WeightedRandomChestContent(new ItemStack(h_compensator),1,2,10));
        ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new WeightedRandomChestContent(new ItemStack(me_conversion_matrix),1,2,10));
        ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new WeightedRandomChestContent(new ItemStack(matterContainerFull),4,8,20));
        ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new WeightedRandomChestContent(new ItemStack(phaser),1,1,10));*/
	}

	private static void addToDungons(Item item, int min, int max, int chance)
	{
        /*ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(item),min,max,chance));
        ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(item),min,max,chance));
        ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(item),min,max,chance));
        ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(item),min,max,chance));
        ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(item),min,max,chance));*/
	}
}
