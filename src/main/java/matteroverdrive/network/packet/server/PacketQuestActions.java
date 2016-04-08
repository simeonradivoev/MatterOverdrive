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

import io.netty.buffer.ByteBuf;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.data.quest.PlayerQuestData;
import matteroverdrive.entity.player.MOExtendedProperties;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.items.Contract;
import matteroverdrive.network.packet.PacketAbstract;
import matteroverdrive.network.packet.client.quest.PacketSyncQuests;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.EnumSet;

/**
 * Created by Simeon on 11/25/2015.
 */
public class PacketQuestActions extends PacketAbstract
{
    public static final int QUEST_ACTION_ABONDON = 0;
    public static final int QUEST_ACTION_COMPLETE = 1;
    public static final int QUEST_ACTION_ADD = 2;
    public static final int QUEST_ACTION_COMPLETE_OBJECTIVE = 3;
    int command;
    int questID;
    int playerID;

    public PacketQuestActions(){}

    public PacketQuestActions(int command,int questID,int playerID)
    {
        this.command = command;
        this.questID = questID;
        this.playerID = playerID;
    }

    public PacketQuestActions(int command,int questID,EntityPlayer entityPlayer)
    {
        this.command = command;
        this.questID = questID;
        this.playerID = entityPlayer.getEntityId();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.command = buf.readInt();
        this.questID = buf.readInt();
        this.playerID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(command);
        buf.writeInt(questID);
        buf.writeInt(playerID);
    }

    public static class ServerHandler extends AbstractServerPacketHandler<PacketQuestActions>
    {
        public ServerHandler(){}

        @Override
        public void handleServerMessage(EntityPlayerMP player, PacketQuestActions message, MessageContext ctx)
        {
            Entity entity = player.worldObj.getEntityByID(message.playerID);
            if (entity instanceof EntityPlayer)
            {
                MOExtendedProperties extendedProperties = MOPlayerCapabilityProvider.GetExtendedCapability((EntityPlayer) entity);
                if (extendedProperties != null)
                {
                    if (message.questID < extendedProperties.getQuestData().getActiveQuests().size()) {
                        if (message.command == QUEST_ACTION_COMPLETE) {
                            QuestStack questStack = extendedProperties.getQuestData().getActiveQuests().get(message.questID);
                            if (QuestStack.canComplete((EntityPlayer) entity, questStack)) {
                                questStack.markComplited(player,true);
                            }
                        } else if (message.command == QUEST_ACTION_ABONDON) {
                            QuestStack abandonedQuest = extendedProperties.getQuestData().removeQuest(message.questID);
                            if (abandonedQuest != null) {
                                extendedProperties.onQuestAbandoned(abandonedQuest);
                            }

                            MatterOverdrive.packetPipeline.sendTo(new PacketSyncQuests(extendedProperties.getQuestData(), EnumSet.of(PlayerQuestData.DataType.ACTIVE_QUESTS)),player);
                            return ;
                        }else if (message.command == QUEST_ACTION_COMPLETE_OBJECTIVE)
                        {
                            QuestStack questStack = extendedProperties.getQuestData().getActiveQuests().get(message.questID);
                            questStack.markComplited((EntityPlayer) entity,false);
                        }
                    }
                    if (message.command == QUEST_ACTION_ADD)
                    {
                        ItemStack contract = extendedProperties.getPlayer().inventory.getStackInSlot(message.questID);
                        if (contract.getItem() instanceof Contract)
                        {
                            extendedProperties.addQuest(((Contract) contract.getItem()).getQuest(contract).copy());
                            extendedProperties.getPlayer().inventory.decrStackSize(message.questID,1);
                        }

                    }
                }
            }
        }
    }
}
