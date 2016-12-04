package matteroverdrive.entity.ai;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * Created by Simeon on 1/22/2016.
 */
public class EntityAIFollowCreator<T extends EntityLiving & IEntityOwnable> extends EntityAIBase
{
	World world;
	float maxDist;
	float minDist;
	private T pet;
	private Entity theOwner;
	private double followSpeed;
	private PathNavigate petPathfinder;
	private int timeToRecalcPath;

	public EntityAIFollowCreator(T pet, double followSpeed, float minDist, float maxDist)
	{
		this.pet = pet;
		this.world = pet.worldObj;
		this.followSpeed = followSpeed;
		this.petPathfinder = pet.getNavigator();
		this.minDist = minDist;
		this.maxDist = maxDist;
		this.setMutexBits(3);

		if (!(pet.getNavigator() instanceof PathNavigateFly))
		{
			throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
		}
	}

	public boolean shouldExecute()
	{
		Entity entity = this.pet.getOwner();

		if (entity == null)
		{
			return false;
		}
		else if (entity instanceof EntityPlayer && ((EntityPlayer)entity).isSpectator())
		{
			return false;
		}
		else if (this.pet.getDistanceSqToEntity(entity) < (double)(this.minDist * this.minDist))
		{
			return false;
		}
		else
		{
			this.theOwner = entity;
			return true;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting()
	{
		return !this.petPathfinder.noPath() && this.pet.getDistanceSqToEntity(this.theOwner) > (double)(this.maxDist * this.maxDist);
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting()
	{
		this.timeToRecalcPath = 0;
	}

	/**
	 * Resets the task
	 */
	public void resetTask()
	{
		this.theOwner = null;
		this.petPathfinder.clearPathEntity();
	}

	private boolean func_181065_a(BlockPos pos)
	{
		IBlockState state = this.world.getBlockState(pos);
		return state.getBlock() == Blocks.AIR || !state.isFullCube();
	}

	/**
	 * Updates the task
	 */
	public void updateTask()
	{
		this.pet.getLookHelper().setLookPositionWithEntity(this.theOwner, 10.0F, (float)this.pet.getVerticalFaceSpeed());

		if (--this.timeToRecalcPath <= 0)
		{
			this.timeToRecalcPath = 10;

			if (!this.petPathfinder.tryMoveToXYZ(this.theOwner.posX, theOwner.posY + theOwner.getEyeHeight(), theOwner.posZ + 2, this.followSpeed))
			{
				if (!this.pet.getLeashed())
				{
					if (this.pet.getDistanceSqToEntity(this.theOwner) >= 144.0D)
					{
						int x = MathHelper.floor_double(this.theOwner.posX) - 1;
						int z = MathHelper.floor_double(this.theOwner.posZ) - 1;
						int y = MathHelper.floor_double(this.theOwner.getEntityBoundingBox().maxY);

						for (int xMov = 0; xMov <= 3; ++xMov)
						{
							for (int zMov = 0; zMov <= 3; ++zMov)
							{
								if ((xMov < 1 || zMov < 1 || xMov > 2 || zMov > 2) && this.func_181065_a(new BlockPos(x + xMov, y, z + zMov)) && this.func_181065_a(new BlockPos(x + xMov, y + 1, z + zMov)))
								{
									this.pet.setLocationAndAngles((double)((float)(x + xMov) + 0.5F), (double)y, (double)((float)(z + zMov) + 0.5F), this.pet.rotationYaw, this.pet.rotationPitch);
									this.petPathfinder.clearPathEntity();
									return;
								}
							}
						}
					}
				}
			}
		}
	}
}
