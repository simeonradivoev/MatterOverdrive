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

package matteroverdrive.api.quest;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 11/19/2015.
 */
public interface IQuestLogic
{
    String modifyTitle(QuestStack questStack,String original);
    boolean canAccept(QuestStack questStack,EntityPlayer entityPlayer);
    String modifyInfo(QuestStack questStack,String info);
    boolean isObjectiveCompleted(QuestStack questStack,EntityPlayer entityPlayer,int objectiveIndex);
    String modifyObjective(QuestStack questStack,EntityPlayer entityPlayer,String objective,int objectiveIndex);
    int modifyObjectiveCount(QuestStack questStack,EntityPlayer entityPlayer,int count);
    void initQuestStack(Random random,QuestStack questStack);
    boolean onEvent(QuestStack questStack, Event event,EntityPlayer entityPlayer);
    boolean areQuestStacksEqual(QuestStack questStackOne,QuestStack questStackTwo);
    void onTaken(QuestStack questStack,EntityPlayer entityPlayer);
    void onCompleted(QuestStack questStack,EntityPlayer entityPlayer);
    int modifyXP(QuestStack questStack,EntityPlayer entityPlayer,int originalXp);
    void modifyRewards(QuestStack questStack, EntityPlayer entityPlayer, List<IQuestReward> rewards);
    String getID();
}
