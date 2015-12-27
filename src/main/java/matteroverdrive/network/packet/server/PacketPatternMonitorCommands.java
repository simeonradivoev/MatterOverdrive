package matteroverdrive.network.packet.server;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import matteroverdrive.data.ItemPattern;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import matteroverdrive.tile.TileEntityMachinePatternMonitor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 4/26/2015.
 */
public class PacketPatternMonitorCommands extends TileEntityUpdatePacket
{
    public static final int COMMAND_SEARCH = 0;
    public static final int COMMAND_REQUEST = 1;
    int command;
    List<ItemPattern> patterns;

    public PacketPatternMonitorCommands()
    {
        super();
        patterns = new ArrayList<>();
    }
    public PacketPatternMonitorCommands(TileEntityMachinePatternMonitor monitor,int command,List<ItemPattern> patterns)
    {
        super(monitor);
        this.command = command;
        this.patterns = patterns;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);
        command = buf.readInt();
        if (command == COMMAND_REQUEST)
        {
            int size = buf.readInt();
            for (int i = 0; i < size; i++)
            {
                patterns.add(new ItemPattern(buf));
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        buf.writeInt(command);
        if (command == COMMAND_REQUEST)
        {
            buf.writeInt(patterns.size());
            for (ItemPattern pattern : patterns)
            {
                pattern.writeToBuffer(buf);
            }
        }
    }

    public static class ServerHandler extends AbstractServerPacketHandler<PacketPatternMonitorCommands>
    {
        @Override
        public IMessage handleServerMessage(EntityPlayer player, PacketPatternMonitorCommands message, MessageContext ctx)
        {
            TileEntity entity = message.getTileEntity(player.worldObj);
            if (entity != null && entity instanceof TileEntityMachinePatternMonitor)
            {
                TileEntityMachinePatternMonitor monitor = (TileEntityMachinePatternMonitor)entity;

                if (message.command == COMMAND_SEARCH)
                {
                    monitor.queueSearch();
                }
                else if (message.command == COMMAND_REQUEST)
                {
                    if (message.patterns != null)
                    {
                        monitor.queuePatternRequest(message.patterns);
                    }
                }
            }
            return null;
        }
    }
}
