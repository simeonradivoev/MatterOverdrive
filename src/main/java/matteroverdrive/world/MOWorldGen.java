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

package matteroverdrive.world;

import cpw.mods.fml.common.IWorldGenerator;
import matteroverdrive.Reference;
import matteroverdrive.data.world.GenPositionWorldData;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.init.MatterOverdriveBlocks;
import matteroverdrive.util.IConfigSubscriber;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.config.Property;

import java.util.HashSet;
import java.util.Random;

/**
 * Created by Simeon on 3/23/2015.
 */
public class MOWorldGen implements IWorldGenerator, IConfigSubscriber
{
    public static WorldGenMinable dilithiumGen;
    public static WorldGenMinable tritaniumGen;
    public static MOAndroidHouseBuilding androidHouse;
    public static WorldGenGravitationalAnomaly anomalyGen;
    public static MOSandPit sandPit;
    public static final int TRITANIUM_VEINS_PER_CHUNK = 10;
    public static final int TRITANIUM_VEIN_SIZE = 6;
    public static final int DILITHIUM_VEINS_PER_CHUNK = 6;
    public static final int DILITHIUM_VEIN_SIZE = 5;
    HashSet<Integer> oreDimentionsBlacklist;

    boolean generateTritanium;
    boolean generateDilithium;
    boolean generateAnomalies;

    public MOWorldGen(ConfigurationHandler configurationHandler)
    {
        tritaniumGen = new WorldGenMinable(MatterOverdriveBlocks.tritaniumOre,TRITANIUM_VEIN_SIZE);
        dilithiumGen = new WorldGenMinable(MatterOverdriveBlocks.dilithium_ore,DILITHIUM_VEIN_SIZE);
        androidHouse = new MOAndroidHouseBuilding("android_house",0.025);
        sandPit = new MOSandPit("sand_pit_house",0.05,3);
        anomalyGen = new WorldGenGravitationalAnomaly("gravitational_anomaly",0.005f,2048,2048 + 8192);
        oreDimentionsBlacklist = new HashSet<>();

        configurationHandler.subscribe(anomalyGen);
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        switch (world.provider.dimensionId)
        {
            case -1:
                generateNether(world,random,chunkX * 16,chunkZ * 16);
                break;
            case 0:
                generateOverworld(world, random, chunkX * 16, chunkZ * 16);
                break;
            case 2:
                generateEnd(world, random, chunkX * 16, chunkZ * 16);
                break;
            default:
                generateOther(world,random,chunkX * 16,chunkZ * 16);
        }

        generateGravitationalAnomalies(world, random, chunkX * 16, chunkZ * 16, world.provider.dimensionId);
        generateOres(world, random, chunkX * 16, chunkZ * 16, world.provider.dimensionId);
        androidHouse.generate(random,chunkX,chunkZ,world,chunkGenerator,chunkProvider);
        sandPit.generate(random,chunkX,chunkZ,world,chunkGenerator,chunkProvider);
    }

    public void generateOverworld(World world,Random random,int chunkX, int chunkZ)
    {

    }
    public void generateNether(World world,Random random,int chunkX, int chunkZ)
    {

    }
    public void generateEnd(World world,Random random,int chunkX, int chunkZ)
    {

    }
    public void generateOther(World world,Random random,int chunkX, int chunkZ)
    {

    }

    public void generateOres(World world,Random random,int chunkX,int chunkZ,int dimentionID)
    {
        if (!oreDimentionsBlacklist.contains(dimentionID))
        {
            if (generateDilithium) {
                for (int i = 0; i < DILITHIUM_VEINS_PER_CHUNK; i++) {
                    int x = chunkX + random.nextInt(16);
                    int z = chunkZ + random.nextInt(16);
                    int y = random.nextInt(28) + 4;

                    if (dilithiumGen.generate(world, random, x, y, z)) {

                    }
                }
            }

            if (generateTritanium) {
                for (int i = 0; i < TRITANIUM_VEINS_PER_CHUNK; i++) {
                    int x = chunkX + random.nextInt(16);
                    int z = chunkZ + random.nextInt(16);
                    int y = random.nextInt(60) + 4;

                    if (tritaniumGen.generate(world, random, x, y, z)) {

                    }
                }
            }
        }
    }
    private void generateGravitationalAnomalies(World world,Random random,int chunkX, int chunkZ,int dimention)
    {
        if (generateAnomalies)
        {
            int x = chunkX + random.nextInt(16);
            int z = chunkZ + random.nextInt(16);
            int y = random.nextInt(60) + 4;

            if (anomalyGen.generate(world, random, x, y, z)) {

            }
        }
    }
    private boolean shouldGenerate(Block block,ConfigurationHandler config)
    {
        Property p = config.config.get(ConfigurationHandler.CATEGORY_WORLD_GEN, ConfigurationHandler.CATEGORY_WORLD_SPAWN + "." + block.getUnlocalizedName(), true);
        p.setLanguageKey(block.getUnlocalizedName() + ".name");
        return p.getBoolean(true);
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config)
    {
        Property shouldGenerateOres = config.config.get(ConfigurationHandler.CATEGORY_WORLD_GEN, ConfigurationHandler.CATEGORY_WORLD_SPAWN_ORES, true);
        shouldGenerateOres.comment = "Should Matter Overdrive Ore Blocks be Generated ?";
        generateTritanium = shouldGenerate(MatterOverdriveBlocks.tritaniumOre, config) && shouldGenerateOres.getBoolean(true);
        generateDilithium = shouldGenerate(MatterOverdriveBlocks.dilithium_ore, config) && shouldGenerateOres.getBoolean(true);
        Property shouldGenerateOthers = config.config.get(ConfigurationHandler.CATEGORY_WORLD_GEN, ConfigurationHandler.CATEGORY_WORLD_SPAWN_OTHER, true);
        shouldGenerateOthers.comment = "Should other Matter Overdrive World Blocks be Generated?";
        generateAnomalies = shouldGenerate(MatterOverdriveBlocks.gravitational_anomaly, config) && shouldGenerateOthers.getBoolean(true);
        this.oreDimentionsBlacklist.clear();
        Property oreDimentionBlacklistProp = config.config.get(config.CATEGORY_WORLD_GEN,"ore_gen_blacklist",new int[]{-1,2});
        oreDimentionBlacklistProp.comment = "A blacklist of all the Dimensions ores shouldn't spawn in";
        oreDimentionBlacklistProp.setLanguageKey("config.ore_gen_blacklist.name");
        int[] oreDimentionBlacklist = oreDimentionBlacklistProp.getIntList();
        for (int i = 0;i < oreDimentionBlacklist.length;i++)
        {
            this.oreDimentionsBlacklist.add(oreDimentionBlacklist[i]);
        }
    }

    public static GenPositionWorldData getWorldPositionData(World world)
    {
        GenPositionWorldData data = (GenPositionWorldData)world.loadItemData(GenPositionWorldData.class,Reference.WORLD_DATA_MO_GEN_POSITIONS);
        if (data == null)
        {
            data = new GenPositionWorldData(Reference.WORLD_DATA_MO_GEN_POSITIONS);
            world.setItemData(Reference.WORLD_DATA_MO_GEN_POSITIONS,data);
        }
        return data;
    }
}
