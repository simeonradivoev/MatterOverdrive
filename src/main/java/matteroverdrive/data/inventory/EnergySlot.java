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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.MOEnergyHelper;
import net.minecraft.item.ItemStack;

/**
 * Created by Simeon on 3/16/2015.
 */
public class EnergySlot extends Slot
{
    public EnergySlot(boolean isMainSlot) {
        super(isMainSlot);
    }

    @Override
    public boolean isValidForSlot(ItemStack itemStack)
    {
        return MOEnergyHelper.isEnergyContainerItem(itemStack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public HoloIcon getHoloIcon()
    {
        return ClientProxy.holoIcons.getIcon("energy");
    }

    @Override
    public boolean keepOnDismantle()
    {
        return true;
    }

    @Override
    public String getUnlocalizedTooltip(){
        return "gui.tooltip.slot.energy";
    }
}
