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

import io.netty.buffer.ByteBuf;
import matteroverdrive.data.MinimapEntityInfo;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 9/7/2015.
 */
public class PacketSendMinimapInfo extends PacketAbstract
{
    List<MinimapEntityInfo> entityInfos;

    public PacketSendMinimapInfo()
    {

    }

    public PacketSendMinimapInfo(List<MinimapEntityInfo> entityInfos)
    {
        this.entityInfos = entityInfos;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        entityInfos = new ArrayList<>();
        int size = buf.readInt();
        for (int i = 0;i < size;i++)
        {
            entityInfos.add(new MinimapEntityInfo().readFromBuffer(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(entityInfos.size());
        for (MinimapEntityInfo entityInfo : entityInfos)
        {
            entityInfo.writeToBuffer(buf);
        }
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketSendMinimapInfo>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public void handleClientMessage(EntityPlayerSP player, PacketSendMinimapInfo message, MessageContext ctx)
        {
            AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(player);
            if (androidPlayer != null && androidPlayer.isAndroid())
            {
                AndroidPlayer.setMinimapEntityInfo(message.entityInfos);
            }
        }
    }
}
