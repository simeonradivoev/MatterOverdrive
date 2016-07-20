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
import matteroverdrive.Reference;
import matteroverdrive.data.matter.ItemHandler;
import matteroverdrive.items.*;
import matteroverdrive.items.android.RougeAndroidParts;
import matteroverdrive.items.android.TritaniumSpine;
import matteroverdrive.items.armour.TritaniumArmor;
import matteroverdrive.items.food.AndroidPill;
import matteroverdrive.items.food.EarlGrayTea;
import matteroverdrive.items.food.RomulanAle;
import matteroverdrive.items.includes.MOBaseItem;
import matteroverdrive.items.starmap.*;
import matteroverdrive.items.tools.TritaniumAxe;
import matteroverdrive.items.tools.TritaniumPickaxe;
import matteroverdrive.items.weapon.*;
import matteroverdrive.items.weapon.module.*;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.lang.reflect.Field;

public class MatterOverdriveItems
{
	public final static Item.ToolMaterial toolMaterialTritanium = EnumHelper.addToolMaterial("tritanium", 2, 3122, 6f, 2f, 14);
	public final static ItemArmor.ArmorMaterial armorMaterialTritanium = EnumHelper.addArmorMaterial("tritanium", "tritanium", 66, new int[] {4, 9, 7, 4}, 20, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2f);

