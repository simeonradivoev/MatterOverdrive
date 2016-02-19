package matteroverdrive.world.dimensions;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeDecorator;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 2/5/2016.
 */
public class BiomeGeneratorSpace extends BiomeGenBase
{
    public BiomeGeneratorSpace(int id)
    {
        super(id,false);
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.topBlock = Blocks.air.getDefaultState();
        this.fillerBlock = Blocks.air.getDefaultState();
        this.temperature = 0;
        this.rainfall = 0;
        setBiomeName("Space");
    }

    public BiomeDecorator createBiomeDecorator()
    {
        return getModdedBiomeDecorator(new BiomeDecoratorSpace());
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float p_76731_1_)
    {
        return 0;
    }
}
