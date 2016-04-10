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

import matteroverdrive.api.renderer.ISpaceBodyHoloRenderer;
import matteroverdrive.starmap.data.SpaceBody;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Created by Simeon on 7/25/2015.
 * Triggered when a Starmap zoom renderer is being rendered.
 */
public class MOEventRegisterStarmapRenderer extends Event
{
	/**
	 * The type of space body being displayed.
	 */
	public final Class<? extends SpaceBody> spaceBodyType;
	/**
	 * The Space body render itself.
	 */
	public final ISpaceBodyHoloRenderer renderer;

	public MOEventRegisterStarmapRenderer(Class<? extends SpaceBody> spaceBodyType, ISpaceBodyHoloRenderer renderer)
	{
		this.spaceBodyType = spaceBodyType;
		this.renderer = renderer;
	}

	@Override
	public boolean isCancelable()
	{
		return true;
	}
}
