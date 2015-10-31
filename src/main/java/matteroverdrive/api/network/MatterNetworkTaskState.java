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

package matteroverdrive.api.network;

/**
 * Created by Simeon on 7/15/2015.
 */
public enum MatterNetworkTaskState
{
	INVALID,
	UNKNOWN,
	WAITING,
	QUEUED,
	PROCESSING,
	FINISHED;

	public static MatterNetworkTaskState get(int ordinal)
	{
		return MatterNetworkTaskState.values()[ordinal];
	}

	public boolean below(MatterNetworkTaskState state)
	{
		return ordinal() < state.ordinal();
	}
	public boolean belowOrEqual(MatterNetworkTaskState state)
	{
		return ordinal() <= state.ordinal();
	}
	public boolean above(MatterNetworkTaskState state){return ordinal() > state.ordinal();}
}
