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

package matteroverdrive.dialog;

import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.entity.player.MOExtendedProperties;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 11/22/2015.
 */
public class DialogMessageQuestOnObjectivesCompleted extends DialogMessage
{
    QuestStack questStack;
    int[] completedObjectives;

    public DialogMessageQuestOnObjectivesCompleted(){super();}
    public DialogMessageQuestOnObjectivesCompleted(QuestStack questStack,int[] completedObjectives){super();this.questStack = questStack;this.completedObjectives = completedObjectives;}

    @Override
    public boolean isVisible(IDialogNpc npc, EntityPlayer player)
    {
        MOExtendedProperties extendedProperties = MOExtendedProperties.get(player);
        if (extendedProperties != null)
        {
            for (QuestStack questStack : extendedProperties.getQuestData().getActiveQuests())
            {
                if (questStack.getQuest().areQuestStacksEqual(questStack,this.questStack))
                {
                    for (int i = 0;i < completedObjectives.length;i++)
                    {
                        if (!questStack.isObjectiveCompleted(player,completedObjectives[i]))
                        {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void setQuest(QuestStack questStack)
    {
        this.questStack = questStack;
    }

    public void setCompletedObjectives(int[] completedObjectives)
    {
        this.completedObjectives = completedObjectives;
    }
}
