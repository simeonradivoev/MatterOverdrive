package matteroverdrive.network.packet.client.starmap;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import matteroverdrive.gui.GuiStarMap;
import matteroverdrive.network.packet.PacketAbstract;
import matteroverdrive.network.packet.client.AbstractClientPacketHandler;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.data.Galaxy;
import matteroverdrive.starmap.data.TravelEvent;
import net.minecraft.client.Minecraft;
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

    public static class ClientHandler extends AbstractClientPacketHandler<PacketUpdateTravelEvents>
    {
        @Override
        public IMessage handleClientMessage(EntityPlayer player, PacketUpdateTravelEvents message, MessageContext ctx)
        {
            GalaxyClient.getInstance().getTheGalaxy().setTravelEvents(message.travelEvents);
            notifyChange();
            return null;
        }

        @SideOnly(Side.CLIENT)
        private void notifyChange()
        {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiStarMap)
            {
                GuiStarMap guiStarMap = (GuiStarMap)Minecraft.getMinecraft().currentScreen;
                guiStarMap.onTravelEventsChange(GalaxyClient.getInstance().getTheGalaxy().getTravelEvents());
            }
        }
    }
}
