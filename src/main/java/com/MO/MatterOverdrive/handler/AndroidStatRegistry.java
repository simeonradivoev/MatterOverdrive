package com.MO.MatterOverdrive.handler;

import com.MO.MatterOverdrive.api.inventory.IBionicStat;
import com.MO.MatterOverdrive.data.biostats.*;
import com.MO.MatterOverdrive.init.MatterOverdriveItems;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

import java.util.HashMap;

/**
 * Created by Simeon on 5/27/2015.
 */
public class AndroidStatRegistry
{
    public static HashMap<String,IBionicStat> stats = new HashMap<String, IBionicStat>();

    public static BioticStatTeleport teleport;
    public static BiostatNanobots nanobots;
    public static BioticStatNanoArmor nanoArmor;
    public static BioticStatFlotation flotation;
    public static BioticStatSpeed speed;
    public static BioticStatHighJump highJump;
    public static BioticStatEqualizer equalizer;
    public static BioticStatShield shield;
    public static BioticStatAttack attack;

    public static boolean registerStat(IBionicStat stat)
    {
        if (stats.containsKey(stat.getUnlocalizedName()))
        {
            return false;
        }
        else
        {
            stats.put(stat.getUnlocalizedName(),stat);
            return true;
        }
    }

    public static IBionicStat getStat(String name)
    {
        return stats.get(name);
    }

    public static boolean hasStat(String name)
    {
        return stats.containsKey(name);
    }

    public static void unregisterStat(IBionicStat stat)
    {
        stats.remove(stat.getUnlocalizedName());
    }

    public static void init()
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

        teleport.addReqiredItm(new ItemStack(MatterOverdriveItems.h_compensator));
        teleport.addToEnabledBlacklist(shield);
        nanoArmor.setRoot(nanobots);
        nanoArmor.addCompetitor(attack);
        highJump.setRoot(speed);
        highJump.addToEnabledBlacklist(shield);
        equalizer.setRoot(highJump);
        shield.setRoot(nanoArmor);
        shield.addReqiredItm(new ItemStack(MatterOverdriveItems.forceFieldEmitter,2));
        attack.addCompetitor(nanoArmor);
        attack.setRoot(nanobots);
    }

    public static void registerAll()
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
    }

    public static void registerIcons(TextureMap holoIcons)
    {
        for (IBionicStat stat : stats.values())
        {
            stat.registerIcons(holoIcons);
        }
    }
}
