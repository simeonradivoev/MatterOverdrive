package com.MO.MatterOverdrive.data;

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

    public int getDistance(int x1,int y1,int z1)
    {
        return MathHelper.round(Math.sqrt((x-x1)*(x-x1) + (y-y1)*(y-y1) + (z-z1)*(z-z1)));
    }
}
