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

package matteroverdrive.api.events;

import matteroverdrive.api.dialog.IDialogNpc;
import matteroverdrive.api.dialog.IDialogOption;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Simeon on 11/22/2015.
 */
public class MOEventDialogInteract extends PlayerEvent
{
	public final IDialogNpc npc;
	public final IDialogOption dialogOption;
	public final Side side;

	public MOEventDialogInteract(EntityPlayer player, IDialogNpc npc, IDialogOption dialogOption, Side side)
	{
		super(player);
		this.npc = npc;
		this.dialogOption = dialogOption;
		this.side = side;
	}

	@Override
	public boolean isCancelable()
	{
		return true;
	}
}
