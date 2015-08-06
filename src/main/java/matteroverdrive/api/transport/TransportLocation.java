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

package matteroverdrive.api.transport;

import cofh.lib.util.helpers.MathHelper;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Simeon on 5/5/2015.
 */
public class TransportLocation
{
    public int x,y,z;
    public String name;

    public TransportLocation(int x,int y,int z,String name)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    public TransportLocation(ByteBuf buf)
    {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.name = ByteBufUtils.readUTF8String(buf);
    }

    public TransportLocation(NBTTagCompound nbt)
    {
        if (nbt != null)
        {
            x = nbt.getInteger("tl_x");
            y = nbt.getInteger("tl_y");
            z = nbt.getInteger("tl_z");
            name = nbt.getString("tl_name");
        }
    }

    public void writeToBuffer(ByteBuf buf)
    {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        ByteBufUtils.writeUTF8String(buf,name);
    }

    public void writeToNBT(NBTTagCompound nbtTagCompound)
    {
        nbtTagCompound.setInteger("tl_x", x);
        nbtTagCompound.setInteger("tl_y", y);
        nbtTagCompound.setInteger("tl_z", z);
        nbtTagCompound.setString("tl_name", name);
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPosition(int x,int y,int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getDistance(int x1,int y1,int z1)
    {
        return MathHelper.round(Math.sqrt((x-x1)*(x-x1) + (y-y1)*(y-y1) + (z-z1)*(z-z1)));
    }
}
