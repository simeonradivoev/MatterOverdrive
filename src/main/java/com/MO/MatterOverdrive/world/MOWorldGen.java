package com.MO.MatterOverdrive.world;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.handler.MOConfigurationHandler;
import com.MO.MatterOverdrive.init.MatterOverdriveBlocks;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.util.Random;

/**
 * Created by Simeon on 3/23/2015.
 */
public class MOWorldGen implements IWorldGenerator
{
    public static WorldGenMinable dilithiumGen;
    public static WorldGenMinable tritaniumGen;
    public static final int TRITANIUM_VEINS_PER_CHUNK = 10;
    public static final int TRITANIUM_VEIN_SIZE = 6;
    public static final int DILITHIUM_VEINS_PER_CHUNK = 4;
    public static final int DILITHIUM_VEIN_SIZE = 3;


    public MOWorldGen()
    {
        tritaniumGen = new WorldGenMinable(MatterOverdriveBlocks.tritaniumOre,TRITANIUM_VEIN_SIZE);
        dilithiumGen = new WorldGenMinable(MatterOverdriveBlocks.dilithiumOre,DILITHIUM_VEIN_SIZE);
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
        if (shouldGenerate(MatterOverdriveBlocks.dilithiumOre.getUnlocalizedName())) {
            for (int i = 0; i < DILITHIUM_VEINS_PER_CHUNK; i++) {
                int x = chunkX + random.nextInt(16);
                int z = chunkZ + random.nextInt(16);
                int y = random.nextInt(28) + 4;

                if (dilithiumGen.generate(world, random, x, y, z)) {

                }
            }
        }

        if (shouldGenerate(MatterOverdriveBlocks.tritaniumOre.getUnlocalizedName()))
        {
            for (int i = 0; i < TRITANIUM_VEINS_PER_CHUNK; i++) {
                int x = chunkX + random.nextInt(16);
                int z = chunkZ + random.nextInt(16);
                int y = random.nextInt(60) + 4;

                if (tritaniumGen.generate(world, random, x, y, z)) {

                }
            }
        }
    }
    public void generateNether(World world,Random random,int chunkX, int chunkZ)
    {

    }
    public void generateEnd(World world,Random random,int chunkX, int chunkZ)
    {

    }
    private boolean shouldGenerate(String name)
    {
        Configuration configuration = MatterOverdrive.configHandler.config;
        Property shouldGenerateOres = configuration.get(MOConfigurationHandler.CATEGORY_WORLD_GEN, Reference.CONFIG_WORLD_SPAWN_ORES,true);
        if (shouldGenerateOres.getBoolean(true))
        {
            return configuration.get(MOConfigurationHandler.CATEGORY_WORLD_GEN,Reference.CONFIG_WORLD_SPAWN + "." + name,true).getBoolean(true);
        }
        return false;
    }
}
