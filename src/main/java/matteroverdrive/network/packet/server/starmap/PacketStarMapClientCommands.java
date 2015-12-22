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

package matteroverdrive.network.packet.server.starmap;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import matteroverdrive.api.starmap.GalacticPosition;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import matteroverdrive.network.packet.server.AbstractServerPacketHandler;
import matteroverdrive.tile.TileEntityMachineStarMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Simeon on 6/19/2015.
 */
public class PacketStarMapClientCommands extends TileEntityUpdatePacket
{

    int zoomLevel;
    GalacticPosition position;
    GalacticPosition destination;

    public PacketStarMapClientCommands()
    {

    }

    public PacketStarMapClientCommands(TileEntityMachineStarMap starMap,int zoomLevel,GalacticPosition position,GalacticPosition destination)
    {
        super(starMap);
        this.zoomLevel = zoomLevel;
        this.position = position;
        this.destination = destination;
    }

    public PacketStarMapClientCommands(TileEntityMachineStarMap starMap)
    {
        super(starMap);
        zoomLevel = starMap.getZoomLevel();
        position = starMap.getGalaxyPosition();
        destination = starMap.getDestination();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        zoomLevel = buf.readByte();
        position = new GalacticPosition(buf);
        destination = new GalacticPosition(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        buf.writeByte(zoomLevel);
        position.writeToBuffer(buf);
        destination.writeToBuffer(buf);
    }

    public static class ServerHandler extends AbstractServerPacketHandler<PacketStarMapClientCommands>
    {

        @Override
        public IMessage handleServerMessage(EntityPlayer player, PacketStarMapClientCommands message, MessageContext ctx)
        {
            TileEntity tileEntity = message.getTileEntity(player.worldObj);
            if (tileEntity instanceof TileEntityMachineStarMap)
            {
                ((TileEntityMachineStarMap) tileEntity).setZoomLevel(message.zoomLevel);
                ((TileEntityMachineStarMap) tileEntity).setGalaxticPosition(message.position);
                ((TileEntityMachineStarMap)tileEntity).setDestination(message.destination);
                ((TileEntityMachineStarMap) tileEntity).forceSync();
            }
            return null;
        }
    }
}
