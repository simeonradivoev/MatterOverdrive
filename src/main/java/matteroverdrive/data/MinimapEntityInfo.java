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

package matteroverdrive.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 9/7/2015.
 */
public class MinimapEntityInfo
{
	boolean isAttacking;
	int entityID;

	public MinimapEntityInfo()
	{
	}

	public MinimapEntityInfo(EntityLivingBase entityLivingBase, EntityPlayer player)
	{
		if (entityLivingBase instanceof EntityLiving && ((EntityLiving)entityLivingBase).getAttackTarget() != null)
		{
			isAttacking = ((EntityLiving)entityLivingBase).getAttackTarget().equals(player);
		}

		entityID = entityLivingBase.getEntityId();
	}

	public static boolean hasInfo(EntityLivingBase entityLivingBase, EntityPlayer player)
	{
		if (entityLivingBase instanceof EntityLiving && ((EntityLiving)entityLivingBase).getAttackTarget() != null)
		{
			return ((EntityLiving)entityLivingBase).getAttackTarget().equals(player);
		}
		return false;
	}

	public MinimapEntityInfo writeToBuffer(ByteBuf buf)
	{
		buf.writeBoolean(isAttacking);
		buf.writeInt(entityID);
		return this;
	}

	public MinimapEntityInfo readFromBuffer(ByteBuf buf)
	{
		isAttacking = buf.readBoolean();
		entityID = buf.readInt();
		return this;
	}

	public int getEntityID()
	{
		return entityID;
	}

	public void setEntityID(int entityID)
	{
		this.entityID = entityID;
	}

	public boolean isAttacking()
	{
		return isAttacking;
	}

	public void setIsAttacking(boolean isAttacking)
	{
		this.isAttacking = isAttacking;
	}
}
