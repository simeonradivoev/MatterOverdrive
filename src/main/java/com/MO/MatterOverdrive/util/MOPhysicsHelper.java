package com.MO.MatterOverdrive.util;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

/**
 * Created by Simeon on 3/7/2015.
 */
public class MOPhysicsHelper
{
    public  static boolean insideBounds(Vec3 pos,AxisAlignedBB bounds)
    {
        return  bounds.minX <= pos.xCoord && bounds.minY <= pos.yCoord && bounds.minZ <= pos.zCoord && bounds.maxX >= pos.xCoord && bounds.maxY >= pos.yCoord && bounds.maxZ >= pos.zCoord;
    }
}
