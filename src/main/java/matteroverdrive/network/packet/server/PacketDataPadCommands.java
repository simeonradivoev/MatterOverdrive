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

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import matteroverdrive.items.DataPad;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Simeon on 8/28/2015.
 */
public class PacketDataPadCommands extends PacketAbstract
{
    public static final int COMMAND_ORDERING = 1;
    NBTTagCompound data;
    int command;

    public PacketDataPadCommands()
    {

    }

    public PacketDataPadCommands(ItemStack dataPad)
    {
        this(dataPad,0);
    }

    public PacketDataPadCommands(ItemStack dataPad,int command)
    {
        data = new NBTTagCompound();
        if (dataPad != null)
        {
            if (dataPad.hasTagCompound())
            {
                if (command == 0)
                {
                    data = dataPad.getTagCompound();
                }
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        command = buf.readInt();
        data = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(command);
        ByteBufUtils.writeTag(buf,data);
    }

    public static class ServerHandler extends AbstractServerPacketHandler<PacketDataPadCommands>
    {

        @Override
        public IMessage handleServerMessage(EntityPlayer player, PacketDataPadCommands message, MessageContext ctx)
        {
            ItemStack dataPadStack = player.getHeldItem();
            if (dataPadStack != null && dataPadStack.getItem() instanceof DataPad)
            {
                if (message.command == 0)
                {
                    dataPadStack.setTagCompound(message.data);
                }
            }
            return null;
        }
    }
}
