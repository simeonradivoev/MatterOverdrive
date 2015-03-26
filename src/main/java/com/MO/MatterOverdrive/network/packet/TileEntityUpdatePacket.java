package com.MO.MatterOverdrive.network.packet;

import com.MO.MatterOverdrive.tile.IMOTileEntity;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Simeon on 3/6/2015.
 */
public class TileEntityUpdatePacket extends AbstractPacket
{
    int x;
    int y;
    int z;

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

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeInt(this.x);
        buffer.writeInt(this.y);
        buffer.writeInt(this.z);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {

    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {

    }
}
