package matteroverdrive.starmap.data;

import matteroverdrive.starmap.GalaxyGenerator;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Simeon on 6/13/2015.
 */
public abstract class SpaceBody
{
    int id;
    String name;

    public SpaceBody()
    {

    }

    public SpaceBody(String name,int id)
    {
        this.id = id;
        this.name = name;
    }

    public void writeToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setInteger("ID", id);
        tagCompound.setString("Name", name);
    }

    public void writeToBuffer(ByteBuf buf)
    {
        buf.writeInt(id);
        ByteBufUtils.writeUTF8String(buf, name);
    }

    public void readFromNBT(NBTTagCompound tagCompound,GalaxyGenerator generator)
    {
        id = tagCompound.getInteger("ID");
        name = tagCompound.getString("Name");
    }

    public void readFromBuffer(ByteBuf buf)
    {
        id = buf.readInt();
        name = ByteBufUtils.readUTF8String(buf);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract SpaceBody getParent();
}
