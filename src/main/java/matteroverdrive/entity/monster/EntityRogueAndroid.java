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

package matteroverdrive.entity.monster;

import cpw.mods.fml.common.registry.EntityRegistry;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.init.MatterOverdriveEntities;
import matteroverdrive.util.IConfigSubscriber;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.config.Property;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Simeon on 5/26/2015.
 */
public class EntityRogueAndroid implements IConfigSubscriber
{
    private static HashSet<String> biomesBlacklist = new HashSet<>();
    private static HashSet<String> biomesWhitelist = new HashSet<>();
    public static HashSet<Integer> dimensionBlacklist = new HashSet<>();
    public static HashSet<Integer> dimensionWhitelist = new HashSet<>();
    private static List<BiomeGenBase.SpawnListEntry> spawnListEntries = new ArrayList<>();

    public static void registerEntity()
    {
        createEntity(EntityMeleeRougeAndroidMob.class, "rogue_android", 0xFFFFF, 0,177);
        createEntity(EntityRangedRogueAndroidMob.class,"ranged_rogue_android",0xFFFFF, 0,178);
    }

    public static void createEntity(Class<? extends EntityLiving> entityClass, String name, int solidColor, int spotColor,int id)
    {
        int randomID = MatterOverdriveEntities.loadIDFromConfig(entityClass, name,id);
        EntityRegistry.registerGlobalEntityID(entityClass, name, randomID);
        EntityRegistry.registerModEntity(entityClass, name, randomID, MatterOverdrive.instance, 64, 1, true);
        spawnListEntries.add(new BiomeGenBase.SpawnListEntry(entityClass, 5, 1, 2));
        addInBiome(BiomeGenBase.getBiomeGenArray());
        createEgg(randomID, solidColor, spotColor);
    }

    private static void addInBiome(BiomeGenBase[] biomes)
    {
        loadBiomeBlacklist(MatterOverdrive.configHandler);
        loadBiomesWhitelist(MatterOverdrive.configHandler);

        for (int i = 0;i < biomes.length;i++)
        {
            if (biomes[i] != null)
            {
                List spawnList = biomes[i].getSpawnableList(EnumCreatureType.monster);
                for (BiomeGenBase.SpawnListEntry entry : spawnListEntries)
                {
                    if (isBiomeValid(biomes[i]) && !spawnList.contains(entry) && entry.itemWeight > 0) {
                        spawnList.add(entry);
                    }
                }

            }
        }
    }

    private static boolean isBiomeValid(BiomeGenBase biome)
    {
        if (biome != null) {
            if (biomesWhitelist.size() > 0) {
                return biomesWhitelist.contains(biome);
            } else {
                return !biomesBlacklist.contains(biome.biomeName.toLowerCase());
            }
        }
        return false;
    }

    public static void createEgg(int id, int solidColor, int spotColor)
    {
        EntityList.entityEggs.put(id, new EntityList.EntityEggInfo(id, solidColor, spotColor));
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config)
    {
        for (BiomeGenBase.SpawnListEntry entry : spawnListEntries)
        {
            entry.itemWeight = config.config.getInt("spawn_chance", ConfigurationHandler.CATEGORY_ENTITIES + ".rogue_android", 5, 0, 100, "The spawn change of the Rogue Android");
        }

        loadDimensionBlacklist(config);
        loadDimesionWhitelist(config);
        loadBiomeBlacklist(config);
        loadBiomesWhitelist(config);

        EntityRangedRogueAndroidMob.UNLIMITED_WEAPON_ENERGY = config.getBool(ConfigurationHandler.CATEGORY_ENTITIES + "rogue_android","unlimited_weapon_energy",true,"Do Ranged Rogue Androids have unlimited weapon energy in their weapons");
    }

    private static void loadBiomeBlacklist(ConfigurationHandler config)
    {
        biomesBlacklist.clear();
        String[] blacklist = config.config.getStringList("biome.blacklist", ConfigurationHandler.CATEGORY_ENTITIES + ".rogue_android", new String[]{"Hell","Sky","MushroomIsland","MushroomIslandShore"}, "Rogue Android biome blacklist");
        for (int i = 0;i < blacklist.length;i++)
        {
            biomesBlacklist.add(blacklist[i].toLowerCase());
        }
    }

    private static void loadBiomesWhitelist(ConfigurationHandler configurationHandler)
    {
        biomesWhitelist.clear();
        String[] whitelist = configurationHandler.config.getStringList("biome.whitelist",ConfigurationHandler.CATEGORY_ENTITIES + "." + "rogue_android",new String[0],"Rogue Android biome whitelist");
        for (int i = 0;i < whitelist.length;i++)
        {
            biomesBlacklist.add(whitelist[i].toLowerCase());
        }
    }

    private static void loadDimensionBlacklist(ConfigurationHandler configurationHandler)
    {
        dimensionBlacklist.clear();
        Property blacklistProp = configurationHandler.config.get(ConfigurationHandler.CATEGORY_ENTITIES + "." + "rogue_android","dimension.blacklist",new int[]{1});
        blacklistProp.comment = "Rogue Android Dimension ID blacklist";
        int[] blacklist = blacklistProp.getIntList();
        for (int i = 0;i < blacklist.length;i++)
        {
            dimensionBlacklist.add(blacklist[i]);
        }
    }

    private static void loadDimesionWhitelist(ConfigurationHandler configurationHandler)
    {
        dimensionWhitelist.clear();
        Property whitelistProp = configurationHandler.config.get(ConfigurationHandler.CATEGORY_ENTITIES + ".rogue_android", "dimension.whitelist", new int[0]);
        whitelistProp.comment = "Rogue Android Dimension ID whitelist";
        int[] whitelist = whitelistProp.getIntList();

        for (int item : whitelist)
        {
            dimensionWhitelist.add(item);
        }
    }
}
