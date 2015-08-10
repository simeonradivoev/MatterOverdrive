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

package matteroverdrive.handler;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.android.IAndroidStatRegistry;
import matteroverdrive.api.android.IBionicStat;
import matteroverdrive.api.events.MOEventRegisterAndroidStat;
import matteroverdrive.data.biostats.*;
import matteroverdrive.init.MatterOverdriveItems;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Simeon on 5/27/2015.
 */
public class AndroidStatRegistry implements IAndroidStatRegistry
{
    private HashMap<String,IBionicStat> stats = new HashMap<>();

    public BioticStatTeleport teleport;
    public BiostatNanobots nanobots;
    public BioticStatNanoArmor nanoArmor;
    public BioticStatFlotation flotation;
    public BioticStatSpeed speed;
    public BioticStatHighJump highJump;
    public BioticStatEqualizer equalizer;
    public BioticStatShield shield;
    public BioticStatAttack attack;
    public BioticStatCloak cloak;
    public BioticStatNightvision nightvision;

    @Override
    public boolean registerStat(IBionicStat stat)
    {
        if (stats.containsKey(stat.getUnlocalizedName()))
        {
            MatterOverdrive.log.warn("Stat with the name '%s' is already present!", stat.getUnlocalizedName());
        }
        else
        {
            if (!MinecraftForge.EVENT_BUS.post(new MOEventRegisterAndroidStat(stat))) {
                stats.put(stat.getUnlocalizedName(), stat);
                return true;
            }
        }
        return false;
    }

    @Override
    public IBionicStat getStat(String name)
    {
        return stats.get(name);
    }

    @Override
    public boolean hasStat(String name)
    {
        return stats.containsKey(name);
    }

    @Override
    public IBionicStat unregisterStat(String statName)
    {
        return stats.remove(statName);
    }

    public void init()
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
        nightvision = new BioticStatNightvision("nightvision",28);

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
    }

    public void registerAll(ConfigurationHandler configurationHandler)
    {
        registerStat(teleport);
        registerStat(nanobots);
        registerStat(nanoArmor);
        registerStat(flotation);
        registerStat(speed);
        registerStat(highJump);
        registerStat(equalizer);
        registerStat(shield);
        registerStat(attack);
        registerStat(cloak);
        registerStat(nightvision);

        configurationHandler.subscribe(teleport);
        configurationHandler.subscribe(shield);
        configurationHandler.subscribe(nanobots);
        configurationHandler.subscribe(cloak);
        configurationHandler.subscribe(highJump);
        configurationHandler.subscribe(nightvision);
    }

    public void registerIcons(TextureMap holoIcons)
    {
        for (IBionicStat stat : stats.values())
        {
            stat.registerIcons(holoIcons);
        }
    }

    public Collection<IBionicStat> getStats()
    {
        return stats.values();
    }
}
