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
import matteroverdrive.entity.player.MOExtendedProperties;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.Random;

/**
 * Created by Simeon on 11/19/2015.
 */
public class QuestLogicKillCreature extends QuestLogic
{
    int minKillCount;
    int maxKillCount;
    Class<? extends EntityLivingBase> creatureClass;

    public QuestLogicKillCreature(Class<? extends EntityLivingBase> creatureClass,int minKillCount,int maxKillCount)
    {
        this.creatureClass = creatureClass;
        this.minKillCount = minKillCount;
        this.maxKillCount = maxKillCount;
    }

    @Override
    public String modifyTitle(QuestStack questStack,String original) {
        return original;
    }

    @Override
    public boolean canAccept(QuestStack questStack, EntityPlayer entityPlayer)
    {
        return true;
    }

    @Override
    public String modifyInfo(QuestStack questStack,String info)
    {
        int maxKillCount = 0;
        if (questStack.getTagCompound() != null)
        {
            maxKillCount = questStack.getTagCompound().getInteger("MaxKillCount");
        }
        return info.replace("%2$s",Integer.toString(maxKillCount));
    }

    @Override
    public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex)
    {
        int killCount = 0;
        int maxKillCount = 0;
        if (questStack.getTagCompound() != null)
        {
            killCount = questStack.getTagCompound().getShort("KillCount");
            maxKillCount = questStack.getTagCompound().getInteger("MaxKillCount");
        }
        return killCount >= maxKillCount;
    }

    @Override
    public String modifyObjective(QuestStack questStack, EntityPlayer entityPlayer,String objetive, int objectiveIndex) {
        int killCount = 0;
        int maxKillCount = 0;
        if (questStack.getTagCompound() != null)
        {
            killCount = questStack.getTagCompound().getShort("KillCount");
            maxKillCount = questStack.getTagCompound().getInteger("MaxKillCount");
        }

        return objetive.replace("%3$s",Integer.toString(killCount)).replace("%2$s",Integer.toString(maxKillCount));
    }

    @Override
    public int modifyObjectiveCount(QuestStack questStack, EntityPlayer entityPlayer,int objectiveCount)
    {
        return objectiveCount;
    }

    @Override
    public void initQuestStack(Random random,QuestStack questStack)
    {
        if (questStack.getTagCompound() == null)
        {
            questStack.setTagCompound(new NBTTagCompound());
        }
        questStack.getTagCompound().setInteger("MaxKillCount",minKillCount + random.nextInt(maxKillCount - minKillCount));
    }

    @Override
    public boolean onEvent(QuestStack questStack, Event event,EntityPlayer entityPlayer)
    {
        if (event instanceof LivingDeathEvent)
        {
            LivingDeathEvent deathEvent = (LivingDeathEvent)event;
            if (deathEvent.entityLiving != null && creatureClass.isInstance(deathEvent.entityLiving))
            {
                if (questStack.getTagCompound() == null)
                {
                    questStack.setTagCompound(new NBTTagCompound());
                }

                int currentKillCount = questStack.getTagCompound().getInteger("KillCount");
                currentKillCount++;
                if (currentKillCount >= questStack.getTagCompound().getInteger("MaxKillCount"))
                {
                    MOExtendedProperties extendedProperties = MOExtendedProperties.get(entityPlayer);
                    if (extendedProperties != null)
                    {
                        questStack.getTagCompound().setInteger("KillCount",currentKillCount);
                        questStack.setCompleted(true);
                    }
                }
                else
                {
                    questStack.getTagCompound().setInteger("KillCount",currentKillCount);
                    return true;
                }
            }
        }
        return false;
    }

    public Class<? extends EntityLivingBase> getCreatureClass()
    {
        return creatureClass;
    }

    @Override
    public boolean areQuestStacksEqual(QuestStack questStackOne, QuestStack questStackTwo)
    {
        return true;
    }
}
