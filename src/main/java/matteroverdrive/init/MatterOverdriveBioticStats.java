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

import matteroverdrive.api.android.BionicStatGuiInfo;
import matteroverdrive.api.android.IAndroidStatRegistry;
import matteroverdrive.data.biostats.*;
import matteroverdrive.handler.ConfigurationHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
	public static BioticStatAutoShield autoShield;
	public static BioticStatStepAssist stepAssist;
	public static BioticStatZeroCalories zeroCalories;
	public static BioticStatWirelessCharger wirelessCharger;
	public static BioticStatInertialDampers inertialDampers;
	public static BioticStatItemMagnet itemMagnet;
	public static BioticStatAirDash airDash;
	public static BioticstatOxygen oxygen;

	public static void init(FMLPreInitializationEvent event)
	{
		teleport = new BioticStatTeleport("teleport", 48);
		nanobots = new BiostatNanobots("nanobots", 26);
		nanoArmor = new BioticStatNanoArmor("nano_armor", 30);
		flotation = new BioticStatFlotation("floatation", 14);
		speed = new BioticStatSpeed("speed", 18);
		highJump = new BioticStatHighJump("high_jump", 36);
		highJump.addReqiredItm(new ItemStack(Blocks.piston));
		equalizer = new BioticStatEqualizer("equalizer", 24);
		equalizer.addReqiredItm(new ItemStack(MatterOverdriveItems.spacetime_equalizer));
		shield = new BioticStatShield("shield", 36);
		attack = new BioticStatAttack("attack", 30);
		cloak = new BioticStatCloak("cloak", 36);
		nightvision = new BioticStatNightVision("nightvision", 28);
		minimap = new BioticStatMinimap("minimap", 18);
		flashCooling = new BioticStatFlashCooling("flash_cooling", 28);
		shockwave = new BioticStatShockwave("shockwave", 32);
		autoShield = new BioticStatAutoShield("auto_shield", 26);
		stepAssist = new BioticStatStepAssist("step_assist", 24);
		zeroCalories = new BioticStatZeroCalories("zero_calories", 18);
		wirelessCharger = new BioticStatWirelessCharger("wireless_charger", 32);
		inertialDampers = new BioticStatInertialDampers("inertial_dampers", 18);
		itemMagnet = new BioticStatItemMagnet("item_magnet", 24);
		airDash = new BioticStatAirDash("air_dash", 28);
		oxygen = new BioticstatOxygen("oxygen", 12);
	}

	public static void configure(FMLInitializationEvent event)
	{
		teleport.addReqiredItm(new ItemStack(MatterOverdriveItems.h_compensator));
		teleport.addToEnabledBlacklist(shield);
		nanoArmor.setRoot(nanobots, false);
		nanoArmor.addCompetitor(attack);
		highJump.setRoot(speed, false);
		highJump.addToEnabledBlacklist(shield);
		inertialDampers.setRoot(highJump, false);
		equalizer.setRoot(inertialDampers, false);
		shield.setRoot(nanoArmor, true);
		shield.addReqiredItm(new ItemStack(MatterOverdriveItems.forceFieldEmitter, 1));
		attack.addCompetitor(nanoArmor);
		attack.setRoot(nanobots, false);
		cloak.setRoot(shield, true);
		minimap.addReqiredItm(new ItemStack(Items.compass));
		flashCooling.setRoot(attack, true);
		shockwave.setRoot(flashCooling, true);
		autoShield.setRoot(shield, true);
		oxygen.setRoot(zeroCalories, true);
		flotation.setRoot(oxygen, true);
		itemMagnet.setRoot(stepAssist, false);
		airDash.setRoot(highJump, true);

		int stepSizeX = 52;
		int stepSizeY = 30;

		wirelessCharger.setGuiInfo(new BionicStatGuiInfo(stepSizeX * -1, stepSizeY * 2));
		teleport.setGuiInfo(new BionicStatGuiInfo(0, stepSizeY * -2));

		zeroCalories.setGuiInfo(new BionicStatGuiInfo(stepSizeX, 0));
		oxygen.setGuiInfo(new BionicStatGuiInfo(stepSizeX, stepSizeY * 2, EnumFacing.UP, true));
		flotation.setGuiInfo(new BionicStatGuiInfo(stepSizeX, stepSizeY * 3, EnumFacing.UP));

		nightvision.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 2, stepSizeY * -2));

		nanobots.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 3, 0));

		nanoArmor.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 3, stepSizeY * 2, EnumFacing.UP, true));
		shield.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 3, stepSizeY * 3, EnumFacing.UP));
		cloak.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 3, stepSizeY * 4, EnumFacing.UP));
		autoShield.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 3 - 30, stepSizeY * 3, EnumFacing.EAST));

		attack.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 3, stepSizeY * -2, EnumFacing.DOWN, true));
		flashCooling.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 3, stepSizeY * -3, EnumFacing.DOWN));
		shockwave.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 3, stepSizeY * -4, EnumFacing.DOWN));

		minimap.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 4, stepSizeY * 2));

		speed.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 5, 0));
		highJump.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 5, stepSizeY * -2, EnumFacing.DOWN, true));
		airDash.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 5 + 30, stepSizeY * -2, EnumFacing.WEST, false));
		inertialDampers.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 5, stepSizeY * -3, EnumFacing.DOWN));
		equalizer.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 5, stepSizeY * -4, EnumFacing.DOWN));

		stepAssist.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 6, stepSizeY * 2));
		itemMagnet.setGuiInfo(new BionicStatGuiInfo(stepSizeX * 6, stepSizeY * 3, EnumFacing.UP));
	}

	public static void registerAll(ConfigurationHandler configurationHandler, IAndroidStatRegistry androidStatRegistry)
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
		androidStatRegistry.registerStat(autoShield);
		androidStatRegistry.registerStat(stepAssist);
		androidStatRegistry.registerStat(zeroCalories);
		androidStatRegistry.registerStat(wirelessCharger);
		androidStatRegistry.registerStat(inertialDampers);
		androidStatRegistry.registerStat(itemMagnet);
		androidStatRegistry.registerStat(airDash);
		androidStatRegistry.registerStat(oxygen);

		configurationHandler.subscribe(teleport);
		configurationHandler.subscribe(shield);
		configurationHandler.subscribe(nanobots);
		configurationHandler.subscribe(cloak);
		configurationHandler.subscribe(highJump);
		configurationHandler.subscribe(nightvision);
	}
}
