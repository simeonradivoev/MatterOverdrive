package matteroverdrive.blocks;

import matteroverdrive.blocks.includes.MOBlockMachine;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.tile.TileEntityMachineSpacetimeAccelerator;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Simeon on 1/22/2016.
 */
public class BlockSpacetimeAccelerator extends MOBlockMachine
{
    public boolean showWave = true;

    public BlockSpacetimeAccelerator(Material material, String name)
    {
        super(material, name);
        setHardness(20.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe", 2);
        //setBlockBounds(4f/16f,0,4f/16f,12f/16f,1,12f/16f);
        setLightLevel(1);
        setHasGui(true);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityMachineSpacetimeAccelerator();
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
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