	public final static MOBaseItem matter_scanner = new MatterScanner("matter_scanner");
	public final static ItemFood emergency_ration = (ItemFood)new ItemFood(8, 0.8F, false).setUnlocalizedName("emergency_ration").setCreativeTab(MatterOverdrive.tabMatterOverdrive_food).setRegistryName(new ResourceLocation(Reference.MOD_ID, "emergency_ration"));
	public final static EarlGrayTea earl_gray_tea = new EarlGrayTea("earl_gray_tea");
	public final static RomulanAle romulan_ale = new RomulanAle("romulan_ale");
	public final static MOBaseItem me_conversion_matrix = new MOBaseItem("me_conversion_matrix");
	public final static Phaser phaser = new Phaser("phaser");
	public final static Battery battery = new Battery("battery", 1 << 19, 400, 800);
	public final static Battery hc_battery = new Battery("hc_battery", 1 << 20, 4096, 4096);
	public final static Battery creative_battery = new CreativeBattery("creative_battery", 1 << 24, 8192, 8192);
	public final static MatterDust matter_dust = new MatterDust("matter_dust", false);
	public final static MatterDust matter_dust_refined = new MatterDust("matter_dust_refined", true);
	public final static IsolinearCircuit isolinear_circuit = new IsolinearCircuit("isolinear_circuit");
	public final static MOBaseItem h_compensator = new MOBaseItem("h_compensator");
	public final static MOBaseItem integration_matrix = new MOBaseItem("integration_matrix");
	public final static MOBaseItem machine_casing = new MOBaseItem("machine_casing");
	public final static MOBaseItem s_magnet = new MOBaseItem("s_magnet");
	public final static MOBaseItem dilithium_ctystal = new MOBaseItem("dilithium_crystal");
	public final static MOBaseItem tritanium_ingot = new MOBaseItem("tritanium_ingot");
	public final static MOBaseItem tritanium_dust = new MOBaseItem("tritanium_dust");
	public final static MOBaseItem tritanium_plate = new MOBaseItem("tritanium_plate");
	public final static PatternDrive pattern_drive = new PatternDrive("pattern_drive", 2);
	public final static ItemUpgrade item_upgrade = new ItemUpgrade("upgrade");
	public final static WeaponModuleColor weapon_module_color = new WeaponModuleColor("weapon_module_color");
	public final static WeaponModuleBarrel weapon_module_barrel = new WeaponModuleBarrel("weapon_module_barrel");
	public final static SecurityProtocol security_protocol = new SecurityProtocol("security_protocol");
	public final static SpacetimeEqualizer spacetime_equalizer = new SpacetimeEqualizer("spacetime_equalizer");
	public final static Wrench wrench = new Wrench("tritanium_wrench");
	public final static RougeAndroidParts androidParts = new RougeAndroidParts("rogue_android_part");
	public final static MOBaseItem forceFieldEmitter = new MOBaseItem("forcefield_emitter");
	public final static ShipFactory shipFactory = new ShipFactory("ship_factory");
	public final static ItemScoutShip scoutShip = new ItemScoutShip("scout_ship");
	public final static ItemColonizerShip colonizerShip = new ItemColonizerShip("ship_colonizer");
	public final static ItemBuildingBase buildingBase = new ItemBuildingBase("building_base");
	public final static AndroidPill androidPill = new AndroidPill("android_pill");
	public final static NetworkFlashDrive networkFlashDrive = new NetworkFlashDrive("network_flash_drive");
	//public static CreativePatternDrive creativePatternDrive;
	public final static PhaserRifle phaserRifle = new PhaserRifle("phaser_rifle");
	public final static EnergyPack energyPack = new EnergyPack("energy_pack");
	public final static TransportFlashDrive transportFlashDrive = new TransportFlashDrive("transport_flash_drive");
	public final static MatterContainer matterContainerFull = new MatterContainer("matter_container_full", true);
	public final static MatterContainer matterContainer = new MatterContainer("matter_container", false);
	public final static DataPad dataPad = new DataPad("data_pad");
	public final static TritaniumSpine tritaniumSpine = new TritaniumSpine("tritainum_spine");
	public final static MOBaseItem tritanium_nugget = new MOBaseItem("tritanium_nugget");
	public final static OmniTool omniTool = new OmniTool("omni_tool");
	public final static TritaniumAxe tritaniumAxe = (TritaniumAxe)new TritaniumAxe("tritanium_axe").setRegistryName(new ResourceLocation(Reference.MOD_ID, "tritanium_axe"));
	public final static TritaniumPickaxe tritaniumPickaxe = (TritaniumPickaxe)new TritaniumPickaxe("tritanium_pickaxe").setRegistryName(new ResourceLocation(Reference.MOD_ID, "tritanium_pickaxe"));
	public final static ItemSword tritaniumSword = (ItemSword)new ItemSword(toolMaterialTritanium).setUnlocalizedName("tritanium_sword").setRegistryName(new ResourceLocation(Reference.MOD_ID, "tritanium_sword"));
	public final static ItemHoe tritaniumHoe = (ItemHoe)new ItemHoe(toolMaterialTritanium).setUnlocalizedName("tritanium_hoe").setRegistryName(new ResourceLocation(Reference.MOD_ID, "tritanium_hoe"));
	public final static ItemSpade tritaniumShovel = (ItemSpade)new ItemSpade(toolMaterialTritanium).setUnlocalizedName("tritanium_shovel").setRegistryName(new ResourceLocation(Reference.MOD_ID, "tritanium_shovel"));
	public final static TritaniumArmor tritaniumHelemet = (TritaniumArmor)new TritaniumArmor(armorMaterialTritanium, 2, EntityEquipmentSlot.HEAD).setUnlocalizedName("tritanium_helmet").setRegistryName(new ResourceLocation(Reference.MOD_ID, "tritanium_helmet"));
	public final static TritaniumArmor tritaniumChestplate = (TritaniumArmor)new TritaniumArmor(armorMaterialTritanium, 2, EntityEquipmentSlot.CHEST).setUnlocalizedName("tritanium_chestplate").setRegistryName(new ResourceLocation(Reference.MOD_ID, "tritanium_chestplate"));
	public final static TritaniumArmor tritaniumLeggings = (TritaniumArmor)new TritaniumArmor(armorMaterialTritanium, 2, EntityEquipmentSlot.LEGS).setUnlocalizedName("tritanium_leggings").setRegistryName(new ResourceLocation(Reference.MOD_ID, "tritanium_leggings"));
	public final static TritaniumArmor tritaniumBoots = (TritaniumArmor)new TritaniumArmor(armorMaterialTritanium, 2, EntityEquipmentSlot.FEET).setUnlocalizedName("tritanium_boots").setRegistryName(new ResourceLocation(Reference.MOD_ID, "tritanium_boots"));
	public final static Contract contract = new Contract("contract");
	public final static PlasmaShotgun plasmaShotgun = new PlasmaShotgun("plasma_shotgun");
	public final static IonSniper ionSniper = new IonSniper("ion_sniper");
	public final static WeaponModuleSniperScope sniperScope = new WeaponModuleSniperScope("sniper_scope");
	public final static ItemBuildingResidential buildingResidential = new ItemBuildingResidential("building_residential");
	public final static ItemBuildingMatterExtractor buildingMatterExtractor = new ItemBuildingMatterExtractor("building_matter_extractor");
	public final static ItemBuildingShipHangar buildingShipHangar = new ItemBuildingShipHangar("building_ship_hangar");
	public final static ItemBuildingPowerGenerator buildingPowerGenerator = new ItemBuildingPowerGenerator("building_power_generator");
	public final static MOBaseItem weaponHandle = new MOBaseItem("weapon_handle");
	public final static MOBaseItem weaponReceiver = new MOBaseItem("weapon_receiver");
	public final static MOBaseItem plasmaCore = new MOBaseItem("plasma_core");
	public final static PortableDecomposer portableDecomposer = new PortableDecomposer("portable_decomposer", 128000, 256, 512, 0.1f);
	public final static WeaponModuleRicochet weaponModuleRicochet = new WeaponModuleRicochet("weapon_module_ricochet");
	public final static WeaponModuleHoloSights weaponModuleHoloSights = new WeaponModuleHoloSights("weapon_module_holo_sights");

