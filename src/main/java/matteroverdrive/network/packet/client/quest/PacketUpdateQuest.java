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
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.api.quest.QuestState;
import matteroverdrive.data.quest.PlayerQuestData;
import matteroverdrive.entity.player.MOExtendedProperties;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.network.packet.PacketAbstract;
import matteroverdrive.network.packet.client.AbstractClientPacketHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 11/20/2015.
 */
public class PacketUpdateQuest extends PacketAbstract
{
	public static final byte UPDATE_QUEST = 0;
	public static final byte ADD_QUEST = 1;
	public static final byte COMPLETE_QUEST = 2;
	private byte questUpdateOperation;
	private int questIndex;
	private QuestStack questStack;
	private QuestState questState;

	public PacketUpdateQuest()
	{
	}

	public PacketUpdateQuest(int questIndex, QuestState questState, PlayerQuestData playerQuestData, byte questUpdateOperation)
	{
		this.questIndex = questIndex;
		this.questState = questState;
		this.questUpdateOperation = questUpdateOperation;
		questStack = playerQuestData.getActiveQuests().get(questIndex);
	}

	public PacketUpdateQuest(int questIndex, QuestState questState, QuestStack questStack, byte questUpdateOperation)
	{
		this.questIndex = questIndex;
		this.questState = questState;
		this.questUpdateOperation = questUpdateOperation;
		this.questStack = questStack;
	}

	public PacketUpdateQuest(QuestStack questStack, byte questUpdateOperation)
	{
		this.questIndex = -1;
		this.questUpdateOperation = questUpdateOperation;
		this.questStack = questStack;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.questIndex = buf.readInt();
		this.questUpdateOperation = buf.readByte();
		if (buf.readBoolean())
		{
			int indexesSize = buf.readByte();
			int[] indexes = new int[] {indexesSize};
			for (int i = 0; i < indexesSize; i++)
			{
				indexes[i] = buf.readShort();
			}
			this.questState = new QuestState(QuestState.Type.values()[buf.readByte()], indexes, buf.readBoolean());
		}
		questStack = QuestStack.loadFromNBT(ByteBufUtils.readTag(buf));
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(questIndex);
		buf.writeByte(questUpdateOperation);
		if (questState != null)
		{
			buf.writeBoolean(true);
			buf.writeByte(questState.getObjectiveIds().length);
			for (int i : questState.getObjectiveIds())
			{
				buf.writeShort(i);
			}
			buf.writeByte(questState.getType().ordinal());
			buf.writeBoolean(questState.isShowOnHud());
		}
		else
		{
			buf.writeBoolean(false);
		}
		NBTTagCompound questStackNBT = new NBTTagCompound();
		questStack.writeToNBT(questStackNBT);
		ByteBufUtils.writeTag(buf, questStackNBT);
	}

	public static class ClientHandler extends AbstractClientPacketHandler<PacketUpdateQuest>
	{
		public ClientHandler()
		{
		}

		@SideOnly(Side.CLIENT)
		@Override
		public void handleClientMessage(EntityPlayerSP player, PacketUpdateQuest message, MessageContext ctx)
		{
			MOExtendedProperties extendedProperties = MOPlayerCapabilityProvider.GetExtendedCapability(player);
			if (extendedProperties != null)
			{
				if (message.questUpdateOperation == UPDATE_QUEST)
				{
					extendedProperties.updateQuestFromServer(message.questIndex, message.questStack, message.questState);
				}
				else if (message.questUpdateOperation == ADD_QUEST)
				{
					extendedProperties.addQuest(message.questStack);
				}
				else if (message.questUpdateOperation == COMPLETE_QUEST)
				{
					extendedProperties.onQuestCompleted(message.questStack, message.questIndex);
				}

			}
		}
	}
}
