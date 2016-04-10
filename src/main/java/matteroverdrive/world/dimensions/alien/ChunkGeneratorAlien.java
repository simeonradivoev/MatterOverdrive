package matteroverdrive.world.dimensions.alien;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraftforge.event.terraingen.InitNoiseGensEvent;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 2/23/2016.
 */
public class ChunkGeneratorAlien implements IChunkGenerator
{
	private final double[] finalClampedNoiseValues;
	private final float[] parabolicField;
	/** A NoiseGeneratorOctaves used in generating terrain */
	public NoiseGeneratorOctaves noiseGen5;
	/** A NoiseGeneratorOctaves used in generating terrain */
	public NoiseGeneratorOctaves noiseGen6;
	public NoiseGeneratorOctaves mobSpawnerNoise;
	double[] noiseValues1;
	double[] noiseValues2;
	double[] noiseValues3;
	double[] noiseValues4;
	/** RNG. */
	private Random rand;
	private NoiseGeneratorOctaves noiseGen1;
	private NoiseGeneratorOctaves noiseGen2;
	private NoiseGeneratorOctaves noiseGen3;
	private NoiseGeneratorPerlin noiseGen4;
	/** Reference to the World object. */
	private World worldObj;
	private ChunkProviderSettings settings;
	private Block oceanBlock = Blocks.water;
	private double[] stoneNoise = new double[256];
	private MapGenBase caveGenerator = new MapGenCaves();
	private MapGenBase ravineGenerator = new MapGenRavine();
	private BiomeGenBase[] biomesForGeneration;

	public ChunkGeneratorAlien(World worldIn, long seed, String settingsString)
	{
		this.worldObj = worldIn;
		this.rand = new Random(seed);
		this.noiseGen1 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen2 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen3 = new NoiseGeneratorOctaves(this.rand, 8);
		this.noiseGen4 = new NoiseGeneratorPerlin(this.rand, 4);
		this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
		this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
		this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
		this.finalClampedNoiseValues = new double[825];
		this.parabolicField = new float[25];

		for (int i = -2; i <= 2; ++i)
		{
			for (int j = -2; j <= 2; ++j)
			{
				float f = 10.0F / MathHelper.sqrt_float((float)(i * i + j * j) + 0.2F);
				this.parabolicField[i + 2 + (j + 2) * 5] = f;
			}
		}

		if (settingsString != null)
		{
			this.settings = ChunkProviderSettings.Factory.jsonToFactory(settingsString).func_177864_b();
			this.oceanBlock = this.settings.useLavaOceans ? Blocks.lava : Blocks.water;
			worldIn.setSeaLevel(this.settings.seaLevel);
		}

		InitNoiseGensEvent.ContextOverworld context = new InitNoiseGensEvent.ContextOverworld(noiseGen1, noiseGen2, noiseGen3, noiseGen4, noiseGen5, noiseGen6, mobSpawnerNoise);
		context = net.minecraftforge.event.terraingen.TerrainGen.getModdedNoiseGenerators(worldIn, this.rand, context);
		this.noiseGen1 = context.getLPerlin1();
		this.noiseGen2 = context.getLPerlin2();
		this.noiseGen3 = context.getPerlin();
		this.noiseGen4 = context.getHeight();
		this.noiseGen5 = context.getScale();
		this.noiseGen6 = context.getDepth();
		this.mobSpawnerNoise = context.getForest();
	}

