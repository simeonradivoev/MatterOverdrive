package matteroverdrive.world.dimensions.alien;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.blocks.alien.BlockTallGrassAlien;
import matteroverdrive.init.MatterOverdriveBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

/**
 * Created by Simeon on 2/24/2016.
 */
public class WorldGenTallGrassAlien extends WorldGenerator
{

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position)
	{
		do
		{
			IBlockState state = worldIn.getBlockState(position);
			if (!state.getBlock().isAir(state, worldIn, position) && !state.getBlock().isLeaves(state, worldIn, position))
			{
				break;
			}
			position = position.down();
		}
		while (position.getY() > 0);

		for (int i = 0; i < 128; ++i)
		{
			BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

			IBlockState blockState = MatterOverdrive.blocks.alienTallGrass.getBlockState().getBaseState().withProperty(BlockTallGrassAlien.TYPE, rand.nextInt(5));
			if (worldIn.isAirBlock(blockpos) && MatterOverdrive.blocks.alienTallGrass.canBlockStay(worldIn, blockpos, blockState))
			{
				worldIn.setBlockState(blockpos, blockState, 2);
			}
		}

		return true;
	}
}
