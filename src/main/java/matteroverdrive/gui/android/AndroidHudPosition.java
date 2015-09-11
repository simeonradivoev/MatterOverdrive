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

package matteroverdrive.gui.android;

/**
 * Created by Simeon on 9/9/2015.
 */
public enum AndroidHudPosition
{
    TOP_LEFT(0,0),
    TOP_CENTER(0.5f,0),
    TOP_RIGHT(1,0),
    MIDDLE_LEFT(0,0.5f),
    MIDDLE_CENTER(0.5f,0.5f),
    MIDDLE_RIGHT(1f,0.5f),
    BOTTOM_LEFT(0,1),
    BOTTOM_CENTER(0.5f,1),
    BOTTOM_RIGHT(1,1);

    public final float x;
    public final float y;

    AndroidHudPosition(float x,float y)
    {
        this.x = x;
        this.y = y;
    }

    public static String[] getNames()
    {
        String[] names = new String[values().length];
        for (int i = 0;i < values().length;i++)
        {
            names[i] = values()[i].name();
        }
        return names;
    }
}
