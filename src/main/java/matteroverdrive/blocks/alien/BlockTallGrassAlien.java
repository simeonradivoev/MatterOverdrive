package matteroverdrive.blocks.alien;

import matteroverdrive.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 2/24/2016.
 */
public class BlockTallGrassAlien extends BlockBush implements net.minecraftforge.common.IShearable
{
	public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 4);

	public BlockTallGrassAlien(String name)
	{
		super(Material.VINE);
		this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, 0));
		setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
		float f = 0.4F;
		// TODO: 3/26/2016 Find how to set block bounds
		//this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.8F, 0.5F + f);
	}

	@SideOnly(Side.CLIENT)
	public int getBlockColor()
	{
		return ColorizerGrass.getGrassColor(0.5D, 1.0D);
	}

	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
	{
		return super.canBlockStay(worldIn, pos, state);
	}

	/**
	 * Whether this Block can be replaced directly by other blocks (true for e.g. tall grass)
	 */
	public boolean isReplaceable(World worldIn, BlockPos pos)
	{
		return true;
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return null;
	}

	/**
	 * Get the quantity dropped based on the given fortune level
	 */
	public int quantityDroppedWithBonus(int fortune, @Nonnull Random random)
	{
		return 1 + random.nextInt(fortune * 2 + 1);
	}

	public void harvestBlock(@Nonnull World worldIn, EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, TileEntity te, ItemStack itemStack)
	{
		super.harvestBlock(worldIn, player, pos, state, te, itemStack);
	}

	// TODO: 3/26/2016 Find how to set block color
	/*@SideOnly(Side.CLIENT)
	public int getRenderColor(IBlockState state)
    {
        if (state.getBlock() != this)
        {
            return super.getRenderColor(state);
        }
        else
        {
            return ColorizerGrass.getGrassColor(0.5D, 1.0D);
        }
    }*/

	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
	{
		return worldIn.getBiome(pos).getGrassColorAtPos(pos);
	}

	public int getDamageValue(World worldIn, BlockPos pos)
	{
		IBlockState iblockstate = worldIn.getBlockState(pos);
		return iblockstate.getBlock().getMetaFromState(iblockstate);
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(@Nonnull Item item, CreativeTabs tab, List<ItemStack> list)
	{
		for (int i = 1; i < 3; ++i)
		{
			list.add(new ItemStack(item, 1, i));
		}
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Nonnull
	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(TYPE, meta);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(TYPE);
	}

	@Nonnull
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, TYPE);
	}

	/**
	 * Get the OffsetType for this Block. Determines if the model is rendered slightly offset.
	 */
	@Nonnull
	@SideOnly(Side.CLIENT)
	public Block.EnumOffsetType getOffsetType()
	{
		return Block.EnumOffsetType.XYZ;
	}

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
		ret.add(new ItemStack(Blocks.TALLGRASS, 1, world.getBlockState(pos).getValue(TYPE)));
		return ret;
	}

	@Nonnull
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune)
	{
		List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
		if (RANDOM.nextInt(8) != 0)
		{
			return ret;
		}
		ItemStack seed = net.minecraftforge.common.ForgeHooks.getGrassSeed(RANDOM, fortune);
		if (seed != null)
		{
			ret.add(seed);
		}
		return ret;
	}
}
