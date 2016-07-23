package matteroverdrive.blocks.alien;

import matteroverdrive.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Simeon on 2/24/2016.
 */
public class BlockFlowerAlien extends BlockBush
{
	public static final PropertyEnum<EnumAlienFlowerType> TYPE = PropertyEnum.create("type", EnumAlienFlowerType.class);

	public BlockFlowerAlien(String name)
	{
		setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
		this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumAlienFlowerType.METAREX));
	}

	/**
	 * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
	 * returns the metadata of the dropped item based on the old metadata of the block.
	 */
	public int damageDropped(IBlockState state)
	{
		return state.getValue(TYPE).getMeta();
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
	{
		for (EnumAlienFlowerType type : EnumAlienFlowerType.values())
		{
			list.add(new ItemStack(itemIn, 1, type.getMeta()));
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
		return this.getDefaultState().withProperty(TYPE, EnumAlienFlowerType.values()[meta]);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(TYPE).getMeta();
	}

	@Nonnull
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, TYPE);
	}

	/**
	 * Get the OffsetType for this Block. Determines if the model is rendered slightly offset.
	 */
	@Nonnull
	@Override
	@SideOnly(Side.CLIENT)
	public Block.EnumOffsetType getOffsetType()
	{
		return Block.EnumOffsetType.XZ;
	}

	public enum EnumAlienFlowerType implements IStringSerializable
	{
		ELOWAN(0, "elowan"),
		BLOOD_ORCHID(1, "blood_orchid"),
		KATTERPOD(2, "katterpod"),
		LASHER(3, "lasher"),
		METAREX(4, "metarex"),
		SAP_SAC(5, "sap_sac"),
		NIRNROOT(6, "nirnroot");


		private final int meta;
		private final String name;
		private final String unlocalizedName;

		EnumAlienFlowerType(int meta, String name)
		{
			this(meta, name, name);
		}

		EnumAlienFlowerType(int meta, String name, String unlocalizedName)
		{
			this.meta = meta;
			this.name = name;
			this.unlocalizedName = unlocalizedName;
		}

		public static EnumAlienFlowerType byMetadata(int meta)
		{
			return values()[meta];
		}

		public int getMeta()
		{
			return this.meta;
		}

		public String toString()
		{
			return this.name;
		}

		public String getName()
		{
			return this.name;
		}

		public String getUnlocalizedName()
		{
			return this.unlocalizedName;
		}
	}
}
