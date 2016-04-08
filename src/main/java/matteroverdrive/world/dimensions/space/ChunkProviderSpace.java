package matteroverdrive.world.dimensions.space;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkGenerator;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 2/5/2016.
 */
public class ChunkProviderSpace implements IChunkGenerator
{
    private World spaceWorld;
    private BiomeGenBase[] biomesForGeneration;
    private Random spaceRandom;

    public ChunkProviderSpace(World world,long seed)
    {
        spaceWorld = world;
        spaceRandom = new Random(seed);
    }

    @Override
    public Chunk provideChunk(int x, int z)
    {
        // TODO: 3/26/2016 Find how to get Chunk Manager
        /*this.spaceRandom.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        this.biomesForGeneration = this.spaceWorld.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, x * 16, z * 16, 16, 16);
        Chunk chunk = new Chunk(this.spaceWorld, chunkprimer, x, z);
        byte[] abyte = chunk.getBiomeArray();

        for (int i = 0; i < abyte.length; ++i)
        {
            abyte[i] = (byte)this.biomesForGeneration[i].biomeID;
        }

        chunk.generateSkylightMap();
        return chunk;*/
        return null;
    }

    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
    {
        return this.spaceWorld.getBiomeGenForCoords(pos).getSpawnableList(creatureType);
    }

    @Override
    public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position)
    {
        return null;
    }

    @Override
    public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_)
    {

    }

    @Override
    public void populate(int x, int z)
    {

    }

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z)
    {
        return false;
    }
}
