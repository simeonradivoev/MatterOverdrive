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

package matteroverdrive.matter_network.packets;

import matteroverdrive.data.BlockPos;
import matteroverdrive.matter_network.MatterNetworkPacket;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 4/30/2015.
 */
public class MatterNetworkBroadcastPacket extends MatterNetworkPacket
{
    int broadcastType;

    public MatterNetworkBroadcastPacket(){super();}
    public MatterNetworkBroadcastPacket(BlockPos position, int broadcastType, ForgeDirection port)
    {
        super(position,port);
        this.broadcastType = broadcastType;
    }

    @Override
    public boolean isValid(World world)
    {
        return true;
    }

    @Override
    public String getName()
    {
        return "Broadcast Packet";
    }

    public int getBroadcastType() {
        return broadcastType;
    }

    public void setBroadcastType(int broadcastType) {
        this.broadcastType = broadcastType;
    }
}
