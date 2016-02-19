package matteroverdrive.blocks;

import com.google.common.collect.ImmutableList;
import matteroverdrive.blocks.includes.MOBlockContainer;
import matteroverdrive.tile.pipes.TileEntityPipe;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BlockPipe extends MOBlockContainer
{
    public static final float PIPE_MIN_POS = 0.35f;
    public static final float PIPE_MAX_POS = 0.65f;

    public static final ImmutableList<IProperty> CONNECTED_PROPERTIES = ImmutableList.copyOf(
            Stream.of(EnumFacing.VALUES)
                    .map(facing -> PropertyBool.create(facing.getName()))
                    .collect(Collectors.toList())
    );

    public static final ImmutableList<AxisAlignedBB> CONNECTED_BOUNDING_BOXES = ImmutableList.copyOf(
            Stream.of(EnumFacing.VALUES)
                    .map(facing -> {
                        Vec3i directionVec = facing.getDirectionVec();
                        return new AxisAlignedBB(
                                getMinBound(directionVec.getX()), getMinBound(directionVec.getY()), getMinBound(directionVec.getZ()),
                                getMaxBound(directionVec.getX()), getMaxBound(directionVec.getY()), getMaxBound(directionVec.getZ())
                        );
                    })
                    .collect(Collectors.toList())
    );

	public BlockPipe(Material material, String name)
	{
		super(material, name);
		this.useNeighborBrightness = true;
        this.setRotationType(-1);
	}

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, CONNECTED_PROPERTIES.toArray(new IProperty[CONNECTED_PROPERTIES.size()]));
    }

    @Override
    public IBlockState getActualState(IBlockState state,IBlockAccess world,BlockPos pos) {
        for (EnumFacing facing : EnumFacing.VALUES) {
            state = state.withProperty(CONNECTED_PROPERTIES.get(facing.getIndex()), isConnectableSide(facing, world, pos));
        }
        return state;
    }

    private static float getMinBound(int dir) {
        return dir == -1 ? 0 : PIPE_MIN_POS;
    }

    private static float getMaxBound(int dir)
    {
        return dir == 1 ? 1-PIPE_MIN_POS : PIPE_MAX_POS;
    }

    public void setBlockBounds(AxisAlignedBB bb) {
        setBlockBounds((float) bb.minX, (float) bb.minY, (float) bb.minZ, (float) bb.maxX, (float) bb.maxY, (float) bb.maxZ);
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity) {
        setBlockBounds(PIPE_MIN_POS, PIPE_MIN_POS, PIPE_MIN_POS, PIPE_MAX_POS, PIPE_MAX_POS, PIPE_MAX_POS);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);

        state = getActualState(state, worldIn, pos);

        for (EnumFacing facing : EnumFacing.VALUES) {
            if (isConnectableSide(facing,worldIn,pos)) {
                Vec3i directionVec = facing.getDirectionVec();
                AxisAlignedBB axisAlignedBB = new AxisAlignedBB(
                        PIPE_MIN_POS,PIPE_MIN_POS,PIPE_MIN_POS,
                        PIPE_MAX_POS,PIPE_MAX_POS,PIPE_MAX_POS
                ).offset(facing.getFrontOffsetX() * (PIPE_MAX_POS - PIPE_MIN_POS/2),facing.getFrontOffsetY() * (PIPE_MAX_POS - PIPE_MIN_POS/2),facing.getFrontOffsetZ() * (PIPE_MAX_POS - PIPE_MIN_POS/2));
                setBlockBounds(axisAlignedBB);
                super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
            }
        }

        setBlockBounds(0, 0, 0, 1, 1, 1);
    }

    /*@Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
    {
		float size = 0.34375f;

        float xMax = 1 - size;
        float yMax = 1 - size;
        float zMax = 1 - size;

        this.setBlockBounds(size, size, size, xMax, yMax, zMax);
        super.addCollisionBoxesToList(worldIn,pos,state,mask,list,collidingEntity);

        for(int i = 0;i < 6;i++)
        {
            EnumFacing dir = EnumFacing.values()[i];
            if(isConnectableSide(dir,worldIn,pos))
            {
	            if(dir != null)
	            {
	                float xMinNew = size + size * dir.getFrontOffsetX();
                    float xMaxNew = xMax + size * dir.getFrontOffsetX();
                    float yMinNew = size + size * dir.getFrontOffsetY();
                    float yMaxNew = yMax + size * dir.getFrontOffsetY();
                    float zMinNew = size + size * dir.getFrontOffsetZ();
                    float zMaxNew = zMax + size * dir.getFrontOffsetZ();

                    this.setBlockBounds(xMinNew, yMinNew, zMinNew, xMaxNew, yMaxNew, zMaxNew);
                    super.addCollisionBoxesToList(worldIn,pos,state,mask,list,collidingEntity);
	            }
            }
        }
    }*/

	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
    {
        float size = 0.34375f;

        float xMin = size;
        float yMin = size;
        float zMin = size;

        float xMax = 1 - size;
        float yMax = 1 - size;
        float zMax = 1 - size;

        for(int i = 0;i < 6;i++)
        {
            EnumFacing dir = EnumFacing.VALUES[i];
            if(isConnectableSide(dir, world, pos))
            {
	            if(dir != null)
	            {
	                if (dir.getFrontOffsetX() < 0) {
	                    xMin = 0;
	                } else if (dir.getFrontOffsetX() > 0) {
	                    xMax = 1;
	                }

	                if (dir.getFrontOffsetY() < 0) {
	                    yMin = 0;
	                } else if (dir.getFrontOffsetY() > 0) {
	                    yMax = 1;
	                }

	                if (dir.getFrontOffsetZ() < 0) {
	                    zMin = 0;
	                } else if (dir.getFrontOffsetZ() > 0) {
	                    zMax = 1;
	                }
	            }
            }
        }

        this.setBlockBounds(xMin, yMin, zMin, xMax, yMax, zMax);
    }

    public boolean isConnectableSide(EnumFacing dir, IBlockAccess world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityPipe)
        {
            return ((TileEntityPipe)tileEntity).isConnectableSide(dir);
        }
       return false;
    }

    @Override
	public boolean isOpaqueCube()
	{
		return false;
	}

    @Override
    public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        return false;
    }

    @Override
    public boolean isFullCube()
    {
        return false;
    }

}
