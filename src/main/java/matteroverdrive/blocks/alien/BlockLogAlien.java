package matteroverdrive.blocks.alien;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by Simeon on 2/24/2016.
 */
public class BlockLogAlien extends BlockLog
{
	public BlockLogAlien()
	{
		this.setDefaultState(this.blockState.getBaseState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
	}

	/**
	 * Get the MapColor for this Block and the given BlockState
	 */
	public MapColor getMapColor(IBlockState state)
	{
		switch ((BlockLog.EnumAxis)state.getValue(LOG_AXIS))
		{
			case X:
			case Z:
			case NONE:
			default:
				return MapColor.stoneColor;
		}
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.ACACIA.getMetadata() - 4));
		list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4));
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState iblockstate = this.getDefaultState();

		switch (meta)
		{
			case 0:
				iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
				break;
			case 4:
				iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
				break;
			case 8:
				iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
				break;
			default:
				iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
		}

		return iblockstate;
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@SuppressWarnings("incomplete-switch")
	public int getMetaFromState(IBlockState state)
	{
		int i = 0;

		switch ((BlockLog.EnumAxis)state.getValue(LOG_AXIS))
		{
			case X:
				i |= 4;
				break;
			case Z:
				i |= 8;
				break;
			case NONE:
				i |= 12;
		}

		return i;
	}

	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {LOG_AXIS});
	}

	protected ItemStack createStackedBlock(IBlockState state)
	{
		return new ItemStack(Item.getItemFromBlock(this), 1);
	}

	/**
	 * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
	 * returns the metadata of the dropped item based on the old metadata of the block.
	 */
	public int damageDropped(IBlockState state)
	{
		return 0;
	}
}
