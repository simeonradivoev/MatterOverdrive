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

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

/**
 * Created by Simeon on 12/11/2015.
 */
public class EntityAIRangedRunFromMelee extends EntityAIBase
{
	Vec3d destinaton;
	private double minDistanceSq;
	private EntityCreature entity;
	private double moveSpeed;

	public EntityAIRangedRunFromMelee(EntityCreature entity, double moveSpeed)
	{
		this.entity = entity;
		this.moveSpeed = moveSpeed;
		//setMutexBits(1);
	}

	@Override
	public boolean shouldExecute()
	{
		if (this.entity.getAttackTarget() != null && this.entity.getNavigator().noPath())
		{
			double sqDistanceToTargetSq = this.entity.getDistanceSqToEntity(this.entity.getAttackTarget());
			if (sqDistanceToTargetSq + 4 < minDistanceSq)
			{
				int distanceToRun = (int)Math.sqrt(minDistanceSq - sqDistanceToTargetSq);
				destinaton = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.entity, distanceToRun, 4, new Vec3d(this.entity.getAttackTarget().posX, this.entity.getAttackTarget().posY, this.entity.getAttackTarget().posZ));
				return destinaton != null;
			}
		}
		return false;
	}

	@Override
	public void startExecuting()
	{
		if (destinaton != null)
		{
			this.entity.getNavigator().tryMoveToXYZ(destinaton.xCoord, destinaton.yCoord, destinaton.zCoord, moveSpeed);
		}
	}

	@Override
	public boolean continueExecuting()
	{
		return !this.entity.getNavigator().noPath();
	}

	public void setMinDistance(double minDistance)
	{
		this.minDistanceSq = minDistance * minDistance;
	}

	public void setMoveSpeed(double moveSpeed)
	{
		this.moveSpeed = moveSpeed;
	}
}
