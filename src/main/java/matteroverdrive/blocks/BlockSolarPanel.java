package matteroverdrive.blocks;

import matteroverdrive.blocks.includes.MOMatterEnergyStorageBlock;
import matteroverdrive.tile.TileEntityMachineSolarPanel;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by Simeon on 4/9/2015.
 */
public class BlockSolarPanel extends MOMatterEnergyStorageBlock<TileEntityMachineSolarPanel>
{
	public BlockSolarPanel(Material material, String name)
	{
		super(material, name, true, false);

		//this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		setHardness(20.0F);
		this.setResistance(5.0f);
		this.setHarvestLevel("pickaxe", 2);
		setHasGui(true);
	}

	@Override
	public Class<TileEntityMachineSolarPanel> getTileEntityClass()
	{
		return TileEntityMachineSolarPanel.class;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState meta)
	{
		return new TileEntityMachineSolarPanel();
	}

    /*@Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister registrar)
    {
        this.iconTop = registrar.registerIcon(Reference.MOD_ID + ":solar_panel");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int metadata)
    {
        return side == 1 ? iconTop : MatterOverdriveIcons.Base;
    }*/

	public boolean isOpaqueCube()
	{
		return false;
	}

	public boolean renderAsNormalBlock()
	{
		return false;
	}
}
