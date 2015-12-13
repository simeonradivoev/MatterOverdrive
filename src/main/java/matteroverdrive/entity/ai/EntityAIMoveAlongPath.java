/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.entity.ai;

import matteroverdrive.api.entity.IPathableMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.Vec3;

/**
 * Created by Simeon on 12/10/2015.
 */
public class EntityAIMoveAlongPath extends EntityAIBase
{
    private IPathableMob pathableMob;
    private double movePosX;
    private double movePosY;
    private double movePosZ;
    private double movementSpeed;
    private static final String __OBFID = "CL_00001598";

    public EntityAIMoveAlongPath(IPathableMob pathableMob, double moveSpeedMultiply)
    {
        this.pathableMob = pathableMob;
        this.movementSpeed = moveSpeedMultiply;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (pathableMob.getEntity().getAttackTarget() != null)
        {
            return false;
        }
        else if (pathableMob.getCurrentTarget() != null)
        {
            if (!pathableMob.getEntity().getNavigator().noPath())
                return true;

            if (pathableMob.isNearTarget(pathableMob.getCurrentTarget()))
            {
                pathableMob.onTargetReached(pathableMob.getCurrentTarget());
            }else
            {
                if (!pathableMob.getEntity().getNavigator().tryMoveToXYZ(pathableMob.getCurrentTarget().xCoord,pathableMob.getCurrentTarget().yCoord,pathableMob.getCurrentTarget().zCoord,this.movementSpeed))
                {
                    Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(pathableMob.getEntity(), 8, 2, pathableMob.getCurrentTarget());

                    if (vec3 == null)
                    {
                        return false;
                    } else
                    {
                        this.movePosX = vec3.xCoord;
                        this.movePosY = vec3.yCoord;
                        this.movePosZ = vec3.zCoord;
                        pathableMob.getEntity().getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ,this.movementSpeed);
                        return true;
                    }
                }else
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !pathableMob.getEntity().getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {

    }
}
