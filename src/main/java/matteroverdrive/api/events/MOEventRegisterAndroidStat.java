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

/**
 * Created by Simeon on 7/21/2015.
 * Triggered when an Android Ability is registered.
 * When canceled, the ability will not be registered.
 */
public class MOEventRegisterAndroidStat extends Event
{
    /**
     * The Bionic Stat to be registered
     */
    public final IBionicStat stat;
    public MOEventRegisterAndroidStat(IBionicStat stat)
    {
        this.stat = stat;
    }
    public boolean isCancelable()
    {
        return true;
    }
}
