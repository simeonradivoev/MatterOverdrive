package matteroverdrive.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_)
    {
        p_149666_3_.add(new ItemStack(p_149666_1_, 1, 0));
        p_149666_3_.add(new ItemStack(p_149666_1_, 1, 1));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(ROTATED) ? 1 : 0;
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getStateFromMeta(meta);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(ROTATED, meta == 1 ? true : false);
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, ROTATED);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return state.getValue(ROTATED) ? 1 : 0;
    }
}
