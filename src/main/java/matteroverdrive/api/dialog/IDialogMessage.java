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

package matteroverdrive.api.dialog;

import matteroverdrive.api.renderer.IDialogShot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

/**
 * Created by Simeon on 8/9/2015.
 * Used by the Dialog Conversation System.
 */
public interface IDialogMessage
{
    /**
     * The individual ID of each Dialog Message
     * @return
     */
    int getID();

    /**
     * The Parent Dialog Message.
     * @param npc The NPC Entity.
     * @param player The Player.
     * @return The Parent message.
     */
    IDialogMessage getParent(IDialogNpc npc,EntityPlayer player);

    /**
     * A list of Dialog Message Children.
     * This is used as Conversation Options.
     * Once an Option is chose this one becomes active.
     * @param npc The NPC Entity.
     * @param player The Player
     * @return A list of children (options) messages.
     */
    List<IDialogMessage> getOptions(IDialogNpc npc,EntityPlayer player);

    /**
     * Used to get the Text of the Message.
     * Called when this message is active.
     * Represents the words spoken by the NPC.
     * Translation must be handled by implementations.
     * @param npc The NPC entity.
     * @param player The Player.
     * @return The message text.
     */
    String getMessageText(IDialogNpc npc,EntityPlayer player);

    /**
     * Used to display the question (option) the message reprisents.
     * This is used when the parent message is active and shows all children from {@link matteroverdrive.api.dialog.IDialogMessage#getOptions(IDialogNpc, EntityPlayer)}.
     * @param npc The NPC Entity.
     * @param player The Player
     * @return The question (option) text.
     */
    String getQuestionText(IDialogNpc npc,EntityPlayer player);

    /**
     * Called when an option is chosen from the message's children.
     * Not to be confused with {@link matteroverdrive.api.dialog.IDialogMessage#onInteract(IDialogNpc, EntityPlayer)} which is called when the message is becoming active.
     * This is called on the parent, before {@link matteroverdrive.api.dialog.IDialogMessage#onInteract(IDialogNpc, EntityPlayer)}.
     * @param npc The NPC Entity.
     * @param player The Player
     * @param option The Option that was chosen. Not the option (message ID), but the ordering index of the child from {@link matteroverdrive.api.dialog.IDialogMessage#getOptions(IDialogNpc, EntityPlayer)}.
     */
    void onInteract(IDialogNpc npc,EntityPlayer player,int option);

    /**
     * Called when the message is chosen from all the option of the patten from {@link matteroverdrive.api.dialog.IDialogMessage#getOptions(IDialogNpc, EntityPlayer)}.
     * Not to be confused with {@link matteroverdrive.api.dialog.IDialogMessage#onInteract(IDialogNpc, EntityPlayer, int)} which is called on the parent.
     * This method is called after {@link matteroverdrive.api.dialog.IDialogMessage#onInteract(IDialogNpc, EntityPlayer, int)}.
     * @param npc
     * @param player
     */
    void onInteract(IDialogNpc npc,EntityPlayer player);

    /**
     * Can the player interact with this message. Can it be chosen as the next active message from the parent's options.
     * @param npc The NPC Entity
     * @param player The Player
     * @return Can the message be clicked (chosen) as an option.
     */
    boolean canInteract(IDialogNpc npc,EntityPlayer player);

    /**
     * Is the message visible as an option in the parent's {@link matteroverdrive.api.dialog.IDialogMessage#getOptions(IDialogNpc, EntityPlayer)}.
     * @param npc The NPC entity.
     * @param player The Player.
     * @return Is the message visible as an option.
     */
    boolean isVisible(IDialogNpc npc,EntityPlayer player);

    /**
     * Returns the list of available Camera shots the conversation can have once active.
     * @param npc The NPC Entity.
     * @param player The Player.
     * @return Available Camera Shots.
     */
    IDialogShot[] getShots(IDialogNpc npc, EntityPlayer player);

    /**
     * Does the message have a Holo Icon
     * @param npc
     * @param player
     * @return
     */
    String getHoloIcon(IDialogNpc npc, EntityPlayer player);
}
