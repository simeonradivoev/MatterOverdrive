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

package matteroverdrive.container.slot;

import matteroverdrive.client.render.HoloIcon;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 4/8/2015.
 */
public class MOSlot extends Slot
{
	protected String unlocalizedTooltip;
	boolean isVisible = true;

	public MOSlot(IInventory inventory, int slot, int x, int y)
	{
		super(inventory, slot, x, y);
	}

	@SideOnly(Side.CLIENT)
	public boolean func_111238_b()
	{
		return isVisible;
	}

	/**
	 * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
	 */
	@Override
	public boolean isItemValid(ItemStack itemStack)
	{
		return isValid(itemStack);
	}

	public boolean isValid(ItemStack itemStack)
	{
		return true;
	}

	public void setVisible(boolean visible)
	{
		this.isVisible = visible;
	}

	@SideOnly(Side.CLIENT)
	public HoloIcon getHoloIcon()
	{
		return null;
	}

	public String getUnlocalizedTooltip()
	{
		return unlocalizedTooltip;
	}
}
