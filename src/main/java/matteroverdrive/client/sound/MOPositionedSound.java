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

package matteroverdrive.client.sound;

import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 12/6/2015.
 */
public class MOPositionedSound extends PositionedSound
{
    public MOPositionedSound(ResourceLocation p_i45103_1_,float volume,float pitch)
    {
        super(p_i45103_1_);
        this.field_147663_c = pitch;
        this.volume = volume;
    }

    public void setPosition(float x,float y,float z)
    {
        this.xPosF = x;
        this.yPosF = y;
        this.zPosF = z;
    }
}
