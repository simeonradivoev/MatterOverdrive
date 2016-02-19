package matteroverdrive.world.dimensions;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 2/5/2016.
 */
public class ChunkProviderSpace implements IChunkProvider
{
    private World spaceWorld;
    private BiomeGenBase[] biomesForGeneration;
    private Random spaceRandom;
    private int chunkX,chunkZ;

    public ChunkProviderSpace(World world,long seed)
    {
        spaceWorld = world;
        spaceRandom = new Random(seed);
    }

    @Override
    public boolean chunkExists(int x, int z)
    {
        return true;
    }

    @Override
    public Chunk provideChunk(int x, int z)
    {
        chunkX = x; chunkZ = z;
        this.spaceRandom.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        this.biomesForGeneration = this.spaceWorld.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, x * 16, z * 16, 16, 16);
        Chunk chunk = new Chunk(this.spaceWorld, chunkprimer, x, z);
        byte[] abyte = chunk.getBiomeArray();

        for (int i = 0; i < abyte.length; ++i)
        {
            abyte[i] = (byte)this.biomesForGeneration[i].biomeID;
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public Chunk provideChunk(BlockPos blockPosIn)
    {
        return this.provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
    }

    @Override
    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_)
    {

    }

    @Override
    public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_)
    {
        return false;
    }

    @Override
    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate progressCallback)
    {
        return true;
    }

    @Override
    public boolean unloadQueuedChunks()
    {
        return false;
    }

    @Override
    public boolean canSave()
    {
        return true;
    }

    @Override
    public String makeString()
    {
        return "RandomLevelSource";
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
    public int getLoadedChunkCount()
    {
        return 0;
    }

    @Override
    public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_)
    {

    }

    @Override
    public void saveExtraData()
    {

    }
}
