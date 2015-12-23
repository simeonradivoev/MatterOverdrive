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
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 12/9/2015.
 */
public class QuestLogicMine extends AbstractQuestLogic
{
    Block block;
    boolean hasMetadata;
    int metadata;
    int minMineCount;
    int maxMineCount;
    int xpPerMine;

    public QuestLogicMine(Block block,int minMineCount,int maxMineCount,int xpPerMine)
    {
        this.block = block;
        this.minMineCount = minMineCount;
        this.maxMineCount = maxMineCount;
        this.xpPerMine = xpPerMine;
    }

    public QuestLogicMine(Block block,int minMineCount,int maxMineCount,int xpPerMine,int metadata)
    {
        this(block,minMineCount,maxMineCount,xpPerMine);
        this.metadata = metadata;
        this.hasMetadata = true;
    }

    @Override
    public String modifyInfo(QuestStack questStack, String info)
    {
        info = info.replace("$maxMineAmount",Integer.toString(getMaxMineCount(questStack)));
        info = info.replace("$mineBlock",block.getLocalizedName());
        return info;
    }

    @Override
    public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex)
    {
        return getMineCount(questStack) >= getMaxMineCount(questStack);
    }

    @Override
    public String modifyObjective(QuestStack questStack, EntityPlayer entityPlayer, String objective, int objectiveIndex) {
        objective = objective.replace("$mineAmount",Integer.toString(getMineCount(questStack)));
        objective = objective.replace("$maxMineAmount",Integer.toString(getMaxMineCount(questStack)));
        objective = objective.replace("$mineBlock",block.getLocalizedName());
        return objective;
    }

    @Override
    public void initQuestStack(Random random, QuestStack questStack)
    {
        if (questStack.getTagCompound() == null)
            questStack.setTagCompound(new NBTTagCompound());

        questStack.getTagCompound().setInteger("MaxMineCount",random(random,minMineCount,maxMineCount));
    }

    @Override
    public boolean onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer) {
        return false;
    }

    @Override
    public void onCompleted(QuestStack questStack, EntityPlayer entityPlayer) {

    }

    @Override
    public void modifyRewards(QuestStack questStack, EntityPlayer entityPlayer, List<ItemStack> rewards) {

    }

    public int getMineCount(QuestStack questStack)
    {
        if (questStack.getTagCompound() != null)
        {
            return questStack.getTagCompound().getInteger("MineCount");
        }
        return 0;
    }

    public void setMineCount(QuestStack questStack,int mineCount)
    {
        if (questStack.getTagCompound() == null)
            questStack.setTagCompound(new NBTTagCompound());

        questStack.getTagCompound().setInteger("MineCount",mineCount);
    }

    public int getMaxMineCount(QuestStack questStack)
    {
        if (questStack.getTagCompound() != null)
        {
            return questStack.getTagCompound().getInteger("MaxMineCount");
        }
        return 0;
    }
}
