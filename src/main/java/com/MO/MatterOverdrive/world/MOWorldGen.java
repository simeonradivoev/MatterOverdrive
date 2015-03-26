package com.MO.MatterOverdrive.world;

import com.MO.MatterOverdrive.init.MatterOverdriveBlocks;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

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
        for (int i = 0; i < DILITHIUM_VEINS_PER_CHUNK; i++)
        {
            int x = chunkX + random.nextInt(16);
            int z = chunkZ + random.nextInt(16);
            int y = random.nextInt(28) + 4;

            if(dilithiumGen.generate(world,random,x,y,z))
            {

            }
        }

        for (int i = 0; i < TRITANIUM_VEINS_PER_CHUNK; i++)
        {
            int x = chunkX + random.nextInt(16);
            int z = chunkZ + random.nextInt(16);
            int y = random.nextInt(60) + 4;

            if(tritaniumGen.generate(world,random,x,y,z))
            {

            }
        }
    }
    public void generateNether(World world,Random random,int chunkX, int chunkZ)
    {

    }
    public void generateEnd(World world,Random random,int chunkX, int chunkZ)
    {

    }
}
