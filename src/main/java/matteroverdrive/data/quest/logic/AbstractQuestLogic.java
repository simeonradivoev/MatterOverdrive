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

import matteroverdrive.api.quest.IQuestLogic;
import matteroverdrive.data.quest.QuestStack;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 12/5/2015.
 */
public abstract class AbstractQuestLogic implements IQuestLogic
{
    @Override
    public String modifyTitle(QuestStack questStack, String original)
    {
        return original;
    }
    @Override
    public boolean canAccept(QuestStack questStack, EntityPlayer entityPlayer) {
        return true;
    }
    @Override
    public int modifyObjectiveCount(QuestStack questStack, EntityPlayer entityPlayer, int count) {
        return count;
    }
    @Override
    public boolean areQuestStacksEqual(QuestStack questStackOne, QuestStack questStackTwo) {
        return true;
    }
    @Override
    public int modifyXP(QuestStack questStack, EntityPlayer entityPlayer, int originalXp) {
        return originalXp;
    }
}
