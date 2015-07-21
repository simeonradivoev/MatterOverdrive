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

package matteroverdrive.network.packet.client.starmap;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import matteroverdrive.network.packet.PacketAbstract;
import matteroverdrive.network.packet.client.AbstractClientPacketHandler;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.data.Galaxy;
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
            message.galaxy.setWorld(player.worldObj);
            GalaxyClient.getInstance().setTheGalaxy(message.galaxy);
            GalaxyClient.getInstance().loadClaimedPlanets();
            return null;
        }
    }
}
