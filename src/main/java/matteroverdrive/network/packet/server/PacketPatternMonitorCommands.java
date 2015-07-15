package matteroverdrive.network.packet.server;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import matteroverdrive.tile.TileEntityMachinePatternMonitor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Simeon on 4/26/2015.
 */
public class PacketPatternMonitorCommands extends TileEntityUpdatePacket
{
    public static final int COMMAND_SEARCH = 0;
    public static final int COMMAND_REQUEST = 1;
    int command;
    NBTTagCompound data;

    public PacketPatternMonitorCommands(){super();}
    public PacketPatternMonitorCommands(TileEntityMachinePatternMonitor monitor,int command,NBTTagCompound data)
    {
        super(monitor);
        this.command = command;
        this.data = data;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);
        command = buf.readInt();
        data = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        buf.writeInt(command);
        ByteBufUtils.writeTag(buf, data);
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
                    if (message.data != null)
                    {
                        monitor.queuePatternRequest(message.data.getTagList("Requests",10));
                    }
                }
            }
            return null;
        }
    }
}
