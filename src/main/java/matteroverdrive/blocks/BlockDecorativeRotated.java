package matteroverdrive.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Simeon on 1/24/2016.
 */
public class BlockDecorativeRotated extends BlockDecorative
{
	private static final PropertyBool ROTATED = PropertyBool.create("rotated");

	public BlockDecorativeRotated(Material material, String name, float hardness, int harvestLevel, float resistance, int mapColor)
	{
		super(material, name, hardness, harvestLevel, resistance, mapColor);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(@Nonnull Item item, CreativeTabs tab, List<ItemStack> list)
	{
		list.add(new ItemStack(item, 1, 0));
		list.add(new ItemStack(item, 1, 1));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(ROTATED) ? 1 : 0;
	}

	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return getStateFromMeta(meta);
	}

	@Nonnull
	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(ROTATED, meta == 1 ? true : false);
	}

	@Nonnull
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, ROTATED);
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return state.getValue(ROTATED) ? 1 : 0;
	}
}
