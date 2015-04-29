package com.MO.MatterOverdrive.network.packet;

import cofh.lib.util.position.BlockPosition;
import com.MO.MatterOverdrive.network.packet.PacketAbstract;
import com.MO.MatterOverdrive.tile.IMOTileEntity;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Simeon on 3/6/2015.
 */
public class TileEntityUpdatePacket extends PacketAbstract
{
    public int x;
    public int y;
    public int z;

    public TileEntityUpdatePacket()
    {
        super();
    }

    public TileEntityUpdatePacket(int x,int y,int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public TileEntityUpdatePacket(TileEntity entity)
    {
        this(entity.xCoord,entity.yCoord,entity.zCoord);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    public TileEntity getTileEntity(World world)
    {
        return world.getTileEntity(x,y,z);
    }
}
