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
import matteroverdrive.api.events.MOEventDialogInteract;
import matteroverdrive.api.quest.IQuestReward;
import matteroverdrive.api.quest.QuestStack;
import matteroverdrive.entity.EntityVillagerMadScientist;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 11/22/2015.
 */
public class QuestLogicCocktailOfAscension extends AbstractQuestLogic
{
    public static final int MAX_CREEPER_KILS = 5;
    public static final int MAX_GUNPOWDER_COUNT = 5;
    public static final int MAX_MUSHROOM_COUNT = 5;

    @Override
    public String modifyInfo(QuestStack questStack, String info)
    {
        return String.format(info,"",MAX_GUNPOWDER_COUNT,MAX_MUSHROOM_COUNT);
    }

    @Override
    public boolean isObjectiveCompleted(QuestStack questStack, EntityPlayer entityPlayer, int objectiveIndex)
    {
        if (objectiveIndex == 0)
        {
            return getCreeperKillCount(questStack) >= MAX_CREEPER_KILS;
        }
        else if (objectiveIndex == 1)
        {
            return getGunpowderCount(questStack) >= MAX_GUNPOWDER_COUNT;
        }else if (objectiveIndex == 2)
        {
            return getMushroomCount(questStack) >= MAX_MUSHROOM_COUNT;
        }
        else if (objectiveIndex == 3)
        {
            return hasTalkedTo(questStack);
        }
        return false;
    }

    @Override
    public String modifyObjective(QuestStack questStack, EntityPlayer entityPlayer, String objective, int objectiveIndex)
    {
        if (objectiveIndex == 0)
        {
            return objective.replace("%2$s",Integer.toString(getCreeperKillCount(questStack))).replace("%3$s",Integer.toString(MAX_CREEPER_KILS));
        }
        else if (objectiveIndex == 1)
        {
            return objective.replace("%2$s",Integer.toString(getGunpowderCount(questStack))).replace("%3$s",Integer.toString(MAX_GUNPOWDER_COUNT));
        }
        else if (objectiveIndex == 2)
        {
            return objective.replace("%2$s",Integer.toString(getMushroomCount(questStack))).replace("%3$s",Integer.toString(MAX_MUSHROOM_COUNT));
        }
        return objective;
    }

    @Override
    public int modifyObjectiveCount(QuestStack questStack, EntityPlayer entityPlayer, int count)
    {
        if (questStack.hasGiver() && getCreeperKillCount(questStack) >= MAX_CREEPER_KILS && getGunpowderCount(questStack) >= MAX_GUNPOWDER_COUNT && getMushroomCount(questStack) >= MAX_MUSHROOM_COUNT)
        {
            return 4;
        }
        return 3;
    }


    @Override
    public void initQuestStack(Random random, QuestStack questStack)
    {

    }

    public int getCreeperKillCount(QuestStack questStack)
    {
        if (hasTag(questStack))
        {
            return getTag(questStack).getByte("CreeperKills");
        }
        return 0;
    }

    public int getMushroomCount(QuestStack questStack)
    {
        if (hasTag(questStack))
        {
            return getTag(questStack).getByte("MushroomCount");
        }
        return 0;
    }

    public int getGunpowderCount(QuestStack questStack)
    {
        if (hasTag(questStack))
        {
            return getTag(questStack).getByte("GunpowderCount");
        }
        return 0;
    }

    public boolean hasTalkedTo(QuestStack questStack)
    {
        if (questStack.hasGiver()) {
            if (hasTag(questStack))
            {
                return getTag(questStack).getBoolean("TalkedToGiver");
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer)
    {
        if (getCreeperKillCount(questStack) < MAX_CREEPER_KILS && event instanceof LivingDeathEvent)
        {
            if (((LivingDeathEvent) event).entityLiving instanceof EntityCreeper && entityPlayer.getHeldItem() != null && entityPlayer.getHeldItem().getItem() instanceof ItemSpade)
            {
                initTag(questStack);
                byte currentCreeperKillCount = getTag(questStack).getByte("CreeperKills");
                getTag(questStack).setByte("CreeperKills",++currentCreeperKillCount);
                return true;
            }
        }
        else if (event instanceof EntityItemPickupEvent)
        {
            ItemStack itemStack = ((EntityItemPickupEvent) event).item.getEntityItem();
            if (itemStack != null)
            {
                if (itemStack.getItem() instanceof ItemBlock && ((ItemBlock) itemStack.getItem()).field_150939_a == Blocks.red_mushroom && entityPlayer.worldObj.provider.dimensionId == -1)
                {
                    initTag(questStack);
                    byte mushroomCount = getTag(questStack).getByte("MushroomCount");
                    if (mushroomCount < MAX_MUSHROOM_COUNT)
                    {
                        int newMushroomCount = Math.min(mushroomCount + itemStack.stackSize,MAX_MUSHROOM_COUNT);
                        int takenMushrooms = newMushroomCount-mushroomCount;
                        itemStack.stackSize-= takenMushrooms;
                        getTag(questStack).setByte("MushroomCount",(byte)newMushroomCount);
                        return true;
                    }
                }
                else if (itemStack.getItem() == Items.gunpowder)
                {
                    initTag(questStack);

                    byte gunpowderCount = getTag(questStack).getByte("GunpowderCount");
                    if (gunpowderCount < MAX_GUNPOWDER_COUNT)
                    {
                        int newGunpowderCount = Math.min(gunpowderCount + itemStack.stackSize,MAX_MUSHROOM_COUNT);
                        int takenGunpowder = newGunpowderCount-gunpowderCount;
                        itemStack.stackSize-= takenGunpowder;
                        getTag(questStack).setByte("GunpowderCount",(byte) newGunpowderCount);
                        itemStack.stackSize--;
                        return true;
                    }
                }
            }
        }
        else if (event instanceof MOEventDialogInteract)
        {
            if (((MOEventDialogInteract) event).npc instanceof EntityVillagerMadScientist && ((MOEventDialogInteract) event).dialogMessage == EntityVillagerMadScientist.cocktailOfAscensionComplete)
            {
                initTag(questStack);
                getTag(questStack).setBoolean("TalkedToGiver",true);
                questStack.markComplited(entityPlayer,false);

                return true;
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

    }

    @Override
    public void modifyRewards(QuestStack questStack, EntityPlayer entityPlayer, List<IQuestReward> rewards)
    {
    }
}
