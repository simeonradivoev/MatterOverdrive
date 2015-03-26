package com.MO.MatterOverdrive.network.packet;

import com.MO.MatterOverdrive.tile.pipes.TileEntityMatterPipe;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Simeon on 3/6/2015.
 */
public class PacketMatterPipeUpdate extends TileEntityUpdatePacket
{

    private boolean matterVisible;

    public PacketMatterPipeUpdate()
    {

    }

    public PacketMatterPipeUpdate(int x, int y, int z,boolean matterVisible)
    {
        super(x, y, z);
        this.matterVisible = matterVisible;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeInt(this.x);
        buffer.writeInt(this.y);
        buffer.writeInt(this.z);
        buffer.writeBoolean(this.matterVisible);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
        this.matterVisible = buffer.readBoolean();
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        super.handleClientSide(player);
        TileEntity e = player.worldObj.getTileEntity(this.x,this.y,this.z);
        if(e instanceof TileEntityMatterPipe)
        {
            ((TileEntityMatterPipe)player.worldObj.getTileEntity(this.x,this.y,this.z)).setMatterVisible(this.matterVisible);
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        super.handleServerSide(player);
    }
}
