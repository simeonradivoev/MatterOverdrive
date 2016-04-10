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

import matteroverdrive.data.dialog.DialogMessage;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 8/9/2015.
 * Used by Entities that provide Dialog Messages
 */
public interface IDialogNpc
{
	/**
	 * Get the message that is the root of the conversation based on the player that starts the conversation.
	 * @param player The player that starts the conversation.
	 * @return The Root message.
	 */
	IDialogMessage getStartDialogMessage(EntityPlayer player);

	/**
	 * Gets the current active Dialog Player
	 * @return The Dialog Player
	 */
	EntityPlayer getDialogPlayer();

	/**
	 * Sets the current Dialog Player the NPC is interacting with.
	 * @param player The Player
	 */
	void setDialogPlayer(EntityPlayer player);

	/**
	 * Can the NPC talk to the player.
	 * @param player The Player.
	 * @return Can talk to player.
	 */
	boolean canTalkTo(EntityPlayer player);

	/**
	 * Get The Entity that the NPC represents.
	 * Used to enable separation of NPC from Entity.
	 * @return
	 */
	EntityLiving getEntity();

	/**
	 * Called when the player interacts with the NPC
	 * @param player The Player
	 * @param dialogMessage The dialog message the player interacted with.
	 */
	void onPlayerInteract(EntityPlayer player, DialogMessage dialogMessage);
}
