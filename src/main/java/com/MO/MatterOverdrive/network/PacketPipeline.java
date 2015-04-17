package com.MO.MatterOverdrive.network;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.network.packet.*;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

import java.util.*;

/**
 * Created by Simeon on 3/5/2015.
 */
@ChannelHandler.Sharable
public class PacketPipeline extends MessageToMessageCodec<FMLProxyPacket,AbstractPacket>
{
    private EnumMap<Side,FMLEmbeddedChannel> channels;
    private LinkedList<Class<? extends AbstractPacket>> packets = new LinkedList<Class<? extends AbstractPacket>>();
    private boolean isPostInitilized = false;

    public boolean registerPacket(Class<? extends AbstractPacket> clazz)
    {
        if(packets.size() > 256)
        {
            FMLLog.severe("This packet has been registered");
            return  false;
        }

        if(this.packets.contains(clazz))
        {
            FMLLog.severe("This packet has been registered");
            return  false;
        }

        if (this.isPostInitilized)
        {
            FMLLog.severe("Packet registered too late!");
            return false;
        }

        this.packets.add(clazz);
        return  true;
    }

    public void init()
    {
        this.channels = NetworkRegistry.INSTANCE.newChannel(Reference.CHANNEL_NAME,this);
        registerPackets();
    }

    public  void  postInit()
    {
        if(isPostInitilized)
            return;

        isPostInitilized = true;
        Collections.sort(this.packets, new Comparator<Class<? extends AbstractPacket>>() {
            @Override
            public int compare(Class<? extends AbstractPacket> o1, Class<? extends AbstractPacket> o2) {
                int com = String.CASE_INSENSITIVE_ORDER.compare(o1.getCanonicalName(), o2.getCanonicalName());
                if (com == 0)
                    com = o1.getCanonicalName().compareTo(o2.getCanonicalName());

                return com;
            }
        });
    }

    public  void registerPackets()
    {
        registerPacket(TileEntityUpdatePacket.class);
        registerPacket(PacketMatterPipeUpdate.class);
        registerPacket(PacketPhaserUpdate.class);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractPacket msg, List<Object> out) throws Exception
    {
        ByteBuf buffer = Unpooled.buffer();
        Class<? extends AbstractPacket> clazz = msg.getClass();

        if(!this.packets.contains(clazz))
        {
            throw new NullPointerException("This packet has never been registered: " + clazz.getCanonicalName());
        }

        byte discriminator = (byte) this.packets.indexOf(clazz);
        buffer.writeByte(discriminator);
        msg.encodeInto(ctx, buffer);

        FMLProxyPacket proxyPacket = new FMLProxyPacket(buffer,ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get());
        out.add(proxyPacket);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out) throws Exception
    {
        ByteBuf payload = msg.payload();
        byte discriminator = payload.readByte();

        Class<? extends AbstractPacket> clazz = this.packets.get(discriminator);
        if(clazz == null)
        {
            throw new NullPointerException("This packet has never been registered: " + msg);
        }

        AbstractPacket abstractPacket = clazz.newInstance();
        abstractPacket.decodeInto(ctx, payload.slice());

        switch (FMLCommonHandler.instance().getEffectiveSide())
        {
            case CLIENT:
                decodePlayer(abstractPacket);
                break;
            case SERVER:
                decodeServer(abstractPacket,ctx);
                break;
            default:
        }

        out.add(abstractPacket);
    }

    @SideOnly(Side.CLIENT)
    private void decodePlayer(AbstractPacket packet)
    {
        packet.handleClientSide(Minecraft.getMinecraft().thePlayer);
    }

    @SideOnly(Side.SERVER)
    private void decodeServer(AbstractPacket packet,ChannelHandlerContext ctx)
    {
        INetHandler iNetHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
        packet.handleServerSide(((NetHandlerPlayServer)iNetHandler).playerEntity);
    }

    public void sendToServer(AbstractPacket msg)
    {
        if(Minecraft.getMinecraft().theWorld.isRemote) {
            this.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
            this.channels.get(Side.CLIENT).writeAndFlush(msg);
        }
    }

    public void sendToAll(AbstractPacket msg)
    {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        this.channels.get(Side.SERVER).writeOutbound(msg);
    }

    public void sendTo(EntityPlayerMP player, AbstractPacket msg)
    {
        if(!Minecraft.getMinecraft().theWorld.isRemote)
        {
            this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
            this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
            this.channels.get(Side.SERVER).writeOutbound(msg);
        }
    }

    public void sendToAllAround(AbstractPacket msg, NetworkRegistry.TargetPoint point)
    {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        this.channels.get(Side.SERVER).writeOutbound(msg);
    }

    public void sendToDimension(AbstractPacket msg, int dimensionId)
    {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
        this.channels.get(Side.SERVER).writeOutbound(msg);
    }
}
