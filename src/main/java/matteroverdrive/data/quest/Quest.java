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
import net.minecraft.entity.player.EntityPlayer;

import java.util.Random;

/**
 * Created by Simeon on 11/19/2015.
 */
public abstract class Quest
{
    protected String title;
    public Quest(String title)
    {
        this.title = title;
    }
    public abstract boolean canBeAccepted(QuestStack questStack,EntityPlayer entityPlayer);
    public abstract String getInfo(QuestStack questStack,EntityPlayer entityPlayer);
    public abstract String getObjective(QuestStack questStack,EntityPlayer entityPlayer,int objectiveIndex);
    public abstract int getObjectivesCount(QuestStack questStack,EntityPlayer entityPlayer);
    public abstract boolean isObjectiveCompleted(QuestStack questStack,EntityPlayer entityPlayer,int objectiveIndex);
    public String getTitle(QuestStack questStack) {return title;}
    public String getTitle(QuestStack questStack,EntityPlayer entityPlayer)
    {
        return getTitle(questStack);
    }
    public abstract boolean areQuestStacksEqual(QuestStack questStackOne,QuestStack questStackTwo);
    public void initQuestStack(Random random,QuestStack questStack,EntityPlayer entityPlayer)
    {
        initQuestStack(random,questStack);
    }
    public abstract void initQuestStack(Random random,QuestStack questStack);
    public abstract boolean onEvent(QuestStack questStack,Event event,EntityPlayer entityPlayer);
    public abstract void onCompleted(QuestStack questStack,EntityPlayer entityPlayer);
}
