package matteroverdrive.world.dimensions.alien;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.blocks.alien.BlockFlowerAlien;
import matteroverdrive.init.MatterOverdriveBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenSand;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

/**
 * Created by Simeon on 2/24/2016.
 */
public class BiomeDecoratorAlien extends BiomeDecorator
{
	public int alienFlowerPerChunk = 2;
	public WorldGenAlienFlowers worldGenAlienFlowers = new WorldGenAlienFlowers(BlockFlowerAlien.EnumAlienFlowerType.BLOOD_ORCHID);

	public BiomeDecoratorAlien()
	{
		super();
		sandGen = new WorldGenSand(MatterOverdrive.blocks.alienSand, 7);
		gravelAsSandGen = new WorldGenSand(MatterOverdrive.blocks.alienGravel, 6);

	}

	@Override
	protected void genDecorations(Biome biomeGenBaseIn, World worldIn, Random random)
	{
		super.genDecorations(biomeGenBaseIn, worldIn, random);
		if (biomeGenBaseIn instanceof BiomeGeneratorAlien)
		{
			if (net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, random, chunkPos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.FLOWERS))
			{
				for (int l2 = 0; l2 < this.alienFlowerPerChunk; ++l2)
				{
					int i7 = random.nextInt(16) + 8;
					int l10 = random.nextInt(16) + 8;
					int j14 = worldIn.getHeight(this.chunkPos.add(i7, 0, l10)).getY() + 32;

					if (j14 > 0)
					{
						int k17 = random.nextInt(j14);
						BlockPos blockpos1 = this.chunkPos.add(i7, k17, l10);
						BlockFlowerAlien.EnumAlienFlowerType flowerType = ((BiomeGeneratorAlien)biomeGenBaseIn).pickRandomAlienFlower(random, blockpos1);
						this.worldGenAlienFlowers.setGeneratedBlock(flowerType);
						this.worldGenAlienFlowers.generate(worldIn, random, blockpos1);
					}
				}
			}
		}
	}

	@Override
	public void decorate(World worldIn, Random random, Biome p_180292_3_, BlockPos p_180292_4_)
	{
		super.decorate(worldIn, random, p_180292_3_, p_180292_4_);
		if (worldIn == null)
		{
			WorldGenerator gravelGen = new WorldGenMinable(MatterOverdrive.blocks.alienGravel.getDefaultState(), this.chunkProviderSettings.gravelSize);
			this.gravelGen = gravelGen;
		}
	}
}
