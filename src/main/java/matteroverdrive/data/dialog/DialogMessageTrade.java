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

package matteroverdrive.data.dialog;

import com.google.gson.JsonObject;
import matteroverdrive.api.dialog.IDialogNpc;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Simeon on 8/10/2015.
 */
public class DialogMessageTrade extends DialogMessage
{
	public DialogMessageTrade(JsonObject object)
	{
		super(object);
	}

	public DialogMessageTrade()
	{
	}

	public DialogMessageTrade(String message)
	{
		super(message);
	}

	public DialogMessageTrade(String message, String question)
	{
		super(message, question);
	}

	public DialogMessageTrade(String[] messages, String[] questions)
	{
		super(messages, questions);
	}

	@Override
	public void onInteract(IDialogNpc npc, EntityPlayer player)
	{
		if (!player.worldObj.isRemote && npc.getEntity() instanceof IMerchant)
		{
			((IMerchant)npc.getEntity()).setCustomer(player);
			player.displayVillagerTradeGui((IMerchant)npc);
		}
	}
}
