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
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.guide.*;
import matteroverdrive.guide.infograms.InfogramCreates;
import matteroverdrive.guide.infograms.InfogramDepth;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 8/29/2015.
 */
@SideOnly(Side.CLIENT)
public class MatterOverdriveGuides
{
    public static GuideCategory androidCategory;
    public static GuideCategory weaponsCategory;
    public static GuideCategory generalCategory;

    public static void registerGuideElements(FMLInitializationEvent event)
    {
        MatterOverdriveGuide.registerGuideElementHandler("text", GuideElementText.class);
        MatterOverdriveGuide.registerGuideElementHandler("depth", InfogramDepth.class);
        MatterOverdriveGuide.registerGuideElementHandler("creates", InfogramCreates.class);
        MatterOverdriveGuide.registerGuideElementHandler("recipe", GuideElementRecipe.class);
        MatterOverdriveGuide.registerGuideElementHandler("title", GuideElementTitle.class);
        MatterOverdriveGuide.registerGuideElementHandler("image",GuideElementImage.class);
        MatterOverdriveGuide.registerGuideElementHandler("preview",GuideElementPreview.class);
        MatterOverdriveGuide.registerGuideElementHandler("details",GuideElementDetails.class);
        MatterOverdriveGuide.registerGuideElementHandler("tooltip",GuideElementTooltip.class);
    }

