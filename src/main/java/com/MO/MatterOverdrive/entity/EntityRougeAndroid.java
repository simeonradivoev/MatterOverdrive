package com.MO.MatterOverdrive.entity;

import com.MO.MatterOverdrive.MatterOverdrive;
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
public class EntityRougeAndroid
{
    public static void registerEntity()
    {
        createEntity(EntityRougeAndroidMob.class,"rouge_android",0xFFFFF,0);
    }

    public static void  createEntity(Class<? extends EntityLiving> entityClass,String name,int solidColor,int spotColor)
    {
        int randomID = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(entityClass,name,randomID);
        EntityRegistry.registerModEntity(entityClass,name,randomID, MatterOverdrive.instance,64,1,true);
        List<BiomeGenBase> biomeGenBaseList = new ArrayList<BiomeGenBase>();
        for (int i = 0;i < BiomeGenBase.getBiomeGenArray().length;i++)
        {
            if (BiomeGenBase.getBiomeGenArray()[i] != null) {
                biomeGenBaseList.add(BiomeGenBase.getBiomeGenArray()[i]);
            }
        }
        EntityRegistry.addSpawn(entityClass,10,1,3, EnumCreatureType.monster, biomeGenBaseList.toArray(new BiomeGenBase[]{}));

        createEgg(randomID,solidColor,spotColor);
    }

    public static void createEgg(int id,int solidColor,int spotColor)
    {
        EntityList.entityEggs.put(Integer.valueOf(id),new EntityList.EntityEggInfo(id,solidColor,spotColor));
    }
}
