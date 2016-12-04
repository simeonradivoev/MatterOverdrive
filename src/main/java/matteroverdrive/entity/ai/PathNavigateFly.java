package matteroverdrive.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by Simeon on 1/22/2016.
 */
public class PathNavigateFly extends PathNavigate
{
	public PathNavigateFly(EntityLiving entity, World world)
	{
		super(entity, world);
	}

	@Override
	protected PathFinder getPathFinder()
	{
		return new PathFinder(new FlyNodeProcessor());
	}

	/**
	 * If on ground or swimming and can swim
	 */
	@Override
	protected boolean canNavigate()
	{
		return true;
	}

	@Override
	protected Vec3d getEntityPosition()
	{
		return new Vec3d(this.theEntity.posX, this.theEntity.posY, this.theEntity.posZ);
	}

	@Override
	protected void pathFollow()
	{
		Vec3d vec3 = this.getEntityPosition();
		float f = this.theEntity.width * this.theEntity.width;
		int i = 6;

		if (vec3.squareDistanceTo(this.currentPath.getVectorFromIndex(this.theEntity, this.currentPath.getCurrentPathIndex())) < (double)f)
		{
			this.currentPath.incrementPathIndex();
		}

		for (int j = Math.min(this.currentPath.getCurrentPathIndex() + i, this.currentPath.getCurrentPathLength() - 1); j > this.currentPath.getCurrentPathIndex(); --j)
		{
			Vec3d vec31 = this.currentPath.getVectorFromIndex(this.theEntity, j);

			if (vec31.squareDistanceTo(vec3) <= 36.0D && this.isDirectPathBetweenPoints(vec3, vec31, 0, 0, 0))
			{
				this.currentPath.setCurrentPathIndex(j);
				break;
			}
		}

		this.checkForStuck(vec3);
	}

	/**
	 * Trims path data from the end to the first sun covered block
	 */
	@Override
	protected void removeSunnyPath()
	{
		super.removeSunnyPath();
	}

	/**
	 * Returns true when an entity of specified size could safely walk in a straight line between the two points. Args:
	 * pos1, pos2, entityXSize, entityYSize, entityZSize
	 */
	@Override
	protected boolean isDirectPathBetweenPoints(Vec3d pos1, Vec3d pos2, int sizeX, int sizeY, int sizeZ)
	{
		RayTraceResult movingobjectposition = this.worldObj.rayTraceBlocks(pos1, new Vec3d(pos2.xCoord, pos2.yCoord + (double)this.theEntity.height * 0.5D, pos2.zCoord), false, true, false);
		return movingobjectposition == null || movingobjectposition.typeOfHit == RayTraceResult.Type.MISS;
	}
}
