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

package matteroverdrive.api.network;

import matteroverdrive.matter_network.MatterNetworkPacket;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 4/19/2015.
 * Used by Matter Network Cables
 */
public interface IMatterNetworkCable extends IMatterNetworkConnection
{
    /**
     * @return is the cable valid.
     */
    boolean isValid();

    /**
     * Called when the cable needs to broadcast a packet.
     * @param packet the packet being broadcasted.
     * @param from the direction of the broadcast.
     */
    void broadcast(MatterNetworkPacket packet,ForgeDirection from);
}