    public static void registerGuides(FMLPostInitializationEvent event)
    {
        generalCategory = new GuideCategory("general").setHoloIcon("home_icon");
        MatterOverdriveGuide.registerCategory(generalCategory);
        weaponsCategory = new GuideCategory("weapons").setHoloIcon("ammo");
        MatterOverdriveGuide.registerCategory(weaponsCategory);
        androidCategory = new GuideCategory("android").setHoloIcon("android_slot_arms");
        MatterOverdriveGuide.registerCategory(androidCategory);


        //region General
        //Ore
        addEntry(generalCategory, new MOGuideEntryBlock(MatterOverdriveBlocks.dilithium_ore).setGroup("resources"), 3, 0);
        addEntry(generalCategory, new MOGuideEntryBlock(MatterOverdriveBlocks.tritaniumOre).setGroup("resources"), 4, 0);
        addEntry(generalCategory, new MOGuideEntryItem(MatterOverdriveItems.dilithium_ctystal).setGroup("resources"), 3, 1);
        addEntry(generalCategory, new MOGuideEntryItem(MatterOverdriveItems.tritanium_ingot).setGroup("resources"), 4, 1);
        //Machines
        addEntry(generalCategory, new MOGuideEntryBlock(MatterOverdriveBlocks.replicator).setGroup("machines"), 0, 0);
        addEntry(generalCategory,new MOGuideEntryBlock(MatterOverdriveBlocks.decomposer).setGroup("machines"),1,0);
        addEntry(generalCategory,new MOGuideEntryBlock(MatterOverdriveBlocks.recycler).setGroup("machines"),0,1);
        addEntry(generalCategory,new MOGuideEntryBlock(MatterOverdriveBlocks.matter_analyzer).setGroup("machines"),1,1);
        addEntry(generalCategory,new MOGuideEntryBlock(MatterOverdriveBlocks.pattern_storage).setGroup("machines"),0,2);
        addEntry(generalCategory,new MOGuideEntryBlock(MatterOverdriveBlocks.pattern_monitor).setGroup("machines"),1,2);
        addEntry(generalCategory,new MOGuideEntryBlock(MatterOverdriveBlocks.transporter).setGroup("machines"),0,3);
        addEntry(generalCategory,new MOGuideEntryBlock(MatterOverdriveBlocks.holoSign).setGroup("machines"),1,3);
        addEntry(generalCategory,new MOGuideEntryBlock(MatterOverdriveBlocks.inscriber).setGroup("machines"),0,4);
        addEntry(generalCategory,new MOGuideEntryBlock(MatterOverdriveBlocks.contractMarket).setGroup("machines"),1,4);
        //Power
        addEntry(generalCategory,new MOGuideEntry("fusion_reactor").setStackIcons(new ItemStack(MatterOverdriveBlocks.fusion_reactor_controller),
                new ItemStack(MatterOverdriveBlocks.fusion_reactor_coil),
                new ItemStack(MatterOverdriveBlocks.forceGlass),
                new ItemStack(MatterOverdriveBlocks.fusionReactorIO)).setGroup("power"),3,3);
        addEntry(generalCategory,new MOGuideEntryBlock(MatterOverdriveBlocks.gravitational_anomaly).setGroup("power"),4,3);
        addEntry(generalCategory,new MOGuideEntryBlock(MatterOverdriveBlocks.solar_panel).setGroup("power"),3,4);
        addEntry(generalCategory,new MOGuideEntry("batteries").setStackIcons(new ItemStack(MatterOverdriveItems.battery),new ItemStack(MatterOverdriveItems.hc_battery),new ItemStack(MatterOverdriveItems.creative_battery)).setGroup("power"),4,4);
        //Matter
        addEntry(generalCategory,new MOGuideEntry("matter_transport").setStackIcons(MatterOverdriveBlocks.heavy_matter_pipe).setGroup("matter"),6,0);
        addEntry(generalCategory,new MOGuideEntry("matter_fail").setStackIcons(MatterOverdriveItems.matter_dust).setGroup("matter"),7,0);
        addEntry(generalCategory,new MOGuideEntry("matter_plasma",new ItemStack(MatterOverdriveItems.matterContainerFull)).setGroup("matter"),6,1);
        addEntry(generalCategory, new MOGuideEntryItem(MatterOverdriveItems.matter_scanner).setGroup("matter"), 7, 1);
        addEntry(generalCategory, new MOGuideEntryItem(MatterOverdriveItems.pattern_drive).setGroup("matter"), 6, 2);
        //Matter Network
        addEntry(generalCategory,new MOGuideEntryBlock(MatterOverdriveBlocks.network_pipe).setGroup("matter_network"),6,4);
        addEntry(generalCategory, new MOGuideEntryBlock(MatterOverdriveBlocks.network_switch).setGroup("matter_network"), 7, 4);
        addEntry(generalCategory, new MOGuideEntryItem(MatterOverdriveItems.networkFlashDrive).setGroup("matter_network"), 6, 5);
        addEntry(generalCategory, new MOGuideEntryBlock(MatterOverdriveBlocks.network_router).setGroup("matter_network"), 7, 5);
        //endregion
        //Items
        int itemsY = 6;
        addEntry(generalCategory,new MOGuideEntryItem(MatterOverdriveItems.spacetime_equalizer).setGroup("items"),0,itemsY);
        addEntry(generalCategory,new MOGuideEntryItem(MatterOverdriveItems.security_protocol).setGroup("items"),1,itemsY);
        addEntry(generalCategory,new MOGuideEntry("upgrades").setStackIcons(MatterOverdriveItems.item_upgrade).setGroup("items"),2,itemsY);
        addEntry(generalCategory,new MOGuideEntry("drinks").setStackIcons(new ItemStack(MatterOverdriveItems.romulan_ale),new ItemStack(MatterOverdriveItems.earl_gray_tea)).setGroup("items"),3,itemsY);
        addEntry(generalCategory,new MOGuideEntry("food").setStackIcons(new ItemStack(MatterOverdriveItems.emergency_ration)).setGroup("items"),4,itemsY);
        itemsY++;
        addEntry(generalCategory,new MOGuideEntryItem(MatterOverdriveItems.wrench).setGroup("items"),0,itemsY);
        addEntry(generalCategory,new MOGuideEntryItem(MatterOverdriveItems.transportFlashDrive).setGroup("items"),1,itemsY);
        addEntry(generalCategory,new MOGuideEntryItem(MatterOverdriveItems.contract).setGroup("items"),2,itemsY);
        //region Weapons
        //Weapons
        addEntry(weaponsCategory,new MOGuideEntryItem(MatterOverdriveItems.phaser).setGroup("weapons"),4,0);
        addEntry(weaponsCategory,new MOGuideEntryItem(MatterOverdriveItems.phaserRifle).setGroup("weapons"),5,0);
        addEntry(weaponsCategory,new MOGuideEntryItem(MatterOverdriveItems.omniTool).setGroup("weapons"),6,0);
        addEntry(weaponsCategory,new MOGuideEntryItem(MatterOverdriveItems.plasmaShotgun).setGroup("weapons"),4,1);
        addEntry(weaponsCategory,new MOGuideEntryItem(MatterOverdriveItems.ionSniper).setGroup("weapons"),5,1);
        addEntry(weaponsCategory,new MOGuideEntry("tritanium_tools").setStackIcons(new ItemStack(MatterOverdriveItems.tritaniumAxe),new ItemStack(MatterOverdriveItems.tritaniumSword),new ItemStack(MatterOverdriveItems.tritaniumHoe),new ItemStack(MatterOverdriveItems.tritaniumPickaxe)).setGroup("weapons"),6,1);
        //Parts
        addEntry(weaponsCategory,new MOGuideEntryItem(MatterOverdriveItems.energyPack).setGroup("parts"),1,0);
        addEntry(weaponsCategory,new MOGuideEntry("weapon.modules.barrels").setStackIcons(MatterOverdriveItems.weapon_module_barrel).setGroup("parts"),2,0);
        addEntry(weaponsCategory,new MOGuideEntry("weapon.modules.colors").setStackIcons(MatterOverdriveItems.weapon_module_color).setGroup("parts"),1,1);
        addEntry(weaponsCategory,new MOGuideEntryItem(MatterOverdriveItems.sniperScope).setGroup("parts"),2,1);
        //Armor
        addEntry(weaponsCategory,new MOGuideEntry("tritanium_armor").setStackIcons(new ItemStack(MatterOverdriveItems.tritaniumChestplate),new ItemStack(MatterOverdriveItems.tritaniumLeggings),new ItemStack(MatterOverdriveItems.tritaniumBoots),new ItemStack(MatterOverdriveItems.tritaniumHelemet)).setGroup("armor"),1,3);
        //Machines
        addEntry(weaponsCategory,new MOGuideEntryBlock(MatterOverdriveBlocks.weapon_station).setGroup("machines"),4,3);
        //endregion

        //region Android
        //Items
        addEntry(androidCategory,new MOGuideEntry("android.pills").setStackIcons(MatterOverdriveItems.androidPill).setGroup("items"),5,1);
        addEntry(androidCategory,new MOGuideEntry("android.parts").setStackIcons(MatterOverdriveItems.androidParts).setGroup("items"),5,2);
        addEntry(androidCategory,new MOGuideEntryItem(MatterOverdriveItems.tritaniumSpine).setGroup("items"),5,3);
        //Machines
        addEntry(androidCategory,new MOGuideEntryBlock(MatterOverdriveBlocks.androidStation).setGroup("machines"),2,2);
        addEntry(androidCategory,new MOGuideEntryBlock(MatterOverdriveBlocks.chargingStation).setGroup("machines"),3,2);
        //endregion

    }

    private static void addEntry(GuideCategory category,MOGuideEntry entry,int x,int y)
    {
        int paddingTop = 16;
        int paddingLeft = 18;
        category.addEntry(entry);
        entry.setGuiPos(paddingLeft + x * 28, paddingTop + y * 28);
        entry.setId(MatterOverdriveGuide.getNextFreeID());
        MatterOverdriveGuide.registerEntry(entry);
    }
}
