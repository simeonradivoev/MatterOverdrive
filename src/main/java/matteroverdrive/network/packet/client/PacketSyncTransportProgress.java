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
import matteroverdrive.machines.transporter.TileEntityMachineTransporter;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Simeon on 5/4/2015.
 */
public class PacketSyncTransportProgress extends TileEntityUpdatePacket
{
    int progress;

    public PacketSyncTransportProgress(){}
    public PacketSyncTransportProgress(TileEntityMachineTransporter transporter)
    {
        super(transporter);
        this.progress = transporter.getTransportTime();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);
        progress = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        buf.writeInt(progress);
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketSyncTransportProgress>
    {
        public ClientHandler(){}

        @Override
        public IMessage handleClientMessage(EntityPlayer player, PacketSyncTransportProgress message, MessageContext ctx)
        {
            TileEntity entity = message.getTileEntity(player.worldObj);
            if (entity instanceof TileEntityMachineTransporter)
            {
                ((TileEntityMachineTransporter) entity).setTransportTime(message.progress);
            }
            return null;
        }
    }
}
