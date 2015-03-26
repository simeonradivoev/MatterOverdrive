package com.MO.MatterOverdrive.network.packet;

import com.MO.MatterOverdrive.MatterOverdrive;
import com.MO.MatterOverdrive.Reference;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.Sys;

/**
 * Created by Simeon on 3/6/2015.
 */
public class OpenGuiPacket extends AbstractPacket
{
    private byte id;
    private boolean isContainer;

    public OpenGuiPacket(){}

    public OpenGuiPacket(byte id)
    {
        this.id = id;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        buffer.writeByte(id);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
        id = buffer.readByte();
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {

    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        FMLNetworkHandler.openGui(player, MatterOverdrive.instance, id,player.worldObj,(int)player.posX,(int)player.posY,(int)player.posZ);
    }
}
