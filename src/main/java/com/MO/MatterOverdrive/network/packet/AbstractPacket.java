package com.MO.MatterOverdrive.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 3/5/2015.
 */
public abstract class AbstractPacket
{
    public abstract void encodeInto(ChannelHandlerContext ctx,ByteBuf buffer);
    public abstract void decodeInto(ChannelHandlerContext ctx,ByteBuf buffer);

    public abstract void handleClientSide(EntityPlayer player);
    public  abstract void handleServerSide(EntityPlayer player);
}