	public void setBlocksInChunk(int p_180518_1_, int p_180518_2_, ChunkPrimer p_180518_3_)
	{
		this.biomesForGeneration = this.worldObj.getBiomeProvider().getBiomesForGeneration(this.biomesForGeneration, p_180518_1_ * 4 - 2, p_180518_2_ * 4 - 2, 10, 10);
		this.func_147423_a(p_180518_1_ * 4, 0, p_180518_2_ * 4);

		for (int i = 0; i < 4; ++i)
		{
			int j = i * 5;
			int k = (i + 1) * 5;

			for (int l = 0; l < 4; ++l)
			{
				int i1 = (j + l) * 33;
				int j1 = (j + l + 1) * 33;
				int k1 = (k + l) * 33;
				int l1 = (k + l + 1) * 33;

				for (int i2 = 0; i2 < 32; ++i2)
				{
					double d0 = 0.125D;
					double d1 = this.finalClampedNoiseValues[i1 + i2];
					double d2 = this.finalClampedNoiseValues[j1 + i2];
					double d3 = this.finalClampedNoiseValues[k1 + i2];
					double d4 = this.finalClampedNoiseValues[l1 + i2];
					double d5 = (this.finalClampedNoiseValues[i1 + i2 + 1] - d1) * d0;
					double d6 = (this.finalClampedNoiseValues[j1 + i2 + 1] - d2) * d0;
					double d7 = (this.finalClampedNoiseValues[k1 + i2 + 1] - d3) * d0;
					double d8 = (this.finalClampedNoiseValues[l1 + i2 + 1] - d4) * d0;

					for (int j2 = 0; j2 < 8; ++j2)
					{
						double d9 = 0.25D;
						double d10 = d1;
						double d11 = d2;
						double d12 = (d3 - d1) * d9;
						double d13 = (d4 - d2) * d9;

						for (int k2 = 0; k2 < 4; ++k2)
						{
							double d14 = 0.25D;
							double d16 = (d11 - d10) * d14;
							double lvt_45_1_ = d10 - d16;

							for (int l2 = 0; l2 < 4; ++l2)
							{
								if ((lvt_45_1_ += d16) > 0.0D)
								{
									p_180518_3_.setBlockState(i * 4 + k2, i2 * 8 + j2, l * 4 + l2, Blocks.stone.getDefaultState());
								}
								else if (i2 * 8 + j2 < this.settings.seaLevel)
								{
									p_180518_3_.setBlockState(i * 4 + k2, i2 * 8 + j2, l * 4 + l2, this.oceanBlock.getDefaultState());
								}
							}

							d10 += d12;
							d11 += d13;
						}

						d1 += d5;
						d2 += d6;
						d3 += d7;
						d4 += d8;
					}
				}
			}
		}
	}

	public void replaceBlocksForBiome(int p_180517_1_, int p_180517_2_, ChunkPrimer p_180517_3_, BiomeGenBase[] p_180517_4_)
	{
		net.minecraftforge.event.terraingen.ChunkGeneratorEvent.ReplaceBiomeBlocks event = new net.minecraftforge.event.terraingen.ChunkGeneratorEvent.ReplaceBiomeBlocks(this, p_180517_1_, p_180517_2_, p_180517_3_, this.worldObj);
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
		if (event.getResult() == net.minecraftforge.fml.common.eventhandler.Event.Result.DENY)
		{
			return;
		}

		double d0 = 0.03125D;
		this.stoneNoise = this.noiseGen4.func_151599_a(this.stoneNoise, (double)(p_180517_1_ * 16), (double)(p_180517_2_ * 16), 16, 16, d0 * 2.0D, d0 * 2.0D, 1.0D);

		for (int i = 0; i < 16; ++i)
		{
			for (int j = 0; j < 16; ++j)
			{
				BiomeGenBase biomegenbase = p_180517_4_[j + i * 16];
				biomegenbase.genTerrainBlocks(this.worldObj, this.rand, p_180517_3_, p_180517_1_ * 16 + i, p_180517_2_ * 16 + j, this.stoneNoise[j + i * 16]);
			}
		}
	}

