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

package matteroverdrive.client.render.conversation;

import matteroverdrive.util.MOPhysicsHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

/**
 * Created by Simeon on 8/9/2015.
 */
public class DialogShotWide extends DialogShot
{
    float distance;
    float heightOffset;
    boolean oppositeSide;
    public DialogShotWide(float heightOffset, boolean oppositeSide,float distance)
    {
        this.heightOffset = heightOffset;
        this.oppositeSide = oppositeSide;
        this.distance = distance;
    }

    @Override
    public boolean positionCamera(EntityLivingBase active, EntityLivingBase other, float ticks, EntityRendererConversation rendererConversation)
    {
        Vec3 centerDir = rendererConversation.getPosition(active, ticks,false).addVector(0,-active.getEyeHeight() + heightOffset, 0).subtract(rendererConversation.getPosition(other, ticks,false).addVector(0, -other.getEyeHeight() + heightOffset, 0));
        double distance = centerDir.lengthVector()/2 * this.distance;
        Vec3 center = rendererConversation.getPosition(active, ticks,false).addVector(centerDir.xCoord / 2, centerDir.yCoord / 2, centerDir.zCoord / 2);
        Vec3 centerCross = centerDir.normalize().crossProduct(Vec3.createVectorHelper(0, oppositeSide ? -1 : 1, 0)).normalize();
        MovingObjectPosition hit = MOPhysicsHelper.rayTraceForBlocks(center, active.worldObj, distance, ticks, null, true, true, centerCross);
        Vec3 pos = center.addVector(centerCross.xCoord * distance, centerCross.yCoord * distance, centerCross.zCoord * distance);
        if (hit != null) {
            pos = hit.hitVec;
        }

        rendererConversation.setCameraPosition(pos.xCoord, pos.yCoord, pos.zCoord);
        rendererConversation.rotateCameraYawTo(centerCross, 90);
        return true;
    }
}
