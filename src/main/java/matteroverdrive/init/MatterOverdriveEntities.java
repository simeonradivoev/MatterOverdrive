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

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.entity.*;
import matteroverdrive.entity.weapon.PlasmaBolt;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.handler.village.TradeHandlerMadScientist;
import matteroverdrive.handler.village.VillageCreatationMadScientist;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 5/26/2015.
 */
public class MatterOverdriveEntities
{
    public static EntityRogueAndroid rogueandroid;

    public static void init(FMLPreInitializationEvent event,ConfigurationHandler configurationHandler)
    {
        rogueandroid = new EntityRogueAndroid();
        configurationHandler.subscribe(rogueandroid);
    }

    public static void register(FMLPostInitializationEvent event)
    {
        MatterOverdrive.configHandler.config.load();

        addEntity(EntityFailedPig.class, "failed_pig", 15771042, 0x33CC33);
        addEntity(EntityFailedCow.class,"failed_cow",4470310,0x33CC33);
        addEntity(EntityFailedChicken.class,"failed_chicken",10592673,0x33CC33);
        addEntity(EntityFailedSheep.class, "failed_sheep", 15198183, 0x33CC33);
        addEntity(EntityVillagerMadScientist.class, "mad_scientist", 0xFFFFFF, 0);
        //addViligger(666,"mad_scientist.png",new TradeHandlerMadScientist());

        VillagerRegistry.instance().registerVillageTradeHandler(666, new TradeHandlerMadScientist());
        VillageCreatationMadScientist creatationMadScientist = new VillageCreatationMadScientist();
        VillagerRegistry.instance().registerVillageCreationHandler(creatationMadScientist);
        rogueandroid.registerEntity();

        int phaserFireID = registerEntityGlobalIDSafe(PlasmaBolt.class,"phaser_fire");
        EntityRegistry.registerGlobalEntityID(PlasmaBolt.class, "phaser_fire", phaserFireID);
        //EntityRegistry.registerModEntity(PlasmaBolt.class,"phaser_fire",phaserFireID,MatterOverdrive.instance,64,1,true);
        //EntityRegistry.EntityRegistration entityRegistration = EntityRegistry.instance().lookupModSpawn(PlasmaBolt.class,false);
        //entityRegistration.setCustomSpawning(null,true);
        MatterOverdrive.configHandler.save();
    }

    public static void addViligger(int id,String texture,VillagerRegistry.IVillageTradeHandler tradeHandler)
    {
        VillagerRegistry.instance().registerVillagerId(id);
        VillagerRegistry.instance().registerVillagerSkin(id, new ResourceLocation(Reference.PATH_ENTITIES + texture));

    }

    public static int addEntity(Class<? extends Entity> enityClass,String name,int mainColor,int spotsColor)
    {
        int id = registerEntityGlobalIDSafe(enityClass,name);
        EntityRegistry.registerModEntity(enityClass, name, id, MatterOverdrive.instance, 64, 1, true);
        createEgg(id, mainColor, spotsColor);
        return id;
    }

    public static void createEgg(int id,int solidColor,int spotColor)
    {
        EntityList.entityEggs.put(Integer.valueOf(id), new EntityList.EntityEggInfo(id, solidColor, spotColor));
    }

    public static int registerEntityGlobalIDSafe(Class<? extends Entity> entityClass,String name)
    {
        int id = EntityRegistry.findGlobalUniqueEntityId();
        if(MatterOverdrive.configHandler.config.hasKey(ConfigurationHandler.CATEGORY_ENTITIES,getEntityConfigKey(name)))
        {
            id = MatterOverdrive.configHandler.getInt(getEntityConfigKey(name),ConfigurationHandler.CATEGORY_ENTITIES,171);
        }

        while (id < 256)
        {
            try
            {
                EntityRegistry.registerGlobalEntityID(entityClass,name,id);
                break;
            }catch (Exception e)
            {
                id++;
                if (id == 256)
                {
                    throw new RuntimeException("Could not find a free Entity ID for: " + entityClass);
                }
            }

        }
        MatterOverdrive.configHandler.setInt(getEntityConfigKey(name), ConfigurationHandler.CATEGORY_ENTITIES, id);
        return id;
    }

    private static String getEntityConfigKey(String name)
    {
        return "entity." + name + ".id";
    }
}
