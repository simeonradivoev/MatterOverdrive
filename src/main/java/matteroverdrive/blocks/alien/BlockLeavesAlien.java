package matteroverdrive.blocks.alien;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by Simeon on 2/24/2016.
 */
public class BlockLeavesAlien extends BlockLeaves
{
    public static final PropertyEnum<BlockLeavesAlien.EnumType> VARIANT = PropertyEnum.<BlockLeavesAlien.EnumType>create("variant", BlockLeavesAlien.EnumType.class);

    public BlockLeavesAlien()
    {
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.OAK).withProperty(CHECK_DECAY, Boolean.valueOf(true)).withProperty(DECAYABLE, Boolean.valueOf(true)));
    }

    @Override
    public int getLightValue(IBlockState blockState,IBlockAccess world, BlockPos pos)
    {
        IBlockState block = world.getBlockState(pos);
        if (block.getBlock() != this)
        {
            return block.getBlock().getLightValue(blockState,world, pos);
        }
        EnumType type = block.getValue(VARIANT);
        if (type.equals(EnumType.GLOWING))
        {
            return 15;
        }
        return getLightValue(blockState);
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
    {
        IBlockState blockState = worldIn.getBlockState(pos);
        if (blockState.getBlock() == this)
        {
            EnumType type = blockState.getValue(VARIANT);
            if (type.equals(EnumType.GLOWING))
            {
                return 0xffffff;
            }else if (type.equals(EnumType.BUSH))
            {
                return 0xe379f5;
            }
        }
        return BiomeColorHelper.getFoliageColorAtPos(worldIn, pos);
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {

    }

    public int damageDropped(IBlockState state)
    {
        return state.getValue(VARIANT).getMetadata();
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
    {
        for (EnumType type : EnumType.META_LOOKUP)
        {
            list.add(new ItemStack(itemIn, 1, type.getMetadata()));
        }
    }

    protected ItemStack createStackedBlock(IBlockState state)
    {
        return new ItemStack(Item.getItemFromBlock(this), 1, state.getValue(VARIANT).getMetadata());
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT, this.getAlienWoodType(meta)).withProperty(DECAYABLE, Boolean.valueOf((meta & 4) == 0)).withProperty(CHECK_DECAY, Boolean.valueOf((meta & 8) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | state.getValue(VARIANT).getMetadata();

        if (!((Boolean)state.getValue(DECAYABLE)).booleanValue())
        {
            i |= 4;
        }

        if (((Boolean)state.getValue(CHECK_DECAY)).booleanValue())
        {
            i |= 8;
        }

        return i;
    }

    public EnumType getAlienWoodType(int meta)
    {
        return EnumType.byMetadata((meta & 3));
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {VARIANT, CHECK_DECAY, DECAYABLE});
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te,ItemStack itemStack)
    {
        {
            super.harvestBlock(worldIn, player, pos, state, te,itemStack);
        }
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    {
        IBlockState state = world.getBlockState(pos);
        return new java.util.ArrayList(java.util.Arrays.asList(new ItemStack(this, 1, state.getValue(VARIANT).getMetadata())));
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return Blocks.leaves2.isOpaqueCube(state);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return Blocks.leaves2.getBlockLayer();
    }

    @Override
    public BlockPlanks.EnumType getWoodType(int meta)
    {
        return BlockPlanks.EnumType.OAK;
    }

    public enum EnumType implements IStringSerializable
    {
        OAK(0, "oak", MapColor.woodColor),
        GLOWING(1, "glowing", MapColor.obsidianColor),
        BUSH(2,"bush",MapColor.redColor);

        private static final EnumType[] META_LOOKUP = new EnumType[values().length];
        private final int meta;
        private final String name;
        private final String unlocalizedName;
        private final MapColor field_181071_k;

        EnumType(int p_i46388_3_, String p_i46388_4_, MapColor p_i46388_5_)
        {
            this(p_i46388_3_, p_i46388_4_, p_i46388_4_, p_i46388_5_);
        }

        EnumType(int p_i46389_3_, String p_i46389_4_, String p_i46389_5_, MapColor p_i46389_6_)
        {
            this.meta = p_i46389_3_;
            this.name = p_i46389_4_;
            this.unlocalizedName = p_i46389_5_;
            this.field_181071_k = p_i46389_6_;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public MapColor func_181070_c()
        {
            return this.field_181071_k;
        }

        public String toString()
        {
            return this.name;
        }

        public static EnumType byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public String getName()
        {
            return this.name;
        }

        public String getUnlocalizedName()
        {
            return this.unlocalizedName;
        }

        static
        {
            for (EnumType blockplanks$enumtype : values())
            {
                META_LOOKUP[blockplanks$enumtype.getMetadata()] = blockplanks$enumtype;
            }
        }
    }
}