	public static void init(FMLPreInitializationEvent event)
	{
		toolMaterialTritanium.setRepairItem(new ItemStack(tritanium_ingot));
		armorMaterialTritanium.customCraftingMaterial = tritanium_ingot;
	}

	public static void register()
	{
		for (Field field : MatterOverdriveItems.class.getFields())
		{
			if (Item.class.isAssignableFrom(field.getType()))
			{
				try
				{
					GameRegistry.register((Item)field.get(null));
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
		}

		MatterOverdrive.matterRegistry.register(dilithium_ctystal, new ItemHandler(0, true));
		MatterOverdrive.matterRegistry.register(tritanium_ingot, new ItemHandler(0, true));
		MatterOverdrive.matterRegistry.register(tritanium_dust, new ItemHandler(0, true));
		MatterOverdrive.matterRegistry.register(tritanium_plate, new ItemHandler(0, true));

		GameRegistry.addSmelting(new ItemStack(tritanium_dust), new ItemStack(tritanium_ingot), 5);
		GameRegistry.addSmelting(new ItemStack(MatterOverdriveBlocks.tritaniumOre), new ItemStack(tritanium_ingot), 10);

		OreDictionary.registerOre("dustTritanium", tritanium_dust);
		OreDictionary.registerOre("ingotTritanium", tritanium_ingot);
		OreDictionary.registerOre("gemDilithium", dilithium_ctystal);
		OreDictionary.registerOre("matterDust", matter_dust);
		OreDictionary.registerOre("matterDustRefined", matter_dust_refined);
		OreDictionary.registerOre("nuggetTritanium", tritanium_nugget);
	}

	public static void addToDungons()
	{
		weapon_module_color.addToDunguns();
		androidPill.addToDunguns();
		addToDungons(emergency_ration, 1, 8, 6);
		addToDungons(earl_gray_tea, 1, 2, 2);
		addToDungons(romulan_ale, 1, 2, 2);
	}

	public static void addToMODungons()
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
