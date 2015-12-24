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
import cpw.mods.fml.common.gameevent.PlayerEvent;
import matteroverdrive.data.quest.QuestItem;
import matteroverdrive.data.quest.QuestStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 12/9/2015.
 */
public class QuestLogicCraft extends QuestLogicRandomItem
{
    int minCraftCount;
    int maxCraftCount;
    int xpPerCraft;

    public QuestLogicCraft(ItemStack itemStack,int minCraftCount,int maxCraftCount,int xpPerCraft)
    {
        init(new QuestItem[]{QuestItem.fromItemStack(itemStack)},minCraftCount,maxCraftCount,xpPerCraft);
    }

    public QuestLogicCraft(ItemStack[] itemStacks,int minCraftCount,int maxCraftCount,int xpPerCraft)
    {
        QuestItem[] questItems = new QuestItem[itemStacks.length];
        for (int i = 0;i < itemStacks.length;i++)
        {
            questItems[i] = QuestItem.fromItemStack(itemStacks[i]);
        }
        init(questItems,minCraftCount,maxCraftCount,xpPerCraft);
    }

    public QuestLogicCraft(QuestItem questItem,int minCraftCount,int maxCraftCount,int xpPerCraft)
    {
        init(new QuestItem[]{questItem},minCraftCount,maxCraftCount,xpPerCraft);
    }

    public QuestLogicCraft(QuestItem[] questItem,int minCraftCount,int maxCraftCount,int xpPerCraft)
    {
        init(questItem,minCraftCount,maxCraftCount,xpPerCraft);
    }

    protected void init(QuestItem[] items,int minCraftCount,int maxCraftCount,int xpPerCraft)
    {
        init(items);
        this.minCraftCount = minCraftCount;
        this.maxCraftCount = maxCraftCount;
        this.xpPerCraft = xpPerCraft;
    }

    @Override
    public String modifyInfo(QuestStack questStack, String info)
    {
        info = info.replace("$craftMaxAmount",Integer.toString(getMaxCraftCount(questStack)));
        ItemStack itemStack = getItem(questStack);
        info = info.replace("$craftItem",itemStack != null ? itemStack.getDisplayName() : "Unknown Item");
        return info;
    }

    @Override
    public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex)
    {
        return getCraftCount(questStack) >= getMaxCraftCount(questStack);
    }

    public int getCraftCount(QuestStack questStack)
    {
        if (questStack.getTagCompound() != null)
        {
            return questStack.getTagCompound().getInteger("CraftCount");
        }
        return 0;
    }

    public void setCraftCount(QuestStack questStack,int count)
    {
        if (questStack.getTagCompound() == null)
            questStack.setTagCompound(new NBTTagCompound());

        questStack.getTagCompound().setInteger("CraftCount",count);
    }

    public int getMaxCraftCount(QuestStack questStack)
    {
        if (questStack.getTagCompound() != null)
        {
            ItemStack itemStack = getItem(questStack);
            return itemStack.stackSize + questStack.getTagCompound().getInteger("MaxCraftCount");
        }
        return 0;
    }

    @Override
    public String modifyObjective(QuestStack questStack, EntityPlayer entityPlayer, String objective, int objectiveIndex)
    {
        objective = objective.replace("$craftAmount",Integer.toString(getCraftCount(questStack)));
        objective = objective.replace("$craftMaxAmount",Integer.toString(getMaxCraftCount(questStack)));
        ItemStack itemStack = getItem(questStack);
        objective = objective.replace("$craftItem",itemStack != null ? itemStack.getDisplayName() : "Unknown Item");
        return objective;
    }

    @Override
    public void initQuestStack(Random random, QuestStack questStack)
    {
        if (questStack.getTagCompound() == null)
            questStack.setTagCompound(new NBTTagCompound());
        initItemType(random,questStack);
        questStack.getTagCompound().setInteger("MaxCraftCount",random(random,minCraftCount,maxCraftCount));
    }

    @Override
    public boolean onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer)
    {
        if (event instanceof PlayerEvent.ItemCraftedEvent)
        {
            ItemStack itemStack = getItem(questStack);
            if (itemStack != null && ((PlayerEvent.ItemCraftedEvent) event).crafting.isItemEqual(itemStack))
            {
                if (getCraftCount(questStack) < getMaxCraftCount(questStack))
                {
                    setCraftCount(questStack, getCraftCount(questStack) + 1);

                    if (isObjectiveCompleted(questStack,entityPlayer,0) && autoComplete)
                    {
                        questStack.setCompleted(true);
                    } else
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onCompleted(QuestStack questStack, EntityPlayer entityPlayer) {

    }

    @Override
    public void modifyRewards(QuestStack questStack, EntityPlayer entityPlayer, List<ItemStack> rewards) {

    }

    @Override
    public int modifyXP(QuestStack questStack, EntityPlayer entityPlayer, int originalXp)
    {
        return originalXp + xpPerCraft * getMaxCraftCount(questStack);
    }
}
