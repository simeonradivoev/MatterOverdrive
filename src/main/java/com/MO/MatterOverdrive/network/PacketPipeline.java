package com.MO.MatterOverdrive.network;

import com.MO.MatterOverdrive.Reference;
import com.MO.MatterOverdrive.network.packet.AbstractBiPacketHandler;
import com.MO.MatterOverdrive.network.packet.bi.PacketGetDatabase;
import com.MO.MatterOverdrive.network.packet.client.*;
import com.MO.MatterOverdrive.network.packet.server.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import io.netty.channel.ChannelHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;

/**
 * Created by Simeon on 3/5/2015.
 */
@ChannelHandler.Sharable
public class PacketPipeline
{
    protected int packetID;
    public SimpleNetworkWrapper dispatcher;

    public PacketPipeline()
    {
        dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.CHANNEL_NAME);
        packetID = 0;
    }

    public void registerPackets()
    {
        registerPacket(PacketMatterScannerUpdate.ServerHandler.class, PacketMatterScannerUpdate.class);
        registerPacket(PacketPowerUpdate.ClientHandler.class, PacketPowerUpdate.class);
        registerPacket(PacketMatterUpdate.ClientHandler.class, PacketMatterUpdate.class);
        registerPacket(PacketPatternMonitorSync.ClientHandler.class,PacketPatternMonitorSync.class);
        registerPacket(PacketPatternMonitorCommands.ServerHandler.class,PacketPatternMonitorCommands.class);
        registerPacket(PacketReplicationComplete.ClientHandler.class,PacketReplicationComplete.class);
        registerPacket(PacketRemoveTask.ServerHandler.class,PacketRemoveTask.class);
        registerPacket(PacketTransporterCommands.ServerHandler.class, PacketTransporterCommands.class);
        registerPacket(PacketSyncTransportProgress.ClientHandler.class,PacketSyncTransportProgress.class);
        registerBiPacket(PacketGetDatabase.Handler.class, PacketGetDatabase.class);
        registerPacket(PacketUpdateMatterRegistry.ClientHandler.class, PacketUpdateMatterRegistry.class);
        registerPacket(PacketChangeRedstoneMode.ServerHandler.class,PacketChangeRedstoneMode.class);
    }

    public <REQ extends IMessage, REPLY extends IMessage> void registerPacket(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType)
    {
        try {
            Side side = AbstractClientPacketHandler.class.isAssignableFrom(messageHandler) ? Side.CLIENT : Side.SERVER;
            dispatcher.registerMessage(messageHandler, requestMessageType, packetID++, side);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public <REQ extends IMessage, REPLY extends IMessage> void registerBiPacket(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType)
    {
        if (AbstractBiPacketHandler.class.isAssignableFrom(messageHandler))
        {
            dispatcher.registerMessage(messageHandler,requestMessageType,packetID,Side.CLIENT);
            dispatcher.registerMessage(messageHandler,requestMessageType,packetID++,Side.SERVER);
        }
        else
        {
            throw new IllegalArgumentException("Cannot register " + messageHandler.getName() + " on both sides - must extend AbstractBiMessageHandler!");
        }
    }

    //region Util functions
    public void sendToServer(IMessage message)
    {
        dispatcher.sendToServer(message);
    }

    public void sendToAllAround(IMessage message,NetworkRegistry.TargetPoint point)
    {
        dispatcher.sendToAllAround(message, point);
    }

    public void sendToAllAround(IMessage message,int dimention,double x,double y,double z,double range)
    {
        dispatcher.sendToAllAround(message, new NetworkRegistry.TargetPoint(dimention, x, y, z, range));
    }

    public void sendToAllAround(IMessage message,EntityPlayer player,double range)
    {
        dispatcher.sendToAllAround(message, new NetworkRegistry.TargetPoint(player.worldObj.provider.dimensionId,player.posX,player.posY,player.posZ,range));
    }

    public void sendToAllAround(IMessage message,TileEntity tileEntity,double range)
    {
        dispatcher.sendToAllAround(message, new NetworkRegistry.TargetPoint(tileEntity.getWorldObj().provider.dimensionId,tileEntity.xCoord,tileEntity.yCoord,tileEntity.zCoord,range));
    }

    public void sendTo(IMessage message,EntityPlayerMP player)
    {
        dispatcher.sendTo(message, player);
    }

    public void sendToDimention(IMessage message,int dimention)
    {
        dispatcher.sendToDimension(message, dimention);
    }
    public void sendToDimention(IMessage message,World world)
    {
        sendToDimention(message,world.provider);
    }
    public void sendToDimention(IMessage message,WorldProvider worldProvider)
    {
        dispatcher.sendToDimension(message, worldProvider.dimensionId);
    }
    //endregion
}
