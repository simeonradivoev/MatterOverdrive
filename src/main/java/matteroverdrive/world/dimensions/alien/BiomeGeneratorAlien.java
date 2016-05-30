package matteroverdrive.world.dimensions.alien;

import matteroverdrive.blocks.alien.BlockFlowerAlien;
import matteroverdrive.init.MatterOverdriveBlocks;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

/**
 * Created by Simeon on 2/23/2016.
 */
public class BiomeGeneratorAlien extends Biome
{
	protected static final WorldGenAlienBush WORLD_GEN_ALIEN_BUSH = new WorldGenAlienBush();
	protected static final WorldGenBigTree worldGenBigTree = new WorldGenBigTree(false);
	private static final WorldGenAlienForest alienForestTall = new WorldGenAlienForest(false, true);
	private static final WorldGenAlienForest alienForestSmall = new WorldGenAlienForest(false, false);

	public BiomeGeneratorAlien(Biome.BiomeProperties properties)
	{
		super(properties);
		this.fillerBlock = MatterOverdriveBlocks.alienStone.getDefaultState();
		this.theBiomeDecorator.treesPerChunk = 8;
		this.theBiomeDecorator.grassPerChunk = 10;
		this.theBiomeDecorator.flowersPerChunk = 0;
		((BiomeDecoratorAlien)this.theBiomeDecorator).alienFlowerPerChunk = 12;
		this.theBiomeDecorator.generateLakes = false;

		//this.setFillerBlockMetadata(5159473);
		//this.setTemperatureRainfall(0.7F, 0.8F);

		//setColor(353825);

		//this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityWolf.class, 5, 4, 4));
		this.spawnableCreatureList.clear();
		this.spawnableCaveCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableWaterCreatureList.clear();
	}

