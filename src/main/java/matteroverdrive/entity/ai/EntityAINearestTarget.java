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

import com.google.common.base.Predicate;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Created by Simeon on 12/10/2015.
 */
public class EntityAINearestTarget<T extends EntityLivingBase> extends EntityAITarget
{
	private final Class targetClass;
	private final int targetChance;
	private final EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;
	private final Predicate<T> targetEntitySelector;
	private EntityLivingBase targetEntity;

	public EntityAINearestTarget(EntityCreature theEntity, Class targetClass, int targetChance, boolean shouldCheckSight, boolean nearbyOnly, final Predicate<T> predicate)
	{
		super(theEntity, shouldCheckSight, nearbyOnly);
		this.targetClass = targetClass;
		this.targetChance = targetChance;
		this.theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter(theEntity);
		this.setMutexBits(1);
		this.targetEntitySelector = new Predicate<T>()
		{
			private static final String __OBFID = "CL_00001621";

			/**
			 * Return whether the specified entity is applicable to this filter.
			 */
			public boolean apply(@Nullable T entity)
			{
				return !(entity instanceof EntityLivingBase) ? false : (entity != null && !predicate.apply(entity) ? false : EntityAINearestTarget.this.isSuitableTarget(entity, false));
			}
		};
	}

	public boolean shouldExecute()
	{
		if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0)
		{
			return false;
		}
		else
		{
			double d0 = this.getTargetDistance();
			List list = this.taskOwner.worldObj.getEntitiesWithinAABB(this.targetClass, this.taskOwner.getEntityBoundingBox().expand(d0, 4.0D, d0), this.targetEntitySelector);
			Collections.sort(list, this.theNearestAttackableTargetSorter);

			if (list.isEmpty())
			{
				return false;
			}
			else
			{
				this.targetEntity = (EntityLivingBase)list.get(0);
				return true;
			}
		}
	}

	public void startExecuting()
	{
		this.taskOwner.setAttackTarget(this.targetEntity);
		super.startExecuting();
	}
}
