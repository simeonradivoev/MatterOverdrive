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

package matteroverdrive.network.packet.bi;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import matteroverdrive.network.packet.AbstractBiPacketHandler;
import matteroverdrive.network.packet.PacketAbstract;
import matteroverdrive.starmap.GalaxyClient;
import matteroverdrive.starmap.GalaxyServer;
import matteroverdrive.starmap.data.Quadrant;
import matteroverdrive.starmap.data.Star;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 12/19/2015.
 */
public class PacketStarLoading extends PacketAbstract
{
    int quadrantID;
    int starID;
    Star star;

    public PacketStarLoading(){}

    public PacketStarLoading(int quadrantID,int starID)
    {
        this.quadrantID = quadrantID;
        this.starID = starID;
    }

    public PacketStarLoading(int quadrantID,Star star)
    {
        this.quadrantID = quadrantID;
        this.star = star;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        quadrantID = buf.readInt();
        starID = buf.readInt();
        star = new Star();
        if (buf.readBoolean())
        {
            star = new Star();
            star.readFromBuffer(buf);
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(quadrantID);
        buf.writeInt(starID);
        buf.writeBoolean(star != null);
        if (star != null)
        {
            star.writeToBuffer(buf);
        }
    }

    public static class BiHandler extends AbstractBiPacketHandler<PacketStarLoading>
    {

        @Override
        public IMessage handleClientMessage(EntityPlayer player, PacketStarLoading message, MessageContext ctx)
        {
            Quadrant quadrant = GalaxyClient.getInstance().getTheGalaxy().getQuadrantMap().get(message.quadrantID);
            if (quadrant != null && message.star != null)
            {
                quadrant.addStar(message.star);
                message.star.setQuadrant(quadrant);
            }
            return null;
        }

        @Override
        public IMessage handleServerMessage(EntityPlayer player, PacketStarLoading message, MessageContext ctx)
        {
            Quadrant quadrant = GalaxyServer.getInstance().getTheGalaxy().getQuadrantMap().get(message.quadrantID);
            if (quadrant != null)
            {
                Star star = quadrant.getStarMap().get(message.starID);
                if (star != null)
                {
                    return new PacketStarLoading(message.quadrantID,star);
                }
            }
            return null;
        }
    }
}
