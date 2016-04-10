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

package matteroverdrive.data.inventory;

import matteroverdrive.client.render.HoloIcon;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 3/16/2015.
 */
public class Slot
{
	private ItemStack item;
	private int id;
	private boolean drops = true;
	private boolean isMainSlot = false;
	private boolean keepOnDismante = false;
	private boolean sendToClient = false;

	public Slot(boolean isMainSlot)
	{
		this.isMainSlot = isMainSlot;
	}

	public boolean isValidForSlot(ItemStack item)
	{
		return true;
	}

	@SideOnly(Side.CLIENT)
	public HoloIcon getHoloIcon()
	{
		return null;
	}

	public ItemStack getItem()
	{
		return item;
	}

	public void setItem(ItemStack item)
	{
		this.item = item;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public boolean drops()
	{
		return drops;
	}

	public void setDrops(boolean drops)
	{
		this.drops = drops;
	}

	public boolean keepOnDismantle()
	{
		return keepOnDismante;
	}

	public boolean isMainSlot()
	{
		return isMainSlot;
	}

	public void setMainSlot(boolean mainSlot)
	{
		this.isMainSlot = mainSlot;
	}

	public void setKeepOnDismante(boolean keepOnDismante)
	{
		this.keepOnDismante = keepOnDismante;
	}

	public int getMaxStackSize()
	{
		return 64;
	}

	public String getUnlocalizedTooltip()
	{
		return null;
	}

	public Slot setSendToClient(boolean sendToClient)
	{
		this.sendToClient = sendToClient;
		return this;
	}

	public boolean sendsToClient()
	{
		return sendToClient;
	}

	public void onSlotChanged()
	{
	}
}
