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
 * Created by Simeon on 8/12/2015.
 */
public class DialogShotFromBehind extends DialogShot
{
    float distance;
    float sideOffset;

    public DialogShotFromBehind(float distance, float sideOffset)
    {
        this.distance = distance;
        this.sideOffset = sideOffset;
    }

    @Override
    public boolean positionCamera(EntityLivingBase active, EntityLivingBase other, float ticks, EntityRendererConversation rendererConversation)
    {
        Vec3 look = rendererConversation.getLook(other, active, ticks);
        double lookDistance = look.lengthVector();
        look.yCoord = 0;
        look = look.normalize();
        Vec3 left = look.crossProduct(Vec3.createVectorHelper(0, 1, 0));
        Vec3 pos = rendererConversation.getPosition(other, ticks, true).addVector((left.xCoord  * sideOffset) / lookDistance,(left.yCoord * sideOffset) / lookDistance,(left.zCoord * sideOffset) / lookDistance);
        MovingObjectPosition position = MOPhysicsHelper.rayTrace(pos, other.worldObj, distance, ticks, null, true, false, look, other);
        if (position != null)
        {
            pos = position.hitVec;
        }else
        {
            pos.addVector(look.xCoord * distance, look.yCoord * distance, look.zCoord * distance);
        }
        rendererConversation.setCameraPosition(pos);
        Vec3 rotationLook = pos.subtract(rendererConversation.getPosition(active, ticks, true)).normalize();
        rendererConversation.rotateCameraYawTo(rotationLook, -90);
        rendererConversation.setCameraPitch(0);
        return true;
    }
}
