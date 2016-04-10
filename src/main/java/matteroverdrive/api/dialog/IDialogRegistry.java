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

import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 8/13/2015.
 * Used to globally store all message.
 * And get messages by ID. This is useful for reusing messages on different NPCs, questing progression and message lookup by ID.
 */
public interface IDialogRegistry
{
	/**
	 * Register Message to Matter Overdrive Dialog Registry.
	 * @param message The Dialog Message
	 */
	void registerMessage(IDialogMessage message);

	/**
	 * Register Message to Matter Overdrive Dialog Registry with a name
	 * @param name the name of the dialog message
	 * @param message The Dialog Message
	 */
	void registerMessage(ResourceLocation name, IDialogMessage message);

	/**
	 * Gets a message by an ID.
	 * @param uuid The individual ID of the message.
	 * @return The message that is assigned to the ID;
	 */
	IDialogMessage getMessage(int uuid);

	/**
	 * Gets the ID for a given Message
	 * @param dialogMessage the message
	 * @return the id of the message
	 */
	int getMessageId(IDialogMessage dialogMessage);
}
