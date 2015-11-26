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

package matteroverdrive.data.quest.logic;

import cpw.mods.fml.common.eventhandler.Event;
import matteroverdrive.data.quest.QuestStack;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Random;

/**
 * Created by Simeon on 11/19/2015.
 */
public abstract class QuestLogic
{
    public abstract String modifyTitle(QuestStack questStack,String original);
    public abstract boolean canAccept(QuestStack questStack,EntityPlayer entityPlayer);
    public abstract String modifyInfo(QuestStack questStack,String info);
    public abstract boolean isObjectiveCompleted(QuestStack questStack,EntityPlayer entityPlayer,int objectiveIndex);
    public abstract String modifyObjective(QuestStack questStack,EntityPlayer entityPlayer,String objective,int objectiveIndex);
    public abstract int modifyObjectiveCount(QuestStack questStack,EntityPlayer entityPlayer,int count);
    public abstract void initQuestStack(Random random,QuestStack questStack);
    public abstract boolean onEvent(QuestStack questStack, Event event,EntityPlayer entityPlayer);
    public abstract boolean areQuestStacksEqual(QuestStack questStackOne,QuestStack questStackTwo);
    public abstract void onCompleted(QuestStack questStack,EntityPlayer entityPlayer);
}
