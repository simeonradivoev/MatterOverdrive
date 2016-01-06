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
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.data.quest.QuestItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 11/25/2015.
 */
public class QuestLogicCollectItem extends QuestLogicRandomItem
{
    int dimensionID;
    boolean inSpecificDimension;
    boolean destroyOnCollect;
    int xpPerItem;
    int minItemCount;
    int maxItemCount;

    public QuestLogicCollectItem(QuestItem questItem,int minItemCount,int maxItemCount,int xpPerItem)
    {
        init(new QuestItem[]{questItem},minItemCount,maxItemCount,xpPerItem);
    }

    public QuestLogicCollectItem(ItemStack itemStack,int minItemCount,int maxItemCount,int xpPerItem)
    {
        init(new QuestItem[]{QuestItem.fromItemStack(itemStack)},minItemCount,maxItemCount,xpPerItem);
    }

    public QuestLogicCollectItem(Item item, int minItemCount, int maxItemCount, int xpPerItem)
    {
        init(new QuestItem[]{QuestItem.fromItemStack(new ItemStack(item))},minItemCount,maxItemCount,xpPerItem);
    }

    protected void init(QuestItem[] questItems,int minItemCount,int maxItemCount,int xpPerItem)
    {
        super.init(questItems);
        this.minItemCount = minItemCount;
        this.maxItemCount = maxItemCount;
        this.xpPerItem = xpPerItem;
    }

    public QuestLogicCollectItem(ItemStack[] itemStacks,int minItemCount,int maxItemCount,int xpPerItem)
    {
        QuestItem[] questItems = new QuestItem[itemStacks.length];
        for (int i = 0;i < itemStacks.length;i++)
        {
            questItems[i] = QuestItem.fromItemStack(itemStacks[i]);
        }
        init(questItems,minItemCount,maxItemCount,xpPerItem);
    }

    public QuestLogicCollectItem(Item[] items,int minItemCount,int maxItemCount,int xpPerItem)
    {
        QuestItem[] questItems = new QuestItem[items.length];
        for (int i = 0;i < items.length;i++)
        {
            questItems[i] = QuestItem.fromItemStack(new ItemStack(items[i]));
        }
        init(questItems,minItemCount,maxItemCount,xpPerItem);
    }

    public QuestLogicCollectItem(QuestItem[] questItems,int minItemCount,int maxItemCount,int xpPerItem)
    {
        init(questItems,minItemCount,maxItemCount,xpPerItem);
    }

    @Override
    public String modifyInfo(QuestStack questStack, String info) {
        ItemStack itemStack = getItem(questStack);
        return String.format(info,"",getMaxItemCount(questStack),itemStack != null ? itemStack.getDisplayName() : "Unknown Item");
    }

    @Override
    public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex) {
        return getItemCount(entityPlayer,questStack) >= getMaxItemCount(questStack);
    }

    @Override
    public String modifyObjective(QuestStack questStack, EntityPlayer entityPlayer, String objective, int objectiveIndex)
    {
        ItemStack itemStack = getItem(questStack);
        return String.format(objective,"", getItemCount(entityPlayer,questStack),getMaxItemCount(questStack),itemStack != null ? itemStack.getDisplayName() : "Unknown Item");
    }

    @Override
    public void initQuestStack(Random random, QuestStack questStack)
    {
        initTag(questStack);
        initItemType(random,questStack);
        getTag(questStack).setInteger("MaxItemCount",random(random,minItemCount,maxItemCount));
    }

    public int getItemCount(EntityPlayer entityPlayer, QuestStack questStack)
    {
        if (destroyOnCollect)
        {
            if (hasTag(questStack))
            {
                return getTag(questStack).getInteger("ItemCount");
            }
            return 0;
        }
        else
        {
            int itemCount = 0;
            ItemStack itemStack = getItem(questStack);

            if (itemStack != null)
            {
                for (int i = 0; i < entityPlayer.inventory.getSizeInventory(); i++)
                {
                    ItemStack stackInSlot = entityPlayer.inventory.getStackInSlot(i);
                    if (stackInSlot != null)
                    {
                        if (stackInSlot.isItemEqual(itemStack))
                        {
                            itemCount += stackInSlot.stackSize;
                        }
                    }
                }
            }
            return itemCount;
        }
    }

    public void setItemCount(QuestStack questStack,int count)
    {
        if (destroyOnCollect)
        {
            initTag(questStack);
            getTag(questStack).setInteger("ItemCount",count);
        }
    }

    public int getMaxItemCount(QuestStack questStack)
    {
        if (hasTag(questStack))
        {
            ItemStack itemStack = getItem(questStack);
            return itemStack.stackSize + getTag(questStack).getInteger("MaxItemCount");
        }
        return 0;
    }

    @Override
    public boolean onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer)
    {
        if (destroyOnCollect && event instanceof EntityItemPickupEvent && ((EntityItemPickupEvent) event).item.getEntityItem() != null)
        {
            if (inSpecificDimension && entityPlayer.worldObj.provider.dimensionId != dimensionID)
            return false;

            ItemStack itemStack = ((EntityItemPickupEvent) event).item.getEntityItem();
            ItemStack questItem = getItem(questStack);
            if (itemStack != null && questItem != null && ItemStack.areItemStacksEqual(itemStack,questItem))
            {
                initTag(questStack);

                int currentItemCount = getItemCount(entityPlayer,questStack);
                if (currentItemCount < getMaxItemCount(questStack))
                {
                    setItemCount(questStack,++currentItemCount);

                    if (isObjectiveCompleted(questStack,entityPlayer,0) && autoComplete)
                    {
                        questStack.markComplited(entityPlayer,false);
                    }

                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onTaken(QuestStack questStack, EntityPlayer entityPlayer)
    {

    }

    @Override
    public void onCompleted(QuestStack questStack, EntityPlayer entityPlayer)
    {
        if (!destroyOnCollect)
        {
            int itemCount = getMaxItemCount(questStack);
            ItemStack itemStack = getItem(questStack);

            if (itemStack != null)
            {
                for (int i = 0; i < entityPlayer.inventory.getSizeInventory(); i++)
                {
                    ItemStack stackInSlot = entityPlayer.inventory.getStackInSlot(i);
                    if (stackInSlot != null)
                    {
                        if (stackInSlot.isItemEqual(itemStack) && itemCount > 0)
                        {
                            int newItemCount = Math.max(0, itemCount - stackInSlot.stackSize);
                            int takenFromStack = itemCount - newItemCount;
                            entityPlayer.inventory.decrStackSize(i, takenFromStack);
                            itemCount = newItemCount;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void modifyRewards(QuestStack questStack, EntityPlayer entityPlayer, List<IQuestReward> rewards) {

    }

    @Override
    public int modifyXP(QuestStack questStack, EntityPlayer entityPlayer, int originalXp)
    {
        return originalXp + getMaxItemCount(questStack) * xpPerItem;
    }

    public QuestLogicCollectItem setDestroyOnCollect(boolean destroyOnCollect)
    {
        this.destroyOnCollect = destroyOnCollect;
        return this;
    }

    public QuestLogicCollectItem setDimensionID(int dimensionID)
    {
        this.inSpecificDimension = true;
        this.dimensionID = dimensionID;
        return this;
    }
}
