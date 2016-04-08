package matteroverdrive.entity.ai;

import net.minecraft.block.Block;
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
    private T thePet;
    private Entity theOwner;
    World theWorld;
    private double followSpeed;
    private PathNavigate petPathfinder;
    private int field_75343_h;
    float maxDist;
    float minDist;

    public EntityAIFollowCreator(T thePetIn, double followSpeedIn, float minDistIn, float maxDistIn)
    {
        this.thePet = thePetIn;
        this.theWorld = thePetIn.worldObj;
        this.followSpeed = followSpeedIn;
        this.petPathfinder = thePetIn.getNavigator();
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.setMutexBits(3);

        if (!(thePetIn.getNavigator() instanceof PathNavigateFly))
        {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    public boolean shouldExecute()
    {
        Entity entity = this.thePet.getOwner();

        if (entity == null)
        {
            return false;
        }
        else if (entity instanceof EntityPlayer && ((EntityPlayer)entity).isSpectator())
        {
            return false;
        }
        else if (this.thePet.getDistanceSqToEntity(entity) < (double)(this.minDist * this.minDist))
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
        return !this.petPathfinder.noPath() && this.thePet.getDistanceSqToEntity(this.theOwner) > (double)(this.maxDist * this.maxDist);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.field_75343_h = 0;
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.theOwner = null;
        this.petPathfinder.clearPathEntity();
    }

    private boolean func_181065_a(BlockPos p_181065_1_)
    {
        IBlockState iblockstate = this.theWorld.getBlockState(p_181065_1_);
        Block block = iblockstate.getBlock();
        return block == Blocks.air ? true : !block.isFullCube(iblockstate);
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        this.thePet.getLookHelper().setLookPositionWithEntity(this.theOwner, 10.0F, (float)this.thePet.getVerticalFaceSpeed());

        if (--this.field_75343_h <= 0)
        {
            this.field_75343_h = 10;

            if (!this.petPathfinder.tryMoveToXYZ(this.theOwner.posX,theOwner.posY + theOwner.getEyeHeight(),theOwner.posZ + 2, this.followSpeed))
            {
                if (!this.thePet.getLeashed())
                {
                    if (this.thePet.getDistanceSqToEntity(this.theOwner) >= 144.0D)
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
                                    this.thePet.setLocationAndAngles((double)((float)(x + xMov) + 0.5F), (double)y, (double)((float)(z + zMov) + 0.5F), this.thePet.rotationYaw, this.thePet.rotationPitch);
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
