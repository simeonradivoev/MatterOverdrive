package matteroverdrive.blocks;

import matteroverdrive.blocks.includes.MOBlockMachine;
import matteroverdrive.tile.TileEntityMachineStarMap;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by Simeon on 6/13/2015.
 */
public class BlockStarMap extends MOBlockMachine<TileEntityMachineStarMap>
{
	public BlockStarMap(Material material, String name)
	{
		super(material, name);
		//setBoundingBox(new AxisAlignedBB(0, 0, 0, 1, 9/16d, 1));
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
        topIcon = iconRegister.registerIcon(Reference.MOD_ID + ":" + "weapon_station_top");
        bottomIcon = iconRegister.registerIcon(Reference.MOD_ID + ":" + "weapon_station_bottom");
        blockIcon = iconRegister.registerIcon(Reference.MOD_ID + ":" + "starmap_side");
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
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (playerIn.isSneaking())
		{
			TileEntityMachineStarMap starMap = (TileEntityMachineStarMap)worldIn.getTileEntity(pos);
			starMap.zoom();
			return true;
		}
		else
		{
			return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
		}
	}

	@Override
	public Class<TileEntityMachineStarMap> getTileEntityClass()
	{
		return TileEntityMachineStarMap.class;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state)
	{
		return new TileEntityMachineStarMap();
	}

	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
}
