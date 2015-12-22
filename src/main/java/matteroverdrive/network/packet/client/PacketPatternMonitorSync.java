/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.network.packet.client;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import matteroverdrive.data.BlockPos;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import matteroverdrive.tile.TileEntityMachinePatternMonitor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import java.util.HashSet;

/**
 * Created by Simeon on 4/26/2015.
 */
public class PacketPatternMonitorSync extends TileEntityUpdatePacket
{
    HashSet<BlockPos> positions;

    public PacketPatternMonitorSync(){super();}
    public PacketPatternMonitorSync(TileEntityMachinePatternMonitor monitor)
    {
        super(monitor);
        positions = monitor.getDatabases();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);
        int size = buf.readInt();
        positions = new HashSet<BlockPos>(size);
        for (int i = 0; i < size;i++)
        {
            positions.add(new BlockPos(buf.readInt(), buf.readInt(), buf.readInt()));
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        buf.writeInt(positions.size());
        for (BlockPos position : positions)
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

            if (entity != null && entity instanceof TileEntityMachinePatternMonitor) {

                TileEntityMachinePatternMonitor monitor = (TileEntityMachinePatternMonitor)entity;
                monitor.setDatabases(message.positions);
                monitor.forceSearch(true);
            }
            return null;
        }
    }
}
