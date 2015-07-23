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

import matteroverdrive.api.network.IMatterNetworkConnection;
import matteroverdrive.matter_network.MatterNetworkPacket;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Simeon on 4/27/2015.
 */
public class MatterNetworkRequestPacket extends MatterNetworkPacket
{
    int requestType;
    Object request;

    public MatterNetworkRequestPacket(){super();}
    public MatterNetworkRequestPacket(IMatterNetworkConnection sender,int requestType,ForgeDirection port,Object request)
    {
        this(sender,requestType,port,null,request);
    }
    public MatterNetworkRequestPacket(IMatterNetworkConnection sender,int requestType,ForgeDirection port,NBTTagCompound filter,Object request)
    {
        super(sender.getPosition(),port,filter);
        this.requestType = requestType;
        this.request = request;
    }

    @Override
    public boolean isValid(World world)
    {
        return true;
    }

    @Override
    public String getName()
    {
        return "Request Packet";
    }

    public int getRequestType() {
        return requestType;
    }

    public Object getRequest() {
        return request;
    }
}
