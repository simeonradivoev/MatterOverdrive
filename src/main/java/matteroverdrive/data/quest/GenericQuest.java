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
import matteroverdrive.data.quest.logic.QuestLogic;
import matteroverdrive.entity.player.MOExtendedProperties;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Random;

/**
 * Created by Simeon on 11/19/2015.
 */
public class GenericQuest extends Quest
{
    protected QuestLogic questLogic;

    public GenericQuest(QuestLogic questLogic,String title)
    {
        super(title);
        this.questLogic = questLogic;
    }

    @Override
    public boolean canBeAccepted(QuestStack questStack, EntityPlayer entityPlayer)
    {
        MOExtendedProperties extendedProperties = MOExtendedProperties.get(entityPlayer);
        if (extendedProperties != null)
        {
            return questLogic.canAccept(questStack,entityPlayer) && !extendedProperties.hasCompletedQuest(questStack) && !extendedProperties.hasQuest(questStack);
        }
        return false;
    }

    @Override
    public String getTitle(QuestStack questStack)
    {
        return questLogic.modifyTitle(questStack,MOStringHelper.translateToLocal("quest." + title + ".title"));
    }

    @Override
    public String getTitle(QuestStack questStack,EntityPlayer entityPlayer)
    {
        return questLogic.modifyTitle(questStack,replaceVariables(MOStringHelper.translateToLocal("quest." + title + ".title"),entityPlayer));
    }

    @Override
    public String getInfo(QuestStack questStack, EntityPlayer entityPlayer)
    {
        return questLogic.modifyInfo(questStack,replaceVariables(MOStringHelper.translateToLocal("quest." + title + ".info"),entityPlayer));
    }

    @Override
    public String getObjective(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex)
    {
        return questLogic.modifyObjective(questStack,entityPlayer,replaceVariables(MOStringHelper.translateToLocal("quest." + title + ".objective."+objectiveIndex),entityPlayer),objectiveIndex);
    }

    @Override
    public int getObjectivesCount(QuestStack questStack, EntityPlayer entityPlayer)
    {
        return questLogic.modifyObjectiveCount(questStack,entityPlayer,1);
    }

    @Override
    public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex) {
        return questLogic.isObjectiveCompleted(questStack,entityPlayer,objectiveIndex);
    }

    @Override
    public boolean areQuestStacksEqual(QuestStack questStackOne, QuestStack questStackTwo)
    {
        if (questStackOne.getQuest() instanceof GenericQuest && questStackTwo.getQuest() instanceof GenericQuest)
        {
            return ((GenericQuest) questStackOne.getQuest()).getQuestLogic().equals(((GenericQuest) questStackTwo.getQuest()).getQuestLogic());
        }
        return false;
    }

    @Override
    public void initQuestStack(Random random,QuestStack questStack) {
        questLogic.initQuestStack(random,questStack);
    }

    @Override
    public boolean onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer)
    {
        return questLogic.onEvent(questStack,event,entityPlayer);
    }

    @Override
    public void onCompleted(QuestStack questStack, EntityPlayer entityPlayer)
    {
        questLogic.onCompleted(questStack,entityPlayer);
    }

    public String replaceVariables(String text,EntityPlayer entityPlayer)
    {
        if (entityPlayer != null)
        {
            return text.replace("%1$s",entityPlayer.getDisplayName());
        }
        return text;
    }

    public QuestLogic getQuestLogic()
    {
        return questLogic;
    }

    public void setQuestLogic(QuestLogic questLogic)
    {
        this.questLogic = questLogic;
    }
}
