package matteroverdrive.blocks;

import matteroverdrive.blocks.includes.IMultiBlock;
import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.machines.dimensional_pylon.TileEntityMachineDimensionalPylon;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 2/4/2016.
 */
public class BlockPylon extends MOBlock implements ITileEntityProvider, IMultiBlock
{
    public static final PropertyEnum<MultiblockType> TYPE = PropertyEnum.create("type",MultiblockType.class);

    public BlockPylon(Material material, String name)
    {
        super(material, name);
        setHardness(8f);
        setLightOpacity(0);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe", 2);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getDefaultState();
    }


    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this,TYPE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(TYPE, MultiblockType.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        MultiblockType type = state.getValue(TYPE);
        return type.ordinal();
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityMachineDimensionalPylon();
    }

    @Override
    public boolean onWrenchHit(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntity pylon = world.getTileEntity(pos);
        if (pylon != null && pylon instanceof TileEntityMachineDimensionalPylon)
        {
            return ((TileEntityMachineDimensionalPylon)pylon).onWrenchHit(stack,player,world,pos,side,hitX,hitY,hitZ);
        }
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityMachineDimensionalPylon)
        {
            return ((TileEntityMachineDimensionalPylon) tileEntity).openMultiBlockGui(worldIn,playerIn);
        }

        return false;
    }

    public enum MultiblockType implements IStringSerializable
    {
        NORMAL("normal"),DUMMY("dummy"),MAIN("main");

        private final String name;

        MultiblockType(String name)
        {
            this.name = name;
        }

        @Override
        public String getName()
        {
            return name;
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        IBlockState originalBlockState = worldIn.getBlockState(pos.offset(side.getOpposite()));
        if (originalBlockState.getBlock() == this)
        {
            if (originalBlockState.getValue(TYPE) == MultiblockType.DUMMY)
                return false;

        }
        return super.shouldSideBeRendered(originalBlockState,worldIn,pos,side);
    }
}
