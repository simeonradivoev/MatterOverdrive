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

package matteroverdrive.network.packet.client.pattern_monitor;

import matteroverdrive.container.matter_network.ContainerPatternMonitor;
import matteroverdrive.data.matter_network.ItemPatternMapping;
import matteroverdrive.network.packet.PacketAbstract;
import matteroverdrive.network.packet.client.AbstractClientPacketHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 4/26/2015.
 */
public class PacketSendItemPattern extends PacketAbstract
{
    ItemPatternMapping itemPatternMapping;

    public PacketSendItemPattern(){super();}
    public PacketSendItemPattern(int containerId,ItemPatternMapping itemPatternMapping)
    {
        this.itemPatternMapping = itemPatternMapping;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        itemPatternMapping = new ItemPatternMapping(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        itemPatternMapping.writeToBuffer(buf);
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketSendItemPattern>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public void handleClientMessage(EntityPlayerSP player, PacketSendItemPattern message, MessageContext ctx)
        {
            if (player.openContainer instanceof ContainerPatternMonitor)
            {
                ((ContainerPatternMonitor) player.openContainer).setItemPattern(message.itemPatternMapping);
            }
        }
    }
}
