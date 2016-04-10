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

package matteroverdrive.api.events.anomaly;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Created by Simeon on 9/26/2015.
 * Triggered when a {@link matteroverdrive.tile.TileEntityGravitationalAnomaly} consumes an entity or an item.
 */
public class MOEventGravitationalAnomalyConsume extends Event
{
	/**
	 * The entity being consumed.
	 */
	public final Entity entity;
	public final BlockPos pos;

	public MOEventGravitationalAnomalyConsume(Entity entity, BlockPos pos)
	{
		this.entity = entity;
		this.pos = pos;
	}

	public static class Pre extends MOEventGravitationalAnomalyConsume
	{
		public Pre(Entity entity, BlockPos pos)
		{
			super(entity, pos);
		}

		@Override
		public boolean isCancelable()
		{
			return true;
		}
	}

	public static class Post extends MOEventGravitationalAnomalyConsume
	{
		public Post(Entity entity, BlockPos pos)
		{
			super(entity, pos);
		}
	}
}
