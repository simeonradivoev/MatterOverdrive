package matteroverdrive.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
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
public class BlockDecorativeColored extends BlockDecorative
{
	public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);

	public BlockDecorativeColored(Material material, String name, float hardness, int harvestLevel, float resistance, int mapColor)
	{
		super(material, name, hardness, harvestLevel, resistance, mapColor);
	}

	public static void registerRecipes()
	{

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(@Nonnull Item item, CreativeTabs tab, List<ItemStack> list)
	{
		for (EnumDyeColor color : EnumDyeColor.values())
		{
			list.add(new ItemStack(item, 1, color.getMetadata()));
		}
	}

	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return getStateFromMeta(meta);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(COLOR).getMetadata();
	}

	@Nonnull
	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(meta));
	}

	@Nonnull
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, COLOR);
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return state.getValue(COLOR).getMetadata();
	}

	@Nonnull
	@Override
	@Deprecated
	public MapColor getMapColor(IBlockState state)
	{
		return (state.getValue(COLOR)).getMapColor();
	}
}
