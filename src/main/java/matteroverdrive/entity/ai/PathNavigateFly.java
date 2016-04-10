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
	public PathNavigateFly(EntityLiving entitylivingIn, World worldIn)
	{
		super(entitylivingIn, worldIn);
	}

	protected PathFinder getPathFinder()
	{
		return new PathFinder(new FlyNodeProcessor());
	}

	/**
	 * If on ground or swimming and can swim
	 */
	protected boolean canNavigate()
	{
		return true;
	}

	protected Vec3d getEntityPosition()
	{
		return new Vec3d(this.theEntity.posX, this.theEntity.posY, this.theEntity.posZ);
	}

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
	protected void removeSunnyPath()
	{
		super.removeSunnyPath();
	}

	/**
	 * Returns true when an entity of specified size could safely walk in a straight line between the two points. Args:
	 * pos1, pos2, entityXSize, entityYSize, entityZSize
	 */
	protected boolean isDirectPathBetweenPoints(Vec3d posVec31, Vec3d posVec32, int sizeX, int sizeY, int sizeZ)
	{
		RayTraceResult movingobjectposition = this.worldObj.rayTraceBlocks(posVec31, new Vec3d(posVec32.xCoord, posVec32.yCoord + (double)this.theEntity.height * 0.5D, posVec32.zCoord), false, true, false);
		return movingobjectposition == null || movingobjectposition.typeOfHit == RayTraceResult.Type.MISS;
	}
}
