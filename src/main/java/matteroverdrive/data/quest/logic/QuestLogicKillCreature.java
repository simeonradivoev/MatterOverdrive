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
import cpw.mods.fml.common.registry.EntityRegistry;
import matteroverdrive.data.quest.QuestStack;
import matteroverdrive.entity.player.MOExtendedProperties;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 11/19/2015.
 */
public class QuestLogicKillCreature extends AbstractQuestLogic
{
    ItemStack killWithItemStack;
    Item killWithItem;
    boolean explosionOnly;
    boolean burnOnly;
    boolean shootOnly;
    boolean onlyChildren;
    int minKillCount;
    int maxKillCount;
    int xpPerKill;
    Class<? extends EntityLivingBase>[] creatureClasses;

    public QuestLogicKillCreature(Class<? extends EntityLivingBase> creatureClass,int minKillCount,int maxKillCount,int xpPerKill)
    {
        this(new Class[]{creatureClass},minKillCount,maxKillCount,xpPerKill);
    }

    public QuestLogicKillCreature(Class<? extends EntityLivingBase>[] creatureClasses,int minKillCount,int maxKillCount,int xpPerKill)
    {
        this.creatureClasses = creatureClasses;
        this.minKillCount = minKillCount;
        this.maxKillCount = maxKillCount;
        this.xpPerKill = xpPerKill;
    }

    @Override
    public String modifyInfo(QuestStack questStack,String info)
    {
        return String.format(info,"",Integer.toString(getMaxKillCount(questStack)), getTargetName(questStack));
    }

    @Override
    public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex)
    {
        return getKillCount(questStack) >= getMaxKillCount(questStack);
    }

    @Override
    public String modifyObjective(QuestStack questStack, EntityPlayer entityPlayer,String objetive, int objectiveIndex) {
        return String.format(objetive,"",getKillCount(questStack),getMaxKillCount(questStack),getTargetName(questStack));
    }

    @Override
    public void initQuestStack(Random random,QuestStack questStack)
    {
        if (questStack.getTagCompound() == null)
        {
            questStack.setTagCompound(new NBTTagCompound());
        }
        questStack.getTagCompound().setInteger("MaxKillCount",minKillCount + random.nextInt(maxKillCount - minKillCount));
        questStack.getTagCompound().setByte("KillType",(byte) random.nextInt(creatureClasses.length));
    }

    @Override
    public boolean onEvent(QuestStack questStack, Event event,EntityPlayer entityPlayer)
    {
        if (event instanceof LivingDeathEvent)
        {
            LivingDeathEvent deathEvent = (LivingDeathEvent)event;
            Class targetClass = creatureClasses[getKillType(questStack)];
            if (deathEvent.entityLiving != null && targetClass.isInstance(deathEvent.entityLiving))
            {
                if (shootOnly && !((LivingDeathEvent) event).source.isProjectile())
                    return false;
                if (burnOnly && !((LivingDeathEvent) event).source.isFireDamage())
                    return false;
                if (explosionOnly && !((LivingDeathEvent) event).source.isExplosion())
                    return false;
                if (killWithItem != null && (entityPlayer.getHeldItem() == null || entityPlayer.getHeldItem().getItem() != killWithItem))
                    return false;
                if (killWithItemStack != null && (entityPlayer.getHeldItem() == null || !ItemStack.areItemStacksEqual(entityPlayer.getHeldItem(),killWithItemStack)))
                    return false;
                if (onlyChildren && !((LivingDeathEvent) event).entityLiving.isChild())
                    return false;

                if (questStack.getTagCompound() == null)
                {
                    questStack.setTagCompound(new NBTTagCompound());
                }

                int currentKillCount = getKillCount(questStack);
                currentKillCount++;
                if (currentKillCount >= getMaxKillCount(questStack))
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

    public int getKillType(QuestStack questStack)
    {
        if (questStack.getTagCompound() != null)
        {
            return questStack.getTagCompound().getByte("KillType");
        }
        return 0;
    }

    public int getMaxKillCount(QuestStack questStack)
    {
        if (questStack.getTagCompound() != null)
            return questStack.getTagCompound().getInteger("MaxKillCount");
        return 0;
    }

    public int getKillCount(QuestStack questStack)
    {
        if (questStack.getTagCompound() != null)
            return questStack.getTagCompound().getInteger("KillCount");
        return 0;
    }

    public String getTargetName(QuestStack questStack)
    {
        Class<? extends EntityLivingBase> targetClass = creatureClasses[getKillType(questStack)];
        EntityRegistry.EntityRegistration entityRegistration = EntityRegistry.instance().lookupModSpawn(targetClass,true);
        if (entityRegistration != null)
        {
            return entityRegistration.getEntityName();
        }
        else
        {
            String name = (String) EntityList.classToStringMapping.get(targetClass);
            if (name != null)
            {
                return name;
            }
        }
        return "Unknown Target";
    }

    public Class<? extends EntityLivingBase>[] getCreatureClasses()
    {
        return creatureClasses;
    }

    @Override
    public void onCompleted(QuestStack questStack, EntityPlayer entityPlayer)
    {

    }

    @Override
    public void modifyRewards(QuestStack questStack, EntityPlayer entityPlayer, List<ItemStack> rewards) {
        rewards.add(new ItemStack(Items.egg,5));
    }

    @Override
    public int modifyXP(QuestStack questStack, EntityPlayer entityPlayer, int originalXp)
    {

        return originalXp + xpPerKill * getMaxKillCount(questStack);
    }

    public QuestLogicKillCreature setOnlyChildren(boolean onlyChildren)
    {
        this.onlyChildren = onlyChildren;
        return this;
    }

    public QuestLogicKillCreature setShootOnly(boolean shootOnly)
    {
        this.shootOnly = shootOnly;
        return this;
    }

    public QuestLogicKillCreature setBurnOnly(boolean burnOnly)
    {
        this.burnOnly = burnOnly;
        return this;
    }

    public QuestLogicKillCreature setExplosionOnly(boolean explosionOnly)
    {
        this.explosionOnly = explosionOnly;
        return this;
    }
}
