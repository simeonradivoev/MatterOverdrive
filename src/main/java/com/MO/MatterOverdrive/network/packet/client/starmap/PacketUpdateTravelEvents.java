package com.MO.MatterOverdrive.network.packet.client.starmap;

import com.MO.MatterOverdrive.network.packet.PacketAbstract;
import com.MO.MatterOverdrive.network.packet.client.AbstractClientPacketHandler;
import com.MO.MatterOverdrive.starmap.GalaxyClient;
import com.MO.MatterOverdrive.starmap.data.Galaxy;
import com.MO.MatterOverdrive.starmap.data.TravelEvent;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 6/28/2015.
 */
public class PacketUpdateTravelEvents extends PacketAbstract
{
    List<TravelEvent> travelEvents;

    public PacketUpdateTravelEvents()
    {

    }

    public PacketUpdateTravelEvents(Galaxy galaxy)
    {
        travelEvents = galaxy.getTravelEvents();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        if (travelEvents == null)
            travelEvents = new ArrayList<TravelEvent>();

        int size = buf.readInt();
        for (int i = 0;i < size;i++)
        {
            travelEvents.add(new TravelEvent(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(travelEvents.size());
        for (int i = 0;i < travelEvents.size();i++)
        {
            travelEvents.get(i).writeToBuffer(buf);
        }
    }

    public static class Clienthandler extends AbstractClientPacketHandler<PacketUpdateTravelEvents>
    {
        @Override
        public IMessage handleClientMessage(EntityPlayer player, PacketUpdateTravelEvents message, MessageContext ctx)
        {
            GalaxyClient.getInstance().getTheGalaxy().setTravelEvents(message.travelEvents);
            return null;
        }
    }
}
