package matteroverdrive.world.dimensions.alien;

import matteroverdrive.blocks.alien.BlockLeavesAlien;
import matteroverdrive.init.MatterOverdriveBlocks;
import net.minecraft.block.BlockLeaves;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

/**
 * Created by Simeon on 3/4/2016.
 */
public class WorldGenAlienBush extends WorldGenerator
{
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		boolean flag = false;

		for (int i = 0; i < 64; ++i)
		{
			BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

			if (worldIn.isAirBlock(blockpos) && (!worldIn.provider.getHasNoSky() || blockpos.getY() < 254) && Blocks.DOUBLE_PLANT.canPlaceBlockAt(worldIn, blockpos))
			{
				worldIn.setBlockState(blockpos, MatterOverdriveBlocks.alienLeaves.getDefaultState().withProperty(BlockLeavesAlien.VARIANT, BlockLeavesAlien.EnumType.BUSH).withProperty(BlockLeaves.DECAYABLE, false), 2);
				worldIn.setBlockState(blockpos.offset(EnumFacing.UP), MatterOverdriveBlocks.alienLeaves.getDefaultState().withProperty(BlockLeavesAlien.VARIANT, BlockLeavesAlien.EnumType.BUSH).withProperty(BlockLeaves.DECAYABLE, false), 2);
				flag = true;
			}
		}

		return flag;
	}
}
