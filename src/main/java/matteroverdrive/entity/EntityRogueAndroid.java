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

package matteroverdrive.entity;

import cpw.mods.fml.common.registry.EntityRegistry;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.init.MatterOverdriveEntities;
import matteroverdrive.util.IConfigSubscriber;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.HashSet;
import java.util.List;

/**
 * Created by Simeon on 5/26/2015.
 */
public class EntityRogueAndroid implements IConfigSubscriber
{
    private static HashSet<String> biomesBlacklist = new HashSet();
    private static BiomeGenBase.SpawnListEntry spawnListEntry;

    public static void registerEntity()
    {
        createEntity(EntityRougeAndroidMob.class, "rogue_android", 0xFFFFF, 0);
    }

    public static void  createEntity(Class<? extends EntityLiving> entityClass,String name,int solidColor,int spotColor)
    {
        int randomID = MatterOverdriveEntities.registerEntityGlobalIDSafe(entityClass,name);
        EntityRegistry.registerGlobalEntityID(entityClass, name, randomID);
        EntityRegistry.registerModEntity(entityClass, name, randomID, MatterOverdrive.instance, 64, 1, true);
        spawnListEntry = new BiomeGenBase.SpawnListEntry(entityClass,5,1,2);
        addInBiome(BiomeGenBase.getBiomeGenArray());
        createEgg(randomID, solidColor, spotColor);
    }

    private static void addInBiome(BiomeGenBase[] biomes)
    {
        loadBlacklist(MatterOverdrive.configHandler);

        for (int i = 0;i < biomes.length;i++)
        {
            if (biomes[i] != null)
            {
                List spawnList = biomes[i].getSpawnableList(EnumCreatureType.monster);
                if (isBiomeValid(biomes[i]) && !spawnList.contains(spawnList)) {
                    spawnList.add(spawnListEntry);
                }
            }
        }
    }

    private static boolean isBiomeValid(BiomeGenBase biome)
    {
        return biome != null && !biomesBlacklist.contains(biome.biomeName.toLowerCase());
    }

    public static void createEgg(int id,int solidColor,int spotColor)
    {
        EntityList.entityEggs.put(Integer.valueOf(id),new EntityList.EntityEggInfo(id,solidColor,spotColor));
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config)
    {
        if (spawnListEntry != null)
        {
            spawnListEntry.itemWeight = config.config.getInt("rogue_android.spawn.chance", ConfigurationHandler.CATEGORY_WORLD_GEN,5,0,100,"The spawn change of the Rogue Android");
            loadBlacklist(config);
        }
    }

    private static void loadBlacklist(ConfigurationHandler config)
    {
        biomesBlacklist.clear();
        String[] blacklist = config.config.getStringList("rouge.biome.blacklist", ConfigurationHandler.CATEGORY_WORLD_GEN,new String[]{"Hell","Sky"},"The blacklist for Android spawn biomes");
        for (int i = 0;i < blacklist.length;i++)
        {
            biomesBlacklist.add(blacklist[i].toLowerCase());
        }
    }
}
