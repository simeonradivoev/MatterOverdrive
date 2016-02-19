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
import matteroverdrive.entity.*;
import matteroverdrive.entity.monster.EntityMeleeRougeAndroidMob;
import matteroverdrive.entity.monster.EntityMutantScientist;
import matteroverdrive.entity.monster.EntityRangedRogueAndroidMob;
import matteroverdrive.entity.monster.EntityRogueAndroid;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.handler.village.VillageCreatationMadScientist;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

/**
 * Created by Simeon on 5/26/2015.
 */
public class MatterOverdriveEntities
{
    public static final int ENTITY_STARTING_ID = 171;
    public static EntityRogueAndroid rogueandroid;

    public static void init(FMLPreInitializationEvent event,ConfigurationHandler configurationHandler)
    {
        rogueandroid = new EntityRogueAndroid();
        configurationHandler.subscribe(rogueandroid);
    }

    public static void register(FMLPostInitializationEvent event)
    {
        MatterOverdrive.configHandler.config.load();
        int id = 0;
        addEntity(EntityFailedPig.class, "failed_pig", 15771042, 0x33CC33,id++);
        addEntity(EntityFailedCow.class,"failed_cow",4470310,0x33CC33,id++);
        addEntity(EntityFailedChicken.class,"failed_chicken",10592673,0x33CC33,id++);
        addEntity(EntityFailedSheep.class, "failed_sheep", 15198183, 0x33CC33,id++);
        addEntity(EntityVillagerMadScientist.class, "mad_scientist", 0xFFFFFF, 0,id++);
        addEntity(EntityMutantScientist.class,"mutant_scientist",0xFFFFFF,0x00FF00,id++);
        addEntity(EntityMeleeRougeAndroidMob.class, "rogue_android", 0xFFFFF, 0,id++);
        addEntity(EntityRangedRogueAndroidMob.class,"ranged_rogue_android",0xFFFFF, 0,id++);
        addEntity(EntityDrone.class,"drone",0x3e5154, 0xbaa1c4,id++);

        //VillagerRegistry.instance().registerVillageTradeHandler(666, new TradeHandlerMadScientist());
        VillageCreatationMadScientist creatationMadScientist = new VillageCreatationMadScientist();
        VillagerRegistry.instance().registerVillageCreationHandler(creatationMadScientist);
        EntityRogueAndroid.addAsBiomeGen(EntityMeleeRougeAndroidMob.class);
        EntityRogueAndroid.addAsBiomeGen(EntityRangedRogueAndroidMob.class);

        //int phaserFireID = loadIDFromConfig(PlasmaBolt.class,"phaser_fire",170);
        //EntityRegistry.registerGlobalEntityID(PlasmaBolt.class, "phaser_fire", phaserFireID);
        MatterOverdrive.configHandler.save();
    }

    public static int addEntity(Class<? extends Entity> enityClass,String name,int mainColor,int spotsColor,int id)
    {
        //id = loadIDFromConfig(enityClass,name,id);
        //EntityRegistry.registerGlobalEntityID(enityClass,name,id);
        EntityRegistry.registerModEntity(enityClass, name, id, MatterOverdrive.instance, 64, 1, true);
        EntityRegistry.registerEgg(enityClass,mainColor,spotsColor);
        return id;
    }

    public static int loadIDFromConfig(Class<? extends Entity> entityClass,String name,int id)
    {
        return MatterOverdrive.configHandler.getInt(getEntityConfigKey(name),ConfigurationHandler.CATEGORY_ENTITIES,id);
    }

    private static String getEntityConfigKey(String name)
    {
        return "entity." + name + ".id";
    }
}
