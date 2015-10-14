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

import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Simeon on 7/17/2015.
 * This is used by machines that can broadcast messaged over the Matter Network.
 */
public interface IMatterNetworkBroadcaster extends IMatterNetworkConnection
{
    /**
     * Gets the filter used in the broadcasted packets.
     * @return the broadcast filter.
     * This usually contains a list of Block coordinates the broadcaster can broadcast to.
     */
    NBTTagCompound getFilter();
}
