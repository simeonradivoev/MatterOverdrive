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

package matteroverdrive.data.quest;

import cpw.mods.fml.common.eventhandler.Event;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.entity.player.MOExtendedProperties;
import matteroverdrive.network.packet.client.quest.PacketUpdateQuest;
import matteroverdrive.util.MOInventoryHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by Simeon on 11/19/2015.
 */
public class PlayerQuestData
{
    List<QuestStack> activeQuests;
    List<QuestStack> completedQuests;
    MOExtendedProperties extendedProperties;

    public PlayerQuestData(MOExtendedProperties extendedProperties)
    {
        activeQuests = new ArrayList<>();
        completedQuests = new ArrayList<>();
        this.extendedProperties = extendedProperties;
    }

    public void writeToNBT(NBTTagCompound tagCompound,EnumSet<DataType> dataTypes)
    {
        if (dataTypes.contains(DataType.COMPLETED_QUESTS)) {
            if (completedQuests.size() > 0) {
                NBTTagList activeQuestsTagList = new NBTTagList();
                for (QuestStack questStack : completedQuests) {
                    NBTTagCompound questStackNBT = new NBTTagCompound();
                    questStack.writeToNBT(questStackNBT);
                    activeQuestsTagList.appendTag(questStackNBT);
                }
                tagCompound.setTag("CompletedQuests", activeQuestsTagList);
            }
        }
        if (dataTypes.contains(DataType.ACTIVE_QUESTS)) {
            if (activeQuests.size() > 0) {
                NBTTagList activeQuestsTagList = new NBTTagList();
                for (QuestStack questStack : activeQuests) {
                    NBTTagCompound questStackNBT = new NBTTagCompound();
                    questStack.writeToNBT(questStackNBT);
                    activeQuestsTagList.appendTag(questStackNBT);
                }
                tagCompound.setTag("ActiveQuests", activeQuestsTagList);
            }
        }
    }

    public void readFromNBT(NBTTagCompound tagCompound,EnumSet<DataType> dataTypes)
    {
        if (dataTypes.contains(DataType.COMPLETED_QUESTS)) {
            completedQuests.clear();
            try {
                if (tagCompound.hasKey("CompletedQuests", Constants.NBT.TAG_LIST)) {
                    NBTTagList activeQuestsTagList = tagCompound.getTagList("CompletedQuests", Constants.NBT.TAG_COMPOUND);
                    for (int i = 0; i < activeQuestsTagList.tagCount(); i++) {
                        completedQuests.add(QuestStack.loadFromNBT(activeQuestsTagList.getCompoundTagAt(i)));
                    }
                }
            } catch (Exception e) {
                MatterOverdrive.log.log(Level.ERROR, e, "There was a problem while loading Completed Quests");
            }
        }
        if (dataTypes.contains(DataType.ACTIVE_QUESTS)) {
            activeQuests.clear();
            try {
                if (tagCompound.hasKey("ActiveQuests", Constants.NBT.TAG_LIST)) {
                    NBTTagList activeQuestsTagList = tagCompound.getTagList("ActiveQuests", Constants.NBT.TAG_COMPOUND);
                    for (int i = 0; i < activeQuestsTagList.tagCount(); i++) {
                        activeQuests.add(QuestStack.loadFromNBT(activeQuestsTagList.getCompoundTagAt(i)));
                    }
                }
            } catch (Exception e) {
                MatterOverdrive.log.log(Level.ERROR, e, "There was a problem while loading Active Quests");
            }
        }
    }

    public void manageQuestCompletion()
    {
        int i = 0;
        while (i < activeQuests.size())
        {
            if (activeQuests.get(i).isCompleted())
            {
                QuestStack questStack = activeQuests.remove(i);
                extendedProperties.getPlayer().addExperience(questStack.getXP(extendedProperties.getPlayer()));
                List<ItemStack> rewards = new ArrayList<>();
                questStack.addRewards(rewards, extendedProperties.getPlayer());
                for (ItemStack stack : rewards)
                {
                    ItemStack leftItemStack = MOInventoryHelper.addItemInContainer(extendedProperties.getPlayer().inventoryContainer,stack);
                    if (leftItemStack != null)
                    {
                        extendedProperties.getPlayer().worldObj.spawnEntityInWorld(new EntityItem(extendedProperties.getPlayer().worldObj,extendedProperties.getPlayer().posX,extendedProperties.getPlayer().posY+extendedProperties.getPlayer().getEyeHeight(),extendedProperties.getPlayer().posZ,leftItemStack));
                    }
                }
                extendedProperties.onQuestCompleted(questStack,i);
            }else
            {
                i++;
            }
        }
    }

    public boolean hasCompletedQuest(QuestStack quest)
    {
        for (QuestStack q : completedQuests)
        {
            if (q.getQuest().areQuestStacksEqual(q,quest))
            {
                return true;
            }
        }
        return false;
    }

    public boolean hasQuest(QuestStack questStack)
    {
        for (QuestStack q : activeQuests)
        {
            if (q.getQuest().areQuestStacksEqual(q,questStack))
            {
                return true;
            }
        }
        return false;
    }

    public QuestStack addQuest(QuestStack questStack)
    {
        if(questStack.getQuest() != null && activeQuests.add(questStack))
        {
            return questStack;
        }
        return null;
    }

    public void onEvent(Event event)
    {
        if (extendedProperties != null && extendedProperties.getPlayer() != null && extendedProperties.getPlayer() instanceof EntityPlayerMP) {
            for (int i = 0; i < activeQuests.size(); i++) {
                if (activeQuests.get(i).getQuest() != null) {
                    if (activeQuests.get(i).getQuest().onEvent(activeQuests.get(i), event, extendedProperties.getPlayer())) {
                        //MatterOverdrive.packetPipeline.sendTo(new PacketSyncQuests(this,EnumSet.of(DataType.ACTIVE_QUESTS)),(EntityPlayerMP) extendedProperties.getPlayer());
                        MatterOverdrive.packetPipeline.sendTo(new PacketUpdateQuest(i, this,PacketUpdateQuest.UPDATE_QUEST), (EntityPlayerMP) extendedProperties.getPlayer());
                    }
                }
            }
        }
    }

    public void clearActiveQuests()
    {
        activeQuests.clear();
    }

    public void clearCompletedQuests()
    {
        completedQuests.clear();
    }

    public void removeQuest(QuestStack questStack)
    {
        activeQuests.remove(questStack);
    }

    public QuestStack removeQuest(int id)
    {
        return activeQuests.remove(id);
    }

    public List<QuestStack> getActiveQuests()
    {
        return activeQuests;
    }

    public List<QuestStack> getCompletedQuests()
    {
        return completedQuests;
    }

    public enum DataType
    {
        ACTIVE_QUESTS,COMPLETED_QUESTS
    }
}
