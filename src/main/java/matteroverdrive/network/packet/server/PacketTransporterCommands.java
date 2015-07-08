package matteroverdrive.network.packet.server;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import matteroverdrive.api.transport.TransportLocation;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import matteroverdrive.tile.TileEntityMachineTransporter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 5/3/2015.
 */
public class PacketTransporterCommands extends TileEntityUpdatePacket
{
    List<TransportLocation> locations;
    int selected;

    public PacketTransporterCommands(){
        super();
        locations = new ArrayList<TransportLocation>();
    }

    public PacketTransporterCommands(TileEntityMachineTransporter transporter)
    {
        super(transporter);
        locations = transporter.locations;
        selected = transporter.selectedLocation;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);
        selected = buf.readInt();
        int size = buf.readInt();
        for (int i = 0;i < size;i++)
        {
            locations.add(new TransportLocation(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        buf.writeInt(selected);
        int size = locations.size();
        buf.writeInt(size);
        for (int i = 0 ;i < size;i++)
        {
            locations.get(i).writeToBuffer(buf);
        }
    }

    public static class ServerHandler extends AbstractServerPacketHandler<PacketTransporterCommands>
    {
        public ServerHandler(){}


        @Override
        public IMessage handleServerMessage(EntityPlayer player, PacketTransporterCommands message, MessageContext ctx)
        {
            TileEntity tileEntity = message.getTileEntity(player.worldObj);
            if (tileEntity instanceof TileEntityMachineTransporter)
            {
                TileEntityMachineTransporter transporter = (TileEntityMachineTransporter)tileEntity;
                transporter.locations = message.locations;
                transporter.selectedLocation = message.selected;
                transporter.ForceSync();
            }
            return null;
        }
    }
}
