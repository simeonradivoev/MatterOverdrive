package matteroverdrive.network.packet.client.pattern_monitor;

import io.netty.buffer.ByteBuf;
import matteroverdrive.container.matter_network.ContainerPatternMonitor;
import matteroverdrive.network.packet.PacketAbstract;
import matteroverdrive.network.packet.client.AbstractClientPacketHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 1/30/2016.
 */
public class PacketClearPatterns extends PacketAbstract
{
    private int windowID;
    private BlockPos database;
    private int patternStorageId;

    public PacketClearPatterns(){}
    public PacketClearPatterns(int windowID)
    {
        this.windowID = windowID;
    }

    public PacketClearPatterns(int windowID,BlockPos database)
    {
        this(windowID);
        this.database = database;
    }

    public PacketClearPatterns(int windowID,BlockPos database,int patternStorageId)
    {
        this(windowID,database);
        this.patternStorageId = patternStorageId;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        windowID = buf.readByte();
        if (buf.readBoolean())
        {
            database = BlockPos.fromLong(buf.readLong());
        }
        patternStorageId = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeByte(windowID);
        buf.writeBoolean(database != null);
        if (database != null)
        {
            buf.writeLong(database.toLong());
        }
        buf.writeByte(patternStorageId);
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketClearPatterns>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public void handleClientMessage(EntityPlayerSP player, PacketClearPatterns message, MessageContext ctx)
        {
            if (player.openContainer instanceof ContainerPatternMonitor)
            {
                if (message.database != null)
                {
                    if (message.patternStorageId >= 0)
                    {
                        ((ContainerPatternMonitor) player.openContainer).clearPatternStoragePatterns(message.database,message.patternStorageId);
                    }else
                    {
                        ((ContainerPatternMonitor) player.openContainer).clearDatabasePatterns(message.database);
                    }
                }else
                {
                    ((ContainerPatternMonitor) player.openContainer).clearAllPatterns();
                }
            }
        }
    }
}