	private void func_147423_a(int p_147423_1_, int p_147423_2_, int p_147423_3_)
	{
		this.noiseValues4 = this.noiseGen6.generateNoiseOctaves(this.noiseValues4, p_147423_1_, p_147423_3_, 5, 5, (double)this.settings.depthNoiseScaleX, (double)this.settings.depthNoiseScaleZ, (double)this.settings.depthNoiseScaleExponent);
		float f = this.settings.coordinateScale;
		float f1 = this.settings.heightScale;
		this.noiseValues1 = this.noiseGen3.generateNoiseOctaves(this.noiseValues1, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, (double)(f / this.settings.mainNoiseScaleX), (double)(f1 / this.settings.mainNoiseScaleY), (double)(f / this.settings.mainNoiseScaleZ));
		this.noiseValues2 = this.noiseGen1.generateNoiseOctaves(this.noiseValues2, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, (double)f, (double)f1, (double)f);
		this.noiseValues3 = this.noiseGen2.generateNoiseOctaves(this.noiseValues3, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, (double)f, (double)f1, (double)f);
		int i = 0;
		int j = 0;

		for (int k = 0; k < 5; ++k)
		{
			for (int l = 0; l < 5; ++l)
			{
				float f2 = 0.0F;
				float f3 = 0.0F;
				float f4 = 0.0F;
				int i1 = 2;
				BiomeGenBase biomegenbase = this.biomesForGeneration[k + 2 + (l + 2) * 10];

				for (int j1 = -i1; j1 <= i1; ++j1)
				{
					for (int k1 = -i1; k1 <= i1; ++k1)
					{
						BiomeGenBase biomegenbase1 = this.biomesForGeneration[k + j1 + 2 + (l + k1 + 2) * 10];
						float f5 = this.settings.biomeDepthOffSet + biomegenbase1.getBaseHeight() * this.settings.biomeDepthWeight;
						float f6 = this.settings.biomeScaleOffset + biomegenbase1.getHeightVariation() * this.settings.biomeScaleWeight;

						float f7 = this.parabolicField[j1 + 2 + (k1 + 2) * 5] / (f5 + 2.0F);

						if (biomegenbase1.getBaseHeight() > biomegenbase.getBaseHeight())
						{
							f7 /= 2.0F;
						}

						f2 += f6 * f7;
						f3 += f5 * f7;
						f4 += f7;
					}
				}

				f2 = f2 / f4;
				f3 = f3 / f4;
				f2 = f2 * 0.9F + 0.1F;
				f3 = (f3 * 4.0F - 1.0F) / 8.0F;
				double d7 = this.noiseValues4[j] / 8000.0D;

				if (d7 < 0.0D)
				{
					d7 = -d7 * 0.3D;
				}

				d7 = d7 * 3.0D - 2.0D;

				if (d7 < 0.0D)
				{
					d7 = d7 / 2.0D;

					if (d7 < -1.0D)
					{
						d7 = -1.0D;
					}

					d7 = d7 / 1.4D;
					d7 = d7 / 2.0D;
				}
				else
				{
					if (d7 > 1.0D)
					{
						d7 = 1.0D;
					}

					d7 = d7 / 8.0D;
				}

				++j;
				double d8 = (double)f3;
				double d9 = (double)f2;
				d8 = d8 + d7 * 0.2D;
				d8 = d8 * (double)this.settings.baseSize / 8.0D;
				double d0 = (double)this.settings.baseSize + d8 * 4.0D;

				for (int l1 = 0; l1 < 33; ++l1)
				{
					double d1 = ((double)l1 - d0) * (double)this.settings.stretchY * 128.0D / 256.0D / d9;

					if (d1 < 0.0D)
					{
						d1 *= 4.0D;
					}

					double lowerNoiseValue = this.noiseValues2[i] / (double)this.settings.lowerLimitScale;
					double upperNoiseValue = this.noiseValues3[i] / (double)this.settings.upperLimitScale;
					double noiseValue = (this.noiseValues1[i] / 10.0D + 1.0D) / 2.0D;
					double clampedNoiseValue = MathHelper.denormalizeClamp(lowerNoiseValue, upperNoiseValue, noiseValue) - d1;

					if (l1 > 29)
					{
						double d6 = (double)((float)(l1 - 29) / 3.0F);
						clampedNoiseValue = clampedNoiseValue * (1.0D - d6) + -10.0D * d6;
					}

					this.finalClampedNoiseValues[i] = clampedNoiseValue;
					++i;
				}
			}
		}
	}

	@Override
	public Chunk provideChunk(int x, int z)
	{
		this.rand.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
		ChunkPrimer chunkprimer = new ChunkPrimer();
		this.setBlocksInChunk(x, z, chunkprimer);
		this.biomesForGeneration = this.worldObj.getBiomeProvider().loadBlockGeneratorData(this.biomesForGeneration, x * 16, z * 16, 16, 16);
		this.replaceBlocksForBiome(x, z, chunkprimer, this.biomesForGeneration);

		if (this.settings.useCaves)
		{
			this.caveGenerator.generate(this.worldObj, x, z, chunkprimer);
		}

		if (this.settings.useRavines)
		{
			this.ravineGenerator.generate(this.worldObj, x, z, chunkprimer);
		}

		Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);

