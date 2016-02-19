package matteroverdrive.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_)
    {
        for (int i = 0;i < 16;i++)
        {
            p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
        }
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getStateFromMeta(meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(IBlockState blockState)
    {
        EnumDyeColor color = blockState.getValue(COLOR);
        return ItemDye.dyeColors[MathHelper.clamp_int(color.getMetadata(),0,ItemDye.dyeColors.length-1)];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, BlockPos pos, int renderPass)
    {
        EnumDyeColor color = world.getBlockState(pos).getValue(COLOR);
        return ItemDye.dyeColors[MathHelper.clamp_int(color.getMetadata(),0,ItemDye.dyeColors.length-1)];
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(COLOR).getMetadata();
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(meta));
    }

    @Override
    protected BlockState createBlockState()
    {
        return new BlockState(this, COLOR);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return state.getValue(COLOR).getMetadata();
    }
}
