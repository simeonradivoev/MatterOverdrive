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

import matteroverdrive.api.matter.IMatterDatabase;
import matteroverdrive.api.matter.IMatterPatternStorage;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 3/27/2015.
 */
public class PatternStorageSlot extends Slot
{
	private IMatterDatabase database;
	private int storageId;

	public PatternStorageSlot(boolean isMainSlot, IMatterDatabase database, int storageId)
	{
		super(isMainSlot);
		this.database = database;
		this.storageId = storageId;
	}

	@Override
	public boolean isValidForSlot(ItemStack item)
	{
		return item.getItem() instanceof IMatterPatternStorage;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public HoloIcon getHoloIcon()
	{
		return ClientProxy.holoIcons.getIcon("pattern_storage");
	}

	@Override
	public int getMaxStackSize()
	{
		return 1;
	}

	@Override
	public String getUnlocalizedTooltip()
	{
		return "gui.tooltip.slot.pattern_storage";
	}

	public void onSlotChanged()
	{
		database.onPatternStorageChange(storageId);
	}
}
