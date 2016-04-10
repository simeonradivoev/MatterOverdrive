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

package matteroverdrive.gui.element;

import com.mojang.realmsclient.gui.ChatFormatting;
import matteroverdrive.Reference;
import matteroverdrive.api.matter.IMatterStorage;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.util.MatterHelper;
import matteroverdrive.util.RenderUtils;
import net.minecraft.util.ResourceLocation;

import java.util.List;


public class ElementMatterStored extends MOElementBase
{
	public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(Reference.PATH_ELEMENTS + "Matter.png");
	public static final int DEFAULT_SCALE = 42;
	protected IMatterStorage storage;
	// If this is enabled, 1 pixel of energy will always show in the bar as long as it is non-zero.
	protected boolean alwaysShowMinimum = false;
	private int drain = 0;
	private double drainPerTick = 0;

	public ElementMatterStored(MOGuiBase gui, int posX, int posY, IMatterStorage storage)
	{

		super(gui, posX, posY);
		this.storage = storage;

		this.texture = DEFAULT_TEXTURE;
		this.sizeX = 16;
		this.sizeY = DEFAULT_SCALE;

		this.texW = 32;
		this.texH = 64;
	}

	public ElementMatterStored setAlwaysShow(boolean show)
	{

		alwaysShowMinimum = show;
		return this;
	}

	@Override
	public void drawBackground(int mouseX, int mouseY, float gameTicks)
	{

		int amount = getScaled();

		RenderUtils.bindTexture(texture);
		drawTexturedModalRect(posX, posY, 0, 0, sizeX, sizeY);
		drawTexturedModalRect(posX, posY + DEFAULT_SCALE - amount, 16, DEFAULT_SCALE - amount, sizeX, amount);
	}

	@Override
	public void drawForeground(int mouseX, int mouseY)
	{

	}

	@Override
	public void updateInfo()
	{

	}

	@Override
	public void init()
	{

	}

	@Override
	public void addTooltip(List<String> list, int mouseX, int mouseY)
	{

		if (storage.getCapacity() < 0)
		{
			list.add("Infinite " + MatterHelper.MATTER_UNIT);
		}
		else
		{
			list.add(storage.getMatterStored() + " / " + storage.getCapacity() + MatterHelper.MATTER_UNIT);
		}

		if (drain > 0)
		{
			list.add(ChatFormatting.GREEN + "+" + MatterHelper.formatMatter(drain));
		}
		else if (drain < 0)
		{
			list.add(ChatFormatting.RED + MatterHelper.formatMatter(drain));
		}

		if (drainPerTick > 0)
		{
			list.add(ChatFormatting.GREEN + "+" + MatterHelper.formatMatter(drainPerTick) + "/t");
		}
		else if (drainPerTick < 0)
		{
			list.add(ChatFormatting.RED + MatterHelper.formatMatter(drainPerTick) + "/t");
		}
	}

	protected int getScaled()
	{

		if (storage.getCapacity() <= 0)
		{
			return sizeY;
		}
		long fraction = (long)storage.getMatterStored() * sizeY / storage.getCapacity();

		return alwaysShowMinimum && storage.getMatterStored() > 0 ? Math.max(1, Math.round(fraction)) : Math.round(fraction);
	}

	public void setDrain(int amount)
	{
		this.drain = amount;
	}

	public void setDrainPerTick(double amount)
	{
		this.drainPerTick = amount;
	}
}
