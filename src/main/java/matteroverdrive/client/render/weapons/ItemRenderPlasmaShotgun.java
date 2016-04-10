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
 * Created by Simeon on 12/6/2015.
 */
public class ItemRenderPlasmaShotgun extends WeaponItemRenderer
{
	public static final String TEXTURE = Reference.PATH_ITEM + "plasma_shotgun.png";
	public static final String MODEL = Reference.PATH_MODEL + "item/plasma_shotgun.obj";
	public static final float SCALE = 0.85f;
	public static final float THIRD_PERSON_SCALE = 0.6f;
	public static final float ITEM_SCALE = 0.3f;
	public static final float SCALE_DROP = 0.4f;

	public ItemRenderPlasmaShotgun()
	{
		super(new ResourceLocation(MODEL));
	}
}
