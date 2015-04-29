package com.MO.MatterOverdrive.network.packet.client;

import cofh.lib.util.position.BlockPosition;
import com.MO.MatterOverdrive.network.packet.TileEntityUpdatePacket;
import com.MO.MatterOverdrive.tile.TileEntitiyMachinePatternMonitor;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Simeon on 4/26/2015.
 */
public class PacketPatternMonitorSync extends TileEntityUpdatePacket
{
    HashSet<BlockPosition> positions;

    public PacketPatternMonitorSync(){super();}
    public PacketPatternMonitorSync(TileEntitiyMachinePatternMonitor monitor)
    {
        super(monitor);
        positions = monitor.getDatabases();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);
        int size = buf.readInt();
        positions = new HashSet<BlockPosition>(size);
        for (int i = 0; i < size;i++)
        {
            positions.add(new BlockPosition(buf.readInt(),buf.readInt(),buf.readInt()));
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        buf.writeInt(positions.size());
        for (BlockPosition position : positions)
        {
            buf.writeInt(position.x);
            buf.writeInt(position.y);
            buf.writeInt(position.z);
        }
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketPatternMonitorSync>
    {
        @Override
        public IMessage handleClientMessage(EntityPlayer player, PacketPatternMonitorSync message, MessageContext ctx)
        {
            TileEntity entity = message.getTileEntity(player.worldObj);

            if (entity != null && entity instanceof TileEntitiyMachinePatternMonitor) {

                TileEntitiyMachinePatternMonitor monitor = (TileEntitiyMachinePatternMonitor)entity;
                monitor.setDatabases(message.positions);
                monitor.forceSearch(true);
            }
            return null;
        }
    }
}
