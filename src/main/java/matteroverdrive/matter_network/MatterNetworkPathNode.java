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

package matteroverdrive.matter_network;

import matteroverdrive.data.BlockPos;
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

    public MatterNetworkPathNode(BlockPos position)
    {
        this(position,ForgeDirection.UNKNOWN);
    }

    public MatterNetworkPathNode(BlockPos position, ForgeDirection port)
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
    public boolean equals(BlockPos bp) {

        return bp != null && bp.x == x & bp.y == y & bp.z == z;
    }
}
