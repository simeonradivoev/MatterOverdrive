package matteroverdrive.world.dimensions.alien;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.blocks.alien.BlockFlowerAlien;
import matteroverdrive.init.MatterOverdriveBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

/**
 * Created by Simeon on 2/24/2016.
 */
public class WorldGenAlienFlowers extends WorldGenerator
{
	private IBlockState flowerBlockState;

	public WorldGenAlienFlowers(BlockFlowerAlien.EnumAlienFlowerType alienFlowerType)
	{
		this.setGeneratedBlock(alienFlowerType);
	}

	public void setGeneratedBlock(BlockFlowerAlien.EnumAlienFlowerType alienFlowerType)
	{
		this.flowerBlockState = MatterOverdrive.blocks.alienFlower.getDefaultState().withProperty(BlockFlowerAlien.TYPE, alienFlowerType);
	}

	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		for (int i = 0; i < 64; ++i)
		{
			BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

			if (worldIn.isAirBlock(blockpos) && (!worldIn.provider.getHasNoSky() || blockpos.getY() < 255) && MatterOverdrive.blocks.alienFlower.canBlockStay(worldIn, blockpos, this.flowerBlockState))
			{
				worldIn.setBlockState(blockpos, this.flowerBlockState, 2);
			}
		}

		return true;
	}
}