		chunk.generateSkylightMap();
		return chunk;
	}

	@Override
	public void populate(int p_73153_2_, int p_73153_3_)
	{
		BlockFalling.fallInstantly = true;
		int i = p_73153_2_ * 16;
		int j = p_73153_3_ * 16;
		BlockPos blockpos = new BlockPos(i, 0, j);
		BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(blockpos.add(16, 0, 16));
		this.rand.setSeed(this.worldObj.getSeed());
		long k = this.rand.nextLong() / 2L * 2L + 1L;
		long l = this.rand.nextLong() / 2L * 2L + 1L;
		this.rand.setSeed((long)p_73153_2_ * k + (long)p_73153_3_ * l ^ this.worldObj.getSeed());
		boolean flag = false;

		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.terraingen.PopulateChunkEvent.Pre(this, worldObj, rand, p_73153_2_, p_73153_3_, flag));

		if (this.settings.useWaterLakes && !flag && this.rand.nextInt(this.settings.waterLakeChance) == 0
				&& net.minecraftforge.event.terraingen.TerrainGen.populate(this, worldObj, rand, p_73153_2_, p_73153_3_, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAKE))
		{
			int i1 = this.rand.nextInt(16) + 8;
			int j1 = this.rand.nextInt(256);
			int k1 = this.rand.nextInt(16) + 8;
			(new WorldGenLakes(Blocks.water)).generate(this.worldObj, this.rand, blockpos.add(i1, j1, k1));
		}

		if (!flag && this.rand.nextInt(this.settings.lavaLakeChance / 10) == 0 && this.settings.useLavaLakes &&
				net.minecraftforge.event.terraingen.TerrainGen.populate(this, worldObj, rand, p_73153_2_, p_73153_3_, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.LAVA))
		{
			int i2 = this.rand.nextInt(16) + 8;
			int l2 = this.rand.nextInt(this.rand.nextInt(248) + 8);
			int k3 = this.rand.nextInt(16) + 8;

			if (l2 < this.worldObj.getSeaLevel() || this.rand.nextInt(this.settings.lavaLakeChance / 8) == 0)
			{
				(new WorldGenLakes(Blocks.lava)).generate(this.worldObj, this.rand, blockpos.add(i2, l2, k3));
			}
		}

		biomegenbase.decorate(this.worldObj, this.rand, new BlockPos(i, 0, j));
		if (net.minecraftforge.event.terraingen.TerrainGen.populate(this, worldObj, rand, p_73153_2_, p_73153_3_, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ANIMALS))
		{
			WorldEntitySpawner.performWorldGenSpawning(this.worldObj, biomegenbase, i + 8, j + 8, 16, 16, this.rand);
		}
		blockpos = blockpos.add(8, 0, 8);

		boolean doGen = net.minecraftforge.event.terraingen.TerrainGen.populate(this, worldObj, rand, p_73153_2_, p_73153_3_, flag, net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.ICE);
		for (int k2 = 0; doGen && k2 < 16; ++k2)
		{
			for (int j3 = 0; j3 < 16; ++j3)
			{
				BlockPos blockpos1 = this.worldObj.getPrecipitationHeight(blockpos.add(k2, 0, j3));
				BlockPos blockpos2 = blockpos1.down();

				if (this.worldObj.canBlockFreezeWater(blockpos2))
				{
					this.worldObj.setBlockState(blockpos2, Blocks.ice.getDefaultState(), 2);
				}

				if (this.worldObj.canSnowAt(blockpos1, true))
				{
					this.worldObj.setBlockState(blockpos1, Blocks.snow_layer.getDefaultState(), 2);
				}
			}
		}

		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.terraingen.PopulateChunkEvent.Post(this, worldObj, rand, p_73153_2_, p_73153_3_, flag));

		BlockFalling.fallInstantly = false;
	}

	@Override
	public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
	{
		BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(pos);
		return biomegenbase.getSpawnableList(creatureType);
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
	public boolean generateStructures(Chunk chunkIn, int x, int z)
	{
		return false;
	}
}
