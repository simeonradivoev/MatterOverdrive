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

package matteroverdrive.client.render.weapons;

import matteroverdrive.Reference;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 7/25/2015.
 */
public class ItemRendererPhaserRifle extends WeaponItemRenderer
{
    public static final String MODEL = Reference.PATH_MODEL + "item/phaser_rifle.obj";
    public static final String FLASH_TEXTURE = Reference.PATH_FX + "phaser_rifle_flash.png";

    public static ResourceLocation flashTexture;

    public ItemRendererPhaserRifle()
    {
        super(new ResourceLocation(MODEL));
        flashTexture = new ResourceLocation(FLASH_TEXTURE);
    }
}
