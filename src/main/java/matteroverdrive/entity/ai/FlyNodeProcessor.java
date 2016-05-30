package matteroverdrive.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

/**
 * Created by Simeon on 1/22/2016.
 */
public class FlyNodeProcessor extends NodeProcessor
{

	public void postProcess()
	{
		super.postProcess();
	}

	/**
	 * Returns given entity's position as PathPoint
	 */
	public PathPoint getPathPointTo(Entity entityIn)
	{
		return this.openPoint(MathHelper.floor_double(entityIn.getEntityBoundingBox().minX), MathHelper.floor_double(entityIn.getEntityBoundingBox().maxY + 0.5), MathHelper.floor_double(entityIn.getEntityBoundingBox().minZ));
	}

	/**
	 * Returns PathPoint for given coordinates
	 */
	public PathPoint getPathPointToCoords(Entity entityIn, double x, double y, double target)
	{
		return this.openPoint(MathHelper.floor_double(x - (double)(entityIn.width / 2.0F)), MathHelper.floor_double(y + 0.5D), MathHelper.floor_double(target - (double)(entityIn.width / 2.0F)));
	}

	public int findPathOptions(PathPoint[] pathOptions, Entity entityIn, PathPoint currentPoint, PathPoint targetPoint, float maxDistance)
	{
		int i = 0;

		for (EnumFacing enumfacing : EnumFacing.values())
		{
			PathPoint pathpoint = this.func_186328_b(currentPoint.xCoord + enumfacing.getFrontOffsetX(), currentPoint.yCoord + enumfacing.getFrontOffsetY(), currentPoint.zCoord + enumfacing.getFrontOffsetZ());

			if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance)
			{
				pathOptions[i++] = pathpoint;
			}
		}

		return i;
	}

	@Override
	public PathNodeType getPathNodeType(IBlockAccess x, int y, int z, int p_186330_4_)
	{
		return PathNodeType.OPEN;
	}

	@Override
	public PathNodeType getPathNodeType(IBlockAccess blockaccessIn, int x, int y, int z, EntityLiving entitylivingIn, int xSize, int ySize, int zSize, boolean canBreakDoorsIn, boolean canEnterDoorsIn)
	{
		return PathNodeType.OPEN;
	}

	@Override
	public int findPathOptions(PathPoint[] p_186320_1_, PathPoint p_186320_2_, PathPoint p_186320_3_, float p_186320_4_)
	{
		int i = 0;

		for (EnumFacing enumfacing : EnumFacing.values())
		{
			PathPoint pathpoint = this.func_186328_b(p_186320_2_.xCoord + enumfacing.getFrontOffsetX(), p_186320_2_.yCoord + enumfacing.getFrontOffsetY(), p_186320_2_.zCoord + enumfacing.getFrontOffsetZ());

			if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(p_186320_3_) < p_186320_4_)
			{
				p_186320_1_[i++] = pathpoint;
			}
		}

		return i;
	}

	@Override
	public PathPoint getStart()
	{
		return this.openPoint(MathHelper.floor_double(this.entity.getEntityBoundingBox().minX), MathHelper.floor_double(this.entity.getEntityBoundingBox().minY + 0.5D), MathHelper.floor_double(this.entity.getEntityBoundingBox().minZ));
	}

	@Override
	public PathPoint getPathPointToCoords(double p_186325_1_, double p_186325_3_, double p_186325_5_)
	{
		return this.openPoint(MathHelper.floor_double(p_186325_1_ - (double)(this.entity.width / 2.0F)), MathHelper.floor_double(p_186325_3_ + 0.5D), MathHelper.floor_double(p_186325_5_ - (double)(this.entity.width / 2.0F)));
	}

	/**
	 * Returns a point that the entity can safely move to
	 */
	private PathPoint func_186328_b(int x, int y, int z)
	{
		int i = this.func_186327_c(x, y, z);
		return i == -1 ? this.openPoint(x, y, z) : null;
	}

	private int func_186327_c(int x, int y, int z)
	{
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

		for (int i = x; i < x + this.entitySizeX; ++i)
		{
			for (int j = y; j < y + this.entitySizeY; ++j)
			{
				for (int k = z; k < z + this.entitySizeZ; ++k)
				{
					IBlockState state = this.blockaccess.getBlockState(blockpos$mutableblockpos.setPos(i, j, k));
					Block block = state.getBlock();

					if (state.getMaterial() != Material.AIR)
					{
						return 0;
					}
				}
			}
		}

		return -1;
	}
}
