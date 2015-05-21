package com.MO.MatterOverdrive.world;

import cofh.lib.world.WorldGenDungeon;
import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.handler.MOConfigurationHandler;
import com.MO.MatterOverdrive.init.MatterOverdriveBlocks;
import com.MO.MatterOverdrive.util.IConfigSubscriber;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.util.Random;

/**
 * Created by Simeon on 3/23/2015.
 */
public class MOWorldGen implements IWorldGenerator, IConfigSubscriber
{
    public static WorldGenMinable dilithiumGen;
    public static WorldGenMinable tritaniumGen;
    public static WorldGenGravitationalAnomaly anomalyGen;
    public static final int TRITANIUM_VEINS_PER_CHUNK = 10;
    public static final int TRITANIUM_VEIN_SIZE = 6;
    public static final int DILITHIUM_VEINS_PER_CHUNK = 4;
    public static final int DILITHIUM_VEIN_SIZE = 3;

    boolean generateTritanium;
    boolean generateDilithium;
    boolean generateAnomalies;

    public MOWorldGen(MOConfigurationHandler configurationHandler)
    {
        tritaniumGen = new WorldGenMinable(MatterOverdriveBlocks.tritaniumOre,TRITANIUM_VEIN_SIZE);
        dilithiumGen = new WorldGenMinable(MatterOverdriveBlocks.dilithiumOre,DILITHIUM_VEIN_SIZE);
        anomalyGen = new WorldGenGravitationalAnomaly(0.005f,2048,2048 + 8192);

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
        }
    }

    public void generateOverworld(World world,Random random,int chunkX, int chunkZ)
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

        if (generateTritanium)
        {
            for (int i = 0; i < TRITANIUM_VEINS_PER_CHUNK; i++) {
                int x = chunkX + random.nextInt(16);
                int z = chunkZ + random.nextInt(16);
                int y = random.nextInt(60) + 4;

                if (tritaniumGen.generate(world, random, x, y, z)) {

                }
            }
        }

        generateGravitationalAnomalies(world,random,chunkX,chunkZ);
    }
    public void generateNether(World world,Random random,int chunkX, int chunkZ)
    {
        generateGravitationalAnomalies(world,random,chunkX,chunkZ);
    }
    public void generateEnd(World world,Random random,int chunkX, int chunkZ)
    {
        generateGravitationalAnomalies(world,random,chunkX,chunkZ);
    }
    private void generateGravitationalAnomalies(World world,Random random,int chunkX, int chunkZ)
    {
        if (generateAnomalies) {
            int x = chunkX + random.nextInt(16);
            int z = chunkZ + random.nextInt(16);
            int y = random.nextInt(60) + 4;

            if (anomalyGen.generate(world, random, x, y, z)) {

            }
        }
    }
    private boolean shouldGenerate(Block block,MOConfigurationHandler config)
    {
        Property p = config.config.get(MOConfigurationHandler.CATEGORY_WORLD_GEN,MOConfigurationHandler.CATEGORY_WORLD_SPAWN + "." + block.getUnlocalizedName(), true);
        p.setLanguageKey(block.getUnlocalizedName() + ".name");
        return p.getBoolean(true);
    }

    @Override
    public void onConfigChanged(MOConfigurationHandler config) {
        Property shouldGenerateOres = config.config.get(MOConfigurationHandler.CATEGORY_WORLD_GEN, MOConfigurationHandler.CATEGORY_WORLD_SPAWN_ORES, true);
        shouldGenerateOres.comment = "Should Matter Overdrive Ore Blocks be Generated ?";
        generateTritanium = shouldGenerate(MatterOverdriveBlocks.tritaniumOre, config) && shouldGenerateOres.getBoolean(true);
        generateDilithium = shouldGenerate(MatterOverdriveBlocks.dilithiumOre, config) && shouldGenerateOres.getBoolean(true);
        Property shouldGenerateOthers = config.config.get(MOConfigurationHandler.CATEGORY_WORLD_GEN, MOConfigurationHandler.CATEGORY_WORLD_SPAWN_OTHER, true);
        shouldGenerateOthers.comment = "Should other Matter Overdrive World Blocks be Generated?";
        generateAnomalies = shouldGenerate(MatterOverdriveBlocks.gravitational_anomaly, config) && shouldGenerateOthers.getBoolean(true);
    }
}
