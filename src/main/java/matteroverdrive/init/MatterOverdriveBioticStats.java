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
import matteroverdrive.api.android.IAndroidStatRegistry;
import matteroverdrive.data.biostats.*;
import matteroverdrive.handler.ConfigurationHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 9/12/2015.
 */
public class MatterOverdriveBioticStats
{
    public static BioticStatTeleport teleport;
    public static BiostatNanobots nanobots;
    public static BioticStatNanoArmor nanoArmor;
    public static BioticStatFlotation flotation;
    public static BioticStatSpeed speed;
    public static BioticStatHighJump highJump;
    public static BioticStatEqualizer equalizer;
    public static BioticStatShield shield;
    public static BioticStatAttack attack;
    public static BioticStatCloak cloak;
    public static BioticStatNightVision nightvision;
    public static BioticStatMinimap minimap;
    public static BioticStatFlashCooling flashCooling;
    public static BioticStatShockwave shockwave;

    public static void init(FMLPreInitializationEvent event)
    {
        teleport = new BioticStatTeleport("teleport",48);
        nanobots = new BiostatNanobots("nanobots",26);
        nanoArmor = new BioticStatNanoArmor("nano_armor",30);
        flotation = new BioticStatFlotation("floatation",14);
        speed = new BioticStatSpeed("speed",18);
        highJump = new BioticStatHighJump("high_jump",36);
        highJump.addReqiredItm(new ItemStack(Blocks.piston));
        equalizer = new BioticStatEqualizer("equalizer",24);
        equalizer.addReqiredItm(new ItemStack(MatterOverdriveItems.spacetime_equalizer));
        shield = new BioticStatShield("shield",36);
        attack = new BioticStatAttack("attack",30);
        cloak = new BioticStatCloak("cloak",36);
        nightvision = new BioticStatNightVision("nightvision",28);
        minimap = new BioticStatMinimap("minimap",18);
        flashCooling = new BioticStatFlashCooling("flash_cooling",28);
        shockwave = new BioticStatShockwave("shockwave",32);
    }

    public static void register(FMLInitializationEvent event)
    {
        teleport.addReqiredItm(new ItemStack(MatterOverdriveItems.h_compensator));
        teleport.addToEnabledBlacklist(shield);
        nanoArmor.setRoot(nanobots);
        nanoArmor.addCompetitor(attack);
        highJump.setRoot(speed);
        highJump.addToEnabledBlacklist(shield);
        equalizer.setRoot(highJump);
        shield.setRoot(nanoArmor);
        shield.addReqiredItm(new ItemStack(MatterOverdriveItems.forceFieldEmitter, 2));
        attack.addCompetitor(nanoArmor);
        attack.setRoot(nanobots);
        cloak.setRoot(shield);
        minimap.addReqiredItm(new ItemStack(Items.compass));
        flashCooling.setRoot(attack);
        shockwave.setRoot(flashCooling);
    }

    public static void registerAll(ConfigurationHandler configurationHandler,IAndroidStatRegistry androidStatRegistry)
    {
        androidStatRegistry.registerStat(teleport);
        androidStatRegistry.registerStat(nanobots);
        androidStatRegistry.registerStat(nanoArmor);
        androidStatRegistry.registerStat(flotation);
        androidStatRegistry.registerStat(speed);
        androidStatRegistry.registerStat(highJump);
        androidStatRegistry.registerStat(equalizer);
        androidStatRegistry.registerStat(shield);
        androidStatRegistry.registerStat(attack);
        androidStatRegistry.registerStat(cloak);
        androidStatRegistry.registerStat(nightvision);
        androidStatRegistry.registerStat(minimap);
        androidStatRegistry.registerStat(flashCooling);
        androidStatRegistry.registerStat(shockwave);

        configurationHandler.subscribe(teleport);
        configurationHandler.subscribe(shield);
        configurationHandler.subscribe(nanobots);
        configurationHandler.subscribe(cloak);
        configurationHandler.subscribe(highJump);
        configurationHandler.subscribe(nightvision);
    }
}
