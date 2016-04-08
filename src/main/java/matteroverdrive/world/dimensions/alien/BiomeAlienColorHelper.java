package matteroverdrive.world.dimensions.alien;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 2/24/2016.
 */
public class BiomeAlienColorHelper
{
    private static final BiomeAlienColorHelper.ColorResolver stoneColor = (p_180283_1_, blockPosition) -> p_180283_1_.getStoneColorAtPos(blockPosition);

    @SideOnly(Side.CLIENT)
    interface ColorResolver
    {
        int getColorAtPos(BiomeGeneratorAlien p_180283_1_, BlockPos blockPosition);
    }

    private static int func_180285_a(IBlockAccess p_180285_0_, BlockPos p_180285_1_, BiomeAlienColorHelper.ColorResolver p_180285_2_)
    {
        int i = 0;
        int j = 0;
        int k = 0;

        for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(p_180285_1_.add(-1, 0, -1), p_180285_1_.add(1, 0, 1)))
        {
            BiomeGenBase biomeGenBase = p_180285_0_.getBiomeGenForCoords(blockpos$mutableblockpos);
            if (biomeGenBase instanceof BiomeGeneratorAlien)
            {
                int l = p_180285_2_.getColorAtPos((BiomeGeneratorAlien)biomeGenBase, blockpos$mutableblockpos);
                i += (l & 16711680) >> 16;
                j += (l & 65280) >> 8;
                k += l & 255;
            }
        }

        return (i / 9 & 255) << 16 | (j / 9 & 255) << 8 | k / 9 & 255;
    }

    public static int getStoneColorAtPos(IBlockAccess p_180286_0_, BlockPos p_180286_1_)
    {
        return func_180285_a(p_180286_0_, p_180286_1_, stoneColor);
    }
}
