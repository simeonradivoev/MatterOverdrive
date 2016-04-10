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

package matteroverdrive.api.events.bionicStats;

import matteroverdrive.api.android.IBioticStat;
import matteroverdrive.entity.android_player.AndroidPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * Created by Simeon on 7/21/2015.
 * Triggered by most {@link IBioticStat}.
 * For example the {@link matteroverdrive.data.biostats.BioticStatTeleport} triggers the event when used by the player.
 */
public class MOEventBionicStat extends PlayerEvent
{
	/**
	 * The android player using the ability.
	 */
	public final AndroidPlayer android;
	/**
	 * The Ability itself.
	 */
	public final IBioticStat stat;
	/**
	 * The level of the ability being used.
	 */
	public final int level;


	public MOEventBionicStat(IBioticStat stat, int level, AndroidPlayer android)
	{
		super(android.getPlayer());
		this.android = android;
		this.stat = stat;
		this.level = level;
	}

	@Override
	public boolean isCancelable()
	{
		return true;
	}
}
