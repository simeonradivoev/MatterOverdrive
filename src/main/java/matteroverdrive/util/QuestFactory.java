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

package matteroverdrive.util;


import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.quest.IQuest;
import matteroverdrive.data.quest.QuestStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

import java.util.Random;

/**
 * Created by Simeon on 11/19/2015.
 */
public class QuestFactory
{
    public QuestStack generateQuestStack(Random random, IQuest quest)
    {
        QuestStack questStack = new QuestStack(quest);
        quest.initQuestStack(random,questStack);
        return questStack;
    }

    public String getFormattedQuestObjective(EntityPlayer entityPlayer,QuestStack questStack,int objectiveInex)
    {
        boolean isCompleted = questStack.isObjectiveCompleted(entityPlayer,objectiveInex);
        if (isCompleted)
        {
            return EnumChatFormatting.DARK_GREEN + "■ " + questStack.getObjective(entityPlayer,objectiveInex);
        }else
        {
            return EnumChatFormatting.DARK_GREEN + "□ " + questStack.getObjective(entityPlayer,objectiveInex);
        }
    }

    public QuestStack generateQuestStack(String questName)
    {
        IQuest quest = MatterOverdrive.quests.getQuestByName(questName);
        if (quest != null) {
            QuestStack questStack = new QuestStack(quest);
            quest.initQuestStack(MatterOverdrive.quests.random,questStack);
            return questStack;
        }
        return null;
    }
}
