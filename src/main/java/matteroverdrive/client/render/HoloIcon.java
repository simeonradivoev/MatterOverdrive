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

package matteroverdrive.client.render;


import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 8/28/2015.
 */
@SideOnly(Side.CLIENT)
public class HoloIcon
{
	private TextureAtlasSprite icon;
	private int originalWidth;
	private int originalHeight;

	public HoloIcon(TextureAtlasSprite icon, int originalX, int originalY)
	{
		this.icon = icon;
		setOriginalSize(originalX, originalY);
	}

	public void setOriginalSize(int originalX, int originalY)
	{
		this.originalWidth = originalX;
		this.originalHeight = originalY;
	}

	public int getOriginalWidth()
	{
		return originalWidth;
	}

	public int getOriginalHeight()
	{
		return originalHeight;
	}

	public TextureAtlasSprite getIcon()
	{
		return icon;
	}

	public void setIcon(TextureAtlasSprite icon)
	{
		this.icon = icon;
	}
}
