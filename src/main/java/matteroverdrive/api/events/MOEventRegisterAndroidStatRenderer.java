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

import cpw.mods.fml.common.eventhandler.Event;
import matteroverdrive.api.android.IBionicStat;
import matteroverdrive.api.renderer.IBioticStatRenderer;

/**
 * Created by Simeon on 7/24/2015.
 * Triggered by special Bionic Stats that have custom renderers, such as the Shield ability.
 */
public class MOEventRegisterAndroidStatRenderer extends Event
{
    /**
     * The type of bionic stat that is being rendered.
     */
    public final Class<? extends IBionicStat> statClass;
    /**
     * The Bionic Stat renderer itself.
     */
    public final IBioticStatRenderer renderer;

    public MOEventRegisterAndroidStatRenderer(Class<? extends IBionicStat> statClass,IBioticStatRenderer renderer)
    {
        this.statClass = statClass;
        this.renderer = renderer;
    }

    public boolean isCancelable()
    {
        return true;
    }
}
