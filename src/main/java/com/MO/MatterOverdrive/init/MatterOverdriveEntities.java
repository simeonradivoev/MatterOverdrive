package com.MO.MatterOverdrive.init;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.entity.*;
import com.MO.MatterOverdrive.handler.ConfigurationHandler;
import com.MO.MatterOverdrive.handler.village.VillageCreatationMadScientist;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
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

    public static void register(FMLPreInitializationEvent event)
    {
        addEntity(EntityFailedPig.class,"failed_pig",15771042,0x33CC33);
        addEntity(EntityFailedCow.class,"failed_cow",4470310,0x33CC33);
        addEntity(EntityFailedChicken.class,"failed_chicken",10592673,0x33CC33);
        addEntity(EntityFailedSheep.class,"failed_sheep",15198183,0x33CC33);
        addEntity(EntityVillagerMadScientist.class,"mad_scientist",0xFFFFFF,0);
        //addViligger(666,"mad_scientist.png",new TradeHandlerMadScientist());
        VillageCreatationMadScientist creatationMadScientist = new VillageCreatationMadScientist();
        VillagerRegistry.instance().registerVillageCreationHandler(creatationMadScientist);
        rogueandroid.registerEntity();
    }

    public static void addViligger(int id,String texture,VillagerRegistry.IVillageTradeHandler tradeHandler)
    {
        VillagerRegistry.instance().registerVillagerId(id);
        VillagerRegistry.instance().registerVillagerSkin(id,new ResourceLocation(Reference.PATH_ENTITIES + texture));
        VillagerRegistry.instance().registerVillageTradeHandler(id,tradeHandler);
    }

    public static int addEntity(Class<? extends Entity> enityClass,String name,int mainColor,int spotsColor)
    {
        int entityID = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(enityClass,name,entityID);
        EntityRegistry.registerModEntity(enityClass,name,entityID, MatterOverdrive.instance,64,1,true);
        createEgg(entityID,mainColor,spotsColor);
        return entityID;
    }

    public static void createEgg(int id,int solidColor,int spotColor)
    {
        EntityList.entityEggs.put(Integer.valueOf(id),new EntityList.EntityEggInfo(id,solidColor,spotColor));
    }
}
