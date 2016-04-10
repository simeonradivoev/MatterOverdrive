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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 3/11/2015.
 */
public class ItemRendererPhaser extends WeaponItemRenderer
{
	public static final String MODEL = Reference.PATH_MODEL + "item/phaser2.obj";
	private static final String TEXTURE_COLOR_MASK = Reference.PATH_ITEM + "phaser_color_mask.png";
	public static ResourceLocation phaserTextureColorMask;

	public ItemRendererPhaser()
	{
		super(new ResourceLocation(MODEL));
		phaserTextureColorMask = new ResourceLocation(TEXTURE_COLOR_MASK);
	}

	@Override
	public void renderHand(RenderPlayer renderPlayer)
	{
		renderPlayer.renderRightArm(Minecraft.getMinecraft().thePlayer);
	}

	@Override
	public void transformHand(float recoilValue, float zoomValue)
	{
		transformRecoil(recoilValue, zoomValue);
		GlStateManager.translate(0.157, -0.09, -0.17);
		GlStateManager.rotate(3, 0, 1, 0);
		GlStateManager.rotate(90, 1, 0, 0);
		GlStateManager.scale(0.4, 0.4, 0.4);
	}

	@Override
	public float getHorizontalSpeed()
	{
		return 0.1f;
	}
}
