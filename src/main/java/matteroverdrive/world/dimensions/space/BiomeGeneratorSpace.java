package matteroverdrive.world.dimensions.space;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 2/5/2016.
 */
public class BiomeGeneratorSpace extends Biome
{
	public BiomeGeneratorSpace(BiomeProperties biomeProperties)
	{
		super(biomeProperties);
		this.spawnableMonsterList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableCaveCreatureList.clear();
		this.topBlock = Blocks.AIR.getDefaultState();
		this.fillerBlock = Blocks.AIR.getDefaultState();
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
