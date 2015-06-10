package com.MO.MatterOverdrive.entity;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.handler.MOConfigurationHandler;
import com.MO.MatterOverdrive.util.IConfigSubscriber;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 5/26/2015.
 */
public class EntityRougeAndroid implements IConfigSubscriber
{
    private static BiomeGenBase.SpawnListEntry spawnListEntry;

    public static void registerEntity()
    {
        createEntity(EntityRougeAndroidMob.class,"rouge_android",0xFFFFF,0);
    }

    public static void  createEntity(Class<? extends EntityLiving> entityClass,String name,int solidColor,int spotColor)
    {
        int randomID = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(entityClass,name,randomID);
        EntityRegistry.registerModEntity(entityClass,name,randomID, MatterOverdrive.instance,64,1,true);
        spawnListEntry = new BiomeGenBase.SpawnListEntry(entityClass,3,1,2);
        for (int i = 0;i < BiomeGenBase.getBiomeGenArray().length;i++)
        {
            if (BiomeGenBase.getBiomeGenArray()[i] != null) {
                BiomeGenBase.getBiomeGenArray()[i].getSpawnableList(EnumCreatureType.monster).add(spawnListEntry);
            }
        }
        createEgg(randomID,solidColor,spotColor);
    }

    public static void createEgg(int id,int solidColor,int spotColor)
    {
        EntityList.entityEggs.put(Integer.valueOf(id),new EntityList.EntityEggInfo(id,solidColor,spotColor));
    }

    @Override
    public void onConfigChanged(MOConfigurationHandler config)
    {
        if (spawnListEntry != null)
        {
            spawnListEntry.itemWeight = config.config.getInt("rouge_android.spawn.chance",MOConfigurationHandler.CATEGORY_WORLD_GEN,3,0,100,"The spawn change of the Rouge Android");
        }
    }
}
