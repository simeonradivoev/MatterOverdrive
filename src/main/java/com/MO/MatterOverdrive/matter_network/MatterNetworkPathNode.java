package com.MO.MatterOverdrive.matter_network;

import cofh.lib.util.position.BlockPosition;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 4/26/2015.
 */
public class MatterNetworkPathNode
{
    int x;
    int y;
    int z;
    ForgeDirection port;

    public MatterNetworkPathNode(BlockPosition position)
    {
        this(position,ForgeDirection.UNKNOWN);
    }

    public MatterNetworkPathNode(BlockPosition position,ForgeDirection port)
    {
        x = position.x;
        y = position.y;
        z = position.z;
        this.port = port;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof MatterNetworkPathNode)) {
            return false;
        }
        MatterNetworkPathNode bp = (MatterNetworkPathNode) obj;
        return bp.x == x & bp.y == y & bp.z == z;
    }

    @Override
    public int hashCode()
    {
        return (x & 0xFFF) | (y & 0xFF << 8) | (z & 0xFFF << 12);
    }

    // so compiler will optimize
    public boolean equals(BlockPosition bp) {

        return bp != null && bp.x == x & bp.y == y & bp.z == z;
    }
}
