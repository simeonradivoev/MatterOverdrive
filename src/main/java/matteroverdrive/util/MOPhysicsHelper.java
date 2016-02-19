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

package matteroverdrive.util;

import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Simeon on 3/7/2015.
 */
public class MOPhysicsHelper
{
    public  static boolean insideBounds(Vec3 pos,AxisAlignedBB bounds)
    {
        return  bounds.minX <= pos.xCoord && bounds.minY <= pos.yCoord && bounds.minZ <= pos.zCoord && bounds.maxX >= pos.xCoord && bounds.maxY >= pos.yCoord && bounds.maxZ >= pos.zCoord;
    }

    public static MovingObjectPosition rayTrace(EntityLivingBase viewer,World world,double distance,float ticks,Vec3 offset)
    {
        return rayTrace(viewer,world,distance,ticks,offset,false,false);
    }
    public static MovingObjectPosition rayTrace(EntityLivingBase viewer,World world,double distance,float ticks,Vec3 offset,boolean checkBlockCollision,boolean onlySolid)
    {
        return rayTrace(viewer, world, distance, ticks, offset, checkBlockCollision, onlySolid, null);
    }
    public static MovingObjectPosition rayTrace(EntityLivingBase viewer,World world,double distance,float ticks,Vec3 offset,boolean checkBlockCollision,boolean onlySolid,Vec3 dir)
    {
        return rayTrace(getPosition(viewer,ticks),world,distance,ticks,offset,checkBlockCollision,onlySolid,dir,viewer);
    }

    public static MovingObjectPosition rayTrace(Vec3 fromPos,World world,double distance,float ticks,Vec3 offset,boolean checkBlockCollision,boolean onlySolid,Vec3 dir,EntityLivingBase viewer)
    {
        MovingObjectPosition objectMouseOver = null;
        Entity pointedEntity = null;

        if (world != null)
        {
            if (dir == null)
                dir = viewer.getLook(ticks);

            objectMouseOver = MOPhysicsHelper.rayTraceForBlocks(fromPos,world, distance, ticks,offset,checkBlockCollision,onlySolid,dir);
            double d1 = distance;
            Vec3 vec3 = fromPos;
            if (offset != null)
                vec3 = vec3.addVector(offset.xCoord, offset.yCoord, offset.zCoord);

            if (objectMouseOver != null)
            {
                d1 = objectMouseOver.hitVec.distanceTo(vec3);
            }

            Vec3 vec32 = vec3.addVector(dir.xCoord * distance, dir.yCoord * distance, dir.zCoord * distance);
            Vec3 vec33 = null;
            float f1 = 1.0F;
            List list = world.getEntitiesWithinAABBExcludingEntity(viewer, viewer.getEntityBoundingBox().addCoord(dir.xCoord * distance, dir.yCoord * distance, dir.zCoord * distance).expand((double) f1, (double) f1, (double) f1));
            double d2 = d1;

            for (Object aList : list)
            {
                Entity entity = (Entity) aList;

                if (entity.canBeCollidedWith())
                {
                    float f2 = entity.getCollisionBorderSize();
                    AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand((double) f2, (double) f2, (double) f2);
                    MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                    if (axisalignedbb.isVecInside(vec3))
                    {
                        if (0.0D < d2 || d2 == 0.0D)
                        {
                            pointedEntity = entity;
                            vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                            d2 = 0.0D;
                        }
                    } else if (movingobjectposition != null)
                    {
                        double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                        if (d3 < d2 || d2 == 0.0D)
                        {
                            pointedEntity = entity;
                            vec33 = movingobjectposition.hitVec;
                            d2 = d3;
                        }
                    }
                }
            }

            if (pointedEntity != null && d2 < d1)
            {
                if (objectMouseOver != null)
                {
                    objectMouseOver.typeOfHit = MovingObjectPosition.MovingObjectType.ENTITY;
                    objectMouseOver.entityHit = pointedEntity;
                    objectMouseOver.hitVec = vec33;
                }
                else
                {
                    objectMouseOver = new MovingObjectPosition(pointedEntity, vec33);
                }

            }
        }
        return objectMouseOver;
    }


    public static MovingObjectPosition rayTraceForBlocks(EntityLivingBase viewer,World world,double distance,float ticks,Vec3 offset,boolean collisionCheck,boolean onlySolid)
    {
        return rayTraceForBlocks(viewer,world,distance,ticks,offset,collisionCheck,onlySolid,null);
    }
    public static MovingObjectPosition rayTraceForBlocks(EntityLivingBase viewer,World world,double distance,float ticks,Vec3 offset,boolean collisionCheck,boolean onlySolid,Vec3 dir)
    {
        return rayTraceForBlocks(getPosition(viewer,ticks),world,distance,ticks,offset,collisionCheck,onlySolid,dir == null ? viewer.getLook(ticks) : dir);
    }
    public static MovingObjectPosition rayTraceForBlocks(Vec3 fromPosition,World world,double distance,float ticks,Vec3 offset,boolean collisionCheck,boolean onlySolid,Vec3 dir)
    {
        Vec3 vec3 = new Vec3(fromPosition.xCoord,fromPosition.yCoord,fromPosition.zCoord);
        if (offset != null)
        {
            vec3 = vec3.addVector(offset.xCoord, offset.yCoord, offset.zCoord);
        }
        Vec3 vec32 = vec3.addVector(dir.xCoord * distance, dir.yCoord * distance, dir.zCoord * distance);
        return world.rayTraceBlocks(vec3, vec32, collisionCheck, onlySolid, true);
    }

    @SideOnly(Side.CLIENT)
    public static MovingObjectPosition mouseRaytraceForBlocks(int mouseX,int mouseY,int width,int height,EntityLivingBase viewer,World world,boolean collisionCheck,boolean onlySolid)
    {
        Vec3 dir = MOMathHelper.mouseToWorldRay(mouseX, mouseY, width, height);
        Vec3 vec3 = viewer.getPositionEyes(1);
        Vec3 vec32 = vec3.addVector(dir.xCoord * 32, dir.yCoord * 32, dir.zCoord * 32);
        return world.rayTraceBlocks(vec3, vec32, false, false, true);
    }

    public static Vec3 getPosition(EntityLivingBase entity, float p_70666_1_)
    {
        if (p_70666_1_ == 1.0F)
        {
            return new Vec3(entity.posX, entity.posY, entity.posZ);
        }
        else
        {
            double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double)p_70666_1_;
            double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double)p_70666_1_;
            double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double)p_70666_1_;
            return new Vec3(d0, d1, d2);
        }
    }
}
