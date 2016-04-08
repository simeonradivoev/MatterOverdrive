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

package matteroverdrive.network.packet.client.quest;

import io.netty.buffer.ByteBuf;
import matteroverdrive.data.quest.PlayerQuestData;
import matteroverdrive.entity.player.MOExtendedProperties;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.gui.GuiDataPad;
import matteroverdrive.network.packet.PacketAbstract;
import matteroverdrive.network.packet.client.AbstractClientPacketHandler;
import matteroverdrive.util.MOEnumHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;

/**
 * Created by Simeon on 11/19/2015.
 */
public class PacketSyncQuests extends PacketAbstract
{
    int questTypes;
    NBTTagCompound data;

    public PacketSyncQuests()
    {
        this.data = new NBTTagCompound();
    }

    public PacketSyncQuests(PlayerQuestData questData,EnumSet<PlayerQuestData.DataType> dataTypes)
    {
        this();
        questData.writeToNBT(data,dataTypes);
        questTypes = MOEnumHelper.encode(dataTypes);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        questTypes = buf.readInt();
        data = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(questTypes);
        ByteBufUtils.writeTag(buf,data);
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketSyncQuests>
    {
        @SideOnly(Side.CLIENT)
        @Override
        public void handleClientMessage(EntityPlayerSP player, PacketSyncQuests message, MessageContext ctx)
        {
            MOExtendedProperties extendedProperties = MOPlayerCapabilityProvider.GetExtendedCapability(player);
            if (extendedProperties != null && extendedProperties.getQuestData() != null) {
                extendedProperties.getQuestData().readFromNBT(message.data,MOEnumHelper.decode(message.questTypes,PlayerQuestData.DataType.class));
            }
            if (Minecraft.getMinecraft().currentScreen instanceof GuiDataPad)
            {
                ((GuiDataPad) Minecraft.getMinecraft().currentScreen).refreshQuests(extendedProperties);
            }
        }
    }
}
