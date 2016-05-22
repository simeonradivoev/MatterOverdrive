package matteroverdrive.starmap.data;

import io.netty.buffer.ByteBuf;
import matteroverdrive.starmap.GalaxyGenerator;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/**
 * Created by Simeon on 6/13/2015.
 */
public abstract class SpaceBody
{
	//region Protected Vars
	protected int id;
	protected String name;
	//endregion

	//region Constructors
	public SpaceBody()
	{
	}

	public SpaceBody(String name, int id)
	{
		this.id = id;
		this.name = name;
	}
	//endregion

	//region Read - Write
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

	public void readFromNBT(NBTTagCompound tagCompound, GalaxyGenerator generator)
	{
		id = tagCompound.getInteger("ID");
		name = tagCompound.getString("Name");
	}

	public void readFromBuffer(ByteBuf buf)
	{
		id = buf.readInt();
		name = ByteBufUtils.readUTF8String(buf);
	}
	//endregion

	//region Getters and Setters
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getSpaceBodyName()
	{
		return name;
	}

	public void setSpaceBodyName(String name)
	{
		this.name = name;
	}

	public abstract SpaceBody getParent();
	//endregion
}
