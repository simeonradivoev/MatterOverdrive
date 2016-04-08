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

package matteroverdrive.api.quest;

import matteroverdrive.api.events.MOEventDialogInteract;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 12/5/2015.
 */
public interface IQuest
{
    /**
     * Returns the identification name of the quest
     * @return the quest's identification
     */
    String getName();

    /**
     * Gets the tile of the Quest with the given Quest stack.
     * There is also an extended version of this method the {@link IQuest#getTitle(QuestStack,EntityPlayer)}
     * @param questStack the quest stack.
     * @return the title of the quest.
     */
    String getTitle(QuestStack questStack);

    /**
     * Determines if the given questStack can be accepted by the given player.
     * @param questStack the quest stack to be accepted.
     * @param entityPlayer the player.
     * @return can the given questStack can be accepted by the given player.
     */
    boolean canBeAccepted(QuestStack questStack, EntityPlayer entityPlayer);

    /**
     * Gets the title of the Quest with the given Quest stack and player.
     * This is an extension to the {@link IQuest#getTitle(QuestStack)} with an additional parameter, the player.
     * @param questStack the quest stack.
     * @param entityPlayer the player.
     * @return the title of the quest with the given Quest stack and player.
     */
    String getTitle(QuestStack questStack,EntityPlayer entityPlayer);

    /**
     * Compares if two given Quest stacks are the same quest but with different data.
     * This is mainly used to determine if a Quest is contained in the completed quests or active quests.
     * @param questStackOne Quest stack one.
     * @param questStackTwo Quest stack two.
     * @return are the two quest stack the same quest but with different data.
     */
    boolean areQuestStacksEqual(QuestStack questStackOne,QuestStack questStackTwo);

    /**
     * Gets the information/description of the given Quest stack with the given Player.
     * @param questStack the quest stack.
     * @param entityPlayer the player.
     * @return the info/description of the given Quest stack.
     */
    String getInfo(QuestStack questStack,EntityPlayer entityPlayer);

    /**
     * Gets the quest objective info of the given index for the given Quest stack.
     * @param questStack rhe Quest stack.
     * @param entityPlayer the player.
     * @param objectiveIndex the index of the objective requested.
     * @return the requested objective info at the given index.
     */
    String getObjective(QuestStack questStack,EntityPlayer entityPlayer,int objectiveIndex);

    /**
     * Gets the total amount of objectives the quest has.
     * This can change on the fly or based on other objectives.
     * @param questStack the Quest stack.
     * @param entityPlayer the player.
     * @return the total amount of objectives the quest has.
     */
    int getObjectivesCount(QuestStack questStack,EntityPlayer entityPlayer);

    /**
     * Is the objective at the given index complete.
     * @param questStack the Quest stack.
     * @param entityPlayer the player.
     * @param objectiveIndex the objective index.
     * @return is the objective at the given index complete.
     */
    boolean isObjectiveCompleted(QuestStack questStack,EntityPlayer entityPlayer,int objectiveIndex);

    /**
     * Used for Quest stack initialization.
     * Called when a Quest stack is created.
     * This method will not be loaded by the default constructor. That means that it will not be called when loading Quest stacks from data.
     * There is also an extension with a player parameter {@link IQuest#initQuestStack(Random, QuestStack, EntityPlayer)}.
     * @param random a random instance.
     * @param questStack the quest stack.
     */
    void initQuestStack(Random random, QuestStack questStack);

    /**
     * Used for Quest stack initialization.
     * Called when a Quest is added to the player's active quest list.
     * This is somewhat different than the basic initialization method {@link IQuest#initQuestStack(Random, QuestStack)} in that it's
     * called when added to player's quest list not when the Quest stack is created.
     * @param random a random instance.
     * @param questStack the quest stack.
     */
    void initQuestStack(Random random,QuestStack questStack,EntityPlayer entityPlayer);

    /**
     * Used as a event listener for all Events.
     * Currently supports:
     * {@link EntityItemPickupEvent}
     * {@link LivingDeathEvent}
     * {@link MOEventDialogInteract}
     * @param questStack the quest stack.
     * @param event the event.
     * @param entityPlayer the Entity player.
     * @return in what way did the event change the quest and what objective did it change, represented in the QuestState. This is used to synchronize with clients and display objective changes in the Quest HUD.
     */
    QuestState onEvent(QuestStack questStack, Event event, EntityPlayer entityPlayer);

    /**
     * Called once the quest has completed.
     * @param questStack the quest stack.
     * @param entityPlayer the player who completed the quest.
     */
    void onCompleted(QuestStack questStack,EntityPlayer entityPlayer);

    /**
     * Gets the amount of XP the player will receive after quest completion.
     * @param questStack the quest stack.
     * @param entityPlayer the player who will receive the XP.
     * @return the amount of XP the player will receive.
     */
    int getXpReward(QuestStack questStack,EntityPlayer entityPlayer);

    /**
     * Adds to the rewards the player will receive after quest completion.
     * @param questStack the quest stack
     * @param entityPlayer the entity player.
     * @param rewards the list of quest rewards.
     */
    void addToRewards(QuestStack questStack, EntityPlayer entityPlayer, List<IQuestReward> rewards);

    /**
     * Sets the quest stack as completed. Used to control the setting of the quest stack from outside sources.
     * @param questStack the quest stack.
     * @param entityPlayer the Player.
     */
    void setCompleted(QuestStack questStack,EntityPlayer entityPlayer);
}
