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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import matteroverdrive.api.exceptions.MOQuestParseException;
import matteroverdrive.util.MOJsonHelper;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.entity.EntityList;
import net.minecraftforge.fml.common.eventhandler.Event;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.entity.player.MOExtendedProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 11/19/2015.
 */
public class QuestLogicKillCreature extends AbstractQuestLogic
{
    String regex;
    ItemStack killWithItemStack;
    Item killWithItem;
    boolean explosionOnly;
    boolean burnOnly;
    boolean shootOnly;
    boolean onlyChildren;
    int minKillCount;
    int maxKillCount;
    int xpPerKill;
    String[] creatureTypes;

    public QuestLogicKillCreature(){}

    public QuestLogicKillCreature(String creatureClass,int minKillCount,int maxKillCount,int xpPerKill)
    {
        this(new String[]{creatureClass},minKillCount,maxKillCount,xpPerKill);
    }

    public QuestLogicKillCreature(String[] creatureTypes, int minKillCount, int maxKillCount, int xpPerKill)
    {
        this.creatureTypes = creatureTypes;
        this.minKillCount = minKillCount;
        this.maxKillCount = maxKillCount;
        this.xpPerKill = xpPerKill;
    }

    @Override
    public void loadFromJson(JsonObject jsonObject)
    {
        super.loadFromJson(jsonObject);
        regex = MOJsonHelper.getString(jsonObject,"regex",null);
        killWithItemStack = MOJsonHelper.getItemStack(jsonObject,"kill_item",null);
        explosionOnly = MOJsonHelper.getBool(jsonObject,"explosion_only",false);
        burnOnly = MOJsonHelper.getBool(jsonObject,"burn_only",false);
        shootOnly = MOJsonHelper.getBool(jsonObject,"shoot_only",false);
        onlyChildren = MOJsonHelper.getBool(jsonObject,"children_only",false);
        minKillCount = MOJsonHelper.getInt(jsonObject,"kill_count_min");
        minKillCount = MOJsonHelper.getInt(jsonObject,"kill_count_max");
        xpPerKill = MOJsonHelper.getInt(jsonObject,"xp");
        JsonArray creatureTypes = jsonObject.getAsJsonArray("creatures");
        this.creatureTypes = new String[creatureTypes.size()];
        if (creatureTypes != null)
        {
            for (int i = 0; i < creatureTypes.size(); i++)
            {
                this.creatureTypes[i] = creatureTypes.get(i).getAsString();
            }
        }else
        {
            throw new MOQuestParseException("Missing creatures type list in Quest logic");
        }
    }

    @Override
    public String modifyInfo(QuestStack questStack,String info)
    {
        info = info.replace("$maxKillCount",Integer.toString(getMaxKillCount(questStack)));
        if (killWithItemStack != null)
        {
            info = info.replace("$itemStack",killWithItemStack.getDisplayName());
        }
        return info;
    }

    @Override
    public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex)
    {
        return getKillCount(questStack) >= getMaxKillCount(questStack);
    }

    @Override
    public String modifyObjective(QuestStack questStack, EntityPlayer entityPlayer,String objetive, int objectiveIndex) {
        objetive = objetive.replace("$maxKillCount",Integer.toString(getMaxKillCount(questStack)));
        objetive = objetive.replace("$killCount",Integer.toString(getKillCount(questStack)));
        if (killWithItemStack != null)
        {
            objetive = objetive.replace("$itemStack",killWithItemStack.getDisplayName());
        }
        return objetive;
    }

    @Override
    public void initQuestStack(Random random,QuestStack questStack)
    {
        initTag(questStack);
        getTag(questStack).setInteger("MaxKillCount",random(random,minKillCount,maxKillCount));
    }

    @Override
    public boolean onEvent(QuestStack questStack, Event event,EntityPlayer entityPlayer)
    {
        if (event instanceof LivingDeathEvent)
        {
            LivingDeathEvent deathEvent = (LivingDeathEvent)event;
            if (deathEvent.entityLiving != null && isTarget(questStack,deathEvent.entityLiving))
            {
                if (regex != null && !isTargetNameValid(((LivingDeathEvent) event).entity))
                    return false;
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

                initTag(questStack);
                int currentKillCount = getKillCount(questStack);
                if (currentKillCount < getMaxKillCount(questStack))
                {
                    setKillCount(questStack,++currentKillCount);
                    if (isObjectiveCompleted(questStack,entityPlayer,0) && autoComplete)
                    {
                        MOExtendedProperties extendedProperties = MOExtendedProperties.get(entityPlayer);
                        if (extendedProperties != null)
                        {
                            questStack.markComplited(entityPlayer,false);
                        }
                    }

                    return true;
                }
            }
        }
        return false;
    }

    public boolean isTarget(QuestStack questStack,Entity entity)
    {
        EntityRegistry.EntityRegistration registration = EntityRegistry.instance().lookupModSpawn(entity.getClass(),true);
        if (registration != null)
        {
            for (String type : creatureTypes)
            {
                if (registration.getEntityName().equalsIgnoreCase(type))
                {
                    return true;
                }
            }
        }else
        {
            String entityName = EntityList.getEntityString(entity);
            for (String type : creatureTypes)
            {
                if (entityName.equalsIgnoreCase(type))
                {
                    return true;
                }
            }

        }
        return false;
    }

    protected boolean isTargetNameValid(Entity entity)
    {
        return entity.getName().matches(regex);
    }

    @Override
    public void onQuestTaken(QuestStack questStack, EntityPlayer entityPlayer)
    {

    }

    public int getMaxKillCount(QuestStack questStack)
    {
        if (hasTag(questStack))
            return getTag(questStack).getInteger("MaxKillCount");
        return 0;
    }

    public int getKillCount(QuestStack questStack)
    {
        if (hasTag(questStack))
            return getTag(questStack).getInteger("KillCount");
        return 0;
    }

    public void setKillCount(QuestStack questStack,int killCount)
    {
        initTag(questStack);
        getTag(questStack).setInteger("KillCount",killCount);
    }

    public String[] getCreatureTypes()
    {
        return creatureTypes;
    }

    @Override
    public void onQuestCompleted(QuestStack questStack, EntityPlayer entityPlayer)
    {

    }

    @Override
    public void modifyRewards(QuestStack questStack, EntityPlayer entityPlayer, List<IQuestReward> rewards) {

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

    public void setNameRegex(String regex)
    {
        this.regex = regex;
    }
}
