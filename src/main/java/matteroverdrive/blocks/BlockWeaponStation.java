package matteroverdrive.blocks;

import matteroverdrive.blocks.includes.MOBlockMachine;
import matteroverdrive.tile.TileEntityWeaponStation;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by Simeon on 4/13/2015.
 */
public class BlockWeaponStation extends MOBlockMachine<TileEntityWeaponStation>
{

	public BlockWeaponStation(Material material, String name)
	{
		super(material, name);
		setBoundingBox(new AxisAlignedBB(0, 0, 0, 1, 9/16d, 1));
		setHardness(20.0F);
		this.setResistance(9.0f);
		this.setHarvestLevel("pickaxe", 2);
		lightValue = 10;
		setHasGui(true);
	}

    /*@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister)
    {
        super.registerBlockIcons(iconRegister);
        topIcon = iconRegister.registerIcon(Reference.MOD_ID + ":weapon_station_top");
        bottomIcon = iconRegister.registerIcon(Reference.MOD_ID + ":weapon_station_bottom");
        blockIcon = iconRegister.registerIcon(Reference.MOD_ID + ":weapon_station_side");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        if (side == 1)
        {
            return topIcon;
        }
        else if (side == 0)
        {
            return bottomIcon;
        }
        else
        {
            return blockIcon;
        }
    }*/

	@Override
	public Class<TileEntityWeaponStation> getTileEntityClass()
	{
		return TileEntityWeaponStation.class;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state)
	{
		return new TileEntityWeaponStation();
	}

	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

    /*@Override
	public int getRenderType()
    {
        return MOBlockRenderer.renderID;
    }*/
}
