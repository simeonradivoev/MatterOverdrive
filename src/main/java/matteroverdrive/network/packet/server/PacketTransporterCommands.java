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

package matteroverdrive.network.packet.server;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import matteroverdrive.api.transport.TransportLocation;
import matteroverdrive.machines.transporter.TileEntityMachineTransporter;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
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