	@Override
	public BiomeDecorator createBiomeDecorator()
	{
		return new BiomeDecoratorAlien();
	}

	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenTallGrassAlien();
	}

	@Override
	public WorldGenAbstractTree genBigTreeChance(Random rand)
	{
		return rand.nextFloat() < 0.4f ? alienForestTall : alienForestSmall;
	}

	@Override
	public BlockFlower.EnumFlowerType pickRandomFlower(Random rand, BlockPos pos)
	{
		double d0 = MathHelper.clamp_double((1.0D + GRASS_COLOR_NOISE.getValue((double)pos.getX() / 48.0D, (double)pos.getZ() / 48.0D)) / 2.0D, 0.0D, 0.9999D);
		BlockFlower.EnumFlowerType blockflower$enumflowertype = BlockFlower.EnumFlowerType.values()[(int)(d0 * (double)BlockFlower.EnumFlowerType.values().length)];
		return blockflower$enumflowertype == BlockFlower.EnumFlowerType.BLUE_ORCHID ? BlockFlower.EnumFlowerType.POPPY : blockflower$enumflowertype;
	}

	@Override
	public void decorate(World worldIn, Random rand, BlockPos pos)
	{
		int j1 = rand.nextInt(5) - 1;

		for (int k1 = 0; k1 < j1; ++k1)
		{
			for (int i2 = 0; i2 < 5; ++i2)
			{
				int j2 = rand.nextInt(16) + 8;
				int k2 = rand.nextInt(16) + 8;
				int i1 = rand.nextInt(worldIn.getHeight(pos.add(j2, 0, k2)).getY() + 32);

				if (WORLD_GEN_ALIEN_BUSH.generate(worldIn, rand, new BlockPos(pos.getX() + j2, i1, pos.getZ() + k2)))
				{
					break;
				}
			}
		}

		super.decorate(worldIn, rand, pos);
	}

	public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int p_180622_4_, int p_180622_5_, double p_180622_6_)
	{
		int i = worldIn.getSeaLevel();
		IBlockState iblockstate = this.topBlock;
		IBlockState iblockstate1 = this.fillerBlock;
		int j = -1;
		int k = (int)(p_180622_6_ / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
		int l = p_180622_4_ & 15;
		int i1 = p_180622_5_ & 15;
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

		for (int j1 = 255; j1 >= 0; --j1)
		{
			if (j1 <= rand.nextInt(5))
			{
				chunkPrimerIn.setBlockState(i1, j1, l, Blocks.BEDROCK.getDefaultState());
			}
			else
			{
				IBlockState iblockstate2 = chunkPrimerIn.getBlockState(i1, j1, l);

				if (iblockstate2.getMaterial() == Material.AIR)
				{
					j = -1;
				}
				else if (iblockstate2.getBlock() == Blocks.STONE)
				{
					if (j == -1)
					{
						if (k <= 0)
						{
							iblockstate = null;
							iblockstate1 = Blocks.STONE.getDefaultState();
						}
						else if (j1 >= i - 4 && j1 <= i + 1)
						{
							iblockstate = this.topBlock;
							iblockstate1 = this.fillerBlock;
						}

						if (j1 < i && (iblockstate == null || iblockstate.getMaterial() == Material.AIR))
						{
							if (this.getFloatTemperature(blockpos$mutableblockpos.setPos(p_180622_4_, j1, p_180622_5_)) < 0.15F)
							{
								iblockstate = Blocks.ICE.getDefaultState();
							}
							else
							{
								iblockstate = Blocks.WATER.getDefaultState();
							}
						}

						j = k;

						if (j1 >= i - 1)
						{
							chunkPrimerIn.setBlockState(i1, j1, l, iblockstate);
						}
						else if (j1 < i - 7 - k)
						{
							iblockstate = null;
							iblockstate1 = Blocks.STONE.getDefaultState();
							chunkPrimerIn.setBlockState(i1, j1, l, Blocks.GRAVEL.getDefaultState());
						}
						else
						{
							chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);
						}
					}
					else if (j > 0)
					{
						--j;
						chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);

						if (j == 0 && iblockstate1.getBlock() == Blocks.SAND)
						{
							j = rand.nextInt(4) + Math.max(0, j1 - 63);
							iblockstate1 = iblockstate1.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? Blocks.RED_SANDSTONE.getDefaultState() : Blocks.SANDSTONE.getDefaultState();
						}
					}
					else
					{
						chunkPrimerIn.setBlockState(i1, j1, l, MatterOverdriveBlocks.alienStone.getDefaultState());
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getGrassColorAtPos(BlockPos pos)
	{
		double d0 = (double)MathHelper.clamp_float(this.getFloatTemperature(pos), 0.0F, 1.0F);
		double d1 = (double)MathHelper.clamp_float(this.getRainfall(), 0.0F, 1.0F);
		return getModdedBiomeGrassColor(ColorizerAlien.getGrassColor(d0, d1));
	}

	@SideOnly(Side.CLIENT)
	public int getFoliageColorAtPos(BlockPos pos)
	{
		double d0 = (double)MathHelper.clamp_float(this.getFloatTemperature(pos), 0.0F, 1.0F);
		double d1 = (double)MathHelper.clamp_float(this.getRainfall(), 0.0F, 1.0F);
		return getModdedBiomeFoliageColor(ColorizerAlien.getFoliageColor(d0, d1));
	}

	@SideOnly(Side.CLIENT)
	public int getStoneColorAtPos(BlockPos pos)
	{
		double d0 = (double)MathHelper.clamp_float(this.getFloatTemperature(pos), 0.0F, 1.0F);
		double d1 = (double)MathHelper.clamp_float(this.getRainfall(), 0.0F, 1.0F);
		return ColorizerAlien.getStoneColor(d0, d1);
	}

	@Override
	public void addDefaultFlowers()
	{
		for (BlockFlowerAlien.EnumAlienFlowerType type : BlockFlowerAlien.EnumAlienFlowerType.values())
		{
			addFlower(MatterOverdriveBlocks.alienFlower.getDefaultState().withProperty(BlockFlowerAlien.TYPE, type), 10);
		}
	}

	public BlockFlowerAlien.EnumAlienFlowerType pickRandomAlienFlower(Random random, BlockPos pos)
	{
		return BlockFlowerAlien.EnumAlienFlowerType.values()[random.nextInt(BlockFlowerAlien.EnumAlienFlowerType.values().length)];
	}
}
