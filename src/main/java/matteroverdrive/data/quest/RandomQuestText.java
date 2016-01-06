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

import matteroverdrive.api.quest.IQuestLogic;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Random;

/**
 * Created by Simeon on 11/19/2015.
 */
public class RandomQuestText extends GenericQuest
{
    int variationsCount;
    public RandomQuestText(IQuestLogic questLogic, String title, int variationsCount,int xpReward)
    {
        super(questLogic, title,xpReward);
        this.variationsCount = variationsCount;
    }

    @Override
    public void initQuestStack(Random random, QuestStack questStack, EntityPlayer entityPlayer)
    {
        super.initQuestStack(random,questStack,entityPlayer);
        NBTTagCompound data = questStack.getTagCompound();
        if (data == null)
        {
            data = new NBTTagCompound();
            questStack.setTagCompound(data);
        }
        data.setShort("Variation",(short) random.nextInt(variationsCount));
    }

    @Override
    public boolean areQuestStacksEqual(QuestStack questStackOne, QuestStack questStackTwo)
    {
        if (questStackOne == null && questStackTwo == null)
        {
            return true;
        }else
        {
            if (questStackOne.getTagCompound() == null && questStackTwo.getTagCompound() == null)
            {
                return super.areQuestStacksEqual(questStackOne,questStackTwo);
            }else if (questStackOne.getTagCompound() != null && questStackTwo.getTagCompound() != null)
            {
                return super.areQuestStacksEqual(questStackOne,questStackTwo) && questStackOne.getTagCompound().getShort("Variation") == questStackTwo.getTagCompound().getShort("Variation");
            }else
            {
                return false;
            }
        }
    }

    public int getVariation(QuestStack questStack)
    {
        if (questStack.getTagCompound() != null)
        {
            return questStack.getTagCompound().getShort("Variation");
        }
        return 0;
    }

    @Override
    public String getTitle(QuestStack questStack)
    {
        return questLogic.modifyTitle(questStack,MOStringHelper.translateToLocal("quest." + title + "." + getVariation(questStack) + ".title"));
    }

    @Override
    public String getTitle(QuestStack questStack,EntityPlayer entityPlayer)
    {
        return questLogic.modifyTitle(questStack,replaceVariables(MOStringHelper.translateToLocal("quest." + title + "." + getVariation(questStack) + ".title"),entityPlayer));
    }

    @Override
    public String getInfo(QuestStack questStack, EntityPlayer entityPlayer)
    {
        return questLogic.modifyInfo(questStack,replaceVariables(MOStringHelper.translateToLocal("quest." + title + "." + getVariation(questStack) + ".info"),entityPlayer));
    }

    @Override
    public String getObjective(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex)
    {
        return questLogic.modifyObjective(questStack,entityPlayer,replaceVariables(MOStringHelper.translateToLocal("quest." + title + "." + getVariation(questStack) + ".objective."+objectiveIndex),entityPlayer),objectiveIndex);
    }
}
