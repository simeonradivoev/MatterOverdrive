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
import matteroverdrive.entity.player.AndroidPlayer;
import matteroverdrive.init.MatterOverdriveBioticStats;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 6/9/2015.
 */
public class PacketSendAndroidAnction extends PacketAbstract
{
    public static final int ACTION_SHIELD = 0;
    public static final int ACTION_CLOAK = 1;
    public static final int ACTION_NIGHTVISION = 2;
    int action = 0;
    boolean state;
    int options;

    public PacketSendAndroidAnction()
    {

    }

    public PacketSendAndroidAnction(int action,boolean state)
    {
        this(action,state,0);
    }

    public PacketSendAndroidAnction(int action,boolean state,int options)
    {
        this.action = action;
        this.state = state;
        this.options = options;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        action = buf.readInt();
        state = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(action);
        buf.writeBoolean(state);
    }

    public static class ServerHandler extends AbstractServerPacketHandler<PacketSendAndroidAnction>
    {

        @Override
        public IMessage handleServerMessage(EntityPlayer player, PacketSendAndroidAnction message, MessageContext ctx)
        {
            AndroidPlayer androidPlayer = AndroidPlayer.get(player);
            if (androidPlayer != null)
            {
                if (message.action == ACTION_SHIELD)
                {
                    MatterOverdriveBioticStats.shield.setShield(androidPlayer,message.state);
                }
                else if (message.action == ACTION_CLOAK)
                {
                    MatterOverdriveBioticStats.cloak.setActive(androidPlayer,androidPlayer.getUnlockedLevel(MatterOverdriveBioticStats.cloak),message.state);
                }
                else if (message.action == ACTION_NIGHTVISION)
                {
                    MatterOverdriveBioticStats.nightvision.setActive(androidPlayer,androidPlayer.getUnlockedLevel(MatterOverdriveBioticStats.nightvision),message.state);
                }
            }
            return null;
        }
    }
}
