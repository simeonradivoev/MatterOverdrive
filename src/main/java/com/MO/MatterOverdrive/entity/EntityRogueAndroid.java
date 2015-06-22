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
public class EntityRogueAndroid implements IConfigSubscriber
{
    private static BiomeGenBase.SpawnListEntry spawnListEntry;

    public static void registerEntity()
    {
        createEntity(EntityRougeAndroidMob.class,"rogue_android",0xFFFFF,0);
    }

    public static void  createEntity(Class<? extends EntityLiving> entityClass,String name,int solidColor,int spotColor)
    {
        int randomID = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(entityClass,name,randomID);
        EntityRegistry.registerModEntity(entityClass,name,randomID, MatterOverdrive.instance,64,1,true);
        spawnListEntry = new BiomeGenBase.SpawnListEntry(entityClass,3,1,2);
        addInBiome(BiomeGenBase.beach,
                BiomeGenBase.birchForest,
                BiomeGenBase.birchForestHills,
                BiomeGenBase.desert,
                BiomeGenBase.desertHills,
                BiomeGenBase.forest,
                BiomeGenBase.forestHills,
                BiomeGenBase.extremeHills,
                BiomeGenBase.taiga,
                BiomeGenBase.taigaHills,
                BiomeGenBase.megaTaiga,
                BiomeGenBase.megaTaigaHills);
        createEgg(randomID, solidColor, spotColor);
    }

    private static void addInBiome(BiomeGenBase... biomes)
    {
        for (int i = 0;i < biomes.length;i++)
        {
            if (biomes[i] != null)
            {
                biomes[i].getSpawnableList(EnumCreatureType.monster).add(spawnListEntry);
            }
        }
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
            spawnListEntry.itemWeight = config.config.getInt("rogue_android.spawn.chance",MOConfigurationHandler.CATEGORY_WORLD_GEN,3,0,100,"The spawn change of the Rogue Android");
        }
    }
}
