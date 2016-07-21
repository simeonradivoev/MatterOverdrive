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

import matteroverdrive.Reference;
import matteroverdrive.gui.MOGuiBase;
import matteroverdrive.util.MOEnergyHelper;
import matteroverdrive.util.RenderUtils;
import net.darkhax.tesla.api.ITeslaHolder;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

/**
 * Created by Simeon on 4/7/2015.
 */
public class MOElementEnergy extends MOElementBase
{
	protected int energyRequired;
	protected int energyRequiredPerTick;
	protected ITeslaHolder storage;
	protected boolean alwaysShowMinimum = false;

	public MOElementEnergy(MOGuiBase gui, int posX, int posY, ITeslaHolder storage)
	{
		super(gui, posX, posY);
		this.storage = storage;
		if (MOEnergyHelper.ENERGY_UNIT.endsWith("RF"))
		{
			setTexture(Reference.TEXTURE_RF_METER, 32, 64);
		}
		else
		{
			setTexture(Reference.TEXTURE_TESLA_METER, 32, 64);
		}
		this.sizeX = 16;
		this.sizeY = 42;
		this.texW = 32;
		this.texH = 64;
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
		if (this.storage.getCapacity() < 0)
		{
			list.add("Infinite RF");
		}
		else
		{
			list.add(this.storage.getStoredPower() + " / " + this.storage.getCapacity() + MOEnergyHelper.ENERGY_UNIT);
		}

		if (energyRequired > 0)
		{
			list.add(TextFormatting.GREEN + "+" + String.valueOf(energyRequired) + MOEnergyHelper.ENERGY_UNIT + TextFormatting.RESET);
		}
		else if (energyRequired < 0)
		{
			list.add(TextFormatting.RED + String.valueOf(energyRequired) + MOEnergyHelper.ENERGY_UNIT + TextFormatting.RESET);
		}
		if (energyRequiredPerTick > 0)
		{
			list.add(TextFormatting.GREEN + "+" + String.valueOf(energyRequiredPerTick) + MOEnergyHelper.ENERGY_UNIT + "/t" + TextFormatting.RESET);
		}
		else if (energyRequiredPerTick < 0)
		{
			list.add(TextFormatting.RED + String.valueOf(energyRequiredPerTick) + MOEnergyHelper.ENERGY_UNIT + "/t" + TextFormatting.RESET);
		}
	}

	public void drawBackground(int var1, int var2, float var3)
	{
		int var4 = this.getScaled();
		RenderUtils.bindTexture(this.texture);
		this.drawTexturedModalRect(this.posX, this.posY, 0, 0, this.sizeX, this.sizeY);
		this.drawTexturedModalRect(this.posX, this.posY + 42 - var4, 16, 42 - var4, this.sizeX, var4);
	}

	public void drawForeground(int var1, int var2)
	{
	}

	protected int getScaled()
	{
		if (this.storage.getCapacity() <= 0)
		{
			return this.sizeY;
		}
		else
		{
			long var1 = this.storage.getStoredPower() * (long)this.sizeY / this.storage.getCapacity();
			return this.alwaysShowMinimum && this.storage.getStoredPower() > 0 ? (int)Math.max(1, Math.round((double)var1)) : (int)Math.round((double)var1);
		}
	}

	public int getEnergyRequired()
	{
		return energyRequired;
	}

	public void setEnergyRequired(int energyRequired)
	{
		this.energyRequired = energyRequired;
	}

	public int getEnergyRequiredPerTick()
	{
		return energyRequiredPerTick;
	}

	public void setEnergyRequiredPerTick(int energyRequired)
	{
		this.energyRequiredPerTick = energyRequired;
	}
}
