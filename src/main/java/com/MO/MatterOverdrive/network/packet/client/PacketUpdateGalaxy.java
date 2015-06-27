package com.MO.MatterOverdrive.network.packet.client;

import com.MO.MatterOverdrive.network.packet.PacketAbstract;
import com.MO.MatterOverdrive.starmap.GalaxyClient;
import com.MO.MatterOverdrive.starmap.data.Galaxy;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 6/15/2015.
 */
public class PacketUpdateGalaxy extends PacketAbstract {

    Galaxy galaxy;

    public PacketUpdateGalaxy()
    {

    }

    public PacketUpdateGalaxy(Galaxy galaxy)
    {
        this.galaxy = galaxy;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        galaxy = new Galaxy();
        galaxy.readFromBuffer(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        galaxy.writeToBuffer(buf);
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketUpdateGalaxy>
    {

        @Override
        public IMessage handleClientMessage(EntityPlayer player, PacketUpdateGalaxy message, MessageContext ctx)
        {
            GalaxyClient.getInstance().setTheGalaxy(message.galaxy);
            GalaxyClient.getInstance().loadClaimedPlanets();
            return null;
        }
    }
}
