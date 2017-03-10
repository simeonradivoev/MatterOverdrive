package matteroverdrive.blocks;

import matteroverdrive.blocks.includes.MOBlockMachine;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.tile.TileEntityMachineSpacetimeAccelerator;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by Simeon on 1/22/2016.
 */
public class BlockSpacetimeAccelerator extends MOBlockMachine<TileEntityMachineSpacetimeAccelerator>
{
	public boolean showWave = true;

	public BlockSpacetimeAccelerator(Material material, String name)
	{
		super(material, name);
		setHardness(20.0F);
		this.setResistance(9.0f);
		this.setHarvestLevel("pickaxe", 2);
		//setBoundingBox(new AxisAlignedBB(4/16d, 0, 4/16d, 12/16d, 1, 12/16d));
		setLightLevel(1);
		setHasGui(true);
	}

	@Override
	public Class<TileEntityMachineSpacetimeAccelerator> getTileEntityClass()
	{
		return TileEntityMachineSpacetimeAccelerator.class;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state)
	{
		return new TileEntityMachineSpacetimeAccelerator();
	}

	@Override
	@Deprecated
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Override
	public void onConfigChanged(ConfigurationHandler config)
	{
		super.onConfigChanged(config);
		showWave = config.getMachineBool(getUnlocalizedName(), "wave_particle", true, "Show the wave particle when the machine is active");
	}
}
