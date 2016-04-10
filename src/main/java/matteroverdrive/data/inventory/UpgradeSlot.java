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

import matteroverdrive.api.IUpgradeable;
import matteroverdrive.api.inventory.IUpgrade;
import matteroverdrive.api.inventory.UpgradeTypes;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

/**
 * Created by Simeon on 4/9/2015.
 */
public class UpgradeSlot extends Slot
{
	private IUpgradeable upgradeable;

	public UpgradeSlot(boolean isMainSlot, IUpgradeable upgradeable)
	{
		super(isMainSlot);
		this.upgradeable = upgradeable;
	}

	@Override
	public boolean isValidForSlot(ItemStack item)
	{
		if (item.getItem() instanceof IUpgrade)
		{
			IUpgrade upgrade = (IUpgrade)item.getItem();
			UpgradeTypes mainUpgradeType = upgrade.getMainUpgrade(item);
			if (mainUpgradeType != null)
			{
				return upgradeable.isAffectedByUpgrade(mainUpgradeType);
			}
			else
			{
				Map<UpgradeTypes, Double> upgradeMap = upgrade.getUpgrades(item);
				for (final Map.Entry<UpgradeTypes, Double> entry : upgradeMap.entrySet())
				{
					if (upgradeable.isAffectedByUpgrade(entry.getKey()))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public HoloIcon getHoloIcon()
	{
		return ClientProxy.holoIcons.getIcon("upgrade");
	}

	@Override
	public int getMaxStackSize()
	{
		return 1;
	}

	@Override
	public boolean keepOnDismantle()
	{
		return true;
	}

	@Override
	public String getUnlocalizedTooltip()
	{
		return "gui.tooltip.slot.upgrade";
	}
}
