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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 11/25/2015.
 */
public class QuestLogicCollectItem extends AbstractQuestLogic
{
    int dimensionID;
    boolean inSpecificDimension;
    boolean destroyOnCollect;
    ItemStack[] items;
    int minItemCount;
    int maxItemCount;
    int xpPerItem;

    public QuestLogicCollectItem(ItemStack itemStack,int minItemCount,int maxItemCount,int xpPerItem)
    {
        this(new ItemStack[]{itemStack},minItemCount,maxItemCount,xpPerItem);
    }

    public QuestLogicCollectItem(ItemStack[] itemStacks,int minItemCount,int maxItemCount,int xpPerItem)
    {
        this.items = itemStacks;
        this.minItemCount = minItemCount;
        this.maxItemCount = maxItemCount;
        this.xpPerItem = xpPerItem;
    }

    @Override
    public String modifyInfo(QuestStack questStack, String info) {
        return String.format(info,"",getMaxItemCount(questStack),getItem(questStack).getDisplayName());
    }

    @Override
    public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex) {
        return getItemCount(entityPlayer,questStack) >= getMaxItemCount(questStack);
    }

    @Override
    public String modifyObjective(QuestStack questStack, EntityPlayer entityPlayer, String objective, int objectiveIndex)
    {
        return String.format(objective,"", getItemCount(entityPlayer,questStack),getMaxItemCount(questStack),getItem(questStack).getDisplayName());
    }

    @Override
    public void initQuestStack(Random random, QuestStack questStack)
    {
        if (questStack.getTagCompound() == null)
        {
            questStack.setTagCompound(new NBTTagCompound());
        }

        questStack.getTagCompound().setByte("ItemType",(byte) random.nextInt(items.length));
        questStack.getTagCompound().setInteger("MaxItemCount",minItemCount + random.nextInt(maxItemCount-minItemCount));
    }

    public int getItemCount(EntityPlayer entityPlayer, QuestStack questStack)
    {
        if (destroyOnCollect)
        {
            if (questStack.getTagCompound() != null)
            {
                return questStack.getTagCompound().getInteger("ItemCount");
            }
            return 0;
        }
        else
        {
            int itemCount = 0;
            ItemStack itemStack = getItem(questStack);

            for (int i = 0;i < entityPlayer.inventory.getSizeInventory();i++)
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
            return itemCount;
        }
    }

    public int getMaxItemCount(QuestStack questStack)
    {
        if (questStack.getTagCompound() != null)
        {
            return questStack.getTagCompound().getInteger("MaxItemCount");
        }
        return 0;
    }

    public ItemStack getItem(QuestStack questStack)
    {
        return items[getItemType(questStack)];
    }

    public int getItemType(QuestStack questStack)
    {
        if (questStack.getTagCompound() != null)
        {
            return questStack.getTagCompound().getByte("ItemType");
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
            if (ItemStack.areItemStacksEqual(itemStack,getItem(questStack)))
            {
                if (questStack.getTagCompound() == null)
                {
                    questStack.setTagCompound(new NBTTagCompound());
                }

                int currentItemCount = questStack.getTagCompound().getInteger("ItemCount");
                questStack.getTagCompound().setInteger("ItemCount",++currentItemCount);
                if (currentItemCount >= getMaxItemCount(questStack))
                {
                    questStack.setCompleted(true);
                }else
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onCompleted(QuestStack questStack, EntityPlayer entityPlayer)
    {
        if (!destroyOnCollect)
        {
            int itemCount = getMaxItemCount(questStack);
            ItemStack itemStack = getItem(questStack);

            for (int i = 0;i < entityPlayer.inventory.getSizeInventory();i++)
            {
                ItemStack stackInSlot = entityPlayer.inventory.getStackInSlot(i);
                if (stackInSlot != null)
                {
                    if (stackInSlot.isItemEqual(itemStack) && itemCount > 0)
                    {
                        int newItemCount = Math.max(0,itemCount-stackInSlot.stackSize);
                        int takenFromStack = itemCount-newItemCount;
                        entityPlayer.inventory.decrStackSize(i,takenFromStack);
                        itemCount = newItemCount;
                    }
                }
            }
        }
    }

    @Override
    public void modifyRewards(QuestStack questStack, EntityPlayer entityPlayer, List<ItemStack> rewards) {

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
