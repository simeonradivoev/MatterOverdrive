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
	public PathPoint getPathPointTo(Entity entity)
	{
		return this.openPoint(MathHelper.floor_double(entity.getEntityBoundingBox().minX), MathHelper.floor_double(entity.getEntityBoundingBox().maxY + 0.5), MathHelper.floor_double(entity.getEntityBoundingBox().minZ));
	}

	/**
	 * Returns PathPoint for given coordinates
	 */
	public PathPoint getPathPointToCoords(Entity entity, double x, double y, double target)
	{
		return this.openPoint(MathHelper.floor_double(x - (double)(entity.width / 2.0F)), MathHelper.floor_double(y + 0.5D), MathHelper.floor_double(target - (double)(entity.width / 2.0F)));
	}

	public int findPathOptions(PathPoint[] pathOptions, Entity entity, PathPoint currentPoint, PathPoint targetPoint, float maxDistance)
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
	public PathNodeType getPathNodeType(IBlockAccess world, int x, int y, int z)
	{
		return PathNodeType.OPEN;
	}

	@Override
	public PathNodeType getPathNodeType(IBlockAccess world, int x, int y, int z, EntityLiving entity, int xSize, int ySize, int zSize, boolean canBreakDoors, boolean canEnterDoors)
	{
		return PathNodeType.OPEN;
	}

	@Override
	public int findPathOptions(PathPoint[] options, PathPoint currentPoint, PathPoint nextPoint, float maxDistance)
	{
		int i = 0;

		for (EnumFacing enumfacing : EnumFacing.values())
		{
			PathPoint pathpoint = this.func_186328_b(currentPoint.xCoord + enumfacing.getFrontOffsetX(), currentPoint.yCoord + enumfacing.getFrontOffsetY(), currentPoint.zCoord + enumfacing.getFrontOffsetZ());

			if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(nextPoint) < maxDistance)
			{
				options[i++] = pathpoint;
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
	public PathPoint getPathPointToCoords(double x, double y, double z)
	{
		return this.openPoint(MathHelper.floor_double(x - (double)(this.entity.width / 2.0F)), MathHelper.floor_double(y + 0.5D), MathHelper.floor_double(z - (double)(this.entity.width / 2.0F)));
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
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		for (int i = x; i < x + this.entitySizeX; ++i)
		{
			for (int j = y; j < y + this.entitySizeY; ++j)
			{
				for (int k = z; k < z + this.entitySizeZ; ++k)
				{
					IBlockState state = this.blockaccess.getBlockState(pos.setPos(i, j, k));
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
